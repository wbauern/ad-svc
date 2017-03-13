package com.sovrn.ad.service;

import java.util.Collections;
import java.util.List;

import org.springframework.util.CollectionUtils;

import com.sovrn.ad.domain.BidTransaction;

/*
 * A bid selector that simply chooses the highest bid.
 */
public class BasicBidSelector implements BidSelector {

	@Override
	public BidTransaction selectBid(List<BidTransaction> bids) {
		if (CollectionUtils.isEmpty(bids)) {
			return null;
		}
		Collections.sort(bids, (p1, p2) -> p1.getBidprice().compareTo(p2.getBidprice()));
		return bids.get(bids.size()-1);
	}

}
