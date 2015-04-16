package com.pramati.genericwebcrawler.services.processor;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.pramati.genericwebcrawler.services.processor.WCProcessor;

public class WCProcessorTest {

	private WCProcessor crawlerProcessorObj;
	private BlockingQueue<String> queue = new ArrayBlockingQueue<String>(100);
	private URL urlTOCrawl;
	@Before
	public void setUp() throws Exception {

		crawlerProcessorObj= new WCProcessor(queue,null,"2015");
	}



	@Test
	public void getHyperLinktest() {

		String content="dfhfdhdkfhfn fgj <href=\"201503.mbox\"   <href=\"abcd.org\"   <href=\"abcd.org\"";
		Set<String> expectedOutput= new HashSet<String>();

		expectedOutput.add("abcd.org");
		expectedOutput.add("201503.mbox");
		Set<String> actualOutput=crawlerProcessorObj.getHyperlinks(content);

		assertTrue(actualOutput.containsAll(expectedOutput));


	}

	@Test(expected=MalformedURLException.class)

	public void crwalerUitlityTest_UrlException() throws MalformedURLException
	{
		urlTOCrawl= new URL("/mod_mbox/maven-users/201503.mbox/raw/");
		crawlerProcessorObj.crawlUtility(urlTOCrawl);


	}


	@After
	public void tearDown() throws Exception {
		crawlerProcessorObj=null;
	}

}
