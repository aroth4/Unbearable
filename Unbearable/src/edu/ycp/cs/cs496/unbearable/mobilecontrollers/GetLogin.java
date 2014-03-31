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
import edu.ycp.cs.cs496.unbearable.model.Login;
import edu.ycp.cs.cs496.unbearable.persist.Database;
import edu.ycp.cs.cs496.unbearable.persist.IDatabase;


public class GetLogin {
	public boolean getLogin(String username, String password) {
		IDatabase db = Database.getInstance();
		return db.getLogin(username, password);
	}
}
