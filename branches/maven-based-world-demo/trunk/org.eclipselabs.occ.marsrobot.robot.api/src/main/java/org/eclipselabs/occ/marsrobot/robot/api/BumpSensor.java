package org.eclipselabs.occ.marsrobot.robot.api;

import org.eclipselabs.occ.marsrobot.world.api.Subject;

public interface BumpSensor extends Sensor<BumpData>{

	/**
	 * 
	 * @param robotSubject
	 * @return the minimum distance to an obstacle
	 */
	double minimumBumpersMeasurement(Subject robotSubject);
	
	/**
	 * 
	 * @param subject
	 * @return the measurement of all bump-sensors 
	 */
	double[] getBumpersMeasurements(Subject subject);
	
}