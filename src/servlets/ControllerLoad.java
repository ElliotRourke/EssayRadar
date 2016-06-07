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
 * This is the ControllerLoad class.
 * This class is responsible for determining which page to give the user when they load the browser.
 * It uses the sessionID stored in the users cookies to give them the logged in page or the logged out page.
 * @see UsersDB
 */
@WebServlet("/load")
public class ControllerLoad extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	/**
	 * Checks what page the user should be sent to by checking if the cookie's session ID is valid.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		int sessionID = 0;
		
		try{
			for(Cookie cookie : request.getCookies()){
				if(cookie.getName().equals("SessionID")){
					sessionID = new Integer(cookie.getValue()).intValue();
				}
			}
		}catch (NullPointerException e){
				
		}
		
		UsersDB usersDB = new UsersDB();
		
		if(usersDB.isLoggedIn(sessionID)){
			request.setAttribute("username", usersDB.getUser(sessionID).getUsername());
			request.getServletContext().getRequestDispatcher("/WEB-INF/view/upload.jsp").forward(request,response);
		}else{
			request.setAttribute("incorrect", "false");
			request.getServletContext().getRequestDispatcher("/WEB-INF/view/loggedout.jsp").forward(request,response);
		}
		
	}
	
}
