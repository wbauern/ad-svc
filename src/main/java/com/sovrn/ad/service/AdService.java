package com.sovrn.ad.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sovrn.ad.dal.ProviderRepository;
import com.sovrn.ad.dal.entity.Provider;
import com.sovrn.ad.domain.Ad;
import com.sovrn.ad.domain.AdTransaction;

@Service
public class AdService {
	private static final Logger LOGGER = LoggerFactory.getLogger(AdService.class);

	@Autowired
	ProviderRepository providerRepo;

	public Ad findAd(int width, int height, int userid) {
		List<Provider> providers = providerRepo.findUserProviders(userid, width, height);
		providers.stream().forEach(p -> LOGGER.info("provider {}", p.getName()));
		return null;
	}

	public List<AdTransaction> getHistory() {
		// TODO Auto-generated method stub
		return null;
	}

	public void click(int tid, int userid) {
		// TODO Auto-generated method stub
		
	}

}
