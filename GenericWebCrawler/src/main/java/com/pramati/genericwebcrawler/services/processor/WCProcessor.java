package com.pramati.genericwebcrawler.services.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.pramati.genericwebcrawler.utility.Constants;
import com.pramati.genericwebcrawler.utility.CrawlerUtility;
import com.pramati.genericwebcrawler.services.FilterRuleService;
import com.pramati.genericwebcrawler.services.implementor.EmailFilterRule;


public class WCProcessor implements Runnable{

	static Logger logger = Logger.getLogger(WCProcessor.class);

	private final  BlockingQueue<String> sharedQueue;

	private FilterRuleService filterRuleObj;

	private final URL urlToCrawl;
	private  Set <String> crawledUrl = new HashSet<String>();
	private CrawlerUtility crawlerUitlityObj;


	public WCProcessor(BlockingQueue <String>sharedQueue,URL crawlUrl,String year) {

		this.sharedQueue = sharedQueue;
		this.urlToCrawl=crawlUrl;
		this.crawledUrl = new HashSet<String>();
		init(year);



	}

	private void init(String year)
	{
		this.filterRuleObj= new EmailFilterRule(year);	
		this.crawlerUitlityObj= new CrawlerUtility();

	}

	public void run() {

		crawlUtility(urlToCrawl);
		System.out.println(sharedQueue.size());


	}


	private void crawlUtility(URL url)  {

		if(filterRuleObj.exitCriteria(url.toString())) // exit criteria for the recursion
			return;

		String pageContent="";	
		pageContent=crawlerUitlityObj.convertHtmlToString(url,urlToCrawl);
		String link;

		Set<String> hyperlinksSet= getHyperlinks(pageContent);
		Iterator<String> it = hyperlinksSet.iterator();

		while(it.hasNext())
		{
			link=it.next();

			if(!crawledUrl.contains(link) && filterRuleObj.filterCriteria(url+link))
			{
				try {
					System.out.println("adding to the queue"+link);
					crawledUrl.add(link);
					sharedQueue.put(link);
					crawlUtility(new URL(url,link));
				} 

				catch (InterruptedException e) {
					logger.error("Interruption ocurred while adding link to shared queue");		
				}catch (MalformedURLException e) {
					logger.error("Invalid URL");
				}
			}
		}



	}





	private Set<String> getHyperlinks(String pageContent) {

		Pattern p = getHyperlinkPattern();
		Matcher matcher = p.matcher(pageContent);
		String link;
		Set<String> tempLinks= new HashSet<String>();
		while (matcher.find())
		{
			link=matcher.group(1); 


			if(!link.contains(".css"))	{    

				if(!crawledUrl.contains(link))
				{    
					tempLinks.add(link);
				} 

			}    
			else
				continue;
		}

		return tempLinks;
	}


	protected Pattern getHyperlinkPattern()
	{  
		return Pattern.compile(Constants.PATTERN_FOR_HREF);
	}




}
