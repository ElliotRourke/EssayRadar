package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ControllerBack.
 * This class is responsible for return the user to the previous page.
 * And logging them out if their session is invalid.
 */
@WebServlet("/back")
public class ControllerBack extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * This method returns the user to the previously forwarded page.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		if(session != null)
		    session.invalidate();
		request.getRequestDispatcher("/index.html").forward(request,response);
	}
}
