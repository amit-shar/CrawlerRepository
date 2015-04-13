package com.pramati.genericwebcrawler.utility;

public class Constants {
	
	public static final String URL_TO_CRAWL = "http://mail-archives.apache.org/mod_mbox/maven-users/";
	public static final String DIR_PATH="DownLoads";
	public static final String URL_PREFIX="http://mail-archives.apache.org/";
	public static final String PATTERN_FOR_HREF= "href=\"(.*?)\"";
	public static final String PAGINATION_CHECK="<th class=\"pages";
	public static final String AUTHOR_NAME_SEARCH="<td class=\"author";
	public static final String MAIL_SUBJECT_SEARCH="<td class=\"subject";
	public static final String MAIL_DATE_TAG_SEARCH="<td class=\"date";
	public static final String VIEW_RAW_MESSAGE="raw/";
	public static final String PAGINATION_DATE_CHECK="date";
	public static final String SUBJECT_LINKS="subject";


	// PRIVATE //

	/**
  The caller references the constants using <Constants.URL_TO_CRAWL> 
  and so on. Thus, the caller should be prevented from constructing objects of 
  this class, by declaring this private constructor. 
	 */
	private Constants()
	{

		//this prevents even the native class from 
		//calling this constructor as well
		throw	new AssertionError();
	}

}
