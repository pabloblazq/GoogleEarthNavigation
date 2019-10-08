package com.blame.googleearthnavigation;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.blame.googleearthnavigation.bean.NavigationPoint;
import com.blame.googleearthnavigation.bean.NavigationRule;
import com.blame.googleearthnavigation.geonavigation.Navigator;
import com.blame.googleearthnavigation.googleearth.GoogleEarthWebDriverManager;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class Launcher {

	private static final Logger logger = LogManager.getLogger(Launcher.class);

	/**
	 * 
	 * @param args
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws IOException, InterruptedException {

		String baseFolderName = args[0];
		GoogleEarthWebDriverManager webDriverManager = new GoogleEarthWebDriverManager(baseFolderName);
		
		// load the navigation rule from JSON file
		NavigationRule navigationRule = loadNavigationRule(baseFolderName);
        
        // navigate: get the list of navigation points
        Navigator navigator = new Navigator(navigationRule);
        List<NavigationPoint> navigationPointList = navigator.navigate();
        
        // write coordinates into the CSV file
        writeCSVFile(navigationPointList, baseFolderName);

        // take the Google Earth screenshots
        takeScreenshots(webDriverManager, navigationPointList, baseFolderName);
		webDriverManager.close();
		
		logger.info("Application end");
	}

	/**
	 * 
	 * @param baseFolderName
	 * @return
	 * @throws IOException 
	 */
	protected static NavigationRule loadNavigationRule(String baseFolderName) throws IOException {
        Gson gson = new Gson();
        File fileNavigationRule = new File(baseFolderName + File.separator + "navigation_rule.json");
        JsonReader reader = new JsonReader(new FileReader(fileNavigationRule));
        NavigationRule navigationRule = gson.fromJson(reader, NavigationRule.class);
        
        logger.info(navigationRule);
        
        return navigationRule;
	}

	/**
	 * 
	 * @param navigationPointList
	 * @param baseFolderName 
	 * @throws IOException 
	 */
	protected static void writeCSVFile(List<NavigationPoint> navigationPointList, String baseFolderName) throws IOException {
		logger.info("Writing coordinates file ...");

		File fileCSVCoordinates = new File(baseFolderName + File.separator + "coordinates.csv");
		FileWriter fw = new FileWriter(fileCSVCoordinates);
		fw.write("index,latitude,longitude,groundAltitude,heading,look_h,look_t,is_visiting" + System.lineSeparator());
		for(int i = 0; i < navigationPointList.size(); i++) {
			NavigationPoint navigationPoint = navigationPointList.get(i);
			fw.write((i + 1) + "," + 
        			navigationPoint.getCoordinates().getLatitude() + "," + navigationPoint.getCoordinates().getLongitude() + "," +
        			navigationPoint.getGroundAltitude() + "," + navigationPoint.getHeading() + "," + 
        			navigationPoint.getLookDirectionH() + "," + navigationPoint.getLookDirectionT() + "," +
        			navigationPoint.isVisiting() + System.lineSeparator());
        }
        fw.close();
	}

	/**
	 * 
	 * @param webDriverManager 
	 * @param navigationPointList
	 * @param baseFolderName 
	 * @throws InterruptedException 
	 */
	protected static void takeScreenshots(GoogleEarthWebDriverManager webDriverManager, List<NavigationPoint> navigationPointList, String baseFolderName) throws InterruptedException {
		logger.info("Taking screenshots using a WebDriver ...");

		//Thread.sleep(100*60*1000);
		//Thread.sleep(10*1000);
		
//		List<Integer> errorList = Arrays.asList(145, 325, 453, 791);
//        for(int errorIndex = 0; errorIndex < errorList.size(); errorIndex++) {
//        	int i = errorList.get(errorIndex);

        for(int i = 0; i < navigationPointList.size(); i++) {
        	try {
        		logger.info("------------------------------------------------------------------------------------------------------------------------");
				NavigationPoint navigationPoint = navigationPointList.get(i);
				String url = webDriverManager.buildURL(navigationPoint);
				logger.info("Taking  screenshot for URL {}: {}", i + 1, url);
				webDriverManager.prepareScreenshot(url);
				webDriverManager.takeScreenshotToJPG();
				logger.info("Taken   screenshot for URL {}: {}", i + 1, webDriverManager.getCurrentURL());
			} catch (Exception e) {
				logger.catching(e);
			}
        }		
		logger.info("------------------------------------------------------------------------------------------------------------------------");
	}

}
