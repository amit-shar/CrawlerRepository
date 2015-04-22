package com.pramati.genericwebcrawler.services.implementor;

import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.pramati.genericwebcrawler.model.EmailMetaData;
import com.pramati.genericwebcrawler.services.FilterRuleService;
import com.pramati.genericwebcrawler.utility.Constants;
import com.pramati.genericwebcrawler.utility.EmailUtility;

public class EmailFilterRule implements FilterRuleService, Runnable {

	static Logger logger = Logger.getLogger(EmailFilterRule.class);

	private String year;
	private BlockingQueue<String> sharedQueue;
	private EmailUtility emailUitlityObj;

	public EmailFilterRule() {
		sharedQueue = null;
	}

	public void init(BlockingQueue<String> sharedQueue, String year) {

		this.year = year;
		this.sharedQueue = sharedQueue;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public boolean filterCriteria(String link) {

		String filterPattern = this.getYear() + Constants.EMAIL_FILTER_CRITERIA;
		Pattern p = getPattern(filterPattern);
		Matcher m;
		m = p.matcher(link);

		if (m.find())
			return true;
		else
			return false;

	}

	public void run() {

		while (!(Thread.currentThread().isInterrupted())) {

			try {
				Thread.sleep(10);
				System.out.println("In Consumer thread: " + sharedQueue.size());
				String url = sharedQueue.take();
				System.out.println("In Consumer thread: " + url);
				downloadContent(sharedQueue.take());

			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
				logger.error("Interruption ocurred while taking link from shared queue");
			}
		}
	}

	public boolean exitCriteria(String link) {

		if (link.contains(Constants.VIEW_RAW_MESSAGE))
			return true;

		return false;
	}

	public void downloadContent(String url) {

		System.out.println("In dowanload content of consumer thread" + url);

		emailUitlityObj = new EmailUtility();

		String pageContent = emailUitlityObj.convertHtmlToString(url);
		EmailMetaData emailMetaDataObj = getMetaData(pageContent);

		emailUitlityObj.saveEmail(pageContent, year, emailMetaDataObj);

	}

	private EmailMetaData getMetaData(String pageContent) {

		EmailMetaData emailObj = new EmailMetaData();
		System.out.println("In get metadata of consumer");
		emailObj.setDate(getDate(pageContent));
		emailObj.setSubject(getSubject(pageContent));
		emailObj.setSenderName(getSender(pageContent));

		return emailObj;
	}

	protected String getDate(String line) {
		Pattern p = getPattern(Constants.MAIL_DATE_PATTERN);
		Matcher m;
		m = p.matcher(line);

		if (m.find())
			return m.group(1);

		return "";
	}

	protected String getSubject(String line) {

		Pattern p = getPattern(Constants.MAIL_SUBJECT_PATTERN);
		Matcher m;
		m = p.matcher(line);
		if (m.find())
			return m.group(1);

		return "";

	}

	protected String getSender(String line) {

		Pattern p = getPattern(Constants.MAIL_SENDER_PATTERN);
		Matcher m;
		m = p.matcher(line);

		if (m.find())
			return m.group(1);

		return "";
	}

	protected Pattern getPattern(String pattern) {
		return Pattern.compile(pattern);
	}

}
