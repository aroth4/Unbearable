package edu.ycp.cs.cs496.unbearable;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.WindowManager;
import edu.ycp.cs.cs496.unbearable.model.Sprite.Orientation;

public class GamePanel extends SurfaceView implements Callback {
	private Paint pObject;
	private GameThread mThread;
	private Player player;
	private ArrayList<Ledge> ledges = new ArrayList<Ledge>();
	boolean ledgeDetected;
	private static int wLoc; // world scroll location
	private int loc;
	private ArrayList<Integer> randomsX = new ArrayList<Integer>();
	private ArrayList<Integer> randomsY = new ArrayList<Integer>();
	private Random randx, randy;
	private int n;
	boolean onGround;
	boolean onLedge;
	int highestLedge;
	//used to get screen size for different devices
	WindowManager wm;
	Display display;
	Point screenSize;
	
	//area constants 
	private static int groundLevel;

	public GamePanel(Context context, int statusBarHeight) {
		super(context);
		getHolder().addCallback(this);
		pObject = new Paint();
		pObject.setColor(Color.WHITE);
		mThread = new GameThread(this);
		wLoc = 0;
		loc = 0;
		n = 10;
		randx = new Random();
		randy = new Random();
		randx.setSeed(System.currentTimeMillis()+ 234235);
		randy.setSeed(System.currentTimeMillis()+ 23489562);
		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		display = wm.getDefaultDisplay();

		//getWidth and getHeight deprecated pre-API 13 but this must allow API 10+
		screenSize = new Point(display.getWidth(),display.getHeight() - statusBarHeight);
		
		groundLevel = screenSize.y - 74; //74 is bear height (64) plus 10 to set arbitrary ground level
		
		//width then height
		/*player = new Player(getResources(), 10, screenSize.y - 74, 64, 64, 10,
			R.drawable.bear);*/ //spawns bear on ground
		player = new Player(getResources(), 10, 0, 64, 64, 10,
			R.drawable.bear); //spawns bear at top of screen, so he falls to ground

		ledgeDetected=false;
		onGround = false;
		onLedge = false;
		highestLedge = -1;
		
		randomListX(n);
		randomListY(n);
		for (int i = 0; i < screenSize.y; i+= 64) {
		ledges.add(new Ledge(getResources(), 0, 128 + i, 128, 32, 10,
				R.drawable.ledge));
		}
		ledges.add(new Ledge(getResources(), 256, 192, 128, 32, 10,
				R.drawable.ledge));
		
		//Add the ledges to array
		for(int i = 0; i < n; i++)
		{
			ledges.add(new Ledge(getResources(), randomsX.get(i), randomsY.get(i), 128, 32, 10,
					R.drawable.ledge));
		}

		this.setFocusable(true);
		this.requestFocus();
	}
	
	//Set X coordinates for ledges
	public ArrayList<Integer> randomListX(int n)
	{
		//Set random points for x,y
		for (int i=0; i<n; i++)
		{
		    randomsX.add(randx.nextInt(2000));
		}
		return randomsX;
	}
	
