package com.user.info;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter
@Getter
@ToString
public class UserInfoDto {
	private String name;
	private String nick_name;
	private String id;
	private String password;
	private String password_confirm;
	private String email;
	private String phone_number;
	private String address;
	private String simple_pwd;
	private String user_num;
	
	public UserInfoDto() {
		
	}
	
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