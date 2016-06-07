package main;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 * This is the Crawler class.
 * This class is responsible for getting the URLs from the URL database and scraping the content from them.
 * This content is added to the corpus and used in the comparison.
 *@see Comparator
 *@see Corpus
 */
public class Crawler {

	private String url;
	private List<String> urlList = new ArrayList<String>();
	private List<String> webContent = new ArrayList<String>();
	private List<String> document = new ArrayList<String>();
	private BasicDataSource basicDataSource;

	CrawlerThread ct = new CrawlerThread();
	Corpus cp = new Corpus();

	public Crawler(){
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

	public void loadUrl(){
		ResultSet resultSet = null;
		PreparedStatement pstmt = null;
		String query = "SELECT url FROM urls";
		try {
			Connection connection = basicDataSource.getConnection();

			try {
				PreparedStatement statement = connection.prepareStatement("USE essayradar");
				try {
					pstmt = connection.prepareStatement(query);
					resultSet = pstmt.executeQuery();

					while(resultSet.next()){
						url = resultSet.getString(1);
						urlList.add(url);
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

	public void crawl() throws IOException{
		loadUrl();

		for(int i = 0; i < urlList.size(); i++){
			url = urlList.get(i);
			webContent = ct.extractContent(url, null);
			StringBuilder sb = new StringBuilder();
			for(String string : webContent){
				sb.append(string);
			}
			String[] tokenTerms = sb.toString().toLowerCase().replaceAll("[\\W&&[^\\s]]","").split("\\W+");
			for(String token : tokenTerms){
				document.add(token);
			}
			cp.add(document);
		}
	}


	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}
}