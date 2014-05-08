 package edu.ycp.cs.cs496.unbearable;

import android.content.res.Resources;
import edu.ycp.cs.cs496.unbearable.model.Sprite;

public class Enemy extends Sprite {

	private int dX, initialDX; // horizontal delta
	private int dY, initialDY; // vertical delta
	private int speed; // speed at which the character can move
	private boolean moving;
	private boolean jumping;
	private boolean falling;
	private int xMin, xMax;
	private int xLeftTrue, xRightTrue;
	private int yTopTrue, yBottomTrue; //do later, more 'accurate' collision box
	
	public enum EnemyClass {
		SHARK, PENGUIN
	}
	
	EnemyClass enemyClass;

	public Enemy(Resources res, int x, int y, int frameWidth, int frameHeight,
			int fps, int fileID, EnemyClass enemyClass) {
		super(res, x, y, frameWidth, frameHeight, fps, fileID);
		
		dX = 10;
		initialDX = dX;
		dY = 10;
		initialDY = dY;
		speed = 5;
		moving = false;
		jumping = false;
		falling = false;
		xMin = x;
		xMax = x + 400;
		
		setOrientation(Orientation.LEFT);
		this.enemyClass = enemyClass;
		
		//Initializes things for specific enemy classes
		initialize();
	}
	
	public void initialize() {
		if (enemyClass == EnemyClass.SHARK) {
			moving = true;
			setCurrentFrame(4);	
//			setTrueTopY(20);
//			setTrueLeftX(10);
//			setTrueRightX((int)getWidth()-7);
//			setTrueBottomY((int)getHeight()-6);
			
		} else if (enemyClass == EnemyClass.PENGUIN) {
			
		} else {
			
		}
	}
	
	public void updatePosition(long elapsedTime) {
		if (enemyClass == EnemyClass.SHARK) {
			sharkBehavior();
		} else if (enemyClass == EnemyClass.PENGUIN) {
			//penguinBehavior();
		} else {
			
		}
		update(elapsedTime);
	}
	
	public void sharkBehavior() {
		//shark simply moves back and forth
		//manual frame cycling because idk why
		setFrameInitial(3);
		setFrameFinal(6);
		if (getCurrentFrame() >= getFrameFinal() || getCurrentFrame() < getFrameInitial()) {
			setCurrentFrame(getFrameInitial());
		} else {
			setCurrentFrame(getCurrentFrame() + 1);
		}
		
		if (moving) {
			if (getX() <= xMin) {
				//turn right
				setOrientation(Orientation.RIGHT);
				setDX(initialDX);
			} else if (getX() >= xMax) {
				//turn left
				setOrientation(Orientation.LEFT);
				setDX(-(initialDX));
			}
		} //else if (entering_water)
		//exiting water
		//attack
		
		setX(getX() + dX);
	}
	
	public int getDX() {
		return dX;
	}
	
	public void setDX(int dX) {
		this.dX = dX;
	}
	
	public int getDY() {
		return dY;
	}
	
	public void setDY(int dY) {
		this.dY = dY;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public boolean getMoving() {
		return moving;
	}
	
	public void setMoving(boolean moving) {
		this.moving = moving;
	}
	
	public boolean getJumping() {
		return jumping;
	}
	
	public void setJumping(boolean jumping) {
		this.jumping = jumping;
	}
	
	public boolean getFalling() {
		return falling;
	}
	
	public void setFalling(boolean falling) {
		this.falling = falling;
	}
	
	public int getXMin() {
		return xMin;
	}
	
	public void setXMin(int xMin) {
		this.xMin = xMin;
	}
	
	public int getXMax() {
		return xMax;
	}
	
	public void setXMax(int xMax) {
		this.xMax = xMax;
	}
	
	public EnemyClass getEnemyClass() {
		return this.enemyClass;
	}
}
