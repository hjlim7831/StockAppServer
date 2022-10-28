package com.search.viewCntRank;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.stock.detail.dto.StockDto;

@Mapper
@Repository
public interface ViewCntRankMapper {
	
	/**
	 * 검색 기능에서 주식 종목별 조회수 TOP5 조회
	 * @return 조회수 top5 주식 목록
	 */
	List<StockDto> selectViewCntTop5Stock();
	
}
