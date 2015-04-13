package com.pramati.genericwebcrawler.services.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.pramati.genericwebcrawler.utility.Constants;


public class WCProcessor implements Runnable{
	
	static Logger logger = Logger.getLogger(WCProcessor.class);
	
	private final  BlockingQueue<String> sharedQueue;
	private String url;
	private  Set <String> hyperlinksSet = new HashSet<String>();
	

    public WCProcessor(BlockingQueue <String>sharedQueue, String url) {
        this.sharedQueue = sharedQueue;
        this.url=url;
        this.hyperlinksSet = new HashSet<String>();
    }


    public void run() {
       
    	/* for(int i=0; i<10; i++){
            try {
                System.out.println("Produced: " + i);
                sharedQueue.put(i);
                
            } catch (InterruptedException ex) {
              
            }
        }*/
    	
  try {
	crawlUrlWrapper(new URL(url));
} catch (MalformedURLException e) {
	
	e.printStackTrace();
}
  
  System.out.println(sharedQueue.size());


    }


	private void crawlUrlWrapper(URL url) {
		
		try {
			
			crawlUtility(url,"");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private void crawlUtility(URL url,String hyperlink) throws IOException {
		
		
		if(url==null)
			return;
			
		String pageContent="";	
	    pageContent=convertHtmlToString(url,hyperlink);
         
		 hyperlinksSet= getHyperlinks(pageContent);
		 Iterator<String> it = hyperlinksSet.iterator();
		 
		 while(it.hasNext())
		 {
			 crawlUtility(url, it.next());
			 
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
		      
		       System.out.println(link);
		       
		        try {
		        	
		       if(!link.contains(".css"))	    	   
		        	if(link.contains("http")|| link.contains("https"))
		        	{   
		        	  System.out.println("adding to the queue"+link);
		        	  sharedQueue.put(link);
		        	  tempLinks.add(link);
		        	
					}
		        	
		        	/*else
		        	{	 
		        	 sharedQueue.put(url+link);
		        	 tempLinks.add(url+link);
		        	 
		        	}*/
		        	
		       // crawlUtility(url,link);
		        
		       else
		    	   continue;
				} 
		       catch (InterruptedException e) {
					
					e.printStackTrace();
		       }
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
             System.out.println("converting given url"+url);
				if(link.contains("https") || link.contains("http"))
					hyperlink = new URL(link);
				else
				   hyperlink = new URL(baseurl,link);
				is = hyperlink.openStream();  // throws an IOException
				br= new BufferedReader(new InputStreamReader(is));
				
				Writer out = new StringWriter();
				for(int i=br.read();i!=-1;i=br.read()){
					out.write(i);
				}
		   
		     pageSource= out.toString();
			
			} catch (MalformedURLException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			} finally {
				try {
					if (is != null) is.close();
				} catch (IOException ioe) {
					System.out.println("Exception ocurred while closing the file in method: getHyperlinksOfGivenYearService");
					logger.error("Exception ocurred while closing the file in method: getHyperlinksOfGivenYearService");
				}
			} 
		 
		return  pageSource;
		 
	 }
}
