package com.user;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class UserDTO {
	private String name;
	private String nick_name;
	private String id;
	private String password;
	private String email;
	private String phone_number;
	private String address;
	private int simple_pwd;
	private String user_num;
	
	public UserDTO() {
		
	}
	
	public UserDTO(String id) {
		this.id = id;
	}

	public UserDTO(String user_num, String id, String password, int simple_pwd, String name, String nick_name, String email,
			String phone_number, String address) {
		this.user_num = user_num;
		this.name = name;
		this.nick_name = nick_name;
		this.id = id;
		this.password = password;
		this.email = email;
		this.phone_number = phone_number;
		this.address = address;
		this.simple_pwd = simple_pwd;
	}

	@Override
	public String toString() {
		return "UserDTO [name=" + name + ", nick_name=" + nick_name + ", id=" + id + ", password=" + password
				+ ", email=" + email + ", phone_number=" + phone_number + ", address=" + address + "]";
	}

}