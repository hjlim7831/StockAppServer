package com.user;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("user")
public class UserController {
	
	@Resource
	private UserInfo userInfo;
	
	@Autowired
	UserService userService;
	
	@PostMapping("join") // È¸¿ø°¡ÀÔ
	@ResponseBody
	public Map<String, Object> joinUser(@RequestParam Map<String, Object> map) {
		
		String id = map.get("id").toString();
		String password = map.get("password").toString();
		String password_confirm = map.get("password_confirm").toString();
		String simple_pwd_string = map.get("simple_pwd").toString();
		String name = map.get("name").toString();
		String nick_name = map.get("nick_name").toString();
		String email = map.get("email").toString();
		String phone_number = map.get("phone_number").toString();
		String address = map.get("address").toString();

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
		} else if (userService.findSameId(id) != null) {
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
		else if (!Pattern.matches("^[0-9]{6}$", simple_pwd_string)) {
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
		} else if (userService.findSameNickName(nick_name) != null) {
			response = "failure_duplicate_nick_name";
			sentence = "ÀÌ¹Ì Á¸ÀçÇÏ´Â ´Ğ³×ÀÓÀÔ´Ï´Ù.";
		}
		
		// email (1)ÀÌ¸ŞÀÏ Çü½Ä (2)Áßº¹ È®ÀÎ 
		else if (!Pattern.matches("^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", email)) {
			response = "failure_wrong_format_email";
			sentence = "ÀÌ¸ŞÀÏ Çü½Ä¿¡ ¸ÂÃç ÀÔ·ÂÇØ ÁÖ¼¼¿ä.";
		} else if (userService.findSameEmail(email) != null) {
			response = "failure_duplicate_email";
			sentence = "ÀÌ¹Ì ¾ÆÀÌµğ°¡ Á¸ÀçÇÏ´Â ÀÌ¸ŞÀÏÀÔ´Ï´Ù.";
		}
		
		// phone_number (1)10~11ÀÚ¸®, ¼ıÀÚ (2)Áßº¹ È®ÀÎ 
		else if (!Pattern.matches("^[0-9]{10,11}$", phone_number)) {
			response = "failure_wrong_format_phone_number";
			sentence = "ÀüÈ­¹øÈ£´Â 01022223333Ã³·³ ¼ıÀÚ 11ÀÚ¸®·Î ÀÔ·ÂÇØ ÁÖ¼¼¿ä.";
		} else if (userService.findSamePhoneNumber(phone_number) != null) {
			response = "failure_duplicate_phone_number";
			sentence = "ÀÌ¹Ì ¾ÆÀÌµğ°¡ Á¸ÀçÇÏ´Â ÀüÈ­¹øÈ£ÀÔ´Ï´Ù.";
		}
		
		// address

		else {
			// simple_pwd Á¤¼öÇüÀ¸·Î º¯Çü
			int simple_pwd = Integer.valueOf(simple_pwd_string);
			
			// user_num »ı¼º
			String user_num = UUID.randomUUID().toString().replace("-", "");
			
			UserDTO userDTO = new UserDTO(user_num, id, password, simple_pwd, name, nick_name, email, phone_number, address);
			userService.joinUser(userDTO);
			
			response = "success_join";
			sentence = "È¸¿ø°¡ÀÔÀÌ ¿Ï·áµÆ½À´Ï´Ù.";
		}

		returnMap.put("sentence", sentence);
		returnMap.put("response", response);
		return returnMap;
	}
	
	@PostMapping("login") // ·Î±×ÀÎ
	@ResponseBody
	public Map<String, Object> loginUser(@RequestParam Map<String, Object> map) {
		String id = map.get("id").toString();
		String password = map.get("password").toString();
		
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
			UserDTO userDTO = userService.loginUser(id);
			
			if (userDTO == null) {
				response = "failure_notExist_id";
				sentence = "Á¸ÀçÇÏÁö ¾Ê´Â ¾ÆÀÌµğÀÔ´Ï´Ù.";
			} else if (!userDTO.getPassword().equals(password)) {
				response = "failure_wrong_password";
				sentence = "Àß¸øµÈ ºñ¹Ğ¹øÈ£ÀÔ´Ï´Ù.";
			} else {
				response = "success_login";
				sentence = "·Î±×ÀÎÀÌ ¿Ï·áµÆ½À´Ï´Ù.";
				
				// ¼¼¼Ç¿¡ ·Î±×ÀÎ Á¤º¸ ( id, user_num ) ÀúÀå
				userInfo.setId(userDTO.getId());
				userInfo.setUser_num(userDTO.getUser_num());	
			}
		}
		
		// API·Î ÀÀ´ä ³Ñ°ÜÁÖ±â
		returnMap.put("sentence", sentence);
		returnMap.put("response", response);
		return returnMap;
	}
	
	@GetMapping("session") // ·Î±×ÀÎ Á¤º¸
	@ResponseBody
	public UserInfo get() {
		return userInfo;
	}
	
	@PostMapping("logout") // ·Î±×¾Æ¿ô
	public void logoutUser(@RequestParam Map<String, Object> map) {
		userInfo = new UserInfo();
	}
}