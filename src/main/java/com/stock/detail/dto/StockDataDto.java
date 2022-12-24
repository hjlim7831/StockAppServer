package com.stock.detail.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties({"stock_code", "amount", "percentage_of_foreigners"})
public class StockDataDto {
	
	/**
	 * DB의  stock_data 테이블과 호환
	 *
	 * **/
	private String stock_code;
	private String stock_date;
	private long open_price;
	private long high_price;
	private long low_price;
	private long close_price;
	private long amount;
	private double percentage_of_foreigners;
}
