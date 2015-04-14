package com.pramati.genericwebcrawler.services.implementor;

import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.pramati.genericwebcrawler.services.FilterRuleService;

public class EmailFilterRule implements FilterRuleService, Runnable {
	
	private String year;
	private final  BlockingQueue<String> sharedQueue;
	
	
	
	public EmailFilterRule() {
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




	

}
