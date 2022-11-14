package com.user.stock.wishlist;

import lombok.Data;

@Data
public class UserStockWishlistDto {
	private String stock_code;
	private String company_name;
	
	private double now;        // 실시간 주가
	private double diff;       // now와 전날 종와의 차이
	private double rate;       // 전날 종가 대비 now 수익률
	
	private int view_cnt;      // 조회수
}
