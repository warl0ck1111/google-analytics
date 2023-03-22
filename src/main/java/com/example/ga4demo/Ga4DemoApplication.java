package com.example.ga4demo;

import com.example.ga4demo.googleanalytics4.GoogleAnalyticService;
import com.example.ga4demo.googleanalytics4.dto.GoogleAnalyticsAccount;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@SpringBootApplication
@Slf4j
@EnableScheduling
public class Ga4DemoApplication {

	@Autowired
	private GoogleAnalyticService googleAnalyticService;


	public static void main(String[] args) {
		SpringApplication.run(Ga4DemoApplication.class, args);


	}

	@Scheduled(fixedDelay = 1000000000000000L)
	public void testGoogleAnalytics() {
		log.info("testGoogleAnalytics");
		List<GoogleAnalyticsAccount> accounts = googleAnalyticService.getSessionsNb("","",Lo,"");
		log.info("testGoogleAnalytics/accounts: {}");
	}

}
