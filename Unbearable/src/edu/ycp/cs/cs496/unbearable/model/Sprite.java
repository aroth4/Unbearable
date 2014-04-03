package edu.ycp.cs.cs496.unbearable.model;

import java.util.Random;

import edu.ycp.cs.cs496.unbearable.GamePanel;
import edu.ycp.cs.cs496.unbearable.R;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Sprite {
	private float mX;
	private float mY;
	private float mDx;
	private float mDy;
	private Bitmap mBitmap;
	
	 // Constructor
	   public Sprite(Resources res, int x, int y, int dx, int dy) {
	      // Get bitmap from resource file
	      mBitmap = BitmapFactory.decodeResource(res, R.drawable.bear_run);

	      // Store upper left corner coordinates
	      mX = x - mBitmap.getWidth()/2;
	      mY = y - mBitmap.getHeight()/2;

	      // Set random velocity
	      Random rand = new Random();
	      mDx = rand.nextInt(7) - 3;
	      mDy = rand.nextInt(7) - 3;
	   }

	   // Render bitmap at current location 
	   public void doDraw(Canvas canvas) {
	      canvas.drawBitmap(mBitmap, mX, mY, null);
	   }

	   // Update (time-based) position
	   public void update(long elapsedTime) {
	      mX += mDx * (elapsedTime / 20f);
	      mY += mDy * (elapsedTime / 20f);
	      checkBoundary();
	   }

	   // Collision detection
	   private void checkBoundary() {
	      // Left or right boundary
	      if (mX <= 0) {
	         mDx *= -1;
	         mX = 0;
	      } else if (mX + mBitmap.getWidth() >= GamePanel.mWidth) {
	         mDx *= -1;
	         mX = GamePanel.mWidth - mBitmap.getWidth();
	      }

	      // Top or bottom boundary
	      if (mY <= 0) {
	         mDy *= -1;
	         mY = 0;
	      } else if (mY + mBitmap.getHeight() >= GamePanel.mHeight) {
	         mDy *= -1;
	         mY = GamePanel.mHeight - mBitmap.getHeight();
	      }
	   }
	
	// Velocity update method
	public void changeVelocity(float ddx, float ddy) {
		mDx += ddx;
		mDy += ddy;
	}
	
	// Getter methods
	
	public float getCenterX() {
		return mX + mBitmap.getWidth()/2;
	}
	
	public float getCenterY() {
		return mY + mBitmap.getHeight()/2;
	}
	
	public float getWidth() {
		return mBitmap.getWidth()/2;
	}
	
	public float getHeight() {
		return mBitmap.getHeight()/2;
	}
	
	public float getXVelocity() {
		return mDx;
	}
	
	public float getYVelocity() {
		return mDy;
	}

}

