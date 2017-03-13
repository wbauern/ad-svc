package com.sovrn.ad.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import com.sovrn.ad.client.Bid;
import com.sovrn.ad.client.BidCmd;
import com.sovrn.ad.dal.CacheDal;
import com.sovrn.ad.dal.ProviderRepository;
import com.sovrn.ad.dal.entity.Provider;
import com.sovrn.ad.domain.AdTransaction;
import com.sovrn.ad.domain.BidTransaction;
import com.sovrn.ad.domain.ClickResult;

import rx.Observable;

//@RunWith(SpringRunner.class)
@RunWith( MockitoJUnitRunner.class)
public class AdServiceTest {
	
	@Mock
	ProviderRepository providerRepoMock;
	
	@Mock
	BidCmd bidCmdMock;
	
	@Mock
	RestTemplate restTemplateMock;
	
	@Mock
	BidSelector bidSelectorMock;
	
	@Mock
	AdServiceHelper adServiceHelperMock;
	
	@Mock
	CacheDal cacheDalMock;

	@InjectMocks
	AdService adService = new AdService();

	@Test
	public void testFindAllBids() throws Exception {
		
		List<Provider> providerList = new ArrayList<>();
		Provider p = new Provider();
		p.setId(111);
		p.setName("TEST PROVIDER 1");
		p.setUrl("url1");
		providerList.add(p);
		p = new Provider();
		p.setId(222);
		p.setName("TEST PROVIDER 2");
		p.setUrl("url2");
		providerList.add(p);

		when(providerRepoMock.findUserProviders(111, 400, 100)).thenReturn(providerList);

		when(adServiceHelperMock.buildBidCmd(any(), ArgumentMatchers.eq("url1"), ArgumentMatchers.eq(400), ArgumentMatchers.eq(100), 
				ArgumentMatchers.eq("1.1.1.1"), ArgumentMatchers.eq("agent"), ArgumentMatchers.eq("foo.com"))).thenReturn(bidCmdMock);

		when(adServiceHelperMock.buildBidCmd(any(), ArgumentMatchers.eq("url2"), ArgumentMatchers.eq(400), ArgumentMatchers.eq(100), 
				ArgumentMatchers.eq("1.1.1.1"), ArgumentMatchers.eq("agent"), ArgumentMatchers.eq("foo.com"))).thenReturn(bidCmdMock);

		when(bidCmdMock.toObservable()).thenReturn(Observable.just(Bid.builder().bidprice(new BigDecimal(.5)).adhtml("html1").build()));
		when(bidCmdMock.toObservable()).thenReturn(Observable.just(Bid.builder().bidprice(new BigDecimal(.75)).adhtml("html2").build()));
		
		Observable<BidTransaction> bt = adService.findAllBids(400, 100, 111, "1.1.1.1", "agent", "foo.com");
		List<BidTransaction> bidList = bt.toList().toBlocking().first();
		assertEquals(2, bidList.size());
	}
	
	@Test
	public void testFindAd() throws Exception {
		
		List<Provider> providerList = new ArrayList<>();
		Provider p = new Provider();
		p.setId(111);
		p.setName("TEST PROVIDER 1");
		p.setUrl("url1");
		providerList.add(p);
		p = new Provider();
		p.setId(222);
		p.setName("TEST PROVIDER 2");
		p.setUrl("url2");
		providerList.add(p);

		when(providerRepoMock.findUserProviders(111, 400, 100)).thenReturn(providerList);

		when(adServiceHelperMock.buildBidCmd(any(), ArgumentMatchers.eq("url1"), ArgumentMatchers.eq(400), ArgumentMatchers.eq(100), 
				ArgumentMatchers.eq("1.1.1.1"), ArgumentMatchers.eq("agent"), ArgumentMatchers.eq("foo.com"))).thenReturn(bidCmdMock);

		when(adServiceHelperMock.buildBidCmd(any(), ArgumentMatchers.eq("url2"), ArgumentMatchers.eq(400), ArgumentMatchers.eq(100), 
				ArgumentMatchers.eq("1.1.1.1"), ArgumentMatchers.eq("agent"), ArgumentMatchers.eq("foo.com"))).thenReturn(bidCmdMock);

		Bid bid1 = Bid.builder().bidprice(new BigDecimal(.5)).adhtml("html1").build();
		Bid bid2 = Bid.builder().bidprice(new BigDecimal(.75)).adhtml("html2").build();
		when(bidCmdMock.toObservable()).thenReturn(Observable.just(bid1));
		when(bidCmdMock.toObservable()).thenReturn(Observable.just(bid2));
		
		when(bidSelectorMock.selectBid(ArgumentMatchers.any(List.class)))
				.thenReturn(BidTransaction.builder().bidprice(bid2.getBidprice()).build());

		Observable<AdTransaction> at = adService.findAd(400, 100, 111, "1.1.1.1", "agent", "foo.com");
		AdTransaction result = at.toList().toBlocking().first().get(0);
		assertEquals(new BigDecimal(.75), result.getWinningPrice());
		
		verify(cacheDalMock).put(ArgumentMatchers.eq(result.getTransactionId()), ArgumentMatchers.any(AdTransaction.class));
	}
	
	 @Test
	 public void testClick() throws Exception {
	   adService.clickLimit = 5;
	   AdTransaction at = AdTransaction.builder().transactionId("ABC123").timestamp(System.currentTimeMillis()).build();
	   when(cacheDalMock.get("ABC123")).thenReturn(at);
	   adService.click("ABC123", 111);
	   AdTransaction at2 = at.toBuilder().clickResult(ClickResult.CLICK).build();
	   verify(cacheDalMock).put(ArgumentMatchers.eq("ABC123"), ArgumentMatchers.eq(at2));
	 }
	 
   @Test
   public void testClick_Stale() throws Exception {
     adService.clickLimit = 1;
     AdTransaction at = AdTransaction.builder().transactionId("ABC123").timestamp(System.currentTimeMillis()-1100).build();
     when(cacheDalMock.get("ABC123")).thenReturn(at);
     adService.click("ABC123", 111);
     AdTransaction at2 = at.toBuilder().clickResult(ClickResult.STALE).build();
     verify(cacheDalMock).put(ArgumentMatchers.eq("ABC123"), ArgumentMatchers.eq(at2));
   }
	
}
