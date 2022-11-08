package com.data.stock.crawling.realtime.dto;

import lombok.Data;

@Data
public class RealtimeExchangeDto {
	
	/**
	 * 하나은행 실시간 환율 APId의 JSON과 호환되는 DTO
	 * **/
	private String code;
	private String currencyCode;
	private String currencyName;
	private String country;
	private String name;
	private String date;
	private String time;
	private double recurrenceCount;
	private double basePrice;
	private double openingPrice;
	private double highPrice;
	private double lowPrice;
	private String change;
	private double changePrice;
	private double cashBuyingPrice;
	private double cashSellingPrice;
	private double ttBuyingPrice;
	private double ttSellingPrice;
	private double tcBuyingPrice;
	private double fcSellingPrice;
	private double exchangeCommission;
	private double usDollarRate;
	private double high52wPrice;
	private String high52wDate;
	private double low52wPrice;
	private String low52wDate;
	private double currencyUnit;
	private String provider;
	private double timestamp;
	private double id;
	private String modifiedAt;
	private String createdAt;
	private double signedChangePrice;
	private double signedChangeRate;
	private double changeRate;
}
