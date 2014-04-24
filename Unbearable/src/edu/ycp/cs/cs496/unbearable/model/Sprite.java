package edu.ycp.cs.cs496.unbearable.model;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

public class Sprite {
	private Bitmap bitmap; // spritesheet

	private int x, y; // top left x and y 

	private int frameNumber; // number of frames in animation
	private int currentFrame; // current frame of animation
	private int frameInitial; // initial frame in array
	private int frameFinal; // last frame in array
	
	private long frameTick; // time since last frame update
	private int framePeriod; // time between each frame

	private int frameWidth; // width of a sprite frame
	private int frameHeight; // height of a sprite frame

	public enum Orientation {
		// default set to left (player is default set to right)
		LEFT, RIGHT
	}

	Orientation orientation;

	// Constructor
	public Sprite(Resources res, int x, int y, int frameWidth, int frameHeight,
			int fps, int fileID) {
		bitmap = BitmapFactory.decodeResource(res, fileID);
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		frameNumber = (bitmap.getWidth() / frameWidth)
				* (bitmap.getHeight() / frameHeight);
		orientation = Orientation.LEFT;

		currentFrame = 0;
		frameInitial = 0;
		frameFinal = 0;
		frameTick = 0l;
		framePeriod = 1000 / fps;
		
		//set the top left x and y coordinates
		this.x = x;
		this.y = y;
	}

	Bitmap frameCreate(Bitmap d, boolean flip) {
		//creates frame and optionally flips it horizontally
		Matrix m = new Matrix();
		if (flip)
			m.preScale(-1, 1);
		Bitmap temp = Bitmap.createBitmap(d, (currentFrame * frameWidth), 0,
				frameWidth, frameHeight, m, false);
		return temp;
	}

	// Render bitmap at current location
	public void doDraw(Canvas canvas) {
		if (orientation == Orientation.LEFT) {
			canvas.drawBitmap(frameCreate(bitmap, true), x, y, null);
		} else {
			canvas.drawBitmap(frameCreate(bitmap, false), x, y, null);
		}
	}

	// Update frames
	public void update(long elapsedTime) {
		// cycle through frames if boundary frames are different
		if (frameInitial != frameFinal) {
			if (elapsedTime > framePeriod + frameTick) {
				frameTick = elapsedTime;
				currentFrame++;
				if (currentFrame >= frameFinal) {
					currentFrame = frameInitial;
				}
			}
		}
	}

	// Getter/Setter methods
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public int getRightX() {
		return x + frameWidth;
	}
	
	public void setRightX(int x) {
		this.x = x - frameWidth;
	}
	
	public int getBottomY() {
		return y + frameHeight;
	}
	
	public void setBottomY(int y) {
		this.y = y - frameHeight;
	}

	public int getCurrentFrame() {
		return currentFrame;
	}

	public void setCurrentFrame(int currentFrame) {
		this.currentFrame = currentFrame;
	}

	public int getFrameInitial() {
		return frameInitial;
	}

	public void setFrameInitial(int frameInitial) {
		this.frameInitial = frameInitial;
	}

	public int getFrameFinal() {
		return frameFinal;
	}

	public void setFrameFinal(int frameFinal) {
		this.frameFinal = frameFinal;
	}

	public float getCenterX() {
		return x + frameWidth / 2;
	}

	public float getCenterY() {
		return y + frameHeight / 2;
	}

	public float getWidth() {
		return bitmap.getWidth() / frameNumber;
	}

	public float getHeight() {
		return bitmap.getHeight();
	}

	public Orientation getOrientation() {
		return this.orientation;
	}

	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}
}
