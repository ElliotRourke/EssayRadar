package main;


/**
 * The CosineCalculator class.
 * This class is used to normalise the TFIDF values gained from the parser and Comparator classes.
 * The vectors are stored in the parser class and contain all TFIDF values in the document.
 * @see Parser
 * @see Comparator
 */
public class CosineCalculator {

	/**
	 * This method calculates the Cosine Similarity of the document using the Cosine Similarity algorithm.
	 * It retrieves two double vectors from the parser class and uses these as the parameters for the calculation.
	 * @param docVector1 - The first vector created in the parser class.
	 * @param docVector2 - The second vector created in the parser class.
	 * @return cosineSimilarity - The result of the calculation.
	 */
	public double cosineSimilarity(double[] docVector1, double[] docVector2) {
		double dotProduct = 0.0;
		double magnitude1 = 0.0;
		double magnitude2 = 0.0;
		double cosineSimilarity = 0.0;

		for (int i = 0; i < docVector1.length; i++){ 
			dotProduct += docVector1[i] * docVector2[i]; 
			magnitude1 += Math.pow(docVector1[i], 2);
			magnitude2 += Math.pow(docVector2[i], 2);
		}

		magnitude1 = Math.sqrt(magnitude1);
		magnitude2 = Math.sqrt(magnitude2);

		if (magnitude1 != 0.0 | magnitude2 != 0.0) {
			cosineSimilarity = dotProduct / (magnitude1 * magnitude2);
		} else {
			return 0.0;
		}
		return cosineSimilarity;
	}
}
