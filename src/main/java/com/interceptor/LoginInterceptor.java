package com.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import com.user.info.UserInfoSessionDto;

public class LoginInterceptor implements HandlerInterceptor {
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object hanlder)
			throws Exception {
		
		HttpSession session = request.getSession();
		UserInfoSessionDto userInfoSessionDto = (UserInfoSessionDto) session.getAttribute("scopedTarget.userInfoSessionDto");
		
		if (userInfoSessionDto == null || userInfoSessionDto.getId() == null) {
			response.sendRedirect("/api/error/no-login");
			return false;
		}
		return true;
	}
}