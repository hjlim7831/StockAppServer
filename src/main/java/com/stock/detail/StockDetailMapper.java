package com.stock.detail;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.stock.detail.dto.StockDto;
import com.stock.detail.dto.StockRelationsDto;

@Mapper
@Repository
public interface StockDetailMapper {
	StockDto selectStockByCode(String stock_code);
	StockRelationsDto selectRelationsByCode(String stock_code);
}