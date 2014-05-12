package edu.ycp.cs.cs496.unbearable.servlets;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.ycp.cs.cs496.unbearable.model.persist.DatabaseProvider;
import edu.ycp.cs.cs496.unbearable.model.persist.DerbyDatabase;
import edu.ycp.cs.cs496.unbearable.model.persist.FakeDatabase;

public class DatabaseInitListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent e) {
		// fake databsase
//		DatabaseProvider.setInstance(new FakeDatabase()); 
//		System.out.println("FakeDatabase initialized!"); 
		//real database
		DatabaseProvider.setInstance(new DerbyDatabase()); 
		System.out.println("DerbyDatabase initialized!"); 
	}

	@Override
	public void contextDestroyed(ServletContextEvent e) {
		// Webapp is shutting down
	}
	

}
