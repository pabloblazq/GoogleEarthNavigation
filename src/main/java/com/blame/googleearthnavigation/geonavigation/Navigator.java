package com.blame.googleearthnavigation.geonavigation;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.blame.googleearthnavigation.bean.Coordinates;
import com.blame.googleearthnavigation.bean.NavigationPoint;
import com.blame.googleearthnavigation.bean.NavigationRule;
import com.blame.googleearthnavigation.bean.SpotCoordinates;
import com.blame.googleearthnavigation.scrapers.MapCoordinatesDotNetScraper;

public class Navigator {

	private static final Logger logger = LogManager.getLogger(Navigator.class);

	protected NavigationRule navigationRule;
	protected double checkpointChangeThreshold;
	
	protected List<NavigationPoint> navigationPointList;

	protected double currentMaxLookingHDirectionChange;
	protected double currentMaxLookingTDirectionChange;

	protected boolean isSpotOnFocus = true;

	private NavigationPoint navigationPoint;

	private boolean isSpotVisiting;

	//protected MapCoordinatesDotNetScraper elevationScraper;

	/**
	 * Constructor
	 * @param navigationRule
	 */
	public Navigator(NavigationRule navigationRule) {
		this.navigationRule = navigationRule;
		this.checkpointChangeThreshold = navigationRule.getStepDistance() / 2;
		
		this.navigationPointList = new ArrayList<>();
		
		//this.elevationScraper = new MapCoordinatesDotNetScraper();
	}

	/**
	 * Constructor
	 * @param navigationPointList
	 */
	public Navigator(NavigationRule navigationRule, List<NavigationPoint> navigationPointList) {
		this.navigationRule = navigationRule;
		this.checkpointChangeThreshold = navigationRule.getStepDistance() / 2;

		this.navigationPointList = navigationPointList;
	}

	/**
	 * 
	 * @return
	 */
	public List<NavigationPoint> navigate() {
        logger.info("Start of navigation");
		
		List<Coordinates> checkpoints = navigationRule.getCheckpoints();
		
        logger.info("Initial position {}", checkpoints.get(0));
        logger.info("Initial target spot {}", navigationRule.getSpots().get(0));
		
		double heading = GeoNavigationUtils.getDirection(checkpoints.get(0), checkpoints.get(1));
		double lookDirectionH = GeoNavigationUtils.getDirection(checkpoints.get(0), navigationRule.getSpots().get(0));
//		int coordinatesAltitude = elevationScraper.getElevation(checkpoints.get(0));
//		double lookDirectionT = 90. - GeoNavigationUtils.getTiltAngle(
//											checkpoints.get(0), 
//											navigationRule.getSpots().get(0), 
//											navigationRule.getFixedSeaLevelAltitude() - coordinatesAltitude - navigationRule.getSpots().get(0).getGroundAltitude());
//		NavigationPoint navigationPoint = new NavigationPoint(
//				checkpoints.get(0), 
//				navigationRule.getFixedSeaLevelAltitude() - coordinatesAltitude, 
//				heading, 
//				lookDirectionH, 
//				lookDirectionT, 
//				false);
		navigationPoint = new NavigationPoint();
		navigationPoint.setCoordinates(checkpoints.get(0));
		navigationPoint.setHeading(heading);
		navigationPoint.setSpotCoordinates(navigationRule.getSpots().get(0));
		navigationPoint.setSpotLookDirectionH(lookDirectionH);
		navigationPoint.setSpotVisiting(true);
		navigationPoint.setSpotOnFocus(true);
		navigationPointList.add(navigationPoint);
		
		for(int i = 0; i < checkpoints.size() -1; i++) {
			Coordinates checkpointTo = checkpoints.get(i+1);
	        logger.info("Next target checkpoint {} {}", i + 1, checkpointTo);
			navigate(checkpointTo);
	        logger.info("Reached checkpoint {} {}", i + 1, checkpointTo);
		}
		
        logger.info("End of navigation. {} coordinates taken.", navigationPointList.size());
        
        return navigationPointList;
	}

