package com.stock.detail.dto;

import lombok.Data;

@Data
public class StockDto {
	private String stock_code;
	private String company_name;
	private String category;
	private String product;
	private String public_day;
	private String closing_month;
	private String representative;
	private String homepage;
	private String region;
}