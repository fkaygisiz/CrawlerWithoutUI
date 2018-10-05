package com.fatih.crawler;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UrlCountHolder {

	private Map<String, Integer> urlMap = new ConcurrentHashMap<>();

	public Integer addUrl(String url) {
		System.out.println(url);
		return urlMap.merge(url, 1, Integer::sum);
	}

	public Map<String, Integer> getMap() {
		return Collections.unmodifiableMap(urlMap);
	}
}
