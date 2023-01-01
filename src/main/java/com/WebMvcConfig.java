package com;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.interceptor.LoginInterceptor;
import com.interceptor.StockCloseInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
	
	@Bean
    public StockCloseInterceptor stockCloseInterceptor() {
        return new StockCloseInterceptor();
    }
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(stockCloseInterceptor())
				
				// 주식 매매 창
				.addPathPatterns("/stock/trade/*", "/stock/price/*");
		
		registry.addInterceptor(new LoginInterceptor())
				.addPathPatterns("/**")
				
				// 로그인, 회원가입
				.excludePathPatterns("/user/login", "/user/join")
				
				// 메인 페이지 (로그아웃 버전)
				.excludePathPatterns("/stock/major-index", "/stock/category/**", "/stock/recommend", "/stock/is-close")
				
				// 테스트용
				.excludePathPatterns("/testApikey")
				
				// 주식 상세 페이지
				.excludePathPatterns("/stock/info/*", "/stock/graph/*", "/stock/realtime/*", "/stock/relations/*", "/stock/news/*")
				
				// 상장 예정 주식
				.excludePathPatterns("/stock/debut")
				
				// 검색
				.excludePathPatterns("/search/**")
				
				// Interceptor
				.excludePathPatterns("/error/**")
				
				// Swagger
				.excludePathPatterns("/swagger-ui/index.html", "/swagger-ui.html", "/v2/api-docs", "/swagger-resources/**", "/webjars/**")
		
				// test
				.excludePathPatterns("/user/test", "/stock/test");
	}
}
