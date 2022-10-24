package com.crawling.stock.news;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
@Component
public class JsoupNewsComponent {
	String stockNewsUrl;
	StringBuilder sb;

	/**
	 * 특정 주식의 뉴스 페이지. 뉴스 더보기 클릭 시 링크와는 다름
	 * 
	 * @param stock_code 크롤링 해올 주식의 코드
	 */
	private void urlSetter(String stock_code) {
		sb = new StringBuilder();
		sb.append("https://finance.naver.com/item/news_news.naver?code=").append(stock_code);
		this.stockNewsUrl = sb.toString();
	}
	
	/**
	 * 
	 * @param stock_code 크롤링 해올 주식의 코드
	 * @return NewsDto(뉴스 제목, 정보제공, 날짜) 리스트 반환.
	 */
	public List<NewsDto> getNewsList(String stock_code) {
		urlSetter(stock_code);

		Connection conn = Jsoup.connect(stockNewsUrl);
		try {
			Document document = conn.get();
			return documentParse(document);
		} catch (IOException e) {

		}

		return null;
	}

	private List<NewsDto> documentParse(Document document) {
		Elements titleElems = document.select("body > div > table.type5 > tbody > tr > td.title > a");
		Elements infoElems = document.select("body > div > table.type5 > tbody > tr > td.info");
		Elements dateElems = document.select("body > div > table.type5 > tbody > tr > td.date");
		Elements urlElems = document.select("body > div > table.type5 > tbody > tr > td.title > a");
				
		List<NewsDto> list = new ArrayList<NewsDto>();

		for (int i = 0; i < titleElems.size(); i++) {
			StringBuilder sb = new StringBuilder();
			NewsDto news = new NewsDto();
			news.title = titleElems.get(i).text();
			news.info = infoElems.get(i).text();
			news.date = dateElems.get(i).text();
			String url = sb.append("https://finance.naver.com").append(urlElems.get(i).attr("href")).toString();
			news.url = url;
			list.add(news);
		}

		return list;
	}

}
