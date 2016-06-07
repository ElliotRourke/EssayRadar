package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;


/**
 * The Corpus Class.
 * This class handles the fetching and parsing of stored reports from the database.
 * And handles adding these documents to a list which will then be used to compare against other uploaded documents.
 * This is the 'bag' of documents.
 * @see Comparator
 */

public class Corpus {

	private BasicDataSource basicDataSource;
	private Blob blob;
	private List<List<String>> documents = new ArrayList<List<String>>(); // A BAG OF DOCUMENTS
	private List<String> document = new ArrayList<String>();

	/**
	 * This constructor is used to establish a connection to the database and ensure that the tables are created.
	 * And the correct database is in use.
	 */
	public Corpus(){
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
	 * This method pulls the stored documents off the database table Reports.
	 * The BLOB data is then parsed and stored as a list of Strings called 'document'.
	 * The list of Strings is then added to a list of documents called 'document' which is used to compare uploaded documents.
	 * @throws IOException
	 */
	public void generateCorpus() throws IOException {
		ResultSet resultSet = null;
		PreparedStatement pstmt = null;
		String query = "SELECT * FROM reports";

		try {
			Connection connection = basicDataSource.getConnection();

			try {
				PreparedStatement statement = connection.prepareStatement("USE essayradar");
				try {
					pstmt = connection.prepareStatement(query);
					resultSet = pstmt.executeQuery();

					while(resultSet.next()){
						Blob blob = resultSet.getBlob("content");

						//Parsing and adding to document and documents
						InputStream blobInputStream = blob.getBinaryStream();

						try(BufferedReader br = new BufferedReader(new InputStreamReader(blobInputStream))){
							StringBuilder sb = new StringBuilder();
							String line = null;
							while((line = br.readLine()) != null){
								sb.append(line);
							}
							String[] tokenTerms = sb.toString().toLowerCase().replaceAll("[\\W&&[^\\s]]","").split("\\W+");
							for(String token : tokenTerms){
								document.add(token);
							}
							add(document);
						}
					}
				}finally {
					statement.close();
					resultSet.close();
					pstmt.close();
				}
			}finally {
				connection.close();
			}
		}catch (SQLException e) {

		}
	}
	/**
	 * Getter and setter for the 'documents' list.
	 * Adds the document to the 'bag' of documents.
	 * @param doc
	 */
	public void add(List<String> doc){
		documents.add(document);
	}

	public List<List<String>> getCorpus(){
		return documents;
	}

	/**
	 * Getter and setter of the BLOB data from the database.
	 * @param blob
	 */
	public void setBlob(Blob blob) {
		this.blob = blob;
	}

	public Blob getBlob() {
		return blob;
	}
}
