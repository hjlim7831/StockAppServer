package com.user.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountDto {
	private String serial_number;
	private String user_num;
	private int balance;
	private int us;
	private int euro;
	private int jpy;
	private int yuan;
}