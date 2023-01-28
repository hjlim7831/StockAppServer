package com.user.info;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserInfoMapper {
	
	// parameter로 받은 사용자 정보를 넘겨 받아 insert
	public int insertUser(UserInfoDto userInfoDto);
	
	// parameter로 받은 id에 해당하는 id 정보를 select
	public String selectId(String id);
	
	// parameter로 받은 email에 해당하는 id 정보를 select
	public String selectEmail(String email);
	
	// parameter로 받은 nick_name에 해당하는 id 정보를 select
	public String selectNickName(String nick_name);
	
	// parameter로 받은 phone_number에 해당하는 id 정보를 select
	public String selectPhoneNumber(String phone_number);
	
	// parameter로 받은 id에 해당하는 사용자의 id, password, user_num 정보를 select
	public UserInfoDto selectUser(String id);
	
	/**
	 * user_num으로 회원의 회원정보 삭제
	 * @param user_num
	 * @return 삭제 여부 리턴 (1 : 삭제됨, 0 : 삭제되지 않음)
	 */
	public int deleteUserByUUID(String user_num);
	
	/**
	 * 회원의 user_num으로 회원의 계정정보 삭제
	 * @param user_num
	 * @return 삭제 여부 리턴 (1 : 삭제됨, 0 : 삭제되지 않음)
	 */
	public int deleteAccountByUUID(String user_num);
	
	/**
	 * 회원의 user_num으로 회원의 관심 리스트 삭제 
	 * @param user_num
	 * @return 삭제된 정보의 개수 리턴
	 */
	public int deleteWishListByUUID(String user_num);
	
	/**
	 * 회원의 user_num으로 회원의 보유 주식 리스트 삭제
	 * @param user_num
	 * @return 삭제된 정보의 개수 리턴
	 */
	public int deleteMyListByUUID(String user_num);

	
}