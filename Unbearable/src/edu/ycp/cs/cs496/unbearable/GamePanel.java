package edu.ycp.cs.cs496.unbearable;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
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
import edu.ycp.cs.cs496.unbearable.model.Sprite;
import edu.ycp.cs.cs496.unbearable.model.Sprite.Orientation;

public class GamePanel extends SurfaceView implements Callback {
	private Paint pObject;
	private GameThread mThread;
	private Player player;
	private Background background;
	private ArrayList<Ledge> ledges = new ArrayList<Ledge>();
	private static int wLoc; // world scroll location
	private int loc;
	private ArrayList<Integer> randomsX = new ArrayList<Integer>();
	private ArrayList<Integer> randomsY = new ArrayList<Integer>();
	private Random randx, randy;
	private int n;
	boolean onGround;
	boolean onLedge;
	int currentLedge;
	
	int highestLedge;
	private int gameState = 1;
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
		
		groundLevel = screenSize.y - 10; //74 is bear height (64) plus 10 to set arbitrary ground level
		
		//width then height
		/*player = new Player(getResources(), 10, screenSize.y - 74, 64, 64, 10,
			R.drawable.bear);*/ //spawns bear on ground
		background  = new Background(getResources(), 0, 0 + statusBarHeight, 2000, 400, 10, R.drawable.background);
		player = new Player(getResources(), 10, 0, 64, 64, 10,
			R.drawable.bear); //spawns bear at top of screen, so he falls to ground
		onGround = false;
		onLedge = false;
		currentLedge = -1;
		
		randomListX(n);
		randomListY(n);
		
		//ledges from top to bottom
		for (int i = 0; i < screenSize.y; i+= 64) {
		ledges.add(new Ledge(getResources(), 0, 128 + i, 128, 32, 10,
				R.drawable.ledge));
		}
		
		//ledges on ground
		for (int i = 0; i < 2000; i+= 128) { //arbitrary number 2000, for end of level
			ledges.add(new Ledge(getResources(), i, groundLevel, 128, 32, 1, R.drawable.ledge));
		}
		
		//random ledges
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
		checkCollision();
		//Update scrolling
		setUpdateWorld();
		//Update ledges
		for(int i = 0; i < ledges.size(); i++)
		{
			Ledge ledge = ledges.get(i);
			ledge.setX(ledge.getLeftX() + loc);
		}
		
