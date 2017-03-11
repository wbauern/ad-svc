package com.sovrn.ad.service;

import java.util.Map;

import com.sovrn.ad.client.Bid;
import com.sovrn.ad.dal.entity.Provider;

import rx.Observable;

public interface BidSelector {
	public Observable<Bid> selectBid(Observable<Bid> bids);
}
