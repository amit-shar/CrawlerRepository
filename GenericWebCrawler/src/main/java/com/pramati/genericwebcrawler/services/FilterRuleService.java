package com.pramati.genericwebcrawler.services;

import java.util.concurrent.BlockingQueue;

public interface FilterRuleService extends Runnable {

	// public void run() ;
	boolean filterCriteria(String link);

	boolean exitCriteria(String link);

	void downloadContent(String url);

	void init(BlockingQueue<String> sharedQueue, String searchString);

}
