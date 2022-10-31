package com.stock.detail;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.stock.detail.dto.StockDto;

@Mapper
@Repository
public interface StockDetailMapper {
	
	// stock_code에 해당하는 종목 정보 가져오기
	StockDto selectStockByCode(String stock_code);

	// 전체 주식 목록 가져오기
	List<StockDto> selectAllStock();
}