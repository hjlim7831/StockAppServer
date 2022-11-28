package com.user.stock.wishlist;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserStockWishlistDto {
	
	@ApiModelProperty(value = "주식 종목 코드 (숫자 6자리)")
	private String stock_code;
	
	@ApiModelProperty(value = "회사명")
	private String company_name;
	
	@ApiModelProperty(value = "실시간 주가")
	private double now;
	
	@ApiModelProperty(value = "실시간 주가 - 전날 종가")
	private double diff;
	
	@ApiModelProperty(value = "전날 종가 대비 실시간 주가 수익률")
	private double rate;
	
	@ApiModelProperty(value = "조회수")
	private int view_cnt;
}
