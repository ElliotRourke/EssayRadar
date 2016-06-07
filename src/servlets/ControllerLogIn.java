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
 * This is the ControllerLogIn class.
 * This class is responsible for logging the user in, validating the log in and assigning the user a sessionID.
 * The class also checks for administrator log ins and sends users and admins to their respective pages.
 * @see Users
 * @see UsersDB
 */
@WebServlet("/login")
public class ControllerLogIn extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	/**
	 * This method logs the user in or checks if the user is already logged in.
	 * It checks if the user is the administrator and if the username and password is valid.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try{
		
			String username = request.getParameter("username");
			String password = request.getParameter("password");
	
			UsersDB usersDB = new UsersDB();
	
			if(usersDB.checkPrivellage(username, password)){
				adminLogin(request, response);
			}else{
				usersDB.logIn(username, password);
				int sessionID = usersDB.getSessionID(username);
	
				if (usersDB.isLoggedIn(sessionID)) {
					Cookie cookie = new Cookie("SessionID", "" + sessionID);
					cookie.setMaxAge(-1);
					response.addCookie(cookie);
	
					request.setAttribute("username", username);
					request.getServletContext().getRequestDispatcher("/WEB-INF/view/upload.jsp").forward(request,response);
	
				}else{
					request.setAttribute("incorrect", "true");
					request.getServletContext().getRequestDispatcher("/WEB-INF/view/loggedout.jsp").forward(request,response);
				}
			}
		}catch(ServletException e){
			request.setAttribute("incorrect", "true");
			request.getServletContext().getRequestDispatcher("/WEB-INF/view/loggedout.jsp").forward(request,response);
		}
	}


	/**
	 * This method is called if the administrators username and password is entered.
	 * It logs the session of the admin and sends them to the admin page.
	 */
	protected void adminLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		try{
			String username = request.getParameter("username");
			String password = request.getParameter("password");
	
			UsersDB usersDB = new UsersDB();
	
			usersDB.logIn(username, password);
			int sessionID = usersDB.getSessionID(username);
	
			if (usersDB.isLoggedIn(sessionID)) {
				Cookie cookie = new Cookie("SessionID", "" + sessionID);
				cookie.setMaxAge(-1);
				response.addCookie(cookie);
	
				response.sendRedirect("admin");
			}else{
				request.setAttribute("incorrect", "true");
				request.getServletContext().getRequestDispatcher("/WEB-INF/view/loggedout.jsp").forward(request,response);
			}
		}catch(ServletException e){
			request.setAttribute("incorrect", "true");
			request.getServletContext().getRequestDispatcher("/WEB-INF/view/loggedout.jsp").forward(request,response);
		}
	}
}
