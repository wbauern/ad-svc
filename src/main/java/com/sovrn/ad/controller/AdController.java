package com.sovrn.ad.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import com.sovrn.ad.domain.Ad;
import com.sovrn.ad.domain.AdTransaction;
import com.sovrn.ad.service.AdService;

import rx.Observable;
import rx.functions.Action0;

@RestController
public class AdController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AdController.class);

	private static final long DEFERRED_TIMEOUT = 4000L;

	@Autowired
	private AdService adService;

	@Autowired 
	private HttpServletRequest httpServletRequest;

	// TODO: Add validator for URL
	@GetMapping(path="/ad", produces="application/json")
	public DeferredResult<Ad> getAd(@RequestParam("width") int width,
			@RequestParam("height") int height,
			@RequestParam("userid") int userid,
			@RequestParam("url") String url) throws MalformedURLException {
		String userIp = httpServletRequest.getRemoteAddr();
		String userAgent = httpServletRequest.getHeader("user-agent");
		String domain = (new URL(url)).getHost();
		Observable<Ad> adObs = adService.findAd(width, height, userid, userIp, userAgent, domain)
				.map(at -> Ad.builder().tid(at.getTransactionId()).html(at.getWinningHtml()).build());
		DeferredResult<Ad> dr = new DeferredResult<>(DEFERRED_TIMEOUT);
		adObs.subscribe(ad -> { dr.setResult(ad); LOGGER.info("winning ad {}", ad); }, 
				dr::setErrorResult, 
				setNotFoundWhenResultNotSet(dr));
		return dr;
	}

	@GetMapping(path="/click", produces="application/json")
	public void clickEvent(
			@RequestParam("tid") String tid,
			@RequestParam("userid") int userid) {
		adService.click(tid, userid);
	}

	@GetMapping(path="/history", produces="application/json")
	public List<AdTransaction> getHistory() {
		return new ArrayList<AdTransaction>(adService.getHistory());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Action0 setNotFoundWhenResultNotSet(final DeferredResult result) {
		return () -> {
			if (!result.isSetOrExpired()) {
				result.setResult(new ResponseEntity<>(HttpStatus.NOT_FOUND));
			}
		};
	}

	@ExceptionHandler(MalformedURLException.class)
	@ResponseBody
	public ResponseEntity<ErrorResponse> handleTimeoutException(MalformedURLException ex) {

		List<ErrorItem> errorItems = new ArrayList<>();
		errorItems.add(ErrorItem.builder().code("bad_request").message(ex.getMessage()).build());
		ErrorResponse response = ErrorResponse.builder().erroritems(errorItems).status(HttpStatus.BAD_REQUEST.value())
				.build();
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);

	}
}
