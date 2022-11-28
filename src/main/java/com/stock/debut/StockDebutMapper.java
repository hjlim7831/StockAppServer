package com.stock.debut;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface StockDebutMapper {
	List<com.stock.debut.StockDebutDto> selectAllDebutStock();
}
