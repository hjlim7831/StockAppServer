package com;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.interceptor.LoginInterceptor;
import com.interceptor.StockCloseInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new StockCloseInterceptor())
				.addPathPatterns("/stock/*/trade");
		registry.addInterceptor(new LoginInterceptor())
				.excludePathPatterns("user/login", "user/join", "user/login-session", "search/**");
	}
	
}
