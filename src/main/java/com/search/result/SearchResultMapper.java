package com.search.result;

import java.util.List;

import com.stock.detail.StockDto;

public interface SearchResultMapper {
	/**
	 * 전체 주식 목록을 불러오는 method
	 * @return 전체 주식 목록
	 */
	List<StockDto> selectAllStock();
	
}
