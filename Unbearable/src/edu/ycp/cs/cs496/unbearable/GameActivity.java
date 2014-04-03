package edu.ycp.cs.cs496.unbearable;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class GameActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new GamePanel(this));
	}
}
