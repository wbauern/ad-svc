package com.sovrn.ad.service;

import static org.junit.Assert.*;

import java.math.BigDecimal;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sovrn.ad.client.Bid;
import rx.Observable;

public class BasicBidSelectorTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BasicBidSelectorTest.class);

	@Test
	@Ignore
	public void testBasicBidSelector_Empty() {
		BasicBidSelector bbs = new BasicBidSelector();
		Observable<Bid> bids = Observable.empty();
		Observable<Bid> winningBid = bbs.selectBid(bids);
		assertTrue(winningBid.isEmpty().toBlocking().first());
	}
	
	@Test
	public void testBasicBidSelector() {
		BasicBidSelector bbs = new BasicBidSelector();
		Observable<Bid> bids = Observable.just(
				Bid.builder().bidprice(new BigDecimal(.55)).adhtml("AAA").build(),
				Bid.builder().bidprice(new BigDecimal(.45)).adhtml("BBB").build(),
				Bid.builder().bidprice(new BigDecimal(.65)).adhtml("CCC").build(),
				Bid.builder().bidprice(new BigDecimal(.25)).adhtml("DDD").build());
		Bid winningBid = bbs.selectBid(bids)
				.toList()
				.toBlocking()
				.single()
				.get(0);
		assertEquals(winningBid.getAdhtml(), "CCC");
	}
}
