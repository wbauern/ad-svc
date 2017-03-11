package com.sovrn.ad.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import com.sovrn.ad.service.BasicBidSelector;
import com.sovrn.ad.service.BidSelector;

@Configuration
public class AdSvcConfig {
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@Bean
	public BidSelector bidSelector() {
		return new BasicBidSelector();
	}
}
