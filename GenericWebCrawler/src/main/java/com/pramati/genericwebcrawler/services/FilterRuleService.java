package com.pramati.genericwebcrawler.services;

public interface FilterRuleService extends Runnable{
	
	public void run() ;
	boolean filterCriteria(String link);
	boolean exitCriteria(String link);
	void downloadContent(String url);

}
