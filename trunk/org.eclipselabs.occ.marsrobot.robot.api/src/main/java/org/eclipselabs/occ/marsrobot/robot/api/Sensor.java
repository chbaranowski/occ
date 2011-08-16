package org.eclipselabs.occ.marsrobot.robot.api;

import org.eclipselabs.occ.marsrobot.world.api.Subject;

public interface Sensor<T> {
	
	T getSensorData(Subject subject);

}
