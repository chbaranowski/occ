package org.eclipselabs.occ.marsrobot.robot.internal;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.vecmath.Vector3d;

import org.eclipselabs.occ.marsrobot.robot.Robot;
import org.eclipselabs.occ.marsrobot.world.World;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import simbad.sim.CameraSensor;
import simbad.sim.RangeSensorBelt;
import simbad.sim.RobotFactory;
import simbad.sim.SensorDevice;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Modified;
import aQute.bnd.annotation.component.Reference;
import aQute.bnd.annotation.metatype.Configurable;
import aQute.bnd.annotation.metatype.Meta.OCD;

@OCD(name = "Robot", description = "Configuration of a Mars Robot instance.")
interface RobotConfiguration {
	String name();

	int x();

	int z();

	Boolean bumpSensor();

	Boolean cameraSensor();
	
	Boolean sonarSensor();
}

@Component(designateFactory = RobotConfiguration.class, immediate = true)
public class RobotComponent implements Robot {

	RobotConfiguration configuration;

	World world;

	simbad.sim.Robot simbadRobot;
	
	Collection<SensorDevice> sensorDevices;
	
	@Activate
	public void activate(Map<String, Object> props) {
		configuration = Configurable.createConfigurable(RobotConfiguration.class, props);
		sensorDevices = new Vector<SensorDevice>();
		Vector3d startPosition = getStartPosition();
		simbadRobot = new simbad.sim.Robot(getStartPosition(), getName()) {{ create(); }};
		if (configuration.bumpSensor()) {
			RangeSensorBelt sensor = RobotFactory.addBumperBeltSensor(simbadRobot);
			sensorDevices.add(sensor);
		}
		if (configuration.cameraSensor()) {
			CameraSensor sensor = RobotFactory.addCameraSensor(simbadRobot);
			sensorDevices.add(sensor);
		}
		if(configuration.sonarSensor()){
			RangeSensorBelt sensor = RobotFactory.addSonarBeltSensor(simbadRobot);
			sensorDevices.add(sensor);
		}
		world.addObject(simbadRobot);
		move(configuration.x(), configuration.z());
	}

	@Deactivate
	public void deactivate() {
		sensorDevices.clear();
		sensorDevices = null;
		world.detach(simbadRobot);
		simbadRobot = null;
	}

	@Modified
	public void modified(Map<String, Object> props) {
		deactivate();
		activate(props);
	}

	@Reference(dynamic = true)
	public void setWorld(World world) {
		this.world = world;
	}

	public void unsetWorld(World world) {
		this.world = null;
	}

	public String getName() {
		return configuration.name();
	}

	public Vector3d getStartPosition() {
		return new Vector3d(configuration.x(), 0.2, configuration.z());
	}

	@Override
	public void move(double x, double z) {
		world.getSimbadWorld().stopRendering();
		simbadRobot.moveToPosition(x, z);
		simbadRobot.updateSensors(1, world.getSimbadWorld().getPickableSceneBranch());
		world.getSimbadWorld().startRendering();
		world.refreshedWorldContent();
	}

	@Override
	public Collection<SensorDevice> getSensorDevices() {
		return sensorDevices;
	}
	
	@Override
	public void rotateY(double angle){
		world.getSimbadWorld().stopRendering();
		simbadRobot.rotateY(angle);
		simbadRobot.updateSensors(1, world.getSimbadWorld().getPickableSceneBranch());
		world.getSimbadWorld().startRendering();
		world.refreshedWorldContent();
	}
	
}
