package com.user.info;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.user.account.UserAccountService;

@Service
public class UserInfoService {

	@Autowired
	UserInfoMapper userInfoMapper;
	
	@Autowired
	UserAccountService userAccountService;
	
	public Map<String, Object> joinUser(UserInfoDto userInfoDto) {
		
		String id = userInfoDto.getId();
		String password = userInfoDto.getPassword();
		String password_confirm = userInfoDto.getPassword_confirm();
		String simple_pwd = userInfoDto.getSimple_pwd();
		String name = userInfoDto.getName();
		String nick_name = userInfoDto.getNick_name();
		String email = userInfoDto.getEmail();
		String phone_number = userInfoDto.getPhone_number();

		HashMap<String, Object> returnMap = new HashMap<>();
		
		String response = "";
		String sentence = "";
		
		// ÇÊ¼ö ÀÔ·Â°ª(id, password, name, nick_name, email, phone_number) Áß ÇÏ³ª¶óµµ ¾ø´Â °æ¿ì
		if (id.equals("") || password.equals("") || name.equals("") || nick_name.equals("") || email.equals("") || phone_number.equals("")) {
			response = "failure_empty_some";
			sentence = "¾ÆÀÌµğ, ºñ¹Ğ¹øÈ£, ÀÌ¸§, ´Ğ³×ÀÓ, ÀÌ¸ŞÀÏ, ÀüÈ­¹øÈ£¸¦ ¸ğµÎ ÀÔ·ÂÇØ ÁÖ¼¼¿ä.";
		}
		
		// id (1)5~20±ÛÀÚ ¿µ¹®ÀÚ + ¼ıÀÚ (2)Áßº¹ È®ÀÎ 
		else if (!Pattern.matches("^(?=.*[a-zA-z])(?=.*[0-9])(?!.*[^a-zA-z0-9]).{5,20}$", id)) {
			response = "failure_wrong_format_id";
			sentence = "¾ÆÀÌµğ´Â ¿µ¹®ÀÚ¿Í ¼ıÀÚ¸¦ ÇÏ³ª ÀÌ»ó Æ÷ÇÔÇØ 5 ~ 20ÀÚ·Î ÀÔ·ÂÇØ ÁÖ¼¼¿ä.";
		} else if (findSameId(id) != null) {
			response = "failure_duplicate_id";
			sentence = "ÀÌ¹Ì Á¸ÀçÇÏ´Â ¾ÆÀÌµğÀÔ´Ï´Ù.";
		}
		
		// password (1)8~15±ÛÀÚ, ¿µ¹®ÀÚ + ¼ıÀÚ + Æ¯¼ö¹®ÀÚ (2)password¿Í password_confirm µ¿ÀÏ
		else if (!Pattern.matches("^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[`~!@$!%*#^?&\\(\\)\\-_=+])(?!.*[^a-zA-z0-9`~!@$!%*#^?&\\(\\)\\-_=+]).{8,15}$", password)) {
			response = "failure_wrong_format_pwd";
			sentence = "ºñ¹Ğ¹øÈ£´Â ¿µ¹®ÀÚ, ¼ıÀÚ, Æ¯¼ö¹®ÀÚ¸¦ ÇÏ³ª ÀÌ»ó Æ÷ÇÔÇØ 8~15ÀÚ·Î ÀÔ·ÂÇØ ÁÖ¼¼¿ä.";
		} else if (!password.equals(password_confirm)) {
			response = "failure_different_pwd";
			sentence = "ºñ¹Ğ¹øÈ£ È®ÀÎÀÌ Æ²·È½À´Ï´Ù.";
		}
		
		// simple_pwd ¿Ï·á  (1)6ÀÚ¸®, ¼ıÀÚ
		else if (!Pattern.matches("^[0-9]{6}$", simple_pwd)) {
			response = "failure_wrong_format_simple_pwd";
			sentence = "ÇÉ¹øÈ£´Â ¼ıÀÚ 6ÀÚ¸®·Î ÀÔ·ÂÇØ ÁÖ¼¼¿ä.";
		}
		
		// name (1)2~10±ÛÀÚ, ¹®ÀÚ
		else if (!Pattern.matches("^[°¡-ÆR|a-z|A-Z]{2,10}$", name)) {
			response = "failure_wrong_format_name";
			sentence = "ÀÌ¸§Àº ¹®ÀÚ 2 ~ 10ÀÚ·Î ÀÔ·ÂÇØ ÁÖ¼¼¿ä.";
		}
		
		// nickname (1)1~8±ÛÀÚ, ¹®ÀÚ, ¼ıÀÚ (2)Áßº¹ È®ÀÎ 
		else if (!Pattern.matches("^[¤¡-¤¾|°¡-ÆR|a-z|A-Z|0-9]{1,8}$", nick_name)) {
			response = "failure_wrong_format_nick_name";
			sentence = "´Ğ³×ÀÓÀº ÇÑ±Û, ¿µ¾î, ¼ıÀÚ 1 ~ 8ÀÚ¸®·Î ÀÔ·ÂÇØ ÁÖ¼¼¿ä.";
		} else if (findSameNickName(nick_name) != null) {
			response = "failure_duplicate_nick_name";
			sentence = "ÀÌ¹Ì Á¸ÀçÇÏ´Â ´Ğ³×ÀÓÀÔ´Ï´Ù.";
		}
		
		// email (1)ÀÌ¸ŞÀÏ Çü½Ä (2)Áßº¹ È®ÀÎ 
		else if (!Pattern.matches("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", email)) {
			response = "failure_wrong_format_email";
			sentence = "ÀÌ¸ŞÀÏ Çü½Ä¿¡ ¸ÂÃç ÀÔ·ÂÇØ ÁÖ¼¼¿ä.";
		} else if (findSameEmail(email) != null) {
			response = "failure_duplicate_email";
			sentence = "ÀÌ¹Ì ¾ÆÀÌµğ°¡ Á¸ÀçÇÏ´Â ÀÌ¸ŞÀÏÀÔ´Ï´Ù.";
		}
		
		// phone_number (1)10~11ÀÚ¸®, ¼ıÀÚ (2)Áßº¹ È®ÀÎ 
		else if (!Pattern.matches("^[0-9]{10,11}$", phone_number)) {
			response = "failure_wrong_format_phone_number";
			sentence = "ÀüÈ­¹øÈ£´Â 01011112222Ã³·³ ¼ıÀÚ 11ÀÚ¸®·Î ÀÔ·ÂÇØ ÁÖ¼¼¿ä.";
		} else if (findSamePhoneNumber(phone_number) != null) {
			response = "failure_duplicate_phone_number";
			sentence = "ÀÌ¹Ì ¾ÆÀÌµğ°¡ Á¸ÀçÇÏ´Â ÀüÈ­¹øÈ£ÀÔ´Ï´Ù.";
		}
		
		// address

		else {
			// user_num »ı¼º
			String user_num = UUID.randomUUID().toString().replace("-", "");
			userInfoDto.setUser_num(user_num);
			
			userInfoMapper.insertUser(userInfoDto);
			
			response = "success_join";
			sentence = "È¸¿ø°¡ÀÔÀÌ ¿Ï·áµÆ½À´Ï´Ù.";
			
			// È¸¿ø°¡ÀÔ ¼º°ø ½Ã, ÅëÀå °³¼³ÇÏ±â
			userAccountService.makeAccount(user_num);
		}

		returnMap.put("sentence", sentence);
		returnMap.put("response", response);
		
		return returnMap;
	}
	
