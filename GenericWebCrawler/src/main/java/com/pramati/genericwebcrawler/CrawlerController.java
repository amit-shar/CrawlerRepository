package com.pramati.genericwebcrawler;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.pramati.genericwebcrawler.services.FilterRuleService;
import com.pramati.genericwebcrawler.services.processor.WCProcessor;
import com.pramati.genericwebcrawler.services.implementor.EmailFilterRule;
import com.pramati.genericwebcrawler.utility.CrawlerUtility;

public class CrawlerController {

	static Logger logger = Logger.getLogger(CrawlerController.class);

	public static void main(String[] args) throws MalformedURLException {

		BasicConfigurator.configure();
		CrawlerUtility crawlerUtilityObj = new CrawlerUtility();

		System.out.println("Entered url" + args[0]);
		// Creating shared object
		ArrayBlockingQueue<String> sharedQueue = new ArrayBlockingQueue<String>(
				10000);

		URL crawlUrl = new URL(args[0]);
		String year = args[1];

		if (crawlerUtilityObj.isValidYear(year)) {
			@SuppressWarnings("resource")
			ApplicationContext context = new ClassPathXmlApplicationContext(
					"spring.xml");

			WCProcessor wcProcessorObj = (WCProcessor) context
					.getBean("crawlerProcessor");
			wcProcessorObj.init(sharedQueue, crawlUrl, year);

			// Creating Producer and Consumer Thread
			Thread prodThread = new Thread(wcProcessorObj);

			FilterRuleService emailFilterObj = (EmailFilterRule) context
					.getBean("emailFilterRule");
			emailFilterObj.init(sharedQueue, year);

			Thread consThread = new Thread(emailFilterObj);

			// Starting producer and Consumer thread

			prodThread.start();
			consThread.start();
		} else {
			logger.error("In CrawlerController :main(): Year must contains numbers only");
			throw new NumberFormatException();
		}

	}

}
