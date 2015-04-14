package com.pramati.genericwebcrawler.utility;

public class EmailFilterRule {
	
	private String year;
	
	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public boolean filterUrl(String link)
	{
		if(link.contains(".mbox"))
			return true;
		else 
			return false;
		
	}

	

}
