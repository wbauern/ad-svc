package com.sovrn.ad.service;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.mockito.InjectMocks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sovrn.ad.client.BidCmd;
import com.sovrn.ad.dal.CacheDal;
import com.sovrn.ad.dal.ProviderRepository;
import com.sovrn.ad.dal.entity.Provider;
import com.sovrn.ad.domain.Ad;
import com.sovrn.ad.domain.AdTransaction;
import com.sovrn.ad.domain.BidTransaction;
import com.sovrn.ad.domain.ClickResult;

import rx.Observable;
import rx.schedulers.Schedulers;

@Service
public class AdService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AdService.class);

	@Autowired
	ProviderRepository providerRepo;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	BidSelector bidSelector;
	
	@Autowired
	CacheDal cacheDal;
	
	@Autowired
	AdServiceHelper adServiceHelper;
	
	@Value("${CLICKTIMER:5}")
	int clickLimit;
	
	public AdService () {
		
	}
	
	public Observable<AdTransaction> findAd(int width, int height, int userid, String userIp, String userAgent, String domain) {
		List<BidTransaction> bids = 
			findAllBids(width, height, userid, userIp, userAgent, domain)
				.toList()
				.toBlocking()
				.single();
		
		BidTransaction winningBid = bidSelector.selectBid(bids);
		
		AdTransaction adTransaction = AdTransaction.builder()
				.userid(userid)
				.winningHtml(winningBid.getAdHtml())
				.winningPrice(winningBid.getBidprice())
				.winningProvider(winningBid.getProviderId())
				.transactionId(UUID.randomUUID().toString())
				.clickResult(ClickResult.REQUEST)
				.timestamp(System.currentTimeMillis())
				.bids(bids)
				.build();
		
		// Add adTransaction to history cache
		cacheDal.put(adTransaction.getTransactionId(), adTransaction);
		
//		return Observable.just(Ad.builder().tid(adTransaction.getTransactionId()).html(adTransaction.getWinningHtml()).build());		
		return Observable.just(adTransaction);		
	}
	
	protected Observable<BidTransaction> findAllBids(int width, int height, int userid, String userIp, String userAgent, String domain) {
		List<Provider> providers = providerRepo.findUserProviders(userid, width, height);
		return Observable.from(providers)
			.doOnNext(p -> LOGGER.info("provider {}", p.getName()))
			.flatMap(p -> {
				return adServiceHelper.buildBidCmd(restTemplate, p.getUrl(), width, height, userIp, userAgent, domain)
						.toObservable()
						.subscribeOn(Schedulers.io())
						.doOnNext(b -> LOGGER.info("bidprice {} : adhtml {}", b.getBidprice(), b.getAdhtml()))
						.map(b -> 
							BidTransaction.builder().bidprice(b.getBidprice()).providerId(p.getId()).adHtml(b.getAdhtml()).build());
			}, 10);
	}

	public Collection<AdTransaction> getHistory() {
		return cacheDal.getAll();
	}

	public void click(String tid, int userid) {
		AdTransaction adTransaction = cacheDal.get(tid);
		if (adTransaction != null) {
			ClickResult clickResult = ClickResult.CLICK;
			if (System.currentTimeMillis() - adTransaction.getTimestamp() > (clickLimit * 1000)) {
				clickResult = ClickResult.STALE;
			}
			AdTransaction newTransaction = adTransaction.toBuilder().clickResult(clickResult).build();
			cacheDal.put(adTransaction.getTransactionId(), newTransaction);
		}
	}

}
