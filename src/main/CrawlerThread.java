package main;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * This is the CrawlerThread class.
 * This class is responsible for the actual searching of the supplied URL.
 * The query parameter is always set as NULL in order to retrieve all the content on the web page.
 * It adds the content on the web page to a list called Strings which is then sent to the TextNodeExtractor class to get the valid text.
 * The list of text is then returned to the Crawler class and added to the documents list.
 * @see TextNodeExtractor
 * @see Crawler
 */

public class CrawlerThread{


	/**
	 * The method for extracting the content from the web page.
	 * It will then be sent to the TextNodeExtractor to extract all valid text from the content.
	 * @param url - The url the crawler will scrape the content from.
	 * @param query - The specific html section it is searching for, IF set to null then it will extract the entire page.
	 * @return - A list of Strings containing the content of the web page.
	 * @throws IOException
	 */
	public List<String> extractContent(String url, String query) throws IOException{
		Document document = Jsoup.connect(url).get();
		Elements elements = StringUtil.isBlank(query)
				? document.getElementsByTag("body")
						: document.select(query);

				List<String> strings = new ArrayList<String>();
				elements.traverse(new TextNodeExtractor(strings));
				return strings;
	}

}