	/**
	 * 
	 * @param navigationPoint
	 * @param checkpointTo
	 * @param spots
	 * @return
	 */
	protected void navigate(Coordinates checkpointTo) {

		double lookDirectionH = navigationPoint.getSpotLookDirectionH();
//		double lookDirectionT = navigationPoint.getLookDirectionT();

		boolean keepLoopCondition = true;
		while(keepLoopCondition) {

			// next heading
			double heading = calculateHeading(checkpointTo);
			
			// next coordinates
			Coordinates coordinates = GeoNavigationUtils.getNextCoordinates(navigationPoint.getCoordinates(), heading, navigationRule.getStepDistance());
			
			// update isVisiting
			calculateIsVisiting(coordinates);
			
			// calculate the heading look direction
			calculateSpotLookDirectionH(coordinates);
			
			// set the navigation point
			navigationPoint = new NavigationPoint();
			navigationPoint.setCoordinates(coordinates);
			navigationPoint.setHeading(heading);
			navigationPoint.setSpotCoordinates(navigationRule.getSpots().get(0));
			navigationPoint.setSpotLookDirectionH(lookDirectionH);
			navigationPoint.setSpotVisiting(isSpotVisiting);
			navigationPoint.setSpotOnFocus(isSpotOnFocus);
			navigationPointList.add(navigationPoint);

			// stop condition
			double distanceToCheckpoint = GeoNavigationUtils.getDistance(coordinates, checkpointTo);
			if(distanceToCheckpoint < checkpointChangeThreshold) {
				break;
			}
		}
	}

	/**
	 * 
	 * @param coordinates
	 */
	private void calculateSpotLookDirectionH(Coordinates coordinates) {
		SpotCoordinates targetSpot = navigationRule.getSpots().get(0);
		double previousLookDirectionH = navigationPoint.getSpotLookDirectionH();
		double lookDirectionH = GeoNavigationUtils.getDirection(coordinates, targetSpot);
		if(!isSpotOnFocus) {
			double lookDirectionHChange = GeoNavigationUtils.subtractDirections(previousLookDirectionH, lookDirectionH);
			updateLimitOfLookingDirectionHChange(lookDirectionHChange);
			if(Math.abs(lookDirectionHChange) > currentMaxLookingHDirectionChange) {
				if(lookDirectionHChange >= 0) {
					lookDirectionH = GeoNavigationUtils.addDirections(previousLookDirectionH, currentMaxLookingHDirectionChange);
				}
				else {
					lookDirectionH = GeoNavigationUtils.addDirections(previousLookDirectionH, -currentMaxLookingHDirectionChange);
				}
			}
			else {
		        logger.info("Established focus onto spot {}", targetSpot);
				isSpotOnFocus = true;
				this.currentMaxLookingHDirectionChange = 0;
				this.currentMaxLookingTDirectionChange = 0;
			}
		}
	}

	/**
	 * 
	 * @param checkpointTo
	 * @return
	 */
	private double calculateHeading(Coordinates checkpointTo) {
		double previousHeading = navigationPoint.getHeading();
		double heading = GeoNavigationUtils.getDirection(navigationPoint.getCoordinates(), checkpointTo);
		double headingChange = GeoNavigationUtils.subtractDirections(previousHeading, heading);
		if(Math.abs(headingChange) > navigationRule.getMaxHeadingChange()) {
			if(headingChange >= 0) {
				heading = GeoNavigationUtils.addDirections(previousHeading, navigationRule.getMaxHeadingChange());
			}
			else {
				heading = GeoNavigationUtils.addDirections(previousHeading, -navigationRule.getMaxHeadingChange());
			}
		}
		
		return heading;
	}

