package com.blame.googleearthnavigation.bean;

public class SpotCoordinates extends Coordinates {

	protected int groundAltitude;
	
	public SpotCoordinates(double latitude, double longitude, int groundAltitude) {
		super(latitude, longitude);
		this.groundAltitude = groundAltitude;
	}

	public int getGroundAltitude() {
		return groundAltitude;
	}

	@Override
	public String toString() {
		return "SpotCoordinates [groundAltitude=" + groundAltitude + ", latitude=" + latitude + ", longitude="
				+ longitude + "]";
	}
	
}
