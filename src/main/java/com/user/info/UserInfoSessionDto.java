package com.user.info;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Component
@Data
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserInfoSessionDto implements Serializable {
	
	// Session에 저장할 로그인 정보
	@JsonIgnore
	static final long serialVersionUID = 1L;
	
	@ApiModelProperty(value = "사용자 고유 번호")
	private String user_num;
	
	@ApiModelProperty(value = "사용자 ID")
	private String id;
	
	@ApiModelProperty(value = "사용자 닉네임")
	private String nick_name;
}