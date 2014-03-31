package edu.ycp.cs.cs496.unbearable.persist;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
			if (login.getName().equals(username) && login.getPassword().equals(password)) {
				// return a copy
				return new Login(login.getName(), login.getPassword());
			}
		}
		return null;
	}

	@Override
	public Login postLogin(String username, String password) {
		// TODO Auto-generated method stub
		boolean result = false;
		
		for (Login login: LoginList){
			if (login.getName().equals(username) && login.getPassword().equals(password)){
				result = true;
				break;
			}
		}
		if (result == false){
			 return new Login(username, password);
		}
		return null;
	}

}
