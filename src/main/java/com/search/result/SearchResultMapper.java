package com.search.result;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface SearchResultMapper {
	/**
	 * 전체 주식 목록을 불러오는 method
	 * @return 전체 주식 목록
	 */
	List<SearchResultDto> selectAllStock();
	
}
