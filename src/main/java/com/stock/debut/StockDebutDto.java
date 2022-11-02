package com.stock.debut;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class StockDebutDto {
	
	// DB `new_stock_list` table과 호환
	private String company_name;     // 회사 이름
	
	@JsonIgnore
	private String new_listing_date; // 상장 날짜
	
	private String diff;             // 공모가
}