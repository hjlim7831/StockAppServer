package com.search.result;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.stock.detail.StockDto;

@Service
public class SearchResultService {

	// stockDto�� ��ī�� ���絵�� �������ϱ� ���� wrapper class ����
	class StockWrapper implements Comparable<StockWrapper> {
		StockDto stockDto;
		double similarity;

		public StockWrapper() {

		}

		public StockWrapper(StockDto stockDto, double similarity) {
			this.stockDto = stockDto;
			this.similarity = similarity;
		}

		// �������� ����
		@Override
		public int compareTo(StockWrapper o) {
			return Double.compare(o.similarity, this.similarity);
		}

	}

	@Autowired
	SearchResultMapper searchResultMapper;

	public List<StockDto> getSearchResultList(String keyWord) {

		// ���Խ����� �˻���� �ѱ�, ����, ����, ���⸦ ������ ��� ���� ����
		String match = "[^\uAC00-\uD7A30-9a-zA-Z\\s]";
		String replacedKey = keyWord.replaceAll(match, "");

		// ��ī�� ���絵�� ������ stockList ��������
		List<StockDto> list = searchResultMapper.selectAllStock();

		int listLen = list.size();

		StockWrapper[] wrapList = new StockWrapper[listLen];

		// ��ī�� ���絵�� �������ϱ�
		for (int i = 0; i < listLen; i++) {
			String company_name = list.get(i).getCompany_name();
			double similarity = jaccardSimilarity(replacedKey, company_name);
			wrapList[i] = new StockWrapper(list.get(i), similarity);
		}

		Arrays.sort(wrapList);

		List<StockDto> resList = new ArrayList<>();

		for (int i = 0; i < listLen; i++) {
			resList.add(wrapList[i].stockDto);
		}

		return resList;

	}

	// �ܾ�� ������ ��ī�����絵 ��� �޼���
	public double jaccardSimilarity(String str1, String str2) {
		Map<Character, Integer> map1 = new HashMap<>();
		Map<Character, Integer> map2 = new HashMap<>();

		int len1 = str1.length();
		int len2 = str2.length();

		for (int i = 0; i < len1; i++) {
			char target = str1.charAt(i);
			if (map1.containsKey(target)) {
				int tmpNum = map1.get(target);
				map1.put(target, tmpNum + 1);
			} else {
				map1.put(target, 1);
			}
		}

		for (int i = 0; i < len2; i++) {
			char target = str2.charAt(i);
			if (map2.containsKey(target)) {
				int tmpNum = map1.get(target);
				map2.put(target, tmpNum + 1);
			} else {
				map2.put(target, 1);
			}
		}

		Set<Character> set1 = map1.keySet();
		Set<Character> set2 = map2.keySet();

		double sub = 0;
		double uni = 0;
		for (Character c : set1) {
			if (set2.contains(c)) {
				sub += Math.min(map1.get(c), map2.get(c));
			}
		}

		Set<Character> union = new HashSet<>(set1);
		union.addAll(set2);

		for (Character c : union) {
			int num1 = 0;
			int num2 = 0;
			if (set1.contains(c)) {
				num1 = map1.get(c);
			}
			if (set2.contains(c)) {
				num2 = map2.get(c);
			}
			uni += Math.max(num1, num2);
		}

		return sub / uni;

	}

}
