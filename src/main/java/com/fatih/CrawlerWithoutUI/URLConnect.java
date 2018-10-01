package com.fatih.CrawlerWithoutUI;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;

public class URLConnect {

	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/41.0.2228.0 Safari/537.36";
	private URL url;

	public URLConnect(URL url) {
		this.url = url;
	}

	public Optional<Response> execute() throws IOException {
		return Optional.of(Jsoup.connect(url.toExternalForm())
				.followRedirects(false) // to follow redirects
				.timeout(10000).userAgent(USER_AGENT)// to get into pages preventing automated calls
				.execute());
	}

}
