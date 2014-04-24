package edu.ycp.cs.cs496.unbearable;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread extends Thread {
	private boolean mRun = false;
	private long sTime;
	private long eTime;
	private GamePanel mPanel;
	private SurfaceHolder mHolder;

	public GameThread(GamePanel panel) {
		mPanel = panel;
		mHolder = mPanel.getHolder();
	}

	public void setRunning(boolean run) {
		mRun = run;
	}

	@Override
	public void run() {
		Canvas canvas = null;
		sTime = System.currentTimeMillis();

		while (mRun) { 
			canvas = mHolder.lockCanvas();

			if (canvas != null) {
				eTime = System.currentTimeMillis() - sTime;
				mPanel.update(eTime);

				mPanel.doDraw(canvas, eTime);

				mHolder.unlockCanvasAndPost(canvas);
			}
			sTime = System.currentTimeMillis();
		}
	}
}
