package com.stock.trade;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.user.info.UserInfoSessionDto;

@RestController
@RequestMapping("stock")
public class StockTradeController {
	
	@Resource // session에 저장해 둔 로그인 정보 가져오기
	private UserInfoSessionDto userInfoSessionDto;
	
	@Autowired
	StockTradeService userStockTradeService;
	
	@PostMapping("{stock_code}/trade") // stock_code 매매
	public Map<String, Object> trade(@PathVariable String stock_code, StockTradeDto userStockTradeDto) {
		userStockTradeDto.setStock_code(stock_code);
		userStockTradeDto.setStock_price(0);         // 거래 시 주가 정보 넣어주기 (미완성)
		userStockTradeDto.setUser_num(userInfoSessionDto.getUser_num());
		return userStockTradeService.trade(userStockTradeDto);
	}
}