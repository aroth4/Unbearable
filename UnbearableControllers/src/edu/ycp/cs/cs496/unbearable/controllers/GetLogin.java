package edu.ycp.cs.cs496.unbearable.controllers;

import edu.ycp.cs.cs496.unbearable.model.Login;
import edu.ycp.cs.cs496.unbearable.model.persist.DatabaseProvider;
import edu.ycp.cs.cs496.unbearable.model.persist.IDatabase;


public class GetLogin {
	public Login getLogin(String username, String password) {
		IDatabase db = DatabaseProvider.getInstance();
		return db.getLogin(username, password);
	}
	
	public Login doPostReguest(String user, String pass){
		
		IDatabase db = DatabaseProvider.getInstance();
		return db.postLogin(user, pass);
	}
}
