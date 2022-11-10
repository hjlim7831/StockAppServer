package com.interceptor;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.user.info.UserInfoSessionDto;

public class LoginInterceptor implements HandlerInterceptor {
	
	@Resource
	private UserInfoSessionDto userInfoSessionDto;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object hanlder) throws Exception {
		if (userInfoSessionDto.getId() == null) {
			Map<String, Object> resultMap = new HashMap<>();

			resultMap.put("contents", "로그인이 돼 있지 않습니다.");
			resultMap.put("response", "failure_no_login_data");
			
			response.sendRedirect("/error/no-login");
			
			return false;
		}
		return true;
	}
}