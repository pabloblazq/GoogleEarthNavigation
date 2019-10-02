package com.blame.googleearthnavigation.geonavigation;

import com.blame.googleearthnavigation.bean.Coordinates;

public class GeoNavigationUtils {

	private static final double EARTH_RADIUS = 6371000; // metres
	
	public static Coordinates getNextCoordinates(Coordinates coordinatesFrom, double direction, double distance) {

		double lat1 = Math.toRadians(coordinatesFrom.getLatitude());
		double lon1 = Math.toRadians(coordinatesFrom.getLongitude());
		double directionRad = Math.toRadians(direction);

		double lat2 = Math.asin( Math.sin(lat1)*Math.cos(distance/EARTH_RADIUS) +
                Math.cos(lat1)*Math.sin(distance/EARTH_RADIUS)*Math.cos(directionRad) );
		double lon2 = lon1 + Math.atan2(Math.sin(directionRad)*Math.sin(distance/EARTH_RADIUS)*Math.cos(lat1),
                     Math.cos(distance/EARTH_RADIUS)-Math.sin(lat1)*Math.sin(lat2));
		
		return new Coordinates(Math.toDegrees(lat2), Math.toDegrees(lon2));
	}

	public static double getDirection(Coordinates coordinates1, Coordinates coordinates2) {

		double lat1 = Math.toRadians(coordinates1.getLatitude());
		double lat2 = Math.toRadians(coordinates2.getLatitude());
		double lon1 = Math.toRadians(coordinates1.getLongitude());
		double lon2 = Math.toRadians(coordinates2.getLongitude());
		double deltaLon = Math.toRadians(lon2 - lon1);
		
		double y = Math.sin(lon2-lon1) * Math.cos(lat2);
		double x = Math.cos(lat1)*Math.sin(lat2) -
		        Math.sin(lat1)*Math.cos(lat2)*Math.cos(deltaLon);
		double direction = Math.toDegrees(Math.atan2(y, x));
		if(direction < 0) {
			direction += 360;
		}
		return direction;
	}

	public static double getDistance(Coordinates coordinate1, Coordinates coordinate2) {
		
		double lat1 = Math.toRadians(coordinate1.getLatitude());
		double lat2 = Math.toRadians(coordinate2.getLatitude());
		double lon1 = Math.toRadians(coordinate1.getLongitude());
		double lon2 = Math.toRadians(coordinate2.getLongitude());
		double deltaLat = Math.toRadians(lat2 - lat1);
		double deltaLon = Math.toRadians(lon2 - lon1);

		double a = Math.sin(deltaLat/2) * Math.sin(deltaLat/2) +
		        Math.cos(lat1) * Math.cos(lat2) *
		        Math.sin(deltaLon/2) * Math.sin(deltaLon/2);
		double c = 2 * Math.toDegrees(Math.atan2(Math.sqrt(a), Math.sqrt(1-a)));

		return EARTH_RADIUS * c;
	}
	
	public static double getTiltAngle(Coordinates coordinate1, Coordinates coordinate2, int altitude) {
		
		double distance = getDistance(coordinate1, coordinate2);
		
		return Math.toDegrees(Math.atan2(altitude, distance));
	}

	public static double subtractDirections(double directionFrom, double directionTo) {
		
		double directionDiff = directionTo - directionFrom;
		if(directionDiff > 180) {
			directionDiff = directionDiff - 360;
		}
		else if(directionDiff < -180) {
			directionDiff = directionDiff + 360;
		}
		return directionDiff;
	}

	public static double addDirections(double directionFrom, double directionTo) {
		
		double sumDirection = directionFrom + directionTo;
		if(sumDirection > 360) {
			sumDirection -= 360;
		}
		else if(sumDirection < 0) {
			sumDirection += 360;
		}
		return sumDirection;
	}

}
