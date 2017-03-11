package com.sovrn.ad.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sovrn.ad.domain.Ad;
import com.sovrn.ad.service.AdService;

import rx.Observable;

@RunWith(SpringRunner.class)
@WebMvcTest(AdController.class)
public class AdControllerTest {
	
	  @Autowired
	  private MockMvc mockMvc;
	  
	  @MockBean
	  private AdService adService;
	  
	  private ObjectMapper om = new ObjectMapper();

	  @Test
	  public void testSimpleGet() throws Exception {
	    Ad ad = Ad.builder().tid("ABC123").html("<html></html>").build();

	    Mockito.when(adService.findWinningAd(100, 400, 1, "1.1.1.1", "agent", "foo.com")).thenReturn(Observable.just(ad));

	    MvcResult mvcResult = mockMvc.perform(
	    	get("http://localhost:8080/ad?width=100&height=400&userid=1&url=http://foo.com/home.html")
	    		.header("user-agent", "agent")
	    		.with(request -> {
	    			request.setRemoteAddr("1.1.1.1");
	    			return request;
	    		}))
	        .andExpect(status().isOk())
	        .andExpect(request().asyncStarted())
	        .andReturn();
	      MvcResult r2 = mockMvc.perform(asyncDispatch(mvcResult))
	        .andExpect(status().isOk()).andReturn();
	      String s = r2.getResponse().getContentAsString();

	      Ad result = om.readValue(s, Ad.class);
	      assertEquals("ABC123", result.getTid());
	      assertEquals("<html></html>", result.getHtml());

	    }
}
