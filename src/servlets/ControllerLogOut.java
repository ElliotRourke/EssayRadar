package servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.UsersDB;

/**
 * This is the ControllerLogOut class.
 * This class is responsible for logging the user out and deleting the sessionID from the cookies.
 * @see Users
 * @see UsersDB
 */
@WebServlet("/logout")
public class ControllerLogOut extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * This logs the user out by deleteing the cooking and the session.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			for (Cookie cookie : request.getCookies()) {
				if (cookie.getName().equals("SessionID")) {
					UsersDB usersDB = new UsersDB();
					usersDB.logOut(new Integer(cookie.getValue()).intValue());

					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
		}
		catch (NullPointerException e) {
		}
		request.setAttribute("incorrect", "false");
		request.getServletContext().getRequestDispatcher("/index.html").forward(request,response);
	}

}
