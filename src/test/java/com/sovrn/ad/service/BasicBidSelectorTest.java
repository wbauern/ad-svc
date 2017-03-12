package com.sovrn.ad.service;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sovrn.ad.domain.BidTransaction;

public class BasicBidSelectorTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BasicBidSelectorTest.class);
	
	@Test
	public void testBasicBidSelector() {
		BasicBidSelector bbs = new BasicBidSelector();
		List<BidTransaction> bids = new ArrayList<BidTransaction>();
		bids.add(BidTransaction.builder().bidprice(new BigDecimal(.55)).adHtml("AAA").build());
		bids.add(BidTransaction.builder().bidprice(new BigDecimal(.45)).adHtml("BBB").build());
		bids.add(BidTransaction.builder().bidprice(new BigDecimal(.65)).adHtml("CCC").build());
		bids.add(BidTransaction.builder().bidprice(new BigDecimal(.25)).adHtml("DDD").build());
		BidTransaction winningBid = bbs.selectBid(bids);
		assertEquals(winningBid.getAdHtml(), "CCC");
	}
}
