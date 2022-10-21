package com.user.account;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.user.info.UserInfoDto;

@RestController
@RequestMapping("user")
public class UserAccountController {
	
	@Autowired
	UserAccountService userAccountService;
	
	@PostMapping("account")
	public Map<String, Object> lookupAccount(@SessionAttribute("loginUser") UserInfoDto userInfoDto) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		
		String response = "failure_none_login_data";
		UserAccountDto contents = new UserAccountDto();
		
		if (userInfoDto != null) {
			response = "success_lookup_account";
			
			String user_num = userInfoDto.getUser_num();
			contents = userAccountService.lookupAccount(user_num);
		}
		
		resultMap.put("contents", contents);
		resultMap.put("response", response);
		
		return resultMap;
	}
}