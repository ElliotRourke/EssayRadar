package main;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;

/**
 * This is the User class.
 * The user class is responsible for defining what must be included in a user object.
 * It sets and gets the values used for the user object.
 * It implements the Comparable interface for use in the UsersDB class.
 */
public class User implements Comparable<User> {

	private String username;
	private String password;

	public User(String username, String password) throws ServletException{
		setUsername(username);
		setPassword(password);
	}

	public User(){

	}

	/**
	 * Regex here ensures that the username only contains numbers and letters and must be 1 letter or more.
	 * @param username - The username of the user.
	 * @throws ServletException 
	 */
	public void setUsername(String username) throws ServletException {
		
		String pattern = "^[a-zA-Z0-9]{3,15}$";
		Pattern pat = Pattern.compile("[:?!£@#$%^&*()]");
		Matcher mat = pat.matcher(username);
		
		if(username.matches(pattern) && (!mat.find())){
			this.username = username;
		}else{
			throw new ServletException();
		}
	}

	public String getUsername() {
		return username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassword() {
		return password;
	}


	@Override
	public int compareTo(User user) {
		return getUsername().compareTo(user.getUsername());
	}

	@Override
	public String toString() {
		return new String(getUsername() + ":" + getPassword());
	}
}
