package edu.ycp.cs.cs496.unbearable;

import edu.ycp.cs.cs496.unbearable.model.Sprite.Orientation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class GamePanel extends SurfaceView implements Callback {
	public static float mWidth;
	public static float mHeight;
	private Paint pObject;
	private GameThread mThread;
	private Player player;

	public GamePanel(Context context) {
		super(context);
		getHolder().addCallback(this);
		pObject = new Paint();
		pObject.setColor(Color.WHITE);
		mThread = new GameThread(this);
		player = new Player(getResources(), 100, 200, 64, 64, 10,
				R.drawable.bear);

		this.setFocusable(true);
		this.requestFocus();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		mWidth = width;
		mHeight = height;
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
	}

	public void doDraw(Canvas canvas, long elapsed) {
		canvas.drawColor(Color.BLUE);

		player.doDraw(canvas);

		// Debug information drawing
		canvas.drawText("Current Frame: " + player.getCurrentFrame()
				+ ", mWidth: " + mWidth + ", X: " + player.getX()
				+ ", mHeight: " + mHeight + ", Y: " + player.getY(), 10, 10,
				pObject);
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
