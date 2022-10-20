package com.search.recentRecord;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("search")
public class SearchRecordController {
	
	@Resource(name = "searchRecordSessionDto")
	private SearchRecordSessionDto searchRecordSessionDto;
	
	@GetMapping("record")
	@ResponseBody
	public SearchRecordSessionDto searchRecordLookup() {
		System.out.println(searchRecordSessionDto);
		System.out.println("�׽�Ʈ");
		return searchRecordSessionDto; // Ŭ���� �ڷ��� �� ����Ʈ JSON ��ȯ, ��� �� ������ ������ ����
	}
}