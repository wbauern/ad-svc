package com.sovrn.ad.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sovrn.ad.domain.Ad;
import com.sovrn.ad.domain.AdTransaction;
import com.sovrn.ad.service.AdService;

@RestController
public class AdController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AdController.class);

	@Autowired
	private AdService adService;
	
	@GetMapping(path="/ad", produces="application/json")
	public Ad getAd(@RequestParam("width") int width,
			@RequestParam("height") int height,
			@RequestParam("userid") int userid,
			@RequestParam("url") String url) {
		return adService.findAd(width, height, userid);
	}
	
	@GetMapping(path="/click", produces="application/json")
	public void clickEvent(
			@RequestParam("tid") int tid,
			@RequestParam("userid") int userid) {
		adService.click(tid, userid);
	}
	
	@GetMapping(path="/history", produces="application/json")
	public List<AdTransaction> getHistory() {
		return adService.getHistory();
	}
	
}
