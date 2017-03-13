package com.sovrn.ad.dal;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.sovrn.ad.domain.AdTransaction;

@Repository
public class CacheDal {

  private static final Logger LOGGER = LoggerFactory.getLogger(CacheDal.class);

  Cache<String, AdTransaction> cache = CacheBuilder.newBuilder()
      .maximumSize(100)
      .build();
  
  public CacheDal() {
    
  }

  public void put(String transactionId, AdTransaction transaction) {
    LOGGER.info("Adding AdTransaction to cache: key {}", transaction);
    cache.put(transactionId, transaction);
  }
  
  public AdTransaction get(String transactionId) {
    LOGGER.info("Getting AdTransaction from cache: key {}", transactionId);
    return cache.getIfPresent(transactionId);
  }
  
  public Collection<AdTransaction> getAll() {
	  return cache.asMap().values();
  }
  
}
