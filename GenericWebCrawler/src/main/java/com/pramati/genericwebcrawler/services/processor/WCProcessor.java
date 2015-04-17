package com.pramati.genericwebcrawler.services.processor;


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
import com.pramati.genericwebcrawler.services.implementor.EmailFilterRule;


public class WCProcessor implements Runnable{

	static Logger logger = Logger.getLogger(WCProcessor.class);

	private   BlockingQueue<String> sharedQueue;

	private EmailFilterRule filterRuleObj;

	private  URL urlToCrawl;
	private  Set <String> crawledUrl = new HashSet<String>();
	private CrawlerUtility crawlerUtilityObj;
	
	
	public WCProcessor(){
		this.filterRuleObj= new EmailFilterRule();
	}
	
	public WCProcessor(CrawlerUtility crawlerUtilityObj,EmailFilterRule filterRuleObj)
	{
		this.filterRuleObj= new EmailFilterRule();
		this.crawlerUtilityObj= new CrawlerUtility();
		
		
	}


	public void init(BlockingQueue <String>sharedQueue,URL crawlUrl,String year) {

		this.sharedQueue = sharedQueue;
		this.urlToCrawl=crawlUrl;
		this.crawledUrl = new HashSet<String>();
		this.filterRuleObj.setYear(year);	

	}

	

	public void run() {

		crawlUtility(urlToCrawl);
		System.out.println(sharedQueue.size());


	}


	protected void crawlUtility(URL url)  {
		
		crawledUrl.add(url.toString());
		
		try{
			
			String pageContent="";	
			pageContent=crawlerUtilityObj.convertHtmlToString(url,urlToCrawl);
			
			String link;

			Set<String> hyperlinksSet= getHyperlinks(pageContent);
			

			if(hyperlinksSet!=null)
			{
				Iterator<String> it = hyperlinksSet.iterator();

				while(it.hasNext())
				{
					link=it.next();
					URL baseurl=new URL(url,link);
					
					System.out.println("checking baseurl"+baseurl);
					
				if(filterRuleObj.exitCriteria(baseurl.toString()))
				{	
					System.out.println("adding to the queue"+baseurl);
					sharedQueue.put(baseurl.toString());
					continue;
				}

					if(!crawledUrl.contains(baseurl.toString()) && filterRuleObj.filterCriteria(baseurl.toString()))
					{
							crawlUtility(baseurl);

					}
				}

			
		}
			
			
		}catch (InterruptedException e) {
				logger.error("Interruption ocurred while adding link to shared queue");		
			} catch (MalformedURLException e) {
				logger.error("Invalid URL");
			}
	}





	protected Set<String> getHyperlinks(String pageContent) {

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

		}

		return tempLinks;
	}


	protected Pattern getHyperlinkPattern()
	{  
		return Pattern.compile(Constants.PATTERN_FOR_HREF);
	}




}
