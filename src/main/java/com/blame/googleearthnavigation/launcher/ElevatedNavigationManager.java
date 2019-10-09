package com.blame.googleearthnavigation.launcher;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.blame.googleearthnavigation.bean.NavigationPoint;
import com.blame.googleearthnavigation.bean.NavigationRule;
import com.blame.googleearthnavigation.geonavigation.Navigator;
import com.blame.googleearthnavigation.googleearth.GoogleEarthWebDriverManager;
import com.blame.googleearthnavigation.io.FilesManager;

public class ElevatedNavigationManager {

	private static final Logger logger = LogManager.getLogger(ElevatedNavigationManager.class);

	/**
	 * 
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException, InterruptedException {

		String baseFolderName = args[0];
		
		// load the navigation rule from JSON file
		NavigationRule navigationRule = FilesManager.loadNavigationRule(baseFolderName);
        
		// load the navigation points
		List<NavigationPoint> navigationPointList = FilesManager.readCSVFileToNavigationPoints(baseFolderName);
		
        // navigate: get the list of navigation points (no altitude yet)
        Navigator navigator = new Navigator(navigationRule, navigationPointList);
        navigator.extendNavigationPointsWithGroundAltitude();
        
        // take the Google Earth screenshots
		GoogleEarthWebDriverManager webDriverManager = new GoogleEarthWebDriverManager(baseFolderName);
		//Thread.sleep(100*60*1000);
		//Thread.sleep(10*1000);
        webDriverManager.takeScreenshots(navigationPointList);
		webDriverManager.close();

		logger.info("Application end");
	}

}
