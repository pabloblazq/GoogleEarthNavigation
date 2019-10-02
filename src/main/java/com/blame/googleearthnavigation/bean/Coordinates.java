package com.blame.googleearthnavigation.bean;

public class Coordinates {

	protected double latitude;
	protected double longitude;
	
	public Coordinates(double latitude, double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}
	public double getLongitude() {
		return longitude;
	}

	@Override
	public String toString() {
		return "Coordinates [latitude=" + latitude + ", longitude=" + longitude + "]";
	}
	
}
