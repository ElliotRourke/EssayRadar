package main;

import java.util.List;

/**
 * Comparison class.
 * This class handles the actual document comparison using the TFIDF algorithm.
 * The results for these methods are then passed to the required parser.
 * The results are also used as the vectors for calculating the cosine similarity.
 * @see Parser
 * @see CosineCalculator
 */

public class Comparator {


	/**
	 * This method calculates the Text-Frequency of each individual term in the document uploaded by the user.
	 * @param document - The document uploaded by the user.
	 * @param term - The term currently being compared.
	 * Returns the Text-Frequency of the term.
	 */
	public double calculateTF(List<String> document, String term){
		double occurrence = 0;
		for(String word : document){
			if(term.equalsIgnoreCase(word)){
				occurrence++;
			}
		}
		return occurrence / document.size();
	}

	/**
	 * This calculates the frequency of the term in a list of documents held in the corpus.
	 * @see Corpus
	 * @param documents - A 'bag' of documents held in the database.
	 * @param term - The term currently being compared.
	 * Returns the Inverse Document Frequency.
	 */
	public double calculateIDF(List<List<String>> documents, String term){
		double occurrence = 0;
		for(List<String> document : documents){
			for(String word : document){
				if(term.equalsIgnoreCase(word)){
					occurrence++;
					break;
				}
			}
		}
		return 1 + Math.log(documents.size() / occurrence);
	}

	/**
	 * 
	 * @param doc - The document uploaded by the user.
	 * @param docs - The documents held in the corpus.
	 * @param term - The term having its TFIDF value currently being calculated.
	 * Returns the TFIDF value for the term currently being compared.
	 * This is then added to the vectors held in the parser classes.
	 * @see Parser
	 */
	public double calculateTFIDF(List<String> doc, List<List<String>> docs, String term) {
		return calculateTF(doc,term)* calculateIDF(docs,term);
	}

}
