package com.user.info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *  DB `user` table과 호환되는 DTO입니다.
 *  JSON으로 변환 시, user_num과 id만 반환됩니다.
 * **/
@Data
@NoArgsConstructor
@JsonIgnoreProperties({"name", "password", "password_confirm", "simple_pwd", "email", "phone_number", "address"})
public class UserInfoDto {
	
	@ApiModelProperty(value = "사용자 고유 번호", hidden = true)
	private String user_num;
	
	@ApiModelProperty(value = "이름", required = true)
	private String name;
	
	@ApiModelProperty(value = "닉네임", required = true)
	private String nick_name;
	
	@ApiModelProperty(value = "아이디", required = true)
	private String id;
	
	@ApiModelProperty(value = "비밀번호", required = true)
	private String password;
	
	@ApiModelProperty(value = "비밀번호 확인", required = true)
	private String password_confirm;
	
	@ApiModelProperty(value = "pin 번호", required = true)
	private String simple_pwd;
	
	@ApiModelProperty(value = "이메일 주소", required = true)
	private String email;
	
	@ApiModelProperty(value = "전화번호", required = true)
	private String phone_number;
	
	@ApiModelProperty(value = "주소", required = false)
	private String address;

	public UserInfoDto(String id, String user_num, String nick_name) {
		this.id = id;
		this.user_num = user_num;
		this.nick_name = nick_name;
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