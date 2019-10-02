package com.blame.googleearthnavigation.bean;

import java.util.List;

public class NavigationRule {

	protected double stepDistance;
	protected double spotVisitThreshold;
	protected double maxHeadingChange;
	protected double maxLookingDirectionChange;
	protected double deltaMaxLookingDirectionChange;
	protected List<Coordinates> checkpoints;
	protected List<Coordinates> spots;
	
	
	public double getStepDistance() {
		return stepDistance;
	}
	
	public double getSpotVisitThreshold() {
		return spotVisitThreshold;
	}

	public double getMaxHeadingChange() {
		return maxHeadingChange;
	}

	public double getMaxLookingDirectionChange() {
		return maxLookingDirectionChange;
	}

	public double getDeltaMaxLookingDirectionChange() {
		return deltaMaxLookingDirectionChange;
	}

	public List<Coordinates> getCheckpoints() {
		return checkpoints;
	}
	
	public List<Coordinates> getSpots() {
		return spots;
	}

	@Override
	public String toString() {
		return "NavigationRule [stepDistance=" + stepDistance + ", spotVisitThreshold=" + spotVisitThreshold
				+ ", maxHeadingChange=" + maxHeadingChange + ", maxLookingDirectionChange=" + maxLookingDirectionChange
				+ ", deltaMaxLookingDirectionChange=" + deltaMaxLookingDirectionChange + ", checkpoints=" + checkpoints
				+ ", spots=" + spots + "]";
	}


}
