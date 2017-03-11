package com.sovrn.ad.service;

import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.sovrn.ad.client.Bid;
import com.sovrn.ad.client.BidCmd;
import com.sovrn.ad.dal.ProviderRepository;
import com.sovrn.ad.dal.entity.Provider;
import com.sovrn.ad.domain.Ad;
import com.sovrn.ad.domain.AdTransaction;

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

	public Observable<Ad> findWinningAd(int width, int height, int userid, String userIp, String userAgent, String domain) {
		return bidSelector.selectBid(findAllBids(width, height, userid, userIp, userAgent, domain))
			.doOnNext(b -> LOGGER.info("winning bidprice {} : adhtml {}", b.getBidprice(), b.getAdhtml()))
			.map(b -> Ad.builder().html(b.getAdhtml()).tid(UUID.randomUUID().toString()).build());
	}
	
	public Observable<Bid> findAllBids(int width, int height, int userid, String userIp, String userAgent, String domain) {
		List<Provider> providers = providerRepo.findUserProviders(userid, width, height);
		return Observable.from(providers)
			.doOnNext(p -> LOGGER.info("provider {}", p.getName()))
			.flatMap(p -> {
				return new BidCmd(restTemplate, p.getUrl(), width, height, userIp, userAgent, domain)
						.toObservable()
						.subscribeOn(Schedulers.io())
						.doOnNext(b -> LOGGER.info("bidprice {} : adhtml {}", b.getBidprice(), b.getAdhtml()));		
			}, 10);
	}

	public List<AdTransaction> getHistory() {
		// TODO Auto-generated method stub
		return null;
	}

	public void click(int tid, int userid) {
		// TODO Auto-generated method stub
		
	}

}