		background.setX(background.getLeftX() + loc);
		
	}
	
	//Set scrolling
	public void setUpdateWorld()
	{
		if(player.getX() <= 0)
		{
			wLoc = 1;
			if(player.getMoving() == true && player.getX() >= 0)
				loc = 6;
			else
				loc = 0;
		}
		else if(player.getX() >= (screenSize.x - 300))
		{
			wLoc = -1;
			if(player.getMoving() == true && player.getX() <= 1500)
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
	
	public void checkCollision() {
		if (player.getBottomY() >  groundLevel) {
			//if player's location is beyond the ground level,
			//set player to ground level
			player.setBottomY(groundLevel);
			player.setFalling(false);
			player.setDY(player.getInitialDY());
			onLedge = false;
			onGround = true;
		} else if (player.getJumping()) {
			//if player is jumping
			onGround = false;
			onLedge = false;
		} else if (!onGround && !onLedge && !player.getFalling()){
			//if player is not on ground or ledge, and not falling,
			//player should be falling
			player.setFalling(true);
			player.setDY(0);
		} else {
			doCollision();
		}
	}
	
	public void doCollision() {
		//check to see which ledges the player is within the X boundaries

		ArrayList<Integer> detected = new ArrayList<Integer>();
		for (int i = 0; i < ledges.size(); i++) {
			if (checkLedgeBoundaries(i)) {
				//check here to see if the detected ledge is under/at the ground level
				//and if it is, don't include it, otherwise include it
				if (ledges.get(i).getTopY() <= groundLevel) {
					detected.add(i);
				}
			}
		}
		
		//detected now has a list of all possible ledges the player may land on
		
		if (detected.size() == 0) {
			//no possible ledges, so must land on ground
			currentLedge = -1;
		} else {
			//possible ledges detected, calculate which ledge player should land on
			currentLedge = -1;
			//reset currentLedge to ground level to calculate for possible new ledge
			for (int i = 0; i < detected.size(); i++) {
				Ledge newLedge = ledges.get(detected.get(i));
				//if within boundaries, check if this ledge is under the player
				//and if so, continue to check all the ledges until
				//the highest ledge still under the player is found
				if (currentLedge == -1 && player.getBottomY() <= newLedge.getTopY()) {
					//if ground level, and player is above/on this ledge,
					//set currentLedge to this ledge
					currentLedge = detected.get(i);
				} else if (currentLedge != -1 && newLedge.getTopY() < ledges.get(currentLedge).getTopY()) {
					//if this new ledge is higher than the currently set ledge (and currentledge isn't ground),
					//this is possibly the ledge that the player should land on, so check if the player is higher
					//and set the new ledge as the current ledge
					if (player.getBottomY() <= newLedge.getTopY()) {
						currentLedge = detected.get(i);
					}
				}
			}
		}
		
		//now, the ledge the player should land on has been calculated.
		//if the player isn't on a ledge and isn't falling, make the player fall,
		//and if/when the player lands on the calculated ledge, set falling to false and player's Y on ledge
		
		//calculate player's Y position
		if(currentLedge != -1) {
			if (player.getBottomY() + player.getDY() >= ledges.get(currentLedge).getTopY()) {
				//if the player will surpass the current ledge in the next frame, set it on top of the ledge
				player.setBottomY(ledges.get(currentLedge).getTopY());
				player.setFalling(false);
				player.setDY(player.getInitialDY());
				onLedge = true;
				onGround = false;
			} else if (player.getBottomY() + player.getDY() < ledges.get(currentLedge).getTopY()) {
				//if player is above ledge and will remain above the ledge in the next frame,
				//let player continue to fall
				if (!player.getFalling() && !player.getJumping()) {
					//if player is not falling (and not jumping), make player fall
					player.setFalling(true);
					player.setDY(0);
					onLedge = false;
					onGround = false;
				}
			}
		} else {
			if (!onGround && !player.getFalling()) {
				//if currentLedge is ground, and if player is not on the ground and not falling,
				//player should be falling
				player.setFalling(true);
				player.setDY(0);
			}
		}
	}
	
	boolean checkLedgeBoundaries(int index) {
		//safety check, shouldn't be called if on ground, but
		//if on ground, do nothing;
		//will cause out of bounds if it tries to check the -1 element
		if (index > -1) {
			Ledge ledge = ledges.get(index);
			if ((player.getX() < ledge.getRightX() && player.getX() + player.getWidth() > ledge.getLeftX())) {
				return true;
			}
		}
		return false;
	}
	public void doDraw(Canvas canvas, long elapsed) {
		canvas.drawColor(Color.WHITE);
		background.doDraw(canvas);
		synchronized (ledges) {
			for (Ledge ledge : ledges) {
				ledge.doDraw(canvas);
			}
		}
//		synchronized (land) {
//			for (Sprite sprite : land) {
//				sprite.doDraw(canvas);
//			}
//		}
		
		player.doDraw(canvas);
		// Debug information drawing
		canvas.drawText(
				"Current Frame: " + player.getCurrentFrame()
				+ ", X: " + player.getX() + ", Y: " + player.getY() 
				+ ", ledgeLeft: " + ledges.get(0).getLeftX() + ", ledgeRight: " + ledges.get(0).getRightX()
				//+ ", ledgeDetected: " + ledgeDetected + ", " + wLoc
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
			if (currentLedge != -1) {
				onLedge = false;
				currentLedge = -1;
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
		
		if (keyCode == KeyEvent.KEYCODE_B) {
			for (int i = 0; i < 40; i++)
				System.out.println("DEBUG");
		}
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
