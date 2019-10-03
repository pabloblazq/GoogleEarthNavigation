package com.blame.googleearthnavigation.googleearth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import com.blame.googleearthnavigation.googleearth.GoogleEarthWebDriverManager;

public class WebDriverManagerTest {

	private static final Logger logger = LogManager.getLogger(WebDriverManagerTest.class);
	
	@Test
	public void testWebDriverManager() {
		logger.info("Start of test");

		GoogleEarthWebDriverManager webDriverManager = new GoogleEarthWebDriverManager("test");
		webDriverManager.prepareScreenshot("https://www.google.es/maps/@40.469448,-3.687357,159a,35y,358.96h,90t/data=!3m1!1e3?hl=es");
		webDriverManager.takeScreenshotToJPG();
		webDriverManager.close();
		
		logger.info("End of test");
	}

}
