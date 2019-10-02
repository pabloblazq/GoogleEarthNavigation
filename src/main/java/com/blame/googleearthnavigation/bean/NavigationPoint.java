package com.blame.googleearthnavigation.bean;

public class NavigationPoint {

	// movement
	protected Coordinates coordinates;
	protected double heading;
	
	// looking
	protected double lookDirectionH;
	protected double lookDirectionT;
	protected boolean isVisiting;

	public NavigationPoint(Coordinates coordinates, double heading, double lookDirectionH, double lookDirectionT, boolean isVisiting) {
		super();
		this.coordinates = coordinates;
		this.heading = heading;
		this.lookDirectionH = lookDirectionH;
		this.lookDirectionT = lookDirectionT;
		this.isVisiting = isVisiting;
	}

	public Coordinates getCoordinates() {
		return coordinates;
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
		return "NavigationPoint [coordinates=" + coordinates + ", heading=" + heading + ", lookDirectionH="
				+ lookDirectionH + ", lookDirectionT=" + lookDirectionT + ", isVisiting=" + isVisiting + "]";
	}


}
