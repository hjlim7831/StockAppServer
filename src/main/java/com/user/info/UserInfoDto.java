package com.user.info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 *  DB `user` table과 호환되는 DTO입니다.
 *  JSON으로 변환 시, user_num과 id만 반환됩니다.
 * **/
@Data
@JsonIgnoreProperties({"name", "nick_name", "password", "password_confirm", "simple_pwd", "email", "phone_number", "address"})
public class UserInfoDto {

	private String user_num;          // 사용자 고유번호 
	private String name;			  // 이름
	private String nick_name;         // 닉네임
	private String id;                // 아이디
	private String password;          // 비밀번호
	private String password_confirm;  // 비밀번호 확인
	private String simple_pwd;        // pin 번호
	private String email;             // 이메일
	private String phone_number;      // 전화번호
	private String address;           // 주소

	public UserInfoDto(String id, String user_num) {
		this.id = id;
		this.user_num = user_num;
	}
	
	public UserInfoDto(String id, String password, String password_confirm, String simple_pwd, String name, String nick_name, String email,
			String phone_number, String address) {
		this.name = name;
		this.nick_name = nick_name;
		this.id = id;
		this.password = password;
		this.password_confirm = password_confirm;
		this.email = email;
		this.phone_number = phone_number;
		this.address = address;
		this.simple_pwd = simple_pwd;
	}
}