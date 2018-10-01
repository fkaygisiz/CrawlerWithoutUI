package com.fatih.CrawlerWithoutUI;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

public class CrawlerService {

	private UrlCountHolder urlCountHolder;
	private ExecutorService threadPool;

	public CrawlerService(UrlCountHolder urlCountHolder, ExecutorService threadPool) {
		super();
		this.urlCountHolder = urlCountHolder;
		this.threadPool = threadPool;
	}

	public void getUrl(URL url) {
		try {
			Integer urlCount = urlCountHolder.addUrl(url.toExternalForm());
			if (urlCount > 1) {
				System.out.println(url.toExternalForm() + " " + urlCount);
				return;
			}
			URLConnect urlConnect = new URLConnect(url);
			Optional<List<String>> innerUrls = Optional.ofNullable(urlConnect.execute().get())
					.map(getDocument())
					.map(getAnchorsFromDocument());
			innerUrls.ifPresent(getInnerPageUrls(url));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Consumer<? super List<String>> getInnerPageUrls(URL url) {
		return urls -> urls.parallelStream().map(convertToURL(url)).forEach(this::getUrl);
	}

	private Function<? super String, ? extends URL> convertToURL(URL url) {
		return u -> {
			try {
				return new URL(url, u);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			return null;
		};
	}

	private Function<? super Response, ? extends Document> getDocument() {
		return t -> {
			try {
				return t.parse();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		};
	}

	private Function<? super Document, ? extends List<String>>  getAnchorsFromDocument() {
		return doc->doc.getElementsByTag("a").stream().filter(e -> {
			String href = e.attr("href");
			return href.startsWith("/") || href.toLowerCase().startsWith("http://")
					|| href.toLowerCase().startsWith("https://");
		}).map(e -> e.attr("href")).collect(Collectors.toList());
	}

}
