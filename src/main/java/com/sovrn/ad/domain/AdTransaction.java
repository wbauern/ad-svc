package com.sovrn.ad.domain;

import java.math.BigDecimal;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder=true)
@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(builder = AdTransaction.AdTransactionBuilder.class)
public class AdTransaction {
	
	String transactionId;
	int userid;
	List<BidTransaction> bids;
	BigDecimal winningPrice;
	int winningProvider;
	@JsonIgnore
	String winningHtml;
	ClickResult clickResult;
	long timestamp;
	
	@JsonPOJOBuilder(withPrefix = "")
	public static final class AdTransactionBuilder {
	}
}
