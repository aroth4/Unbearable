package edu.ycp.cs.cs496.unbearable.model.persist;

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
	public Login getLogin(String username, String password) {
		//Check for existing login
		for(Login login : LoginList)
		{
			if (login.getUsername().equals(username)) {
				if (login.getPassword().equals(password))
				{
				//return a copy
				return new Login(login.getUsername(), login.getPassword());
				}
			}
		}
		return null;
	}

	@Override
	public Login postLogin(String username, String password) {
		// Add account if it does not exist
		boolean result = false;
		//Check to make sure
		for (Login login: LoginList){
			if (login.getUsername().equals(username) && login.getPassword().equals(password)){
				result = true;
				break;
			}
		}
		if (result == false){
			Login newLogin = new Login(username, password);
			LoginList.add(newLogin);
			return newLogin;
		}
		return null;
	}

}
