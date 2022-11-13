package com.data.api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

import org.python.icu.util.Calendar;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

@Component
public class OpeningDateComponent {

	@Value("${spcd.apikey}")
	String apiKey;

	String url = "http://apis.data.go.kr/B090041/openapi/service/SpcdeInfoService/getRestDeInfo";
	String filepath = ".\\src\\main\\resources\\openingDate.json";

	Gson gson;

	public boolean isOpeningDate() {
		/*
		 * 1. 「관공서의 공휴일에 관한 규정」에 따른 공휴일 (일요일 포함)
		 * 2. 「근로자의 날 제정에 관한 법률」에 따른 근로자의 날
		 * 3.  토요일
		 * 4.  12월 31일(공휴일 또는 토요일인 경우에는 직전의 매매거래일로 한다)
		 *  그 밖에 경제사정의 급격한 변동 또는 급격한 변동이 예상되거나 거래소가
		 *  시장관리상 필요하다고 인정하는 날
		 */

		gson = new Gson();

		ZonedDateTime zt = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
		int year = zt.getYear();
		int mo = zt.getMonthValue();
		int day = zt.getDayOfMonth();
		String det = String.format("%02d%02d", mo, day);

		// 파일이 아예 없거나, 그 파일이 해당 년도와 일치하지 않으면
		
		if (!Files.exists(Paths.get(filepath))){
			getOpeningDate(year);
		
		}
		
		boolean result;
		JsonObject obj = null;
		Reader reader;
		try {
			reader = new FileReader(filepath);
			obj = gson.fromJson(reader, JsonObject.class);
			if (obj.get("year").getAsInt() != year) {
				getOpeningDate(year);
				obj = gson.fromJson(reader, JsonObject.class);
				
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		result = readOpeningDate(obj, det);
		// {year:yyyy, items:{mmdd : n, }}

		return result;
	}

	private boolean readOpeningDate(JsonObject obj, String det) {
//		System.out.println(obj);
		if (obj.getAsJsonObject("body").get(det) == null) {
			return false;
		}else {
			return true;
		}

	}

	private void getOpeningDate(int year) {

		StringBuffer result = new StringBuffer();

		try {
			UriComponents builder = UriComponentsBuilder.fromHttpUrl(url).queryParam("solYear", year)
					.queryParam("ServiceKey", apiKey).queryParam("_type", "json").build();

			HttpURLConnection urlConnection = (HttpURLConnection) new URL(builder.toString()).openConnection();

			urlConnection.setRequestMethod("GET");
			BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
			String returnLine;
			while ((returnLine = br.readLine()) != null) {
				result.append(returnLine + "\n");
			}

			JsonObject json = gson.fromJson(result.toString(), JsonObject.class);
			JsonArray items = json.getAsJsonObject("response").getAsJsonObject("body").getAsJsonObject("items")
					.getAsJsonArray("item");

			writeOpeningDateFile(items, year);

		} catch (ProtocolException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void writeOpeningDateFile(JsonArray items, int year) {
		JsonObject obj = new JsonObject();
		obj.addProperty("year", year);
		JsonObject body = new JsonObject();
		
		// 1. 공휴일 포함
		for (JsonElement item : items) {
			String modt = item.getAsJsonObject().get("locdate").getAsString().substring(4, 8);
			body.addProperty(modt, "N");
		}
				
		// 2. 토요일, 일요일 포함
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat resDf = new SimpleDateFormat("MMdd");
		Date dateStartDate = null;
		Date dateEndDate = null;
		String stDate = String.format("%d-01-01 00:00:00", year);
		String edDate = String.format("%d-12-31 23:59:59", year);
		System.out.println(stDate);
		System.out.println(edDate);
		
		try {
			dateStartDate = df.parse(stDate);
			dateEndDate = df.parse(edDate);
		}catch (ParseException p) {
			System.out.println(p.getStackTrace());
		}
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(dateStartDate);
		
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(dateEndDate);
		
		
		while(cal1.before(cal2)) {
			if (cal1.isWeekend()) {
				String tmpDate = resDf.format(cal1.getTime());
				if (!body.keySet().contains(tmpDate)) {
					body.addProperty(tmpDate, "N");
				}
			}
			cal1.add(Calendar.DATE, 1);
		}
		
		//3. 근로자의 날 포함
		if (!body.keySet().contains("0501")) {
			body.addProperty("0501", "N");
		}
		
		//4. 12월 31일. (이미 쉬는 날이면, 그 전의 날 중 쉬지 않는 날 하루 제외)
		boolean flag = true;
		int day = 31;
		while(flag) {
			String last = String.format("12%02d", day);
			if (!body.keySet().contains(last)) {
				flag = false;
				body.addProperty(last, "N");
			}
			day--;
		}
		
		obj.add("body", body);

		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(filepath));
			gson.toJson(obj, writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
