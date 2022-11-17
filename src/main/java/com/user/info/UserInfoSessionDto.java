package com.user.info;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Component
@Data
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserInfoSessionDto implements Serializable {
	
	// Session에 저장할 로그인 정보
	@JsonIgnore
	static final long serialVersionUID = 1L;
	
	private String user_num;  // 사용자 고유번호
	private String id;        // 사용자 아이디
	private String nick_name; // 닉네임
}