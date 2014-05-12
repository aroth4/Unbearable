package edu.ycp.cs.cs496.unbearable;

import edu.ycp.cs.cs496.unbearable.model.Sprite;
import android.content.res.Resources;

public class Ledge extends Sprite {
	
	public Ledge(Resources res, int x, int y, int frameWidth, int frameHeight,
			int fps, int fileID) {
		super(res, x, y, frameWidth, frameHeight, fps, fileID);
	}
	
	public int getTopY() {
		return this.getY();
	}
	
	public int getBottomY() {
		return (int) (this.getY() + this.getHeight());
	}
	
	public int getLeftX() {
		return this.getX();
	}
	
	public int getRightX() {
		return (int) (this.getX() + this.getWidth());
	}
	
	public void setLeftX(int x) {
		setX(x);
	}
	
	public void setRight(int x) {
		setX((int) (x + getWidth()));
	}

}
