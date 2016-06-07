package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * The FileParser class.
 * This is a child of the Parser class.
 * This class is responsible for the parsing of '.txt' files uploaded by the user.
 * @see Parser
 */
public class FileParser extends Parser{

	private List<String> document = new ArrayList<String>(); //A SINGLE DOCUMENT
	private List<double[]> cosVector = new ArrayList<double[]>();
	private String fileName;
	private double simResult;

	/**
	 * Retrieves the name of the file from the uploaded files path.
	 */
	@Override
	public String getName(String path){
		Path p = Paths.get(path);
		fileName = p.getFileName().toString();
		return fileName;
	}

	/**
	 * The parse method is responsible for taking the document removing illegal white space (return characters and tabs) and other illegal characters.
	 * The characters are replaced with normal white spaces which the parse method then uses to split the document into single token words.
	 * These words are added to the 'document' list which is the uploaded document transformed into a list so that the data can be used in other methods.
	 */
	@Override
	public void parse(String path) throws FileNotFoundException, IOException{
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
			StringBuilder sb = new StringBuilder();
			String line = null;
			while((line = br.readLine()) != null){
				sb.append(line);
			}
			String[] tokenTerms = sb.toString().toLowerCase().replaceAll("[\\W&&[^\\s]]","").split("\\W+");
			for(String token : tokenTerms){
				document.add(token);
			}
		}
	}

	/**
	 * The getTFIDF method is responsible for calling the Comparator class in order to retrieve the TFIDF values for each term in the uploaded document.
	 * It uses the 'document' list - the document uploaded by the user.
	 * And the 'documents' list - the documents fetched from the database and loaded by the Corpus Class.
	 * The method also calls the crawler which retrieves text from urls in a database table.
	 * To calculate the TFIDF which is then stored in the double vector 'cosVector'.
	 * The 'cosVector' is then used to calculate the cosine similarity.
	 * @see Corpus
	 * @see Comparator
	 * @see Crawler
	 */
	@Override
	public void getTFIDF() throws FileNotFoundException, IOException, SQLException{
		Corpus cp = new Corpus();
		Comparator comp = new Comparator();
		Crawler cc = new Crawler();
		cp.generateCorpus();
		cc.crawl();


		double[] tfidfVectors = new double[document.size()];
		int count = 0;

		for(int i = 0; i < document.size(); i++){
			String term = document.get(i);
			comp.calculateTF(document, term);
			comp.calculateIDF(cp.getCorpus(), term);
			comp.calculateTFIDF(document, cp.getCorpus(), term);
			if(comp.calculateTFIDF(document, cp.getCorpus(), term) < 1){
				tfidfVectors[count] = (comp.calculateTFIDF(document, cp.getCorpus(), term));
				count++;
			}
			cosVector.add(tfidfVectors);
		}
	}

	/**
	 * This method calculates the cosine similarity for the uploaded document.
	 * It takes the 'cosVector' - A vector comprising of all the TFIDF values of the uploaded document.
	 * And passes them as to the CosineCalculator class.
	 * @see CosineCalculator
	 */
	@Override
	public void getCosineSimilarity() {
		for (int i = 0; i < cosVector.size(); i++) {
			for (int j = 0; j < cosVector.size(); j++) {
				{simResult = new CosineCalculator().cosineSimilarity(cosVector.get(i), cosVector.get(j));
				}
			}
		}
	}

	/**
	 * The last method to be called in the upload sequence.
	 * This method compiles the filename, result of the CosineCalculator class and the path into a new Report.
	 * This report is then uploaded and stored in the database reports table.
	 * @throws IOException 
	 * @see Report
	 * @see ReportsDB
	 */
	@Override
	public void createReport(String path) throws SQLException, IOException{
		ReportsDB rpdb = new ReportsDB();
		rpdb.storeReport(fileName, simResult, path);
	}

	/**
	 * Used to retrieve the document for other methods.
	 */
	@Override
	public List<String> getDoc(){
		return document;
	}
	

}
