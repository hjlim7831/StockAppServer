package com.stock.trade;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class StockTradeDto {
	
	@ApiModelProperty(value = "매매할 주식 코드", hidden = true)
	private String stock_code;
	
	@ApiModelProperty(value = "매매 이후 주식 구매 총 가격", hidden = true)
	private double now_price;
	
	@ApiModelProperty(value = "매매 이후 보유 주식 수", hidden = true)
	private int stock_cnt;
	
	@ApiModelProperty(value = "사용자 고유 번호", hidden = true)
	private String user_num;
}