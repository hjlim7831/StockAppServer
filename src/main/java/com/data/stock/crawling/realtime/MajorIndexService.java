package com.data.stock.crawling.realtime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service
public class MajorIndexService {

	public MajorIndexDto[] getMajorIndexRealtime() {
		// KOSPI, KOSDAQ, KPI200
		String urlLink = "https://polling.finance.naver.com/api/realtime?query=SERVICE_INDEX:KOSPI,KOSDAQ,KPI200";
		MajorIndexDto[] miArr = new MajorIndexDto[3];
		
		try {
			URL url = new URL(urlLink);

			InputStream input = url.openStream();
			InputStreamReader isr = new InputStreamReader(input);
			BufferedReader reader = new BufferedReader(isr);
			StringBuilder jsonString = new StringBuilder();
			int c;
			while ((c = reader.read()) != -1) {
				jsonString.append((char) c);
			}
			Gson gson = new Gson();
			JsonObject json = gson.fromJson(jsonString.toString(), JsonObject.class);
			JsonArray result = json.getAsJsonObject("result").getAsJsonArray("areas").get(0).getAsJsonObject()
					.getAsJsonArray("datas");
			for (int i = 0; i < 3; i++) {
				miArr[i] = gson.fromJson(result.get(i),MajorIndexDto.class);
			}
			System.out.println(Arrays.toString(miArr));

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return miArr;

	}
}
