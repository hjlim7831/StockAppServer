package com.user.rank.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserHoldingStockDto {
	
	@ApiModelProperty(value = "주식 코드")
	private String stock_code;
	
	@ApiModelProperty(value = "주식 수량")
	private int stock_cnt;
}
