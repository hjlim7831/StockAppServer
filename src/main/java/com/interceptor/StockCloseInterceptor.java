package com.interceptor;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

public class StockCloseInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 매매 가능 시간 : 9:00 - 15:30
		ZonedDateTime zt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
		int year = zt.getYear();
		int mo = zt.getMonthValue();
		int day = zt.getDayOfMonth();
		
		ZonedDateTime st = ZonedDateTime.of(year, mo, day, 9, 0, 0, 0, ZoneId.of("Asia/Seoul"));
		ZonedDateTime ed = ZonedDateTime.of(year, mo, day, 15, 30, 0, 0, ZoneId.of("Asia/Seoul"));

		System.out.println(zt);
		System.out.println(st);
		System.out.println(ed);
		
		if (zt.isAfter(st) && zt.isBefore(ed)) {
			response.sendRedirect("/error/stock-close");
			
		}
		
		
		return true;
	}

}
