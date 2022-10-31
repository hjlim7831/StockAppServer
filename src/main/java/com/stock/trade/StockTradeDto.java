package com.stock.trade;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockTradeDto {
	private String user_num;
	private int trade_id;
	
	private String stock_code;
	private int share;
	private int stock_price;
	
	private int after_trade_balance;
	private int after_trade_share;
}