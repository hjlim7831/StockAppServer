package com.stock.trade;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockTradeDto {
	private int trade_id;        // 매수 0, 매도 1
	
	private String stock_code;   // 매매할 주식 코드
	private int share;           // 매매할 주식 수
	
	private double now_price;    // 매매 이후 구매 가격
	private int stock_cnt;       // 매매 이후 보유 주식 수
	
	private String user_num;     // 사용자 고유 번호
}