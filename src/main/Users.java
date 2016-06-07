package main;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;

import org.mindrot.jbcrypt.BCrypt;

/**
 * This is the Users class.
 * The parent class of UsersDB.
 * It is responsible for hashing passwords, checking if passwords are valid and creating the users sessionID.
 * Furthermore the class is a template for the UsersDB class and defines what methods it must have.
 */
public abstract class Users {

	/**
	 * This method employs the Bcrypt library to hash passwords.
	 * It generates salt and adds it to the hashing ensuring unique and secure password hashes.
	 * @param password - The plain text password the user entered.
	 * @return - Returns the now hashed password.
	 */
	public String hashPassword(String password){
		// Hash a password for the first time and add salt
		String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
		return hashed;
	}

	/**
	 * This method checks whether or not a plain text password matches a hash password.
	 * @param user - The current user attempting to login.
	 * @param password - The password entered by the user.
	 * @return - Returns true if the plain text password matches the hashed password when hashed, else returns false.
	 */
	public boolean isValid(User user, String password){
		try{
			return BCrypt.checkpw(password,user.getPassword());
		}catch(NullPointerException e){
			return false;
		}
	}

	//Check for more secure
	/**
	 * This method creates the sessionID given to users for the duration of their log in.
	 * @return - Returns the sessionID that is stored in the sessions database table.
	 */
	public int createSessionID(){
		byte[] sessionIDBytes = new byte[4];
		int sessionID = 0;
		try {
			do {
				SecureRandom.getInstance("SHA1PRNG").nextBytes(sessionIDBytes);
				sessionID = (new BigInteger(sessionIDBytes)).intValue();
			}
			while(isLoggedIn(sessionID) || (sessionID < 0));
		}catch (NoSuchAlgorithmException e) {

		}
		return sessionID;
	}

	public abstract void logIn(String name, String password) throws ServletException;
	public abstract void logOut(int sessionID);

	public abstract User createUser(String username, String password) throws ServletException;
	public abstract boolean isLoggedIn(int sessionID);
	public abstract int getSessionID(String username);
	public abstract User getUser(String username) throws ServletException;
	public abstract User getUser(int sessionID) throws ServletException;
	public abstract boolean checkUser(String username);
	public abstract boolean checkPrivellage(String username, String password) throws ServletException;
	public abstract List<User> list() throws SQLException, ServletException;

}
