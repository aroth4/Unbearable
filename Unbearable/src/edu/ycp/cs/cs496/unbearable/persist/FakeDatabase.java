package edu.ycp.cs.cs496.unbearable.persist;

import java.util.ArrayList;
import java.util.List;

import edu.ycp.cs.cs496.unbearable.model.Login;

public class FakeDatabase implements IDatabase {
private List<Login> LoginList;
	
	public FakeDatabase() {
		LoginList = new ArrayList<Login>();
		
		// Populate initial list with master account
		LoginList.add(new Login("admin", "admin"));

	}
	@Override
	public Login getLogin(String itemName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Login postLogin(String username, String password) {
		// TODO Auto-generated method stub
		return null;
	}

}
