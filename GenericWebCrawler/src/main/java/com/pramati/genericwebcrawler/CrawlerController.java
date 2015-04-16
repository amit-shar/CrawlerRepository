package com.pramati.genericwebcrawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.pramati.genericwebcrawler.services.FilterRuleService;
import com.pramati.genericwebcrawler.services.processor.WCProcessor;
import com.pramati.genericwebcrawler.services.implementor.EmailFilterRule;
import com.pramati.genericwebcrawler.utility.CrawlerUtility;


public class CrawlerController {


	static Logger logger = Logger.getLogger(CrawlerController.class);


	public static void main(String[] args) throws MalformedURLException {

		BasicConfigurator.configure();
		CrawlerUtility crawlerUtilityObj= new CrawlerUtility();

		System.out.println("Entered url"+args[0]);
		//Creating shared object
		ArrayBlockingQueue<String> sharedQueue = new ArrayBlockingQueue<String>(10000);

		URL crawlUrl=new URL(args[0]);
		String year= args[1];

		if(crawlerUtilityObj.isValidYear(year))
		{
			//Creating Producer and Consumer Thread
			Thread prodThread = new Thread(new WCProcessor(sharedQueue,crawlUrl,year));


			FilterRuleService  emailFilterObj= new EmailFilterRule(year,sharedQueue);


			Thread consThread = new Thread(emailFilterObj);

			//Starting producer and Consumer thread

			prodThread.start();
			consThread.start();
		}

		else
		{

			System.out.println("Year must contains numbers only");
			logger.error("In CrawlerController :main(): Year must contains numbers only"); 
			throw new NumberFormatException();
		}



	}

	

}
