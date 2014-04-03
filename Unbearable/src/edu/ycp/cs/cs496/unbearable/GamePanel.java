package edu.ycp.cs.cs496.unbearable;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

import edu.ycp.cs.cs496.unbearable.model.Sprite;

public class GamePanel extends SurfaceView implements Callback {
	public static float mWidth;
	public static float mHeight;
	// TODO: Add class fields
	private Paint pObject;
	//private Thread rThread;
	private GameThread mThread;
	private ArrayList<Sprite> mSpriteList = new ArrayList<Sprite>();
	private int mNumSprites;
	
	public GamePanel(Context context) {
		super(context);
		// TODO: Initialize class fields
		getHolder().addCallback(this);
		//rThread = new Thread();
		pObject = new Paint();
		pObject.setColor(Color.WHITE);
		mThread = new GameThread(this);
		mSpriteList.add(new Sprite(getResources(), 10, 10, -3, 3));
	    mNumSprites = mSpriteList.size();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		mWidth = width;
		mHeight = height;
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// TODO: Start thread
		if(!mThread.isAlive()) {
			mThread = new GameThread(this);
			mThread.setRunning(true);
			mThread.start();
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO: Stop thread
		if(mThread.isAlive()) {
			mThread.setRunning(false);
		}
	}

	public void update(long elapsedTime) {
		// TODO: Update ball (thread safe) and check end of game
		synchronized(mSpriteList) {
			for (Sprite bSprite : mSpriteList) {
				bSprite.update(elapsedTime);
			}
		}
	}
	
	public void doDraw(Canvas canvas, long elapsed) {
		canvas.drawColor(Color.BLUE);

		// TODO: Draw bear (thread safe)
		synchronized (mSpriteList) {
			for (Sprite bSprite : mSpriteList) {
				bSprite.doDraw(canvas);
			}
		}
	}

}
