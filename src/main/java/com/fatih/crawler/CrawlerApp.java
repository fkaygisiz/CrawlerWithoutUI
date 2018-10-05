package com.fatih.crawler;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class CrawlerApp {
	public static void main(String[] args) throws IOException {
		CrawlerApp crawlerApp = new CrawlerApp();

		URL url = new URL("https://en.wikipedia.org/wiki/Europe");
		Map<String, Integer> urlsAndCounts = crawlerApp.getUrlsAndCounts(url, 2);

		crawlerApp.writeToFile(urlsAndCounts, "out.txt");
	}

	private Map<String, Integer> getUrlsAndCounts(URL url, int maxChildPageLevel) {
		UrlCountHolder urlCountHolder = new UrlCountHolder();
		CrawlerService crawlerService = new CrawlerService(urlCountHolder, maxChildPageLevel, 0);
		crawlerService.getUrl(url);
		return urlCountHolder.getMap();
	}

	private void writeToFile(Map<String, Integer> map, String fileName) {
		FileWriter fileWriter = new FileWriter(fileName);
		fileWriter.writeFile(map);
	}
}
