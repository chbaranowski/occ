package org.eclipselabs.occ.marsrobot.world.internal;

import javax.vecmath.Vector3d;

import org.eclipselabs.occ.marsrobot.world.internal.simbad.sim.Agent;
import org.eclipselabs.occ.marsrobot.world.internal.simbad.sim.CameraSensor;
import org.eclipselabs.occ.marsrobot.world.internal.simbad.sim.RangeSensorBelt;
import org.eclipselabs.occ.marsrobot.world.internal.simbad.sim.RobotFactory;

/** Describe the robot */
public class Robot extends Agent {

	RangeSensorBelt sonars;
	CameraSensor camera;

	public Robot(Vector3d position, String name) {
		super(position, name);
		// Add camera
		camera = RobotFactory.addCameraSensor(this);
		// Add sonars
		sonars = RobotFactory.addSonarBeltSensor(this,4);
	}

	/** This method is called by the simulator engine on reset. */
	public void initBehavior() {
		// nothing particular in this case
	}

	/**
	 * This method is called cyclically (20 times per second) by the simulator
	 * engine.
	 */
	public void performBehavior() {
		
		// progress at 0.5 m/s
		setTranslationalVelocity(0.5);
		// frequently change orientation
		if ((getCounter() % 100) == 0)
			setRotationalVelocity(Math.PI / 2 * (0.5 - Math.random()));

		// print front sonar every 100 frames
		if (getCounter() % 100 == 0)
			System.out
					.println("Sonar num 0  = " + sonars.getMeasurement(0));
		
	}
}