package org.eclipselabs.occ.marsrobot.world.internal;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import org.eclipselabs.occ.marsrobot.world.api.BehaviorListener;
import org.eclipselabs.occ.marsrobot.world.api.BoxInstance;
import org.eclipselabs.occ.marsrobot.world.api.Dimension;
import org.eclipselabs.occ.marsrobot.world.api.Environment;
import org.eclipselabs.occ.marsrobot.world.api.SensorData;
import org.eclipselabs.occ.marsrobot.world.api.Subject;
import org.eclipselabs.occ.marsrobot.world.api.Simulation;
import org.eclipselabs.occ.marsrobot.world.api.Simulator;
import org.eclipselabs.occ.marsrobot.world.api.WallInstance;
import org.eclipselabs.occ.marsrobot.world.api.World;
import org.eclipselabs.occ.marsrobot.world.internal.simbad.gui.Simbad;
import org.eclipselabs.occ.marsrobot.world.internal.simbad.sim.Arch;
import org.eclipselabs.occ.marsrobot.world.internal.simbad.sim.Box;
import org.eclipselabs.occ.marsrobot.world.internal.simbad.sim.CameraSensor;
import org.eclipselabs.occ.marsrobot.world.internal.simbad.sim.EnvironmentDescription;
import org.eclipselabs.occ.marsrobot.world.internal.simbad.sim.RangeSensorBelt;
import org.eclipselabs.occ.marsrobot.world.internal.simbad.sim.RobotFactory;
import org.eclipselabs.occ.marsrobot.world.internal.simbad.sim.Wall;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@Component
public class WorldSimbadImpl implements World, Simulator, SensorData {

	private List<BehaviorListener> behaviorListeners = new ArrayList<BehaviorListener>();

	private EnvironmentDescription environment = new EnvironmentDescription();
	
	private ArrayList<DefaultWallInstance> walls = new ArrayList<DefaultWallInstance>();

	@Reference(optional = true, multiple = true, unbind = "stopEnvironment", dynamic = true)
	public void startEnvironment(Environment environment) {
		startSimulation();
		// TODO: Impl
	}

	@Reference(optional = true, multiple = true, unbind = "removeBehaviorListener", dynamic = true)
	public void addBehaviorListener(BehaviorListener listener) {
		behaviorListeners.add(listener);
	}

	public void removeBehaviorListener(BehaviorListener listener) {
		behaviorListeners.remove(listener);
	}

	public void stopEnvironment(Environment environment) {
		// TODO: Impl
	}

	@Override
	public Simulation startSimulation() {
		Simbad simulator = new Simbad(environment, false);
		simulator.start(environment);
		return new DefaultSimulation(simulator, environment);
	}

	@Override
	public WallInstance createWall(int x, int y, int z, int length, int height) {
		Wall wall = new Wall(new Vector3d(x, y, z), length, height, environment);
		environment.add(wall);
		
		DefaultWallInstance defaultWall = new DefaultWallInstance(wall);
		defaultWall.setHeight(height);
		defaultWall.setLength(length);
		defaultWall.setWidth(length);
		defaultWall.setxPos(x);
		defaultWall.setzPos(z);
		walls.add(defaultWall);
		
		return defaultWall;
	}

	@Override
	public Subject createSubject(int x, int y, int z, String name) {
		Robot robot = new Robot(new Vector3d(x, y, z), name){
			@Override
			public void performBehavior() {
				for (BehaviorListener behaviorListener : behaviorListeners) {
					behaviorListener.performBehavior();
				}
			}
		};
		environment.add(robot);
		SubjectImpl subjectImpl = new SubjectImpl(robot);
		subjectImpl.setWorld(this);
		
		RangeSensorBelt bumper = RobotFactory.addBumperBeltSensor(robot, 4);

		subjectImpl.setBumpSensor(bumper);
		subjectImpl.setSonarSensor(robot.sonars); 
		subjectImpl.setCameraSensor(robot.camera);
		
		return subjectImpl;
	}

	@Override
	public BoxInstance createBox(int x, int y, int z, int xLen, int yLen, int zLen) {

		Vector3d pos = new Vector3d(x, y, z);
		Vector3f extent = new Vector3f(xLen, yLen, zLen);
		Box box = new Box(pos, extent, environment);
		float red = 0.9f;
		float green = 0.1f;
		float blue = 0.1f;
		Color3f color = new Color3f(red, green, blue);
		box.setColor(color);
		environment.add(box);

		return new DefaultBoxInstance(box);
	}

	@Override
	public DefaultArchInstance createArch(int x, int y, int z) {
		
		Vector3d pos = new Vector3d(x, y, z);
		Arch arch = new Arch(pos, environment);
		environment.add(arch);
		
		return new DefaultArchInstance(arch);
	}

