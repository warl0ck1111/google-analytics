package com.example.ga4demo;

import com.example.ga4demo.googleanalytics4.GoogleAnalytic4Service;
import com.example.ga4demo.googleanalytics4.dto.GoogleAnalytics4Account;
import com.example.ga4demo.googleanalytics4.core.types.GA4MediumType;
import com.example.ga4demo.googleanalytics4.core.types.GoogleAnalytics4SinceDateType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@SpringBootApplication
@Slf4j
@EnableScheduling
public class Ga4DemoApplication {

	@Autowired
	private GoogleAnalytic4Service googleAnalytic4Service;


	public static void main(String[] args) {
		SpringApplication.run(Ga4DemoApplication.class, args);


	}

	@Scheduled(fixedDelay = 1000000000000000L)
	public void testGoogleAnalytics() {
		log.info("testGoogleAnalytics");
		String testDomainName= "incremys.com";
		String propertyId = "177959715";
		GA4MediumType mediumType = GA4MediumType.DIRECT;
		GoogleAnalytics4SinceDateType sinceDateType = GoogleAnalytics4SinceDateType.LAST_7_DAYS;



		List<GoogleAnalytics4Account> accounts = googleAnalytic4Service.getAccounts();
		log.info("GoogleAnalytics4/accounts="+accounts);

		Long sessionsNb = googleAnalytic4Service.getSessionsNb(testDomainName, propertyId, LocalDate.now().minusDays(7), LocalDate.now());
		log.info("GoogleAnalytics4/sessionsNb="+sessionsNb);

		Set<String> propertyUrlsForMedium = googleAnalytic4Service.getPropertyUrlsForMedium(testDomainName, propertyId, mediumType);
		log.info("GoogleAnalytics4/propertyUrlsForMedium="+propertyUrlsForMedium);

		Set<String> propertyUrlsSince = googleAnalytic4Service.getPropertyUrlsSince(testDomainName, propertyId, sinceDateType);
		log.info("GoogleAnalytics4/propertyUrlsSince="+propertyUrlsSince);

		Set<String> propertyUrlsForMediumSince = googleAnalytic4Service.getPropertyUrlsForMediumSince(testDomainName, propertyId, mediumType, sinceDateType);
		log.info("GoogleAnalytics4/propertyUrlsForMediumSince="+propertyUrlsForMediumSince);


	}

}
