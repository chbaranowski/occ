package org.eclipselabs.occ.marsrobot.robot.internal;

import org.eclipselabs.occ.marsrobot.robot.api.BumpData;
import org.eclipselabs.occ.marsrobot.robot.api.BumpSensor;
import org.eclipselabs.occ.marsrobot.world.api.Dimension;
import org.eclipselabs.occ.marsrobot.world.api.SensorData;
import org.eclipselabs.occ.marsrobot.world.api.Subject;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@Component
public class BumpSensorImpl implements BumpSensor {
	
	private BumpData bumpData;
	
	private SensorData sensorData;
	
	@Reference
	public void setSensorData(SensorData sensorData){
		this.sensorData = sensorData;
	}
	
	public void setBumpData(BumpData bumpData) {
		this.bumpData = bumpData;
	}

	public BumpData getBumpData() {
		return bumpData;
	}
	
	@Override
	public BumpData getSensorData(Subject subject) {
		Dimension worldDimension = subject.getWorld().getDimension();
		bumpData = new BumpData();
		bumpData.setDimension(worldDimension);
		return bumpData;
	}
	
	@Override
	public double minimumBumpersMeasurement(Subject robotSubject) {
		double minimum = sensorData.getMinimumBumpersMeasurement(robotSubject);
		return minimum;
	}

	@Override
	public double[] getBumpersMeasurements(Subject subject) {
		double[] measurement = sensorData.getBumperMeasurements(subject);
		return measurement;
	}
	
}
