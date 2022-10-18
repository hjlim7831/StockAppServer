package com.user.account;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserAccountDto {
	private String serial_number;
	private String user_num;
	private int balance;
	private int us;
	private int euro;
	private int jpy;
	private int yuan;
	
	public UserAccountDto(String serial_number, String user_num, int balance, int us, int euro, int jpy, int yuan) {
		this.serial_number = serial_number;
		this.user_num = user_num;
		this.balance = balance;
		this.us = us;
		this.euro = euro;
		this.jpy = jpy;
		this.yuan = yuan;
	}
}