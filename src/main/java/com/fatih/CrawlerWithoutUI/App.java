package com.fatih.CrawlerWithoutUI;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class App {
	public static void main(String[] args) throws MalformedURLException {
		UrlCountHolder urlCountHolder = new UrlCountHolder();
		ExecutorService threadPool = Executors.newFixedThreadPool(50);

		CrawlerService crawlerService = new CrawlerService(urlCountHolder, threadPool);
		URL url = new URL("https://www.wikizero.pro/index.php?q=aHR0cHM6Ly9lbi53aWtpcGVkaWEub3JnL3dpa2kvRXVyb3Bl");
		crawlerService.getUrl(url);
		Map<String, Integer> map = urlCountHolder.getMap();
		map.entrySet().stream().forEach(e -> System.out.println(e.getKey() + " # " + e.getValue()));
	}
}
