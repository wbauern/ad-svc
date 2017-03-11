package com.sovrn.ad.service;

import com.sovrn.ad.client.Bid;

import rx.Observable;

/*
 * A bid selector that simply chooses the highest bid.
 */
public class BasicBidSelector implements BidSelector {

	// TODO: get working with empty observable
	@Override
	public Observable<Bid> selectBid(Observable<Bid> bids) {
//		return Observable.empty();
		return bids
			.toSortedList(BasicBidSelector::compareBidPrice)
			.flatMap(list -> Observable.from(list))
			.last();	
	}

	private static Integer compareBidPrice(Bid bid1, Bid bid2) {
	    return bid1.getBidprice().compareTo(bid2.getBidprice());
	  }
}
