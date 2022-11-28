package com.data.stock.test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

@Component
public class JsoupComponent {

	public List<KospiStockDto> getKosPiStockList() {
		final String stockList = "https://finance.naver.com/sise/sise_market_sum.nhn?&page=1";
		Connection conn = Jsoup.connect(stockList);
		try {
			Document document = conn.get();
			return getKosPiStockList(document);
		} catch (IOException ignored) {
		}
		return null;
	}

	public List<KospiStockDto> getKosPiStockList(Document document) {
		Elements kosPiTable = document.select("table.type_2 tbody tr");
		List<KospiStockDto> list = new ArrayList<>();
		for (Element element : kosPiTable) {
			if (element.attr("onmouseover").isEmpty()) {
				continue;
			}
			list.add(createKosPiStockDto(element.select("td")));
		}
		return list;
	}

	public KospiStockDto createKosPiStockDto(Elements td) {
		KospiStockDto kospiStockDto = KospiStockDto.builder().build();
		Class<?> clazz = kospiStockDto.getClass();
		Field[] fields = clazz.getDeclaredFields();

		for (int i = 0; i < td.size(); i++) {
			String text;
			if (td.get(i).select(".center a").attr("href").isEmpty()) {
				text = td.get(i).text();
			} else {
				text = "https://finance.naver.com" + td.get(i).select(".center a").attr("href");
			}
			fields[i].setAccessible(true);
			try {
				fields[i].set(kospiStockDto, text);
			} catch (Exception ignored) {
			}
		}
		return kospiStockDto;
	}
}