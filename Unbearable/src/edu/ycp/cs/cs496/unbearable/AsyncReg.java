package edu.ycp.cs.cs496.unbearable;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import edu.ycp.cs.cs496.unbearable.model.Login;
import edu.ycp.cs.cs496.unbearable.model.json.JSON;
import android.os.AsyncTask;

public class AsyncReg extends AsyncTask<String, String, String>{
	public static boolean checkLogin;
	@Override
	protected String doInBackground(String... params) {
		// TODO Auto-generated method stub
		try {
			System.out.println(params[0] + " " + params [1]);
			Registration(params[0], params[1]);
			//System.out.println(checkLogin);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
public boolean Registration(String username, String password)throws URISyntaxException, JsonGenerationException, JsonMappingException, IOException{
		
		// Create HTTP client
		HttpClient client = new DefaultHttpClient();
		// Construct URI

		String uri = "http://10.0.2.2:8081/newlogin";
		
		//loginPost getPost = new loginPost();
		
		// Create a Login object containing the username and password
		Login login = new Login();
		login.setUsername(username);
		login.setPassword(password);
		
		// Encode the Login object's data as JSON
		StringWriter sw = new StringWriter();
		JSON.getObjectMapper().writeValue(sw, login);
	
		// Create a POST request containing the JSON-encoded Login object
		StringEntity reqEntity = new StringEntity(sw.toString());
		reqEntity.setContentType("application/json");
		// Construct request
		HttpPost request = new HttpPost(uri);
		
		// process request
		request.setEntity(reqEntity);
		
		HttpResponse response = client.execute(request);
		System.out.println("Response");
		System.out.println(response.getStatusLine().getStatusCode());
		System.out.println(username + " " + password);
		// Parse response
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			// Copy the response body to a string
			HttpEntity entity = response.getEntity();
			
			if (entity == null){
				System.out.println("registration failed!");
				checkLogin = false;
				return false;
			}else{
				System.out.println("Registration is a SUCCESS");
				checkLogin = true;
				return true;
			}
			
		}
		else{	
			System.out.println("FAILED");
			checkLogin = false;
			return false;
		}
		
	}

}

