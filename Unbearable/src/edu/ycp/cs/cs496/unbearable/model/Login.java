package edu.ycp.cs.cs496.unbearable.model;

public class Login {
	
/**
 * Class to represent an inventory item.
 */
	private String username;
	private String password;

	/**
	 * Default constructor.
	 */
	public Login() {
		
	}
	
	/**
	 * Constructor.
	 * 
	 * @param username  login name
	 * @param password  login password
	 */
	public Login(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	/**
	 * Set the item name.
	 * 
	 * @param name the item name to set
	 */
	public void setUsername(String name) {
		this.username = name;
	}
	
	/**
	 * Get the item name.
	 * 
	 * @return the item name.
	 */
	public String getName() {
		return username;
	}
	
	/**
	 * Set the item quantity.
	 * 
	 * @param quantity the item quantity.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Get the item quantity.
	 * 
	 * @return the item quantity.
	 */
	public String getPassword() {
		return password;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return username + " - " + password;
	}
}

