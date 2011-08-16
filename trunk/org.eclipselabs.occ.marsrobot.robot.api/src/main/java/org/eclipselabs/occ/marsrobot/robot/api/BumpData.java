package org.eclipselabs.occ.marsrobot.robot.api;

import org.eclipselabs.occ.marsrobot.world.api.Dimension;

public class BumpData {

	private Dimension worldDimension;
	private double[] measurements;
	private boolean[] hits;
	
	public void setDimension(Dimension worldDimension) {
		this.worldDimension = worldDimension;
	}

	public Dimension getWorldDimension() {
		return worldDimension;
	}

	public void setMeasurements(double[] measurements) {
		this.measurements = measurements;
	}

	public double[] getMeasurements() {
		return measurements;
	}

	public void setHits(boolean[] hits) {
		this.hits = hits;
	}

	public boolean[] getHits() {
		return hits;
	}

}
