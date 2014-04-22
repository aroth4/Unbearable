package edu.ycp.cs.cs496.unbearable.model.persist;

import edu.ycp.cs.cs496.unbearable.model.Login;

/**
 * Persistence operations for fruit web service.
 */
public interface IDatabase {

	//public boolean getLogin(String username, String password);
	/**
	 * Get the {@link Login} object associated with the given
	 * username and password.
	 * 
	 * @param username the username
	 * @param password the password
	 * @return the {@link Login} object, or null if there is no such username/password
	 */
	public Login getLogin(String username, String password);

	public Login postLogin(String username, String password);

}
