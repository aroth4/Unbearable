package edu.ycp.cs.cs496.unbearable.model;

import static org.junit.Assert.*;

import org.junit.Test;
/**
 * Test the method in the Login class 
 * @author hdao2
 *
 */
public class LoginTest {

	public Login log;
	
	public LoginTest(){
		log = new Login();
	}
	/**
	 * Test getUserName() method
	 */
	@Test
	public void testGetUserName() {
		log.setUsername("Crash");
		assertEquals("Crash", log.getName());
	}
	
	/**
	 * Test setUserName() method
	 */
	@Test
	public void testSetuserName(){
		log.setUsername("Dash");
		assertEquals("Dash", log.getName());
	}
	/**
	 * Test setPassword
	 */
	@Test
	public void testSetPassword(){
		log.setPassword("dashing");
		assertEquals("dashing", log.getPassword());
	}
	/**
	 * Test getPassword
	 */
	@Test
	public void testGetPassword(){
		log.setPassword("dashing");
		assertEquals("dashing", log.getPassword());
	}

}
