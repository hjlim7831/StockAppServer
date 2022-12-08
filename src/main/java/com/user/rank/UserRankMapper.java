package com.user.rank;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import com.user.rank.dto.UserAccountDto;
import com.user.rank.dto.UserHoldingStockDto;
import com.user.rank.dto.UserRankDto;

@Mapper
@Repository
public interface UserRankMapper {
	
	List<String> selectAllUser();
	
	UserAccountDto selectUserAccount(String user_num);
	
	List<UserHoldingStockDto> selectUserHoldingStock(String user_num);
	
	List<String> selectAllHoldingStock();
	
	int updateRank(UserRankDto userRankDto);
	
	int selectUserRank(String user_num);
}