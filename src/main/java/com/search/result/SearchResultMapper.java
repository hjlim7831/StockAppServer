package com.search.result;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.stock.detail.dto.StockDto;

@Mapper
@Repository
public interface SearchResultMapper {
	/**
	 * 전체 주식 목록을 불러오는 method
	 * @return 전체 주식 목록
	 */
	List<StockDto> selectAllStock();
	
}
