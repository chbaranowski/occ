package org.eclipselabs.occ.marsrobot.world.api;

public interface SensorData {

	/**
	 * 
	 * @param subject the subject, e.g. a specific robot
	 * @return returns the measurements of the bump-sensor
	 */
	public double[] getBumperMeasurements(Subject subject);
	
	/**
	 * 
	 * @param subject
	 * @return returns the minimum of all bumpers-sensors measurements
	 */
	public double getMinimumBumpersMeasurement(Subject subject);
	
	/**
	 * 
	 * @param subject
	 * @return returns the measurements of the sonar-sensor
	 */
	public double[] getSonarMeasurements(Subject subject);
	
	/**
	 * 
	 * @param subject
	 * @return returns the sonar-hits, if one has hit, true is returned
	 */
	public boolean getSonarOneHasHit(Subject subject);
	
	/**
	 * 
	 * @param subject
	 * @return returns the maximum range of the sonar sensor
	 */
	public float getSonarMaxRange(Subject subject);
	
	/**
	 * @param the subject (e.g. a robot)
	 * @return the minimum of all sonar-sensor-measurements
	 */
    public double minimumSonarMeasurement(Subject subject);

    /**
     * @param the subject (e.g. a robot)
     * @return the maximum of all sonar-sensor-measurements
     */
	public double maximumSonarMeasurement(Subject subject);

	/**
	 * 
	 * @param subject
	 * @return returns the image width in pixels
	 */
	public int getCameraImageWidth(Subject subject);

	/**
	 * 
	 * @param subject
	 * @return returns the image height in pixels
	 */
	public int getCameraImageHeight(Subject subject);
	
}
