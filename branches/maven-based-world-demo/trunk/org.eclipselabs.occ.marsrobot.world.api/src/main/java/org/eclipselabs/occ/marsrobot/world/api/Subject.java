package org.eclipselabs.occ.marsrobot.world.api;

public interface Subject {

	/**
	 * 
	 * @param x
	 * @param z
	 * moves the subject to the given position
	 */
	void moveToPosition(double x, double z);
	
	/**
	 * 
	 * @param angle
	 * rotates the subject about the y axis
	 */
	void rotateSubjectY(double angle);
	
	/**
	 * 
	 * @return the world in which the subject acts
	 */
	World getWorld();
	
	/**
	 * 
	 * @return true if collision is detected, else false
	 */
	public boolean collisionDetected();
	
	/**
	 * 
	 * @return the x- and z- position of the subject; first entry in array: x-position
	 */
	public double[] getActualXZPosition();
	
	/**
	 * 
	 * @return returns true if one agent is very near (physical contact)
	 */
	public boolean anotherAgentIsVeryNear();
	
}
