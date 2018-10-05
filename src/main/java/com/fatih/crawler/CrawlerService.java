package com.fatih.crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

public class CrawlerService {

	private UrlCountHolder urlCountHolder;
	private int maxChildPageLevel;
	private int childPageLevel;

	public CrawlerService(UrlCountHolder urlCountHolder, int maxChildPageLevel, int childPageLevel) {
		super();
		this.urlCountHolder = urlCountHolder;
		this.maxChildPageLevel = maxChildPageLevel;
		this.childPageLevel = childPageLevel;
	}

	public void getUrl(URL url) {

		try {
			Integer urlCount = urlCountHolder.addUrl(url.toExternalForm());
			if (urlCount > 1 || maxChildPageLevel <= childPageLevel) {
				return;
			}
			URLConnect urlConnect = new URLConnect(url);
			Optional<List<String>> innerUrls = Optional.ofNullable(urlConnect.execute().get()).flatMap(getDocument())
					.flatMap(doc -> Optional.ofNullable(getAnchorsFromDocument(doc)));
			innerUrls.ifPresent(getInnerPageUrls(url));
		} catch (IOException e) {
			e.getMessage();
		}
	}

	private Consumer<List<String>> getInnerPageUrls(URL url) {
		return urls -> urls.parallelStream().map(convertToURL(url)).filter(Objects::nonNull).forEach(getChildPage());
	}

	private Consumer<URL> getChildPage() {
		return u -> new CrawlerService(urlCountHolder, maxChildPageLevel, childPageLevel + 1).getUrl(u);
	}

	private Function<String, URL> convertToURL(URL url) {
		return u -> {
			try {
				return new URL(url, u);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			return null;
		};
	}

	private Function<Response, Optional<Document>> getDocument() {
		return t -> {
			try {
				return Optional.of(t.parse());
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			return Optional.empty();
		};
	}

	private List<String> getAnchorsFromDocument(Document doc) {
		return doc.getElementsByTag("a").stream().filter(e -> {
			String href = e.attr("href");
			return !href.toLowerCase().startsWith("javascript:");
		}).map(e -> e.attr("href")).collect(Collectors.toList());
	}

}