	@Override
	public Dimension getDimension() {
		
		Dimension dimension = new Dimension();
		ArrayList<Integer> xPositions = new ArrayList<Integer>();
		ArrayList<Integer> zPositions = new ArrayList<Integer>();
		int length = 0;
		int width = 0;
		int height = 0;
		int minxPos = 0;
		int maxxPos = 0;
		int minzPos = 0;
		int maxzPos = 0;
		
		if(walls != null) {
			for (int i=0; i<walls.size(); i++) {
				DefaultWallInstance wall = walls.get(i);
				int xPos = wall.getxPos();
				xPositions.add(xPos);
				int zPos = wall.getzPos();
				zPositions.add(zPos);
				
				if(wall.getHeight() > height) {
					height = wall.getHeight();
				}
			}
			
			for (int j=0; j<xPositions.size(); j++) {
				if(minxPos > xPositions.get(j)) {
					minxPos = xPositions.get(j);
				}
				if(minzPos > zPositions.get(j)) {
					minzPos = zPositions.get(j);
				}
				if(maxxPos < xPositions.get(j)) {
					maxxPos = xPositions.get(j);
				}
				if(maxzPos < zPositions.get(j)) {
					maxzPos = zPositions.get(j);
				}
			}
			width  = maxxPos-minxPos;
			length = maxzPos-minzPos;
		}
		
		dimension.setWidth(width);
		dimension.setHeight(height);
		dimension.setLength(length);
		
		return dimension;
	}

	@Override
	public double[] getBumperMeasurements(Subject subject) {
		SubjectImpl subjectImpl = (SubjectImpl) subject;
		RangeSensorBelt bumpSensor = subjectImpl.getBumpSensor();
		
		double[] measurements = new double[bumpSensor.getNumSensors()];
		for (int i=0; i<bumpSensor.getNumSensors(); i++) {
			measurements[i] = bumpSensor.getMeasurement(i);
		}
		
		return measurements;
	}
	
	@Override
	public double getMinimumBumpersMeasurement(Subject subject) {
		double[] measurements = getBumperMeasurements(subject);
		double nearest = 100;
		
		for(int i=0; i<measurements.length; i++) {
			double data = measurements[i];
			if(data<nearest) {
				nearest = data;
			}
		}
	
		return nearest;
	}
	
	@Override
	public double[] getSonarMeasurements(Subject subject) {
		SubjectImpl subjectImpl = (SubjectImpl) subject;
		RangeSensorBelt sonarSensor = subjectImpl.getSonarSensor();
		
		double[] measurements = new double[sonarSensor.getNumSensors()];
		for (int i=0; i<sonarSensor.getNumSensors(); i++) {
			measurements[i] = sonarSensor.getMeasurement(i);
		}
		
		return measurements;
	}

	@Override
	public boolean getSonarOneHasHit(Subject subject) {
		SubjectImpl subjectImpl = (SubjectImpl) subject;
		RangeSensorBelt sonarSensor = subjectImpl.getSonarSensor();
		
		boolean oneHasHits = sonarSensor.oneHasHit();
		
		return oneHasHits;
	}

	@Override
	public float getSonarMaxRange(Subject subject) {
		SubjectImpl subjectImpl = (SubjectImpl) subject;
		RangeSensorBelt sonarSensor = subjectImpl.getSonarSensor();
		float maxRange = sonarSensor.getMaxRange();
		return maxRange;
	}

	@Override
	public int getCameraImageWidth(Subject subject) {
		SubjectImpl subjectImpl = (SubjectImpl) subject;
		CameraSensor camera = subjectImpl.getCameraSensor();
		int width = camera.getImageWidth();
		return width;
	}

	@Override
	public int getCameraImageHeight(Subject subject) {
		SubjectImpl subjectImpl = (SubjectImpl) subject;
		CameraSensor camera = subjectImpl.getCameraSensor();
		int height = camera.getImageHeight();
		return height;
	}

	@Override
	public double minimumSonarMeasurement(Subject subject) {
		double[] measurements = getSonarMeasurements(subject);
		double nearest = 100;
			
		for(int i=0; i<measurements.length; i++) {
			double data = measurements[i];
			if(data<nearest) {
				nearest = data;
			}
		}
	
		return nearest;
	}

	@Override
	public double maximumSonarMeasurement(Subject subject) {
		double[] measurements = getSonarMeasurements(subject);		
		double farthest = 0;
		
		for(int i=0; i<measurements.length; i++) {
			double data = measurements[i];
			if(data>farthest) {
				farthest = data;
			}
		}
		
		return farthest;
	}

}
