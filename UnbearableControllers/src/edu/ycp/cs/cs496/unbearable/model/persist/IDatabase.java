package edu.ycp.cs.cs496.unbearable.model.persist;

import edu.ycp.cs.cs496.unbearable.model.Login;

/**
 * Persistence operations for fruit web service.
 */
public interface IDatabase {

	public boolean getLogin(String username, String password);
	

	public Login postLogin(String username, String password);

}
