package com.user.account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountDto {
	
	// DB `account` table과 호환
	private String serial_number; // 통장 번호
	private String user_num;      // 사용자 번호
	private double balance;          // 원화
	private double us;               // 달러
	private double euro;             // 유로
	private double jpy;              // 엔화
	private double yuan;             // 위안
}