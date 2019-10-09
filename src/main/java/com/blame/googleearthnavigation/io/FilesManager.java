package com.blame.googleearthnavigation.io;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.blame.googleearthnavigation.bean.Coordinates;
import com.blame.googleearthnavigation.bean.NavigationPoint;
import com.blame.googleearthnavigation.bean.NavigationRule;
import com.blame.googleearthnavigation.bean.SpotCoordinates;
import com.blame.googleearthnavigation.geonavigation.GeoNavigationUtils;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

public class FilesManager {

	private static final Logger logger = LogManager.getLogger(FilesManager.class);

	protected static final String FIELD_SEPARATOR = ",";

	/**
	 * 
	 * @param baseFolderName
	 * @return
	 * @throws IOException 
	 */
	public static NavigationRule loadNavigationRule(String baseFolderName) throws IOException {
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
	public static void writeNavigationPointsToCSVFile(List<NavigationPoint> navigationPointList, String baseFolderName) throws IOException {
		logger.info("Writing coordinates file ...");

		File fileCSVCoordinates = new File(baseFolderName + File.separator + "coordinates.csv");
		FileWriter fw = new FileWriter(fileCSVCoordinates);
		fw.write("index" + FIELD_SEPARATOR + "latitude" + FIELD_SEPARATOR + "longitude" + FIELD_SEPARATOR + "altitude" + FIELD_SEPARATOR + "ground_altitude" + FIELD_SEPARATOR + 
				"spot_latitude" + FIELD_SEPARATOR + "spot_longitude" + FIELD_SEPARATOR + "heading" + FIELD_SEPARATOR + "look_h" + FIELD_SEPARATOR + "look_t" + FIELD_SEPARATOR + 
				"is_visiting" + FIELD_SEPARATOR + "is_spot_on_focus" + System.lineSeparator());
		for(int i = 0; i < navigationPointList.size(); i++) {
			NavigationPoint navigationPoint = navigationPointList.get(i);
			StringBuffer sb = new StringBuffer(String.valueOf(i + 1)).append(FIELD_SEPARATOR);
			sb.append(GeoNavigationUtils.round(navigationPoint.getCoordinates().getLatitude(), 6)).append(FIELD_SEPARATOR);
			sb.append(GeoNavigationUtils.round(navigationPoint.getCoordinates().getLongitude(), 6)).append(FIELD_SEPARATOR);
			if(navigationPoint.getAltitude() != null) {
				sb.append(navigationPoint.getAltitude()).append(FIELD_SEPARATOR);
			} else {
				sb.append(FIELD_SEPARATOR);
			}
			if(navigationPoint.getGroundAltitude() != null) {
				sb.append(navigationPoint.getGroundAltitude()).append(FIELD_SEPARATOR);
			} else {
				sb.append(FIELD_SEPARATOR);
			}
			sb.append(navigationPoint.getSpotCoordinates().getLatitude()).append(FIELD_SEPARATOR);
			sb.append(navigationPoint.getSpotCoordinates().getLongitude()).append(FIELD_SEPARATOR);
			sb.append(navigationPoint.getHeading()).append(FIELD_SEPARATOR);
			sb.append(navigationPoint.getSpotLookDirectionH()).append(FIELD_SEPARATOR);
			if(navigationPoint.getSpotLookDirectionT() != null) {
				sb.append(navigationPoint.getSpotLookDirectionT()).append(FIELD_SEPARATOR);
			} else {
				sb.append(FIELD_SEPARATOR);
			}
			sb.append(navigationPoint.isSpotVisiting()).append(FIELD_SEPARATOR);
			sb.append(navigationPoint.isSpotOnFocus()).append(System.lineSeparator());
			
			fw.write(sb.toString());
        }
        fw.close();
		logger.info("Coordinates file written.");
	}

	/**
	 * 
	 * @param baseFolderName
	 * @return
	 * @throws IOException 
	 */
	public static List<NavigationPoint> readCSVFileToNavigationPoints(String baseFolderName) throws IOException {
		logger.info("Reading coordinates file ...");
		
		List<NavigationPoint> navigationPointList = new ArrayList<>();
		
		boolean isFirstLine = true;
		Scanner scanner = new Scanner(new File(baseFolderName + File.separator + "coordinates.csv"));
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if(isFirstLine) {
				isFirstLine = false;
				continue;
			}
			String[] fields = line.split(FIELD_SEPARATOR);
			NavigationPoint navigationPoint = new NavigationPoint();
			navigationPoint.setCoordinates(new Coordinates(Double.parseDouble(fields[1]), Double.parseDouble(fields[2])));
			if(!fields[3].equals("")) {
				navigationPoint.setAltitude(Integer.parseInt(fields[3]));
			}
			if(!fields[4].equals("")) {
				navigationPoint.setGroundAltitude(Integer.parseInt(fields[4]));
			}
			//TODO: read spot ground altitude
			navigationPoint.setSpotCoordinates(new SpotCoordinates(Double.parseDouble(fields[5]), Double.parseDouble(fields[6]), 20));
			navigationPoint.setHeading(Double.parseDouble(fields[7]));
			navigationPoint.setSpotLookDirectionH(Double.parseDouble(fields[8]));
			if(!fields[9].equals("")) {
				navigationPoint.setSpotLookDirectionT(Double.parseDouble(fields[9]));
			}
			navigationPoint.setSpotVisiting(Boolean.parseBoolean(fields[10]));
			navigationPoint.setSpotOnFocus(Boolean.parseBoolean(fields[11]));
			
			navigationPointList.add(navigationPoint);
		}
		scanner.close();
		
		logger.info("Coordinates file read.");
		return navigationPointList;
	}
}
