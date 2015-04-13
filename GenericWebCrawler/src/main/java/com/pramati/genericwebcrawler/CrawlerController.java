package com.pramati.genericwebcrawler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import com.pramati.genericwebcrawler.services.processor.WCProcessor;


public class CrawlerController {

	
	static Logger logger = Logger.getLogger(CrawlerController.class);
	
	public static void main(String[] args) {
		
		BasicConfigurator.configure();

		
		 System.out.println("Entered url"+args[0]);
		   //Creating shared object
	     BlockingQueue sharedQueue = new ArrayBlockingQueue<String>(1000);
	     
	    
	 
	     //Creating Producer and Consumer Thread
	     Thread prodThread = new Thread(new WCProcessor(sharedQueue,args[0]));
	    // Thread consThread = new Thread(new WCProcessor(sharedQueue));

	     //Starting producer and Consumer thread
	     prodThread.start();
	     //consThread.start();

	


	}

}
