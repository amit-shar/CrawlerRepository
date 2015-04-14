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
import com.pramati.genericwebcrawler.utility.EmailFilterRule;


public class WCProcessor implements Runnable{

	static Logger logger = Logger.getLogger(WCProcessor.class);

	private final  BlockingQueue<String> sharedQueue;

	private EmailFilterRule emailFilterObj;

	private final URL urlToCrawl;
	private  Set <String> crawledUrl = new HashSet<String>();


	public WCProcessor(BlockingQueue <String>sharedQueue,URL crawlUrl) {

		this.sharedQueue = sharedQueue;
		this.urlToCrawl=crawlUrl;
		this.crawledUrl = new HashSet<String>();


	}


	public void run() {

		crawlUtility(urlToCrawl,"");
		System.out.println(sharedQueue.size());


	}


	private void crawlUtility(URL url,String hyperlink)  {

		String pageContent="";	
		pageContent=convertHtmlToString(url,hyperlink);
		String link;

		Set<String> hyperlinksSet= getHyperlinks(pageContent);
		Iterator<String> it = hyperlinksSet.iterator();

		while(it.hasNext())
		{
			link=it.next();

			if(!crawledUrl.contains(link) && emailFilterObj.filterUrl(url+link))
			{
				try {
					System.out.println("adding to the queue"+url+link);
					crawledUrl.add(link);
					sharedQueue.put(url+link);
					crawlUtility(new URL(url,link),"");
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


	protected String convertHtmlToString(URL baseurl,String link) 

	{
		URL hyperlink;
		InputStream is = null;

		BufferedReader br;
		String pageSource="";

		try {

			hyperlink = new URL(baseurl,link);

			if(hyperlink.getHost().equals(urlToCrawl.getHost()))
			{
				is = hyperlink.openStream();  // throws an IOException

				br= new BufferedReader(new InputStreamReader(is));

				Writer out = new StringWriter();
				for(int i=br.read();i!=-1;i=br.read()){
					out.write(i);
				}

				pageSource= out.toString();

			} 

		}catch (MalformedURLException e) {

			logger.error("Invalid URL");
		} catch (IOException e) {
			logger.error("File no found found for the inptut URL");

		} finally {
			try {
				if (is != null) is.close();
			} catch (IOException ioe) {
				logger.error("Exception ocurred while closing the file in method: convertHtmlToString");
			}
		} 

		return  pageSource;

	}
}
