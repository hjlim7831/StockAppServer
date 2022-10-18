package com.user.account;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserAccountMapper {
	
	public void insertAcount(UserAccountDto userAccountDto);
}