package edu.ycp.cs.cs496.unbearable;

import android.content.res.Resources;
import edu.ycp.cs.cs496.unbearable.model.Sprite;

public class Collectible extends Sprite {
	
	private int value;
	private boolean collected;

	public Collectible(Resources res, int x, int y, int frameWidth,
			int frameHeight, int fps, int fileID) {
		super(res, x, y, frameWidth, frameHeight, fps, fileID);
		
		value = 1;
		collected = false;
	}
	
	public void update() {
		
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value;
	}
	
	public boolean getCollected() {
		return collected;
	}
	
	public void setCollected(boolean collected) {
		this.collected = collected;
	}
}
