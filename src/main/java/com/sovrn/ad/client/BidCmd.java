package com.sovrn.ad.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixObservableCommand;

import rx.Observable;

public class BidCmd extends HystrixObservableCommand<Bid> {
	  private static final Logger LOGGER = LoggerFactory.getLogger(BidCmd.class);

	  private static final Setter SETTER = Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProviderCommands"))
	      .andCommandKey(HystrixCommandKey.Factory.asKey("BidCommand"));

	    private final RestTemplate restTemplate;
	    private final String providerUrl;
	    private final int width;
	    private final int height;
	    private String userIp;
	    private String userAgent;
	    private String domain;

	    public BidCmd(RestTemplate restTemplate, String providerUrl, int width, int height, 
	    		String userIp, String userAgent, String domain) {
	        super(SETTER);
	        this.restTemplate = restTemplate;
	        this.providerUrl = providerUrl;
	        this.width = width;
	        this.height = height;
	        this.userIp = userIp;
	        this.userAgent = userAgent;
	        this.domain = domain;
	    }

	    @Override
	    protected Observable<Bid> construct() {
	    	LOGGER.info("Calling Provider URL {}", providerUrl);
	    	BidRequest bidRequest = 
	    			BidRequest.builder().width(width).height(height).domain("").userip("")
	    			.useragent("").domain(domain).useragent(userAgent).userip(userIp)
	    			.build();
	        
	    	HttpEntity<BidRequest> request = new HttpEntity<>(bidRequest);

	    	
	        ResponseEntity<Bid> response = restTemplate.exchange(providerUrl, HttpMethod.POST, request, Bid.class);
	        if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
	          return Observable.empty();
	        }
	        return Observable.just(response.getBody());
	    }
	    
//	    @Override
//	    protected Observable<Bid> resumeWithFallback() {
//	      LOGGER.info("In resumeWithFallback");
//	    }
	}
