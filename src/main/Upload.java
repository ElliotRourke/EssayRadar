package main;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;

import servlets.ControllerCompare;

/**
 * The Upload class.
 * This class is responsible for handling the uploads from the user.
 * It is called by the ControllerCompare servlet and handles the sorting of document file types to their respective parser.
 * The class also handles incorrect file type uploads by throwing a new Servlet Exception.
 * The class has been designed so that it can be easily expanded upon.
 * @see ControllerCompare
 * @see Parser
 */
public class Upload {

	/**
	 * This method hands all uploads and sends them to their respective parser.
	 * @param path - The path used is the relative path for the web project - e.g "C:\..Installation path\Radar 2.0\WebContent\WEB-INF\data\..."
	 * This method does have the disadvantage of holding the uploaded file in the folder.
	 * To repeat uploads the file must be renamed or removed from the data folder.
	 * @throws ServletException - For incorrect file uploads.
	 * @throws IOException
	 */
	public void uploadFiles(String path) throws ServletException, IOException{

		FileParser fp = new FileParser();
		DocParser dp = new DocParser();
		try {
			if(path.endsWith(".txt")){ //***Checks what extension the candidate file ends with and assigns a parser
				fp.getName(path); //***gets the name of the file for storage
				fp.parse(path); //***parses the file
				fp.getTFIDF(); //***calculates the TFIDF
				fp.getCosineSimilarity(); //***Normalises TFIDF and gets similarity percentage
				fp.createReport(path); //***adds report to the database
				delete(path); //***deletes file from data folder after parsing
			}else if (path.endsWith(".docx")){
				dp.getName(path);
				dp.parse(path);
				dp.getTFIDF();
				dp.getCosineSimilarity();
				dp.createReport(path);
				delete(path);
			}else{
				throw new IOException();
			}

		}catch (IOException e) {
			//Catches wrong file type submissions
			throw new ServletException();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Delete method.
	 * Deletes the files after they have been uploaded.
	 * If the file cannot be deleted it returns the reason why and throws a new ServletException.
	 * @param path : Location of file.
	 * @throws IOException 
	 * @throws ServletException 
	 */
	public void delete(String path) throws ServletException{
		File tempFile = new File(path);
		
		System.out.println("Attempting to delete " + tempFile.getAbsolutePath());
		if (!tempFile.exists()){		
			System.out.println("  Doesn't exist");
			throw new ServletException();	
		}else if (!tempFile.canWrite()){	
			System.out.println("  No write permission");
			throw new ServletException();
		}else{
		  if (tempFile.delete()){
		    System.out.println("  Deleted!");
		  }else{
		    System.out.println("  Delete failed - reason unknown");
		    throw new ServletException();
		  }
		}
	}

}
