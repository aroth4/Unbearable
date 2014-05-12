package edu.ycp.cs.cs496.unbearable;

import java.io.Console;

import android.content.res.Resources;
import edu.ycp.cs.cs496.unbearable.model.Sprite;
import edu.ycp.cs.cs496.unbearable.model.Sprite.Orientation;

public class Player extends Sprite {

	private int dY; // jumping delta
	private int speed; // speed at which the character can move
	private boolean moving;
	private boolean jumping;
	private boolean falling;
	private boolean worldMove;
	private int localMove;
	private static final int initialDY = 17;

	public Player(Resources res, int x, int y, int frameWidth, int frameHeight,
			int fps, int fileID) {
		super(res, x, y, frameWidth, frameHeight, fps, fileID);

		dY = initialDY;
		speed = 10;
		moving = false;
		jumping = false;
		falling = false;
		worldMove = false;
		
		setOrientation(Orientation.RIGHT); 
	}

	public void updatePosition(long elapsedTime) {
		if (jumping) {
			setFrameInitial(1);
			setFrameFinal(1);
			setCurrentFrame(1);
			dY--;
			setY(getY() - dY);
			if (dY <= 0) {
				falling = true;
				jumping = false;
				setCurrentFrame(0);
			}
		}

		if (falling) {
			setFrameInitial(2);
			setFrameFinal(2);
			setCurrentFrame(2);
			dY++;
			setY(getY() + dY);
		}

		if (moving) {
			if (jumping == false && falling == false) {
				setFrameInitial(1);
				setFrameFinal(4);
			}

			if (getOrientation() == Orientation.LEFT) {
				setX(getX() - speed + localMove*10 );
			} else {
				setX(getX() + speed + localMove*10 );
			}
		}

		if (jumping == false && falling == false && moving == false) {
			setFrameInitial(0);
			setFrameFinal(0);
			setCurrentFrame(0);
		}
		localMove = GamePanel.getUpdateWorld();
		update(elapsedTime);
	}

	public int getDY() {
		return dY;
	}
	
	public int getInitialDY() {
		return initialDY;
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
	
	public void setWorldMove(boolean worldMove){
		this.worldMove = worldMove;
	}
	
}
