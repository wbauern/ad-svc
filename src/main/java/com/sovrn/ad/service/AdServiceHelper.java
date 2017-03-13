package com.sovrn.ad.service;

import java.net.MalformedURLException;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;
import com.netflix.hystrix.HystrixObservableCommand.Setter;
import com.sovrn.ad.client.BidCmd;

@Component
public class AdServiceHelper {
	
	@Value("${provider.timeoutms:200}")
	int providerTimeoutMs;
	
	BidCmd buildBidCmd(RestTemplate restTemplate, String providerUrl, int width, int height, 
    		String userIp, String userAgent, String domain) {
		String providerCmdKey;
		try {
			providerCmdKey = (new URL(providerUrl)).getHost();
		} catch (MalformedURLException e) {
			providerCmdKey = providerUrl;
		}
		HystrixCommandProperties.Setter commandProperties = HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(100);
		Setter setter = Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProviderCommands"))
			      .andCommandKey(HystrixCommandKey.Factory.asKey(providerCmdKey)).andCommandPropertiesDefaults(commandProperties);
		return new BidCmd(setter, restTemplate, providerUrl, width, height, userIp, userAgent, domain);
	}
}
