package com.pramati.genericwebcrawler.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;


import org.apache.log4j.Logger;

import com.pramati.genericwebcrawler.model.EmailMetaData;


public class EmailUtility {


	static Logger logger = Logger.getLogger(EmailUtility.class);



	public void saveEmail(String mailContent,String mailYear,EmailMetaData emailData ) {
     
		
		System.out.println("Inside save email method of email utility");
		createDirectory(Constants.DIR_PATH);
		File directory=createDirectory(Constants.DIR_PATH+"/"+mailYear);

		String filePath;

		if(directory!=null){

			if(mailContent!=null){

				filePath=getFilePath(emailData);
				File messageRawFile= createFile(directory,filePath);	

				saveEmailToFile(messageRawFile,mailContent);

			}


		}


		else
			logger.error("directory not created");

	}

	/*
	 * This method creates the directory.
	 * 
	 */

	public File createDirectory(String dirPath) {


		//String dirPath="Downloads";

		if(dirPath!=null){

			File directory= new File(dirPath);
			boolean success;

			if (directory.exists()) {
				System.out.println("Directory already exists ...");

			} else {
				System.out.println("Directory not exists, creating now");

				success = directory.mkdir();
				if (success) {
					System.out.printf("Successfully created new directory : %s%n", dirPath);
				} else {
					System.out.printf("Failed to create new directory: %s%n", dirPath);
				}
			}
			return directory;
		}
		
		return null;
	}

	public void saveEmailToFile(File messageRawFile, String mailContent) {

		
		FileWriter fw=null;
		try{
			System.out.println("Saving to file");
			fw=new FileWriter(messageRawFile);
			fw.write(mailContent);
			fw.flush();

		}  catch (IOException e) {
			e.printStackTrace();
		}finally {
			try {
				if (fw != null) fw.close();
			} catch (IOException ioe) {
				logger.error("Exception ocurred while closing the file");	
			}
		}

	}


	/*
	 * This method creates the file to store the email raw message.
	 */
	public File createFile(File directory,String filePath) {

		boolean success=false;

		File messageRawFile= new File(directory, filePath);

		if (messageRawFile.exists()) {
			System.out.println("File already exists");

		} else {
			System.out.println("No such file exists, creating now");
			try {
				success = messageRawFile.createNewFile();
			} catch (IOException e) {

				logger.error("Exception ocurred while creating the file");
			}
			if (success) {
				System.out.printf("Successfully created new file: %s%n", messageRawFile);

			} else {
				System.out.printf("Failed to create new file: %s%n",messageRawFile);

			}

		}
		return messageRawFile;
	}


	/*
	 * This method creates the file name and path for the mail.
	 */
	public String getFilePath(EmailMetaData emailObj) {

		String filePath="";

		if(emailObj!=null)
		{  
			if(emailObj.getDate()!=null)
			{
				//String date[]=emailObj.getDate().split(" ");
			  filePath=emailObj.getDate()+emailObj.getSenderName().replaceAll("/","")+emailObj.getSubject().replaceAll("/","")+emailObj.getDate()+".txt";
			  if(filePath.equals(".txt"))
				  filePath="NoavAvailableInfo";
			
			}

		}
		return filePath;


	}



	public String convertHtmlToString(String baseurl) 

	{
		URL hyperlink;
		InputStream is = null;

		BufferedReader br;
		String pageSource="";

		try {

			hyperlink = new URL(baseurl);

			System.out.println("In convert to string method email utility"+ hyperlink);


			is = hyperlink.openStream();  // throws an IOException

			br= new BufferedReader(new InputStreamReader(is));

			Writer out = new StringWriter();
			for(int i=br.read();i!=-1;i=br.read()){
				out.write(i);
			}

			pageSource= out.toString();


		}catch (MalformedURLException e) {

			logger.error("Invalid URL");
		} catch (IOException e) {
			logger.error("File not found for the inptut URL");

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