	public HashMap<String, Object> loginUser(UserInfoDto userInfoDto) {
		
		String id = userInfoDto.getId();
		String password = userInfoDto.getPassword();
		
		HashMap<String, Object> returnMap = new HashMap<>();
		
		String response = "";
		String sentence = "";
		
		if (id.equals("") && password.equals("")) {
			response = "failure_empty_id_pwd";
			sentence = "¾ÆÀÌµğ¿Í ºñ¹Ğ¹øÈ£¸¦ ÀÔ·ÂÇØ ÁÖ¼¼¿ä.";
		} else if (id.equals("")) {
			response = "failure_empty_id";
			sentence = "¾ÆÀÌµğ¸¦ ÀÔ·ÂÇØ ÁÖ¼¼¿ä.";
		} else if (password.equals("")) {
			response = "failure_empty_password";
			sentence = "ºñ¹Ğ¹øÈ£¸¦ ÀÔ·ÂÇØ ÁÖ¼¼¿ä.";
		} else {
			// DB¿¡¼­ ÀÔ·ÂÇÑ id¿¡ ÇØ´çÇÏ´Â Á¤º¸ °¡Á®¿À±â
			UserInfoDto selectUserInfoDto = userInfoMapper.selectUser(id);
			
			if (selectUserInfoDto == null) {
				response = "failure_notExist_id";
				sentence = "Á¸ÀçÇÏÁö ¾Ê´Â ¾ÆÀÌµğÀÔ´Ï´Ù.";
			} else if (!selectUserInfoDto.getPassword().equals(password)) {
				response = "failure_wrong_password";
				sentence = "Àß¸øµÈ ºñ¹Ğ¹øÈ£ÀÔ´Ï´Ù.";
			} else {
				response = "success_login";
				sentence = "·Î±×ÀÎÀÌ ¿Ï·áµÆ½À´Ï´Ù.";
			}
		}
		
		// API·Î ÀÀ´ä ³Ñ°ÜÁÖ±â
		returnMap.put("sentence", sentence);
		returnMap.put("response", response);
		
		return returnMap;
	}
	
	public String findSameId(String id) {
		return userInfoMapper.selectId(id);
	}
	
	public String findSameEmail(String email) {
		return userInfoMapper.selectEmail(email);
	}
	
	public String findSameNickName(String nick_name) {
		return userInfoMapper.selectNickName(nick_name);
	}
	
	public String findSamePhoneNumber(String phone_number) {
		return userInfoMapper.selectPhoneNumber(phone_number);
	}
}