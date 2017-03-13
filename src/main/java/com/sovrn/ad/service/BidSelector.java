package com.sovrn.ad.service;

import java.util.List;

import com.sovrn.ad.domain.BidTransaction;

public interface BidSelector {
	public BidTransaction selectBid(List<BidTransaction> bids);
}
