package com.pramati.genericwebcrawler.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.log4j.Logger;



public class CrawlerUtility {
	
	
	static Logger logger = Logger.getLogger(CrawlerUtility.class);
	
	public String convertHtmlToString(URL baseurl,URL urlToCrawl) 

	{
		URL hyperlink;
		InputStream is = null;

		BufferedReader br;
		String pageSource="";

		try {

			hyperlink = baseurl;//new URL(baseurl,link);
			
            //System.out.println("In convert to string method"+ hyperlink);
            
			if(isHostSame(hyperlink.getHost(),urlToCrawl.getHost()))    //hyperlink.getHost().equals(urlToCrawl.getHost()))
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
	
	
	
	
	private boolean isHostSame(String hyperlink, String baseUrl)
	{
		
		return hyperlink.equals(baseUrl);
			
		
	}
	
	
	public  boolean isValidYear(String mailYear)
	{
		if(mailYear!=null && mailYear.length()==4 && !mailYear.equals("0000") && mailYear.matches("[0-9]+") )
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}
