package com.data.stock.crawling.realtime.dto;

import lombok.Data;

@Data
public class RealtimeMajorIndexDto {
	private String ms; // 상태
	private double nv; // 현재 가격
	private double cv; // 전일비
	private double cr; // 등락률(%)
	private String rf;
	private double ov;
	private double hv;
	private double lv;
	private double aq;
	private double aa;
	private double bs;
	private String cd; // 주요지수 종류(코드)
}
