package com.stock.category;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.stock.detail.dto.StockDto;

@Mapper
@Repository
public interface StockCategoryMapper {
	/**
	 * 종목별 주식 목록을 가져온다.
	 * @param category_name (it[IT], ctr[건설], bio[바이오], chem[화학], ent[엔터])
	 * @return
	 */
	public List<StockDto> selectCategoryStocks(String category_name);

}
