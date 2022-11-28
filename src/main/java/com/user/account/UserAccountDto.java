package com.user.account;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountDto {
	
	/**
	 * DB `account` table과 호환
	 * **/
	
	@ApiModelProperty(value = "통장 번호")
	private String serial_number;
	
	@JsonIgnore
	@ApiModelProperty(value = "사용자 고유 번호")
	private String user_num;
	
	@ApiModelProperty(value = "원화")
	private double balance;
	
	@ApiModelProperty(value = "달러")
	private double us;
	
	@ApiModelProperty(value = "유로")
	private double euro;
	
	@ApiModelProperty(value = "엔화")
	private double jpy;
	
	@ApiModelProperty(value = "위안")
	private double yuan;
}