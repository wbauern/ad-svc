package com.sovrn.ad.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sovrn.ad.dal.CacheDal;

@Configuration
public class CacheConfig {

  @Bean
  public CacheDal cacheDal() {
    return new CacheDal();
  }
}
