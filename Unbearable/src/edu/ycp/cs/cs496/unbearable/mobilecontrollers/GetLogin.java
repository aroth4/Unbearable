package edu.ycp.cs.cs496.unbearable.mobilecontrollers;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.impl.client.DefaultHttpClient;

import edu.ycp.cs.cs496.unbearable.model.Login;
import edu.ycp.cs.cs496.unbearable.model.json.JSON;


public class GetLogin {
	public Login getLogin(String username, String password) throws ClientProtocolException, URISyntaxException, IOException {
		return makeGetRequest(username, password);
	}

	private Login makeGetRequest(String username, String password) {
		// TODO: Implement method to issue get item request
				// Create HTTP client
				 		HttpClient client = new DefaultHttpClient();
						
						// Construct URI
						URI uri;
						uri = URIUtils.createURI("http", "10.0.2.2", 8081, "/inventory/" + itemName, 
								    null, null);

						// Construct request
						HttpGet request = new HttpGet(uri);
						
						// Execute request
						HttpResponse response = client.execute(request);

						// Parse response
						if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
							// Copy the response body to a string
							HttpEntity entity = response.getEntity();
							
							// Parse JSON
							return JSON.getObjectMapper().readValue(entity.getContent(), Item.class);
						} 
				// Return null if invalid response
				return null;
	}
}