	/**
	 * 
	 * @param coordinates
	 */
	protected void calculateIsVisiting(Coordinates coordinates) {
		
		SpotCoordinates targetSpot = navigationRule.getSpots().get(0);
		double previousDistanceToSpot = GeoNavigationUtils.getDistance(navigationPoint.getCoordinates(), targetSpot);

		double distanceToSpot = GeoNavigationUtils.getDistance(coordinates, targetSpot);
		if(!isSpotVisiting && distanceToSpot < navigationRule.getSpotVisitThreshold()) {
	        logger.info("Started visit to spot {}", targetSpot);
			isSpotVisiting = true;
		}
		else if(isSpotVisiting && distanceToSpot > navigationRule.getSpotVisitThreshold() && distanceToSpot > previousDistanceToSpot) {
	        logger.info("Finished visit to spot {}", targetSpot);
	        isSpotVisiting = false;
			List<SpotCoordinates> spots = navigationRule.getSpots();
			spots.remove(targetSpot);
			if(!spots.isEmpty()) {
				isSpotOnFocus = false;
		        logger.info("Changing target spot to {}", spots.get(0));
			}
		}
	}

	
	public void extendNavigationPointsWithGroundAltitude() {
		
		for(int index = 0; index < navigationPointList.size(); index++) {

			NavigationPoint navigationPoint = navigationPointList.get(index);
			
			int groundAltitude = navigationRule.getFixedSeaLevelAltitude() - navigationPoint.getAltitude();
			
			SpotCoordinates targetSpot = navigationPoint.getSpotCoordinates();
			double lookDirectionT = 90. - GeoNavigationUtils.getTiltAngle(navigationPoint.getCoordinates(), targetSpot, groundAltitude - targetSpot.getGroundAltitude());
			if(index > 0 && !isSpotOnFocus) {
				Double previousLookDirectionT = navigationPointList.get(index - 1).getSpotLookDirectionT();
				double lookDirectionTChange = GeoNavigationUtils.subtractDirections(previousLookDirectionT, lookDirectionT);
				updateLimitOfLookingDirectionTChange(lookDirectionTChange);
				if(Math.abs(lookDirectionTChange) > currentMaxLookingTDirectionChange) {
					if(lookDirectionTChange >= 0) {
						lookDirectionT = GeoNavigationUtils.addDirections(previousLookDirectionT, currentMaxLookingTDirectionChange);
					}
					else {
						lookDirectionT = GeoNavigationUtils.addDirections(previousLookDirectionT, -currentMaxLookingTDirectionChange);
					}
				}
			}
			
			navigationPoint.setGroundAltitude(groundAltitude);
			navigationPoint.setSpotLookDirectionT(lookDirectionT);
		}
	}
	
	/**
	 * 
	 * @param lookDirectionHChange
	 * @return
	 */
	protected void updateLimitOfLookingDirectionHChange(double lookDirectionHChange) {
		
		// decelerate looking change
		if(needToDecelerateLookingDirectionChange(lookDirectionHChange, currentMaxLookingHDirectionChange)) {
			if(currentMaxLookingHDirectionChange > navigationRule.getDeltaMaxLookingDirectionChange()) {
				currentMaxLookingHDirectionChange -= navigationRule.getDeltaMaxLookingDirectionChange();
			}
		}
		// accelerate looking change
		else {
			if(currentMaxLookingHDirectionChange < navigationRule.getMaxLookingDirectionChange()) {
				currentMaxLookingHDirectionChange += navigationRule.getDeltaMaxLookingDirectionChange();
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	protected void updateLimitOfLookingDirectionTChange(double lookDirectionTChange) {
		
		// decelerate looking change
		if(needToDecelerateLookingDirectionChange(lookDirectionTChange, currentMaxLookingTDirectionChange)) {
			if(currentMaxLookingTDirectionChange > navigationRule.getDeltaMaxLookingDirectionChange()) {
				currentMaxLookingTDirectionChange -= navigationRule.getDeltaMaxLookingDirectionChange();
			}
		}
		// accelerate looking change
		else {
			if(currentMaxLookingTDirectionChange < navigationRule.getMaxLookingDirectionChange()) {
				currentMaxLookingTDirectionChange += navigationRule.getDeltaMaxLookingDirectionChange();
			}
		}
	}
	
	/**
	 * 
	 * @param lookDirectionChange
	 * @return
	 */
	protected boolean needToDecelerateLookingDirectionChange(double lookDirectionChange, double maxLookingDirectionChange) {
		
		double directionChangeCounter = 0.;
		double currentMaxLookingDirectionChange = navigationRule.getDeltaMaxLookingDirectionChange();
		while(directionChangeCounter < Math.abs(lookDirectionChange)) {
			
			directionChangeCounter += currentMaxLookingDirectionChange;
			currentMaxLookingDirectionChange += navigationRule.getDeltaMaxLookingDirectionChange();
			if(currentMaxLookingDirectionChange > maxLookingDirectionChange) {
				return false;
			}
		}

		return true;
	}
}
