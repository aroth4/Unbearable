package edu.ycp.cs.cs496.unbearable;

import java.util.ArrayList;

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
		//Debug crap (solved sort of)
		//place images in NO_DPI to make Android NOT scale the images
		//automatically (and therefore incorrectly)
		/*

		ledges.add(new Ledge(getResources(), 0, 64, 128, 32, 10,
				R.drawable.ledge));
		ledges.add(new Ledge(getResources(), 0, 128, 256, 64, 10,
				R.drawable.ledgetest));
		ledges.add(new Ledge(getResources(), 0, 192, 256, 64, 10,
				R.drawable.bear));
		ledges.add(new Ledge(getResources(), 256, 192, 256, 64, 10,
				R.drawable.ledgetest));
		ledges.add(new Ledge(getResources(), 0, 192+64, 256, 64, 10,
				R.drawable.ledgetest));
		 */
		ledges.add(new Ledge(getResources(), 300, screenSize.y - 48, 128, 32, 10,
				R.drawable.ledge));
		ledges.add(new Ledge(getResources(), 200, screenSize.y - 148, 128, 32, 10,
				R.drawable.ledge));
		
		this.setFocusable(true);
		this.requestFocus();
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
		player.updatePosition(System.currentTimeMillis());
		checkLedge();
	}
	
	public void checkLedge() {
		//checks to see if bear is on the ground or a ledge
		//if not on ledge return -1
		//else return int equal to ledge in array
//		if (player.getJumping() == false && player.getFalling() == false && 
//				player.getY() < groundLevel && ledge == -1) { //OR if NOT on ledge
//			player.setFalling(true);
//			player.setDY(0);
//		}
//		if (player.getY() == groundLevel) {
//			return -1;
//		} else 
		
		if (player.getJumping() == false && player.getFalling() == false) {
			//if not in the process of jumping OR falling, player's Y is not changing
			//therefore, if it's on the ground or on a ledge, let its Y alone,
			//and if it's in the air and its Y is not changing, make it fall
			System.out.println("Detected Player's Y is not changing");
			if (player.getY() + player.getHeight() < groundLevel) {
				//if player's bottom Y is above ground level, make it fall
				System.out.println("Above ground, so should fall");
				player.setFalling(true);
				player.setDY(0);
				return;
			}
		}
		
		if (player.getFalling()) {
			//if player is falling
			if (player.getY()> groundLevel) {
				//if player's bottom Y is lower than ground level
				System.out.println("Lower than ground, so should be on ground");
				player.setFalling(false);
				player.setDY(player.getInitialDY());
				player.setY(groundLevel);
			}
		} 
		
		//check ledges here
		synchronized(ledges) {
			for (int i = 0; i < ledges.size(); i++) {
				Ledge ledge = ledges.get(i);
				
				if (ledgeDetected) {
					if (player.getX() < ledge.getRightX() && player.getX() + player.getWidth() > ledge.getLeftX()) {
						//if a ledge was previously detected and the player is still within the boundaries of ledge, do nothing
					} else {
						//if not within boundaries still, make player start to fall
						player.setFalling(true);
						player.setDY(0);
					}
				}
				
				if (player.getY() < ledge.getTopY() && player.getFalling() == true) {
						//&&
						
						/*(
							//xleft > ledgeleft AND xleft < ledgeright 
							(player.getX() > ledge.getLeftX() && player.getX() < ledge.getRightX()) ||
							//xleft < ledgeright AND xright > ledgeleft
							(player.getX() < ledge.getRightX() && player.getX() + player.getWidth() > ledge.getLeftX()) ||
							//xright > ledgeleft AND xright < ledgeright
							(player.getX() + player.getWidth() > ledge.getLeftX() && player.getX() + player.getWidth() < ledge.getRightX())
						)*/
					if (player.getX() < ledge.getRightX() && player.getX() + player.getWidth() > ledge.getLeftX()) {
						ledgeDetected = true;
						//player is falling and is above ledge, so allow it to
						//fall until it reaches the ledge's top Y (on top of ledge)
						if ((player.getY() + player.getHeight()) + player.getDY() >= ledge.getTopY()) {
							//if the next update will cause the player to go under the ledge
							//make the player on the ledge instead!
							player.setFalling(false);
							player.setY((int) (ledge.getTopY() - player.getHeight()));
							player.setDY(player.getInitialDY());
							return;
						}
					} else {
						ledgeDetected = false;
					}
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
				+ ", ledgeDetected: " + ledgeDetected
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

}
