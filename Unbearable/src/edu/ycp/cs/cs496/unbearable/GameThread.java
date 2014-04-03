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
		// TODO: Initialize panel and holder objects
		mPanel = panel;
		mHolder = mPanel.getHolder();
	}
	
	public void setRunning(boolean run) {
		mRun = run;
	}
	
	@Override
	public void run() {
		Canvas canvas = null;
		// TODO: Update and draw with thread safe canvas
		
		//Get the time when thread starts
		sTime = System.currentTimeMillis();
		
		//Thread loop
		while(mRun) {
			//Obtain lock on canvas object
			canvas = mHolder.lockCanvas();
			
			if(canvas != null) {
				//Update state based on elapsed time
				eTime = System.currentTimeMillis() - sTime;
				mPanel.update(eTime);
				
				//render the updated scene
				mPanel.doDraw(canvas, eTime);
				
				//Release lock on canvas object
				mHolder.unlockCanvasAndPost(canvas);
			}
			sTime = System.currentTimeMillis();
		}
	}
	
}
