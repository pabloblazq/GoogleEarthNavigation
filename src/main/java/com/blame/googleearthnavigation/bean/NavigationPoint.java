package com.blame.googleearthnavigation.bean;

public class NavigationPoint {

	// movement
	protected Coordinates coordinates;
	protected int groundAltitude;
	protected double heading;
	
	// looking
	protected double lookDirectionH;
	protected double lookDirectionT;
	protected boolean isVisiting;

	public NavigationPoint(Coordinates coordinates, int groundAltitude, double heading, double lookDirectionH, double lookDirectionT, boolean isVisiting) {
		super();
		this.coordinates = coordinates;
		this.groundAltitude = groundAltitude;
		this.heading = heading;
		this.lookDirectionH = lookDirectionH;
		this.lookDirectionT = lookDirectionT;
		this.isVisiting = isVisiting;
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public int getGroundAltitude() {
		return groundAltitude;
	}

	public double getHeading() {
		return heading;
	}

	public double getLookDirectionH() {
		return lookDirectionH;
	}

	public double getLookDirectionT() {
		return lookDirectionT;
	}

	public boolean isVisiting() {
		return isVisiting;
	}

	@Override
	public String toString() {
		return "NavigationPoint [coordinates=" + coordinates + ", groundAltitude=" + groundAltitude + ", heading="
				+ heading + ", lookDirectionH=" + lookDirectionH + ", lookDirectionT=" + lookDirectionT
				+ ", isVisiting=" + isVisiting + "]";
	}

}