	//Set Y coordinates for ledges
	public ArrayList<Integer> randomListY(int n)
	{
		//Set random points for x,y
		for (int i=0; i<n; i++)
		{
		    randomsY.add(randy.nextInt(300));
		}
		return randomsY;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	public void surfaceCreated(SurfaceHolder holder) {
		if (!mThread.isAlive()) {
			mThread = new GameThread(this);
			mThread.setRunning(true);
			mThread.start();
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		if (mThread.isAlive()) {
			mThread.setRunning(false);
		}
	}

	public void update(long elapsedTime) {
		//Update player
		player.updatePosition(System.currentTimeMillis());
		//Check ledges
		checkLedge();
		//Update scrolling
		setUpdateWorld();
		//Update ledges
		for(int i = 0; i < ledges.size(); i++)
		{
			Ledge ledge = ledges.get(i);
			ledge.setX(ledge.getLeftX() + loc);
		}
		
	}
	//Set scrolling
	public void setUpdateWorld()
	{
		if(player.getX() <= 0)
		{
			wLoc = 1;
			if(player.getMoving() == true)
				loc = 6;
			else
				loc = 0;
		}
		else if(player.getX() >= (screenSize.x - 300))
		{
			wLoc = -1;
			if(player.getMoving() == true)
				loc = -6;
			else
				loc = 0;
		}
		else{
			wLoc = 0;
			loc = 0;
		}
	}
	
	//Get scrolling location
	public static int getUpdateWorld()
	{
		return wLoc;
	}
	
public void checkLedge(){
		
		// Ground Level //
		//if on ground level and Y is not changing, return -1 because player must be on ground
		if (player.getY() == groundLevel && player.getJumping() == false && player.getFalling() == false) {
			//if player is on the ground and neither jumping nor falling, return
			//System.out.println("Player on Ground");
			onLedge = false;
			return;
		} else if (player.getY() < groundLevel && player.getJumping() == false && player.getFalling() == false && highestLedge == -1) {
			//if player is above ground level and neither jumping nor falling,
			//since player is also NOT on a ledge, player should begin to fall
			System.out.println("Player in the air, falling!");
			player.setFalling(true);
			player.setDY(0);
		} else if (player.getY() >= groundLevel && player.getFalling() == true) {
			//if player is on or below ground level and falling, 
			//player must have hit the ground, so set player's location to ground level
			System.out.println("Player hit Ground");
			player.setFalling(false);
			onLedge = false;
			player.setY(groundLevel);
			player.setDY(player.getInitialDY());
			highestLedge = -1;
			return;
		}
		
		//check for ledges
		if (onLedge) {
			if (player.getJumping() || player.getFalling()) {
				onLedge = false;
			} else {
				//player is on a ledge, so check to see if the player is still within the X boundaries of the ledge
				//if not, that means the player has walked off the ledge, so make player fall
				if (highestLedge != -1) {
					if ((player.getX() < ledges.get(highestLedge).getRightX() && player.getX() + 
							player.getWidth() > ledges.get(highestLedge).getLeftX())) {
						//if player is still within X boundaries, check Y of highest ledge
						if (player.getBottomY() >= ledges.get(highestLedge).getTopY()) {
							//if the player's Y is under the highest ledge, reset highest ledge and begin to fall
							//player.setBottomY(ledges.get(highestLedge).getTopY());
						} else {
							player.setFalling(true);
							player.setDY(0);
							onLedge = false;
						}
					} else {
						//player is not within X boundaries of ledge
						highestLedge = -1;
					}
				}
			}
		} else if (!onGround && !onLedge) {
			//if player is not on a ledge or on the ground, check the ledges
			System.out.println("Begin to check each ledge");
			synchronized(ledges) {
				for (int i = 0; i < ledges.size(); i++) {
					Ledge ledge = ledges.get(i);
					System.out.println("Checking Ledge " + i);
					//use this For loop ONLY to determine the ledge that the player would fall onto
					if (player.getX() < ledge.getRightX() && player.getX() + player.getWidth() > ledge.getLeftX()
							&& !player.getJumping()) {
						//player is within the X boundaries of a ledge and NOT jumping and ABOVE ledge
						System.out.println("Player is within X boundaries of ledge " + i);
						
						//check to see if player is above ledge, and if not, ignore ledge and go to the next ledge
						if (player.getBottomY() < ledge.getTopY() && player.getFalling()) {
							//player is above ledge and falling 
							System.out.println("Player is above ledge " + i);
							if (highestLedge != -1 && highestLedge != i) {
							//if a ledge was detected previously, check to see if this new ledge is higher than old
							//if higher, make player on top of it
							//if lower, make player remain on previous ledge
								System.out.println("A highest ledge was previously detected as " + highestLedge);
								System.out.println("Current ledge being considered is ledge " + i);
								
								if (ledges.get(highestLedge).getTopY() > ledge.getTopY()) {
									//if the previously highest ledge is lower than the newly detected ledge,
									//make new ledge the detected ledge
									//note: a greater Y value means it is LOWER
									
									System.out.println("Highest Ledge " + highestLedge + " Y: " + ledges.get(highestLedge).getTopY());
									System.out.println("This Ledge " + i + " Y: " + ledges.get(i).getTopY());
									highestLedge = i;
									//System.out.println("New higher ledge set to " + i);
								}
								
							} else {
								//if a ledge was NOT previously detected, since this is the first ledge,
								//make it the highest ledge
								highestLedge = i;
								System.out.println("No highest ledge set, so highest ledge is now " + i);
							}
						} else {
							System.out.println("Player is NOT above ledge " + i);
							if (highestLedge == i) {
								System.out.println("Player has fallen off ledge " + i);
								//highestLedge = -1;
							}
						}
					} else {
						System.out.println("Player is NOT within X boundaries of ledge " + i);
					}
				}
				
			}
			System.out.println("Will land on ledge " + highestLedge);
			if (highestLedge != -1) {
				if (player.getBottomY() >= ledges.get(highestLedge).getTopY()) {
					//player should be on ledge
					player.setBottomY(ledges.get(highestLedge).getTopY());
					player.setFalling(false);
					player.setDY(player.getInitialDY());
					onLedge = true;
				}
			}
		}
	}

	public void doDraw(Canvas canvas, long elapsed) {
		canvas.drawColor(Color.BLUE);
		synchronized (ledges) {
			for (Ledge ledge : ledges) {
				ledge.doDraw(canvas);
			}
		}
		
		player.doDraw(canvas);

		// Debug information drawing
		canvas.drawText(
				"Current Frame: " + player.getCurrentFrame()
				+ ", X: " + player.getX() + ", Y: " + player.getY() 
				+ ", ledgeLeft: " + ledges.get(0).getLeftX() + ", ledgeRight: " + ledges.get(0).getRightX()
				+ ", ledgeDetected: " + ledgeDetected + ", " + wLoc
				//+ ", screen height: " + screenSize.y +  ", screen width: " + screenSize.x
				//+ ", Bitmap Width: " + player.getWidth() + ", Bitmap Height: " + player.getHeight()
				//+ ", Center Width: " + player.getCenterX() + "Center Height: " + player.getCenterY()
				, 10, 10, pObject);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// possibly should be placed inside Player class
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			player.setMoving(true);
			player.setOrientation(Orientation.LEFT);
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			player.setMoving(true);
			player.setOrientation(Orientation.RIGHT);
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			if (player.getFalling() != true) {
				player.setJumping(true);
			}
			return true;
		}
		
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			if (highestLedge != -1) {
				onLedge = false;
				highestLedge = -1;
				player.setFalling(true);
				player.setDY(0);
				player.setBottomY(player.getBottomY()+1);
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		//The way this is now has the bear's movement end if EITHER key is let go,
		//even if the other key is still held down,
		//so, not correct behavior
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			player.setMoving(false);
			return true;
		}
		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			player.setMoving(false);
			return true;
		}
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			player.setMoving(!player.getMoving());
		}
		return true;
	}
	
	public int getWorldMove(){
		return wLoc;
	}

}
