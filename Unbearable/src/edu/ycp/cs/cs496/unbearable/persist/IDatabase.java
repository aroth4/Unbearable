package edu.ycp.cs.cs496.unbearable.persist;

import java.util.List;

import edu.ycp.cs.cs496.unbearable.model.Login;

/**
 * Persistence operations for fruit web service.
 */
public interface IDatabase {

	public Login getLogin(String username, String password);
	

	public Login postLogin(String username, String password);

}
