package edu.ycp.cs.cs496.unbearable.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.ycp.cs.cs496.unbearable.model.Login;
import edu.ycp.cs.cs496.unbearable.model.json.JSON;

public class HighScoreServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// Check to make sure that the user is logged in
		Login login = (Login) req.getSession().getAttribute("login");
		if (login == null) {
			// This user is not logged in
			resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
			resp.setContentType("application/json");
			JSON.getObjectMapper().writeValue(resp.getWriter(), (Boolean)false);
		} else {
			// The user is logged in
		}
	}
}
