package edu.ycp.cs.cs496.unbearable;

import java.net.URI;

import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.DefaultHttpClient;

import edu.ycp.cs.cs496.unbearable.model.Login;
import edu.ycp.cs.cs496.unbearable.model.json.*;
import edu.ycp.cs.cs496.unbearable.util.SystemUiHider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class MainActivity extends Activity {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setDefaultView();
		
	}
	
	 //figure out if the text fields are empty
    private boolean IsEmpty(EditText ItemToTest)
    {
    	  if (ItemToTest.getText().toString().trim().length() > 0) {
    	        return false;
    	    } else {
    	        return true;
    	    }
    }
    
 // Method for displaying data entry view
    public void setDefaultView() {
        setContentView(R.layout.activity_main);
        
        // TODO: Obtain references to widgets
        Button loginButton = (Button) findViewById(R.id.Login);
        Button registerButton = (Button) findViewById(R.id.Register);
        
        // TODO: Set onClickListeners for buttons
        loginButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					//LoginSuccess();
					 //Toast.makeText(MainActivity.this,"I need implemented!", Toast.LENGTH_LONG).show();
					
					
					// TODO: use web service to log in
					
					EditText username = (EditText) findViewById(R.id.UsernameInput);
			  		EditText password = (EditText) findViewById(R.id.PasswordInput);
			  		if(IsEmpty(username) || IsEmpty(password))
			  		{
			  			Toast.makeText(MainActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
			  		}
			  		else
			  		{
			  			String checkName = username.getText().toString();
			  			String checkPass = password.getText().toString();
			  			
			  			boolean LoginCheck = LoginPost(checkName, checkPass);
			  			//Toast.makeText(MainActivity.this, checkName + checkPass, Toast.LENGTH_SHORT).show();
			  			if(LoginCheck == true)
			  			{
			  				//Username exists and is correct! Go to game
			  				Toast.makeText(MainActivity.this, "Login Successful! Time to game!", Toast.LENGTH_SHORT).show();
							startActivity(new Intent(MainActivity.this, GameActivity.class));
			  			}
			  			else
			  			{
			  				//username doesn't exist or was input wrong, return area
			  				Toast.makeText(MainActivity.this, "Username/Password incorrect or does not exist", Toast.LENGTH_SHORT).show();
			  			}
			  			
			  		}
					
					
					 
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
			public boolean LoginPost(String username, String password){
		
				// Create HTTP client
//				HttpClient client = new DefaultHttpClient();
//				
//				JSON.getObjectMapper().writeValue(sw, newItem);
				
				//loginPost getPost = new loginPost();
				Login login = new Login();
				login.setUsername(username);
				login.setPassword(password);
//				@Override
//				public Item addItem(String itemName, int numItem) {
//					// add item to the inventory
//					for (Item item : inventory){
//						if(item.getName().equals(itemName)){
//							item.setQuantity(numItem);
//							return item;
//						}
//					
//					}
				
				return false;
				
			}
			
			
		});
        
        // TODO: Set onClickListeners for buttons
        registerButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
					Toast.makeText(MainActivity.this,"Clicked!", Toast.LENGTH_LONG).show();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				
			}
		});

    }
    
 
    
}

		
		
		
		
		
		
		
		
		
		
//		final View controlsView = findViewById(R.id.fullscreen_content_controls);
//		final View contentView = findViewById(R.id.);
//
//		// Set up an instance of SystemUiHider to control the system UI for
//		// this activity.
//		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
//				HIDER_FLAGS);
//		mSystemUiHider.setup();
//		mSystemUiHider
//				.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
//					// Cached values.
//					int mControlsHeight;
//					int mShortAnimTime;
//
//					@Override
//					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//					public void onVisibilityChange(boolean visible) {
//						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
//							// If the ViewPropertyAnimator API is available
//							// (Honeycomb MR2 and later), use it to animate the
//							// in-layout UI controls at the bottom of the
//							// screen.
//							if (mControlsHeight == 0) {
//								mControlsHeight = controlsView.getHeight();
//							}
//							if (mShortAnimTime == 0) {
//								mShortAnimTime = getResources().getInteger(
//										android.R.integer.config_shortAnimTime);
//							}
//							controlsView
//									.animate()
//									.translationY(visible ? 0 : mControlsHeight)
//									.setDuration(mShortAnimTime);
//						} else {
//							// If the ViewPropertyAnimator APIs aren't
//							// available, simply show or hide the in-layout UI
//							// controls.
//							controlsView.setVisibility(visible ? View.VISIBLE
//									: View.GONE);
//						}
//
//						if (visible && AUTO_HIDE) {
//							// Schedule a hide().
//							delayedHide(AUTO_HIDE_DELAY_MILLIS);
//						}
//					}
//				});
//
//		// Set up the user interaction to manually show or hide the system UI.
//		contentView.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View view) {
//				if (TOGGLE_ON_CLICK) {
//					mSystemUiHider.toggle();
//				} else {
//					mSystemUiHider.show();
//				}
//			}
//		});
//
//		// Upon interacting with UI controls, delay any scheduled hide()
//		// operations to prevent the jarring behavior of controls going away
//		// while interacting with the UI.
//		findViewById(R.id.dummy_button).setOnTouchListener(
//				mDelayHideTouchListener);
//	}
//
//	@Override
//	protected void onPostCreate(Bundle savedInstanceState) {
//		super.onPostCreate(savedInstanceState);
//
////		// Trigger the initial hide() shortly after the activity has been
////		// created, to briefly hint to the user that UI controls
////		// are available.
////		delayedHide(100);
////	}
////
////	/**
////	 * Touch listener to use for in-layout UI controls to delay hiding the
////	 * system UI. This is to prevent the jarring behavior of controls going away
////	 * while interacting with activity UI.
////	 */
////	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
////		@Override
////		public boolean onTouch(View view, MotionEvent motionEvent) {
////			if (AUTO_HIDE) {
////				delayedHide(AUTO_HIDE_DELAY_MILLIS);
////			}
////			return false;
////		}
////	};
////
////	Handler mHideHandler = new Handler();
////	Runnable mHideRunnable = new Runnable() {
////		@Override
////		public void run() {
////			mSystemUiHider.hide();
////		}
////	};
////
////	/**
////	 * Schedules a call to hide() in [delay] milliseconds, canceling any
////	 * previously scheduled calls.
////	 */
////	private void delayedHide(int delayMillis) {
////		mHideHandler.removeCallbacks(mHideRunnable);
////		mHideHandler.postDelayed(mHideRunnable, delayMillis);
////	}
//}
