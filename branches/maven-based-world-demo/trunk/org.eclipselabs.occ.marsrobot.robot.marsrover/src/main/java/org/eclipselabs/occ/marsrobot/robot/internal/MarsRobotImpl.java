package org.eclipselabs.occ.marsrobot.robot.internal;

import org.eclipselabs.occ.marsrobot.robot.api.BumpData;
import org.eclipselabs.occ.marsrobot.robot.api.BumpSensor;
import org.eclipselabs.occ.marsrobot.robot.api.MarsRobot;
import org.eclipselabs.occ.marsrobot.world.api.BehaviorListener;
import org.eclipselabs.occ.marsrobot.world.api.Subject;
import org.eclipselabs.occ.marsrobot.world.api.World;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@Component
public class MarsRobotImpl implements MarsRobot, BehaviorListener {
	
	private World world;
	private Subject robotSubject;
	private BumpSensor bumpSensor;

	@Reference
	public void setWorld(World world) {
		this.world = world;
	}
	
	@Reference
	public void setBumpSensor(BumpSensor bumpSensor){
		this.bumpSensor = bumpSensor;
	}
	

	@Activate
	public void start(){
		robotSubject = world.createSubject(0, 0, 0, getName());
		
	}

	@Override
	public String getName() {
		return "Mars Robot 1";
	}

	@Override
	public void performBehavior() {
		BumpData data = bumpSensor.getSensorData(robotSubject);
		System.out.println("The world's length: " + data.getWorldDimension().getLength());
		System.out.println("The world's width: " + data.getWorldDimension().getWidth());
		System.out.println("The world's height: " + data.getWorldDimension().getHeight());
		
		robotSubject.moveToPosition(2,2);
		double[] robotPosition = robotSubject.getActualXZPosition();
		
		double[] bumpersMeasurement = bumpSensor.getBumpersMeasurements(robotSubject);
		double dist = bumpSensor.minimumBumpersMeasurement(robotSubject);
		
		for(int i=0; i<bumpersMeasurement.length; i++) {
			System.out.println("Measurement bumper " + i + ": " + bumpersMeasurement[i]);
		}
		System.out.println("Minimum bumper measurement: " + dist);
		System.out.println("x-position: " + robotPosition[0] + " z-position: " + robotPosition[1]);
		
	}
	
}
