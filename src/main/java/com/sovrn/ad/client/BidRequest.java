package com.sovrn.ad.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = BidRequest.BidRequestBuilder.class)
public class BidRequest {
	int width;
	int height;
	String domain;
	String userip;
	String useragent;
	
	@JsonPOJOBuilder(withPrefix = "")
	public static final class BidRequestBuilder {
	}
}
