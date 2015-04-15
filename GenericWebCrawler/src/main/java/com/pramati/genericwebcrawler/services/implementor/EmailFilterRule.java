package com.pramati.genericwebcrawler.services.implementor;

import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pramati.genericwebcrawler.services.FilterRuleService;
import com.pramati.genericwebcrawler.utility.CrawlerUtility;

public class EmailFilterRule implements FilterRuleService {
	
	private String year;
	private final  BlockingQueue<String> sharedQueue;
	private CrawlerUtility crawlerUitlityObj;
	
	
	
	public EmailFilterRule() {
		sharedQueue=null;
	}
	
	public EmailFilterRule(String year) {
		sharedQueue=null;
	}
	

	public EmailFilterRule(String year,BlockingQueue<String> sharedQueue){
		
		this.year=year;
		this.sharedQueue=sharedQueue;
	}
	
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public boolean filterCriteria(String link)
	{
		

		if(link.contains("2015") && (link.contains(".mbox/date?")|| link.contains(".mbox/%") || link.contains("/raw/")))
			return true;
		else 
			return false;
		
	}

	public void run() {
        while(true){
            try {
                System.out.println("Consumed: "+ sharedQueue.take());
            } catch (InterruptedException ex) {
               // Logger.getLogger(Consumer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

	public boolean exitCriteria(String link) {
		
		if(link.contains("/raw/"))
			return true;
		
		return false;
	}

	public void downloadContent(String url) {
		crawlerUitlityObj =new CrawlerUtility();
		String pageContent = crawlerUitlityObj.convertHtmlToString(url);
		
		
		
	}




	

}
