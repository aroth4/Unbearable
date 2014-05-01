package edu.ycp.cs.cs496.unbearable.servlets;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import edu.ycp.cs.cs496.unbearable.model.persist.DatabaseProvider;
import edu.ycp.cs.cs496.unbearable.model.persist.FakeDatabase;

public class DatabaseInitListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent e) {
		// Webapp is starting
		DatabaseProvider.setInstance(new FakeDatabase()); // TODO: use a real database 
		System.out.println("Database initialized!");
	}

	@Override
	public void contextDestroyed(ServletContextEvent e) {
		// Webapp is shutting down
		System.out.println("webapp is shut down");
	}

}
