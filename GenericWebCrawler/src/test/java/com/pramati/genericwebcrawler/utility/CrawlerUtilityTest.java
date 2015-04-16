package com.pramati.genericwebcrawler.utility;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CrawlerUtilityTest {

	private CrawlerUtility crawlerUtilityObj;
	
	@Before
	public void setUp() throws Exception {
		crawlerUtilityObj =  new CrawlerUtility();
	}

	@After
	public void tearDown() throws Exception {
		crawlerUtilityObj= null;
	}

	@Test
	public void isValidYeartest() {
		
		assertTrue(crawlerUtilityObj.isValidYear("2015"));
		
	}
	
	@Test
	public void convertHtmlToStringTest_UrlException() throws MalformedURLException
	{
		URL urlTOCrawl= new URL("http://google.com/maven-users/201503.mbox/raw/");
		URL url= new URL("http://maven-users.archives/mod_mbox/maven-users/201503.mbox/raw/");
		
		assertEquals("",crawlerUtilityObj.convertHtmlToString(urlTOCrawl,url));
		
	}

}
