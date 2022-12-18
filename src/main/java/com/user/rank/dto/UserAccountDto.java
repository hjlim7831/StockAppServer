package com.user.rank.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class UserAccountDto {
	
	@ApiModelProperty(value = "원화 잔고")
	private double balance;
	
	@ApiModelProperty(value = "달러 잔고")
	private double us;
	
	@ApiModelProperty(value = "유로 잔고")
	private double euro;
	
	@ApiModelProperty(value = "엔화 잔고")
	private double jpy;
	
	@ApiModelProperty(value = "위안 잔고")
	private double yuan;
}
