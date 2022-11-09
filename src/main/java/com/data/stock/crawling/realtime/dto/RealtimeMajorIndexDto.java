package com.data.stock.crawling.realtime.dto;

import lombok.Data;

@Data
public class RealtimeMajorIndexDto {
	private String ms; // 상태
	private String nv; // 현재 가격
	private String cv; // 전일비
	private String cr; // 등락률(%)
	private String rf;
	private String ov;
	private String hv;
	private String lv;
	private String aq;
	private String aa;
	private String bs;
	private String cd; // 주요지수 종류(코드)
}
