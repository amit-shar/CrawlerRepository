package com.pramati.genericwebcrawler.services.implementor;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmailFilterRuleTest {

	private EmailFilterRule emailFilterRuleObj;

	@Before
	public void setUp() throws Exception {
		emailFilterRuleObj= new EmailFilterRule("2015"); 

	}



	@Test
	public void filterCriteriatest() {

		assertTrue(emailFilterRuleObj.filterCriteria("/mod_mbox/maven-users/201503.mbox/"));
		assertFalse(emailFilterRuleObj.filterCriteria("/mod_mbox/maven-users/20103.mbox/raw"));	

	}


	@Test
	public void exitCriteriaTest()
	{

		assertTrue(emailFilterRuleObj.exitCriteria("/mod_mbox/maven-users/20103.mbox/raw/"));
		assertFalse(emailFilterRuleObj.exitCriteria("/mod_mbox/maven-users/201503.mbox/"));
	}


	@Test
	public void getDateTest()
	{
		String expectedOutput=emailFilterRuleObj.getDate("fhgdshfgdhsj Date: 02, Mar, 2015");
		String actulaOutput="02, Mar, 2015";
		assertTrue(actulaOutput.equals(expectedOutput));

	}


	@Test
	public void getSubjectTest()
	{
		String expectedOutput=emailFilterRuleObj.getSubject("fhgdshf@%^gdhsj Subject: This is a test case");
		String actulaOutput="This is a test case";
		assertTrue(actulaOutput.equals(expectedOutput));

	}

	@Test
	public void getSenderTest()
	{
		String expectedOutput=emailFilterRuleObj.getSender("fhgdshfgdhsj From: john@abc.com");
		String actulaOutput="john@abc.com";
		assertTrue(actulaOutput.equals(expectedOutput));

	}


	@After
	public void tearDown() throws Exception {

		emailFilterRuleObj= null;
	}

}
