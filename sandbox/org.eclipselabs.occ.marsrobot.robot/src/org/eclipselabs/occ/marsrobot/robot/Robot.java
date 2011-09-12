package org.eclipselabs.occ.marsrobot.robot;

import java.util.Collection;

import simbad.sim.SensorDevice;

public interface Robot {

	String getName();
	
	void move(double x, double z);
	
	Collection<SensorDevice> getSensorDevices();
	
	void rotateY(double angle);
	
}
