package com.blame.googleearthnavigation.bean;

public class NavigationPoint {

	// movement
	protected Coordinates coordinates;
	protected Integer altitude;
	protected Integer groundAltitude;
	protected double heading;
	
	// looking
	protected SpotCoordinates spotCoordinates;
	protected double spotLookDirectionH;
	protected Double spotLookDirectionT;
	protected boolean isSpotVisiting;
	protected boolean isSpotOnFocus;
	
	public Coordinates getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(Coordinates coordinates) {
		this.coordinates = coordinates;
	}
	public Integer getAltitude() {
		return altitude;
	}
	public void setAltitude(Integer altitude) {
		this.altitude = altitude;
	}
	public Integer getGroundAltitude() {
		return groundAltitude;
	}
	public void setGroundAltitude(Integer groundAltitude) {
		this.groundAltitude = groundAltitude;
	}
	public double getHeading() {
		return heading;
	}
	public void setHeading(double heading) {
		this.heading = heading;
	}
	public SpotCoordinates getSpotCoordinates() {
		return spotCoordinates;
	}
	public void setSpotCoordinates(SpotCoordinates spotCoordinates) {
		this.spotCoordinates = spotCoordinates;
	}
	public double getSpotLookDirectionH() {
		return spotLookDirectionH;
	}
	public void setSpotLookDirectionH(double spotLookDirectionH) {
		this.spotLookDirectionH = spotLookDirectionH;
	}
	public Double getSpotLookDirectionT() {
		return spotLookDirectionT;
	}
	public void setSpotLookDirectionT(Double spotLookDirectionT) {
		this.spotLookDirectionT = spotLookDirectionT;
	}
	public boolean isSpotVisiting() {
		return isSpotVisiting;
	}
	public void setSpotVisiting(boolean isSpotVisiting) {
		this.isSpotVisiting = isSpotVisiting;
	}
	public boolean isSpotOnFocus() {
		return isSpotOnFocus;
	}
	public void setSpotOnFocus(boolean isSpotOnFocus) {
		this.isSpotOnFocus = isSpotOnFocus;
	}
	@Override
	public String toString() {
		return "NavigationPoint [coordinates=" + coordinates + ", altitude=" + altitude + ", groundAltitude="
				+ groundAltitude + ", heading=" + heading + ", spotCoordinates=" + spotCoordinates
				+ ", spotLookDirectionH=" + spotLookDirectionH + ", spotLookDirectionT=" + spotLookDirectionT
				+ ", isSpotVisiting=" + isSpotVisiting + ", isSpotOnFocus=" + isSpotOnFocus + "]";
	}
}
