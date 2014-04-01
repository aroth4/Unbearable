package edu.ycp.cs.cs496.unbearable.persist;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import android.widget.Toast;

import edu.ycp.cs.cs496.unbearable.RegistrationActivity;
import edu.ycp.cs.cs496.unbearable.model.Login;

public class FakeDatabase implements IDatabase {
private List<Login> LoginList;
	
	public FakeDatabase() {
		LoginList = new ArrayList<Login>();
		
		// Populate initial list with master account
		LoginList.add(new Login("admin", "admin"));

	}
	@Override
	public boolean getLogin(String username, String password) {
		//Check for existing login
		for(Login login : LoginList)
		{
			if (login.getName().equals(username)) {
				if (login.getPassword().equals(password))
				{
				//return a copy
				//return new Login(login.getName(), login.getPassword());
				return true;
				}
			}
		}
		return false;
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
			Login newLogin = new Login(username, password);
			LoginList.add(newLogin);
			return newLogin;
		}
		return null;
	}

}
