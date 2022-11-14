package com.user.stock.holding;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class UserStockHoldingDto {
	
	// DB `my_list` table과 호환
	private String stock_code;     // 주식 종목 코드
	private String company_name;   // 해당 종목 회사 이름
	
	@JsonIgnore
	private String user_num;       // 사용자 고유 번호
	
	private int stock_cnt;         // 보유 주식 개수
	private double now_price;      // 수익금
	private double earnings_ratio; // 수익률
	
	public UserStockHoldingDto(String stock_code, String user_num) {
		this.stock_code = stock_code;
		this.user_num = user_num;
	}
}