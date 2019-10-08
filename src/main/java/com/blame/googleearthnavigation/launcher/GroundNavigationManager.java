package com.blame.googleearthnavigation.launcher;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.blame.googleearthnavigation.bean.NavigationPoint;
import com.blame.googleearthnavigation.bean.NavigationRule;
import com.blame.googleearthnavigation.geonavigation.Navigator;
import com.blame.googleearthnavigation.io.FilesManager;

public class GroundNavigationManager {

	private static final Logger logger = LogManager.getLogger(GroundNavigationManager.class);

	/**
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {

		String baseFolderName = args[0];
		
		// load the navigation rule from JSON file
		NavigationRule navigationRule = FilesManager.loadNavigationRule(baseFolderName);
        
        // navigate: get the list of navigation points (no altitude yet)
        Navigator navigator = new Navigator(navigationRule);
        List<NavigationPoint> navigationPointList = navigator.navigate();
        
        // write coordinates into the CSV file
        FilesManager.writeNavigationPointsToCSVFile(navigationPointList, baseFolderName);

		logger.info("Application end");
	}

}
