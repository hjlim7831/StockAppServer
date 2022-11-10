package com.interceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.user.info.UserInfoSessionDto;

public class LoginInterceptor implements HandlerInterceptor {

	@Resource
	private UserInfoSessionDto userInfoSessionDto;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object hanlder)
			throws Exception {
		if (userInfoSessionDto == null || userInfoSessionDto.getId() == null) {
			response.sendRedirect("/api/error/no-login");
			return false;
		}
		return true;
	}
}