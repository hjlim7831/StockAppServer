package com.search.recentRecord;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("search")
public class SearchRecordController {

	@Resource
	private SearchRecordSessionDto searchRecordSessionDto;
	
	// �ֱ� �˻� ��� ��ȸ�ϱ�
	@GetMapping("record")
	public Map<String, Object> searchRecordLookup() {
		Map<String, Object> resultMap = new HashMap<>();
		String response = "succes_lookup_search_record";
		
		// �ֱ� �˻� 
		if (searchRecordSessionDto.getSearchRecordDtoList() == null) {
			searchRecordSessionDto.setSearchRecordDtoList(new ArrayList<>());
		}
		
		List<SearchRecordDto> contents = searchRecordSessionDto.getSearchRecordDtoList();
		resultMap.put("contents", contents);
		resultMap.put("response", response);
		
		return resultMap; // Ŭ���� �ڷ��� �� ����Ʈ JSON ��ȯ, ��� �� ������ ������ ����
	}
}