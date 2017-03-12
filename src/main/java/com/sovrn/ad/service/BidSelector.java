package com.sovrn.ad.service;

import java.util.List;
import java.util.Map;

import com.sovrn.ad.client.Bid;
import com.sovrn.ad.dal.entity.Provider;
import com.sovrn.ad.domain.BidTransaction;

import rx.Observable;

public interface BidSelector {
	public BidTransaction selectBid(List<BidTransaction> bids);
}
