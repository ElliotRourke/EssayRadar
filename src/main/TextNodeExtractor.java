package main;
import java.util.List;

import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeVisitor;

/**
 * This is the TextNodeExtractor class.
 * This class is responsible for extracting the text from the content in the List Strings sent by the CrawlerThread class.
 * 
 */
public class TextNodeExtractor implements NodeVisitor {

	private final List<String> strings;

	public TextNodeExtractor(List<String> strings) {
		this.strings = strings;
	}

	/**
	 * This method gets the first seen node of the html document.
	 * The head node - e.g the first node to be visited - is the first node in the document as the 	query is set to null.
	 * The head method then retrieves all text nodes and adds them to the List 'strings'.
	 */
	@Override
	public void head(Node node, int depth) {
		if (node instanceof TextNode) {
			TextNode textNode = ((TextNode) node);
			String text = textNode.getWholeText();
			if (!StringUtil.isBlank(text)) {
				strings.add(text);
			}
		}
	}

	@Override
	public void tail(Node node, int depth) {

	}

}
