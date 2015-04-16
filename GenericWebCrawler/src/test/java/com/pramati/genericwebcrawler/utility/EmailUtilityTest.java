package com.pramati.genericwebcrawler.utility;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmailUtilityTest {

	private EmailUtility emailUtilityObj;
	@Before
	public void setUp() throws Exception {
		
		emailUtilityObj= new EmailUtility();
	}

	@After
	public void tearDown() throws Exception {
		
		emailUtilityObj=null;
	}

	@Test
	public void convertHtmlToStringtest() {
		
		assertEquals("",emailUtilityObj.convertHtmlToString("mod_maven/201503.mbox"));
		
		
	}
	
	@Test
	public void createDirectoryTest() {

		File obj =emailUtilityObj.createDirectory(null);
		assertEquals(obj, null);
	}

}
