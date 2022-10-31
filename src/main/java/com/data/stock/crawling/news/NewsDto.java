package com.data.stock.crawling.news;

import lombok.Data;

@Data
public class NewsDto {
	String title; // 뉴스 제목
	String info; // 뉴스 정보 제공
	String date; // 뉴스 날짜
	String url; // 뉴스 url
	
}
