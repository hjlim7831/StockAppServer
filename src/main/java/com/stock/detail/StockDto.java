package com.stock.detail;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
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
