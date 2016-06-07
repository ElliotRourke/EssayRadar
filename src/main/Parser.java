package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * This is the parser class.
 * Parent of DocParser and FileParser and any future parsers.
 * This class is the defining template for all parsers used in this program.
 * It sets all methods that parsers must have to function.
 * @see DocParser
 * @see FileParser
 */
public abstract class Parser {

	public abstract String getName(String path);
	public abstract void parse(String path) throws FileNotFoundException, IOException;
	public abstract void getTFIDF() throws FileNotFoundException, IOException, SQLException;
	public abstract void getCosineSimilarity();
	public abstract void createReport(String path) throws FileNotFoundException, SQLException, IOException;
	public abstract List<String> getDoc();
}
