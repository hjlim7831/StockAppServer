package com.stock.detail;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.stock.detail.dto.StockDataDto;
import com.stock.detail.dto.StockDto;
import com.stock.detail.dto.StockInfoDto;

@Mapper
@Repository
public interface StockDetailMapper {
	
	// stock_code에 해당하는 종목 정보 가져오기
	StockInfoDto selectStockByCode(String stock_code);

	// 전체 주식 목록 가져오기
	List<StockDto> selectAllStock();
	
	// stock_code의 한 달치 시가, 종가, 고가, 저가 가져오기
	List<StockDataDto> selectStockByDate(String stock_code);
}