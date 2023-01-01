package com.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.web.servlet.HandlerInterceptor;

import com.data.api.OpeningDateComponent;

@Configurable
public class StockCloseInterceptor implements HandlerInterceptor {
	
	@Autowired
	OpeningDateComponent openingDateComponent;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
//		if (openingDateComponent.isOpen()) {
//			return true;
//		}
//
//		response.sendRedirect("/api/error/stock-close");
//		return false;
		return true;

	}

}
