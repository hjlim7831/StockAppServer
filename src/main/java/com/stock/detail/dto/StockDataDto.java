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
	private String open_price;
	private String high_price;
	private String low_price;
	private String close_price;
	private String amount;
	private String percentage_of_foreigners;
}
