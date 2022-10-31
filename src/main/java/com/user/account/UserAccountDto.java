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
	private int balance;          // 원화
	private int us;               // 달러
	private int euro;             // 유로
	private int jpy;              // 엔화
	private int yuan;             // 위안
}