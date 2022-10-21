package com.crawling.stock.news;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class CrawlingNewsComponent {

	private WebDriver driver;

	String stockNewsUrl;
	StringBuilder sb;

	public void setUp() {
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
	}

	public void tearDown() {
		if (driver != null) {
			driver.quit();
		}
	}

	/**
	 * 특정 주식의 뉴스 페이지. 해당 페이지에서 크롤링 할 예정
	 * 
	 * @param stock_code 크롤링 해올 주식의 코드
	 */
	public void urlSetter(String stock_code) {
		sb = new StringBuilder();
		sb.append("https://finance.naver.com/item/news.naver?code=").append(stock_code);
		this.stockNewsUrl = sb.toString();
	}

	public List<NewsDto> getNewsList() {
		
		driver.get(stockNewsUrl);
		
		System.out.println("체크");
		WebElement element = driver.findElement(By.id("page"));
		System.out.println(element);
		
		driver.quit();
		
		return null;
	}

}
