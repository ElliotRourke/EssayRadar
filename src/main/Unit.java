package main;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;

/**
 * UNIT TESTING FOR ESSAY RADAR
 * @author User
 *
 */
public class Unit {

	public static void main(String[] args) {

		/*
		 * PARSED > COMPARED > CALCULATED > STORED > REPORTED
		 * BECAREFUL WHEN RUNNING UNIT TEST YOU STORE MULTIPLE TEST.TXT/DOCX
		 */
		
		//Database Users Table Unit Test
		Users usersDB = new UsersDB();
		try {
			
			//premade user
			User user1 = new User("john","doe");
			System.out.println(user1);
	
			//creates user
			usersDB.createUser("jane", "doe");
	
			//Shows current session ID and hashed password stored in database
			System.out.println("SessionID: " + usersDB.createSessionID());
			System.out.println("john: " + usersDB.getUser("john"));
	
			//Logs user in
			usersDB.logIn("john", "doe");
			
		} catch (ServletException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		//checks if the user is currently signed in
		System.out.println("SessionID: " + usersDB.getSessionID("john"));
		System.out.println("Is the user signed in?(True = Yes /// False = No): " + usersDB.isLoggedIn(usersDB.getSessionID("john")));

		//Logs user out
		usersDB.logOut(usersDB.getSessionID("john"));

		//***Parsing and Database Parsing Unit Test***
		FileParser fp = new FileParser();
		DocParser dp = new DocParser();
		String path = "F:/Eclipse/EssayRadar/src/Test.text";
		
		try {
			if(path.endsWith(".txt")){ //***Checks what extension the candidate file ends with and assigns a parser

				fp.getName(path); //***gets the name of the file for storage

				fp.parse(path); //***parses the file

				fp.getTFIDF(); //***calculates the TFIDF

				fp.getCosineSimilarity(); //***Normalizes TFIDF and gets similarity percentage

				//ADD IN crawler shit

				fp.createReport(path); //***adds report to the database

			}else if (path.endsWith(".docx")){
				dp.getName(path);
				dp.parse(path);
				dp.getTFIDF();
				dp.getCosineSimilarity();
				//ADD IN crawler shit
				dp.createReport(path);
			}else{
				throw new IOException();
			}

		}catch (IOException e) {
			//To prompt new upload
			e.printStackTrace();
		} catch (SQLException e) {
			//If reports fail
			e.printStackTrace();
		}
	}

}
