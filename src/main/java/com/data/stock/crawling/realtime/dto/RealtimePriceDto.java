package com.data.stock.crawling.realtime.dto;

import lombok.Data;

@Data
public class RealtimePriceDto {
	
	/**
	 * 네이버 실시간 주식 API의 JSON과 호환되는 DTO
	 * **/
	private double marketSum;  // 시가 총액
	private double per;        // PER
	private double eps;        // EPS
	private double pbr;        // PBR
	private double now;        // 실시간 주가
	private double diff;       // now와 전날 종와의 차이
	private double rate;       // 전날 종가 대비 now 수익률
	private double quant;      // 거래량
	private double amount;     // 거래 대금 (단위 : 백만)
	private double high;       // 고가
	private double low;        // 저가
	private double risefall;
}