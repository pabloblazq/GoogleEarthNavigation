package com.blame.googleearthnavigation.scrapers;

import org.junit.Test;

import com.blame.googleearthnavigation.bean.Coordinates;

public class MapCoordinatesDotNetScraperTest {

	@Test
	public void testGetElevation() {
	
		MapCoordinatesDotNetScraper scraper = new MapCoordinatesDotNetScraper();
		int elevation = scraper.getElevation(new Coordinates(40.477835256029785, -3.689574301242829));
		
		System.out.println(elevation);
	}
}
