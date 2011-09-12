package org.eclipselabs.occ.marsrobot.world.internal;

import javax.vecmath.Point3d;

import org.eclipselabs.occ.marsrobot.world.api.Subject;
import org.eclipselabs.occ.marsrobot.world.api.World;
import org.eclipselabs.occ.marsrobot.world.internal.simbad.sim.CameraSensor;
import org.eclipselabs.occ.marsrobot.world.internal.simbad.sim.RangeSensorBelt;

public class SubjectImpl implements Subject{

	private final Robot robot;
	private World world;
	
	private RangeSensorBelt bumper; 
	private RangeSensorBelt sonar;
	private CameraSensor camera;

	public SubjectImpl(Robot robot) {
		this.robot = robot;
	}
	
	@Override
	public void moveToPosition(double x, double z) {
		robot.moveToPosition(x, z);	
	}

	@Override
	public void rotateSubjectY(double angle) {
		robot.rotateY(angle);
	}

	@Override
	public World getWorld() {
		return world;
	}

	public void setWorld(World world) {
		this.world = world;
	}
	
	public void setBumpSensor(RangeSensorBelt bumpSensor) {
		this.bumper = bumpSensor;
	}

	public RangeSensorBelt getBumpSensor() {
		return bumper;
	}

	public void setSonarSensor(RangeSensorBelt sonar) {
		this.sonar = sonar;		
	}
	
	public RangeSensorBelt getSonarSensor() {
		return sonar;
	}
	
	public void setCameraSensor(CameraSensor camera) {
		this.camera = camera;
	}

	public CameraSensor getCameraSensor() {
		return camera;
	}
	
	@Override
	public boolean collisionDetected() {
		boolean collision = robot.collisionDetected();
		return collision;
	}

	@Override
	public double[] getActualXZPosition() {
		double[] xzPos = new double[2];
		Point3d coord = new Point3d();
		robot.getCoords(coord);
		xzPos[0] = coord.x;
		xzPos[1] = coord.z;
		return xzPos;
	}
	
	@Override
	public boolean anotherAgentIsVeryNear() {
		boolean agentNear = robot.anOtherAgentIsVeryNear();
		return agentNear;
	}

}
