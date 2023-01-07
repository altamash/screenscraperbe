package com.screenscraper;

import com.screenscraper.model.AppData;
import com.screenscraper.service.ScreenScraperService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenScraperApplication implements InitializingBean {

	@Autowired
	private AppData appData;
	@Autowired
	private ScreenScraperService screenScraperService;

	public static void main(String[] args) {
		SpringApplication.run(ScreenScraperApplication.class, args);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		appData.setData(screenScraperService.getData());
	}
}
