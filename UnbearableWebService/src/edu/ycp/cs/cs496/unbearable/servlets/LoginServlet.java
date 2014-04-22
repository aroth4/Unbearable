package edu.ycp.cs.cs496.unbearable.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ycp.cs.cs496.unbearable.controllers.GetLogin;
import edu.ycp.cs.cs496.unbearable.model.Login;
import edu.ycp.cs.cs496.unbearable.model.json.JSON;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		Login login = JSON.getObjectMapper().readValue(req.getReader(), Login.class);
		String userName = login.getName();
		String password = login.getPassword();
		
		// Use a controller to determine if this username/password corresponds to an existing user
		GetLogin controller = new GetLogin();
		login = controller.getLogin(userName, password);
		
		if (login == null) {
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			resp.setContentType("application/json");
			JSON.getObjectMapper().writeValue(resp.getWriter(), (Boolean)false);
		} else {
			// The login succeeded: set information in the session to
			// record the fact that the client successfully logged in
			req.getSession().setAttribute("login", login);
			
			// Set status code and content type
			resp.setStatus(HttpServletResponse.SC_OK);
			resp.setContentType("application/json");
					
			// Return the item in JSON format
			JSON.getObjectMapper().writeValue(resp.getWriter(), (Boolean)true);
		}
	}
}
