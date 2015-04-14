package com.pramati.genericwebcrawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.pramati.genericwebcrawler.services.processor.WCProcessor;
import com.pramati.genericwebcrawler.utility.EmailFilterRule;


public class CrawlerController {

	
	static Logger logger = Logger.getLogger(CrawlerController.class);
	 
	
	public static void main(String[] args) throws MalformedURLException {
		
		BasicConfigurator.configure();
		EmailFilterRule emailFilterObj = new EmailFilterRule();
		
		 System.out.println("Entered url"+args[0]);
		   //Creating shared object
	     BlockingQueue sharedQueue = new ArrayBlockingQueue<String>(10000);
	     
	    URL crawlUrl=new URL(args[0]);
	    String year= args[1];
	    emailFilterObj.setYear(year);
	 
	     //Creating Producer and Consumer Thread
	     Thread prodThread = new Thread(new WCProcessor(sharedQueue,crawlUrl));
	    // Thread consThread = new Thread(new WCProcessor(sharedQueue));

	     //Starting producer and Consumer thread
	     prodThread.start();
	     //consThread.start();

	


	}

}
