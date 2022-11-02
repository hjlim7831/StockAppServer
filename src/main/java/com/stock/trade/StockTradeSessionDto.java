package com.stock.trade;

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
public class StockTradeSessionDto implements Serializable {
	
	// session에 저장할 거래 시 사용할 현재 주가
	
	@JsonIgnore
	static final long serialVersionUID = 1L;
	
	private String stock_code;
	private int stock_price;		
}
