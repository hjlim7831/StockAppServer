package com.crawling.stock.news;

public class CrawlingNewsMain {
	public static void main(String[] args) {
		CrawlingNewsComponent comp = new CrawlingNewsComponent();
		
		comp.setUp();
		comp.urlSetter("005930");
		
		comp.getNewsList();
		
		
		
	}
}
