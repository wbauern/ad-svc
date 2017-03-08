package com.sovrn.ad.domain;

import java.util.List;

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
@JsonDeserialize(builder = AdTransaction.AdTransactionBuilder.class)
public class AdTransaction {
	
	int transactionId;
	int userid;
	List<Bid> bids;
	Double winningPrice;
	int winningProvider;
	ClickResult clickResult;
	
	@JsonPOJOBuilder(withPrefix = "")
	public static final class AdTransactionBuilder {
	}
}
