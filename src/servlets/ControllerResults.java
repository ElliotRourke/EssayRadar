package servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import main.Report;
import main.ReportsDB;
import main.Upload;

/**
 * This is the ControllerResults class.
 * This class is responsible for displaying the result of the comparison to the user after a successful upload.
 * @see ReportsDB
 * @see Upload
 */
@WebServlet("/result")
public class ControllerResults extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Displays the results of their upload to the user by fetching the report from the database.
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		ReportsDB reportsDB = new ReportsDB();
		try {
			List<Report> reports = reportsDB.list();
			request.setAttribute("reports", reports);
			request.getRequestDispatcher("/WEB-INF/view/results.jsp").forward(request, response);
		} catch (SQLException e) {
			throw new ServletException("Cannot obtain products from DB", e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doGet(request,response);
	}


}
