package edu.ycp.cs.cs496.unbearable.model.persist;

public class DatabaseProvider {
	private static IDatabase theInstance;
	
	public static void setInstance(IDatabase db) {
		theInstance = db;
		
	}
	
	public static IDatabase getInstance() {
		return theInstance;
	}
}
