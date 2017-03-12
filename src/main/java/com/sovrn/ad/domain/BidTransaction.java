package com.sovrn.ad.domain;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@JsonDeserialize(builder = BidTransaction.BidBuilder.class)
public class BidTransaction {
	int providerId;
	BigDecimal bidprice;
	
	@JsonIgnore
	String adHtml;
	
	@JsonPOJOBuilder(withPrefix = "")
	public static final class BidBuilder {
	}
}
