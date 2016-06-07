package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.User;
import main.UsersDB;

/**
 * This is the ControllerAdmin class.
 * This class is responsible for displaying the usernames currently stored in the user database table.
 */
@WebServlet("/admin")
public class ControllerAdmin extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
	
	/**
	 * This method retrieves a list of currently taken usernames from the database.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String username = request.getParameter("username");
		
		UsersDB usersDB = new UsersDB();
		try {
			List<User> users = usersDB.list();
			request.setAttribute("users", users);
			request.setAttribute("username", username);
			request.getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request, response);
		} catch (SQLException e) {
			throw new ServletException("Cannot obtain products from DB", e);
		}
	}
}
