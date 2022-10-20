package com.search.result;

import java.util.List;

import com.stock.detail.StockDto;

public interface SearchResultMapper {
	
	List<StockDto> selectAllStock();
	
}
