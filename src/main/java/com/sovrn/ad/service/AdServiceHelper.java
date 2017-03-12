package com.sovrn.ad.service;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.sovrn.ad.client.BidCmd;

@Component
public class AdServiceHelper {
	BidCmd buildBidCmd(RestTemplate restTemplate, String providerUrl, int width, int height, 
    		String userIp, String userAgent, String domain) {
		
		return new BidCmd(restTemplate, providerUrl, width, height, userIp, userAgent, domain);
	}
}
