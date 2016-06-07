package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;

import servlets.ControllerCompare;
import servlets.ControllerResults;

/**
 * This is the ReportsDB class.
 * This class is responsible for establishing the connection to the database when storing reports.
 * And is responsible for storing the reports and retrieving the results of the document uploaded by the user.
 * The results are called by the ControllerResults class.
 * @see ControllerResults
 * @see Parser
 */
public class ReportsDB {
	private BasicDataSource basicDataSource;


	public ReportsDB(){
		basicDataSource = new BasicDataSource();
		basicDataSource.setDriverClassName("com.mysql.jdbc.Driver");
		basicDataSource.setUrl("jdbc:mysql://localhost?useSSL=false");
		basicDataSource.setUsername("admin");
		basicDataSource.setPassword("password");

		try {
			Connection connection = basicDataSource.getConnection();

			try {
				Statement statement = connection.createStatement();

				try {
					statement.executeUpdate("USE essayradar");
				}catch (SQLException noDatabaseException) {

					try {
						statement.executeUpdate("CREATE DATABASE essayradar");
						statement.executeUpdate("USE essayradar");
						statement.executeUpdate("CREATE TABLE users (username VARCHAR(256), password VARCHAR(256), PRIMARY KEY (username))");
						statement.executeUpdate("CREATE TABLE sessions (id INT NOT NULL, username VARCHAR(256), INDEX (username), PRIMARY KEY(id))");
						statement.executeUpdate("CREATE TABLE reports (id INT NOT NULL AUTO_INCREMENT,filename VARCHAR(256) NOT NULL,score DOUBLE NOT NULL, grade VARCHAR(30) NOT NULL,content MEDIUMBLOB NOT NULL,PRIMARY KEY(id))");
						statement.executeUpdate("CREATE TABLE urls (id INT NOT NULL, url VARCHAR(256), PRIMARY KEY(id))");
					}catch (SQLException noTableException) {

					}
				}finally {
					statement.close();
				}
			}finally {
				connection.close();
			}
		}catch (SQLException e) {

		}
	}

	/**
	 * This method stores the reports after they have been uploaded and compared with the corpus.
	 * It stores the file name, score and assigns an overall grade to the uploaded essay.
	 * @param filename - The name of the file.
	 * @param score - The similarity score it recieved.
	 * @param path - The path that it was stored in.
	 * @return - A new report comprising of the set values.
	 * @throws IOException 
	 * @see Upload
	 * @see ControllerCompare
	 */
	public Report storeReport(String filename, double score, String path) throws IOException {
		int id = 0;
		String grade;

		try {
			Connection connection = basicDataSource.getConnection();

			try {
				PreparedStatement statement = connection.prepareStatement("USE essayradar");
				try {
					statement.executeQuery();
					statement.close();
					statement = connection.prepareStatement("INSERT INTO reports(id,filename,score,grade,content) VALUES(?,?,?,?,?)");
					statement.setInt(1, id);
					statement.setString(2, filename);
					statement.setDouble(3, score);
					if(score >= 0.3){
						grade = "FAIL";
						statement.setString(4,grade);
					}else{
						grade = "PASS";
						statement.setString(4, grade);
					}
					try(InputStream inputStream = new FileInputStream(new File(path))){
						statement.setBlob(5, inputStream);
						statement.executeUpdate();
						statement.close();
						return new Report(id,filename,score,grade);
					}

				}catch (SQLException e) {
					e.printStackTrace();
				}finally {
					statement.close();
				}
			}finally {
				connection.close();
			}
		}catch (SQLException e) {

		}
		return null;
	}

	/**
	 * This method is used to display the results for the report uploaded by the user after the comparison has been completed.
	 * It works in conjunction with the ControllerResults class to display the report in a table that the controller creates.
	 * @see ControllerResults
	 * @throws SQLException
	 */
	public List<Report> list() throws SQLException {	
		List<Report> reports = new ArrayList<Report>();
		try (
				Connection connection = basicDataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement("SELECT id, filename, score, grade FROM reports ORDER BY id DESC LIMIT 1");
				ResultSet resultSet = statement.executeQuery();
				) {
			if(resultSet.next()) {
				Report report = new Report();
				report.setId(resultSet.getInt("id"));
				report.setFilename(resultSet.getString("filename"));
				report.setScore(resultSet.getLong("score"));
				report.setGrade(resultSet.getString("grade"));
				reports.add(report);
			}
		}

		return reports;
	}
}
