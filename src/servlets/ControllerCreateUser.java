package servlets;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.UsersDB;

/**
 * Servlet implementation class ControllerCreateUser
 * This class is responsible for creating users and adding them to the user database table.
 * It catches exceptions for invalid usernames and returns the administrator to the create user page.
 */
@WebServlet("/create")
public class ControllerCreateUser extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * This method checks for invalid usernames.
	 * Usernames that are already taken or have not been entered.
	 * It then passes the username and password entered to the database and adds it if it is valid.
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String a = request.getParameter("username");
		String name = a.toLowerCase();
		String pass = request.getParameter("password");
		
		Pattern pat = Pattern.compile("[:?!£@#$%^&*()]");
		Matcher mat = pat.matcher(name);
		
		UsersDB usersDB = new UsersDB();
		try{			
			if((!pass.equals("")) && (!name.equals("") && usersDB.checkUser(name) && (name.length() < 8) && (pass.length() < 13) && (!mat.find()))){
				usersDB.createUser(name, pass);
				response.sendRedirect("admin?s=");
			}else{
				request.setAttribute("message", "true");
				request.getServletContext().getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request,response);
			}
		}catch(NullPointerException e){
			request.setAttribute("message", "true");
			request.getServletContext().getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request,response);
		}catch(ServletException e){
			request.setAttribute("message", "true");
			request.getServletContext().getRequestDispatcher("/WEB-INF/view/admin.jsp").forward(request,response);
		}

	}

}
