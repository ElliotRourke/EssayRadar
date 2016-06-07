package main;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 * This is the UsersDB class.
 * This class is the child of Users.
 * This class carries out the database functions defined in the Users class.
 * It stores, gets, checks and creates users.
 * @see Users
 * @see User
 */
public class UsersDB extends Users {	
	private BasicDataSource basicDataSource;

	public UsersDB(){
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

					}
					catch (SQLException noTableException) {
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
	 * This method is responsible for creating a new user.
	 * It retrieves the username and password input by the ControllerCreateUser class.
	 * It then inserts this information into the users database.
	 * @throws ServletException 
	 * @see ControllerCreateUser
	 */
	@Override
	public User createUser(String username, String password) throws ServletException {
		String hashedPassword = hashPassword(password);

		try {
			Connection connection = basicDataSource.getConnection();

			try {
				PreparedStatement statement = connection.prepareStatement("USE essayradar");

				try {
					statement.executeQuery();
					statement.close();

					statement = connection.prepareStatement("INSERT INTO users(username,password) VALUES(?,?)");
					statement.setString(1, username);
					statement.setString(2, hashedPassword);
					statement.executeUpdate();
					statement.close();

					return new User(username, hashedPassword);
				}catch (SQLException e) {

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
	 * This method logs the user into the system.
	 * Firstly it sends the session ID to the logOut method to hold so that the users session can be logged.
	 * Then it gets the user and checks if they are in the database table users.
	 * It then checks if the password entered for the user is valid.
	 * Finally it logs the user in.
	 * It works in tangent with ControllerLogIn.
	 * @throws ServletException 
	 * @see ControllerLogIn
	 */
	@Override
	public void logIn(String username, String password) throws ServletException {
		logOut(getSessionID(username));

		User user = getUser(username);

		if(isValid(user,password)){
			int sessionID = createSessionID();

			try {
				Connection connection = basicDataSource.getConnection();

				try {
					PreparedStatement statement = connection.prepareStatement("USE essayradar");

					try {
						statement.executeQuery();
						statement.close();

						statement = connection.prepareStatement("INSERT INTO sessions(id,username) VALUES(?,?)");
						statement.setInt(1, sessionID);
						statement.setString(2, username);
						statement.executeUpdate();
						statement.close();

					}catch (SQLException e) {

					}finally {
						statement.close();
					}
				}finally {
					connection.close();
				}
			}catch (SQLException e) {

			}
		}
	}

	/**
	 * It has the sessionID from the logged in user.
	 * It then fetches the the record where the id matches the stored id in the sessions table of the database.
	 * The session is then delete logging the user out.
	 * This works in tangent with the ControllerLogOut class.
	 * @see ControllerLogOut
	 */
	@Override
	public void logOut(int sessionID) {
		try {
			Connection connection = basicDataSource.getConnection();

			try {
				PreparedStatement statement = connection.prepareStatement("USE essayradar");

				try {
					statement.executeQuery();
					statement.close();

					statement = connection.prepareStatement("DELETE FROM sessions WHERE id=?");
					statement.setInt(1, sessionID);
					statement.executeUpdate();
					statement.close();
				}catch (SQLException e) {

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
	 * This method checks if the sessions ID is already logged in the database sessions table.
	 * If the session exists it returns true, else it returns false.
	 */
	@Override
	public boolean isLoggedIn(int sessionID) {
		try {
			Connection connection = basicDataSource.getConnection();

			try {
				PreparedStatement statement = connection.prepareStatement("USE essayradar");

				try {
					statement.executeQuery();
					statement.close();

					statement = connection.prepareStatement("SELECT COUNT(*) FROM sessions WHERE id=?");
					statement.setInt(1, sessionID);

					ResultSet resultSet = statement.executeQuery();

					try {
						resultSet.next();
						return resultSet.getInt(1) > 0;
					}catch (SQLException e) {

					}finally {
						resultSet.close();
					}
				}catch (SQLException e) {

				}finally {
					statement.close();
				}
			}finally {
				connection.close();
			}
		}catch (SQLException e) {

		}
		return false;
	}

	/**
	 * This method retrieves the sessionID linked to the username currently logged in.
	 * It is used to log users  out of the program by returning their sessionID linked to the username they logged in with.
	 */
	@Override
	public int getSessionID(String username) {
		try {
			Connection connection = basicDataSource.getConnection();

			try {
				PreparedStatement statement = connection.prepareStatement("USE essayradar");

				try {
					statement.executeQuery();
					statement.close();

					statement = connection.prepareStatement("SELECT id FROM sessions WHERE username=?");
					statement.setString(1, username);

					ResultSet resultSet = statement.executeQuery();

					try {
						resultSet.next();
						return resultSet.getInt(1);
					}catch (SQLException e) {

					}finally {
						resultSet.close();
					}
				}catch (SQLException e) {

				}finally {
					statement.close();
				}
			}finally {
				connection.close();
			}
		}catch (SQLException e) {

		}
		return 0;
	}

	/**
	 * This method is used to compare passwords when users attempt to log in.
	 * It retrieves the password stored in the users table of the database and compares it to the password entered by the user.
	 * This works in tangent with the ControllerLogIn class.
	 * @throws ServletException 
	 * @see ControllerLogIn
	 */
	@Override
	public User getUser(String username) throws ServletException {
		try {
			Connection connection = basicDataSource.getConnection();

			try {
				PreparedStatement statement = connection.prepareStatement("USE essayradar");

				try {
					statement.executeQuery();
					statement.close();

					statement = connection.prepareStatement("SELECT password FROM users WHERE username=?");
					statement.setString(1, username);

					ResultSet resultSet = statement.executeQuery();

					try {
						resultSet.next();
						return new User(username, resultSet.getString(1));
					}catch (SQLException e) {

					}finally {
						resultSet.close();
					}
				}catch (SQLException e) {

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
	 * This retrieves the username of the current session holder.
	 * @throws ServletException 
	 */
	@Override
	public User getUser(int sessionID) throws ServletException {
		try {
			Connection connection = basicDataSource.getConnection();

			try {
				PreparedStatement statement = connection.prepareStatement("USE essayradar");

				try {
					statement.executeQuery();
					statement.close();

					statement = connection.prepareStatement("SELECT username FROM sessions WHERE id=?");
					statement.setInt(1, sessionID);

					ResultSet resultSet = statement.executeQuery();

					try {
						resultSet.next();
						return getUser(resultSet.getString(1));
					}catch (SQLException e) {

					}finally {
						resultSet.close();
					}
				}catch (SQLException e) {

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
	 * This method checks whether or not the user exists in the database.
	 * It is used in conjucntion with the ControllerCreateUser to check if the username is taken.
	 * @see ControllerCreateUser
	 */
	@Override
	public boolean checkUser(String username) {
		try {
			Connection connection = basicDataSource.getConnection();

			try {
				PreparedStatement statement = connection.prepareStatement("USE essayradar");

				try {
					statement.executeQuery();
					statement.close();

					statement = connection.prepareStatement("SELECT username FROM users WHERE id=?");
					statement.setString(1, username);

					ResultSet resultSet = statement.executeQuery();

					try {
						resultSet.next();
						return false;
					}catch (SQLException e) {

					}finally {
						resultSet.close();
					}
				}catch (SQLException e) {

				}finally {
					statement.close();
				}
			}finally {
				connection.close();
			}
		}catch (SQLException e) {

		}
		return true;
	}

	/**
	 * This method checks if the user is the administrator.
	 * If the user is the administrator it assigns them a session ID.
	 * This works with ControllerAdmin to send the administrator to the admin page.
	 * @throws ServletException 
	 * @see ControllerAdmin
	 */
	@Override
	public boolean checkPrivellage(String username, String password) throws ServletException{
		logOut(getSessionID(username));

		User user = getUser(username);

		if(username.equals("administrator")){

			if(isValid(user,password)){
				int sessionID = createSessionID();

				try {
					Connection connection = basicDataSource.getConnection();

					try {
						PreparedStatement statement = connection.prepareStatement("USE essayradar");

						try {
							statement.executeQuery();
							statement.close();

							statement = connection.prepareStatement("INSERT INTO sessions(id,username) VALUES(?,?)");
							statement.setInt(1, sessionID);
							statement.setString(2, username);
							statement.executeUpdate();
							statement.close();
							return true;

						}catch (SQLException e) {

						}finally {
							statement.close();
						}
					}finally {
						connection.close();
					}
				}catch (SQLException e) {

				}
			}
		}
		return false;
	}

	/**
	 * This method fetches all users from the users table in the database.
	 * It works with ControllerAdmin and ControllerCreateUser to allow an up-to-date list of taken usernames to be viewed when creating a user.
	 * @throws ServletException 
	 * @see ControllerAdmin
	 * @see ControllerCreateUser
	 */
	@Override
	public List<User> list() throws SQLException, ServletException {	
		List<User> users = new ArrayList<User>();
		try (
				Connection connection = basicDataSource.getConnection();
				PreparedStatement statement = connection.prepareStatement("SELECT username FROM users");
				ResultSet resultSet = statement.executeQuery();
				) {
			while(resultSet.next()) {
				User user = new User();
				user.setUsername(resultSet.getString("username"));
				users.add(user);
			}
		}

		return users;
	}
}
