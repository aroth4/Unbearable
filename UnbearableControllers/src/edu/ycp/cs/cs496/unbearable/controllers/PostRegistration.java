package edu.ycp.cs.cs496.unbearable.controllers;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

import edu.ycp.cs.cs496.unbearable.model.Login;
import edu.ycp.cs.cs496.unbearable.model.json.JSON;

/**
 * Controller to get an {@link Item} given the item name.
 */

public class PostRegistration {
	public boolean postItem(String username, String password) throws URISyntaxException, JsonGenerationException, JsonMappingException, IOException {

		return postReg(username,password);
	}
	
	public boolean postReg(String username, String password) throws URISyntaxException, JsonGenerationException, JsonMappingException, IOException {
		// TODO: Implement method to issue post request
		// Create HTTP client
 		HttpClient client = new DefaultHttpClient();
		// Construct URI
		URI uri;
		uri = URIUtils.createURI("http", "10.0.2.2", 8081, "/login/", 
				    null, null);

		// Create JSON object from Item
		Login newLogin = new Login();
		newLogin.setUsername(username);
		newLogin.setPassword(password);
		StringWriter sw = new StringWriter();
		JSON.getObjectMapper().writeValue(sw, newLogin);
		
		// Add JSON object to request
		StringEntity reqEntity = new StringEntity(sw.toString());
		reqEntity.setContentType("application/json");
		
		// Construct request
		HttpPost request = new HttpPost(uri);
		
		request.setEntity(reqEntity);
			
		// Execute request
		@SuppressWarnings("unused")
		HttpResponse response = client.execute(request);

		return true;
	}
}