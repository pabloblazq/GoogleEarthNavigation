package com.blame.googleearthnavigation.bean;

public class NavigationPoint {

	// movement
	protected Coordinates coordinates;
	protected Integer altitude;
	protected Integer groundAltitude;
	protected double heading;
	
	// looking
	protected double lookDirectionH;
	protected Double lookDirectionT;
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

	public NavigationPoint(Coordinates coordinates, double heading, double lookDirectionH, boolean isVisiting) {
		super();
		this.coordinates = coordinates;
		this.heading = heading;
		this.lookDirectionH = lookDirectionH;
		this.isVisiting = isVisiting;
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

	public Integer getAltitude() {
		return altitude;
	}

	public Integer getGroundAltitude() {
		return groundAltitude;
	}

	public double getHeading() {
		return heading;
	}

	public double getLookDirectionH() {
		return lookDirectionH;
	}

	public Double getLookDirectionT() {
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
