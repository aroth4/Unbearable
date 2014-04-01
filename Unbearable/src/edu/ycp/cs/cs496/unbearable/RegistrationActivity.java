package edu.ycp.cs.cs496.unbearable;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.client.ClientProtocolException;
import org.xml.sax.SAXException;

import edu.ycp.cs.cs496.unbearable.mobilecontrollers.GetLogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistrationActivity extends Activity{
	
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
        setContentView(R.layout.register_view);
        
      //Init buttons
    	Button SubmitMeButton = (Button) findViewById(R.id.SubmitNew);
        Button CancelButton = (Button) findViewById(R.id.cancelButton);
    
    // TODO: Set onClickListeners for buttons
    SubmitMeButton.setOnClickListener(new View.OnClickListener() {
		
    	//Clicked submit
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			try {
				submitInfo();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	});

    
    // Clicked cancel
    CancelButton.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			try {
				startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
		}
	});
    
    }
    
    //Submit Info
    public void submitInfo() throws URISyntaxException, ClientProtocolException,
  	IOException, ParserConfigurationException, SAXException{
    	//Initialize controllers and get field value
  		EditText username = (EditText) findViewById(R.id.new_username);
  		EditText password = (EditText) findViewById(R.id.new_password);
  		EditText password2 = (EditText) findViewById(R.id.check_new_password);
  		//If nothing is entered, return error
  		if(IsEmpty(username) || IsEmpty(password) || IsEmpty(password2) )
  		{
  			Toast.makeText(RegistrationActivity.this, "Please fill out all fields", Toast.LENGTH_SHORT).show();
  		}
  		//Another controller and parse the string value
  		else
  		{
  			String newUsername = username.getText().toString();
  			String newPassword = password.getText().toString();
  			String checkPassword = password2.getText().toString();
  			if(newPassword.equals(checkPassword))
  			{
  				//Insert controller here to post it to database
  	  			Toast.makeText(RegistrationActivity.this,"Registration Successful!", Toast.LENGTH_LONG).show();
  	  			GetLogin logControl = new GetLogin();
  	  			logControl.doPostReguest(newUsername, newPassword);
  	  			startActivity(new Intent(RegistrationActivity.this, MainActivity.class));
  				

  			}
  			else
  			{
  				Toast.makeText(RegistrationActivity.this,"Passwords don't match!", Toast.LENGTH_LONG).show();
  			}
  		}
     }

}
