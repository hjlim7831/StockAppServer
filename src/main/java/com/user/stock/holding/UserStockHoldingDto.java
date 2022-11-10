package com.user.stock.holding;

import lombok.Data;

@Data
public class UserStockHoldingDto {
	
	// DB `my_list` table과 호환
	private String stock_code; // 주식 종목 코드
	private String user_num;   // 사용자 고유 번호
	private double now_price;  // 주식 구매 가격
	private int stock_cnt;     // 보유 주식 개수
	
	public UserStockHoldingDto(String stock_code, String user_num) {
		this.stock_code = stock_code;
		this.user_num = user_num;
	}
}