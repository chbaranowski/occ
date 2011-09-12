package org.eclipselabs.occ.marsrobot.environment.mars;

import org.eclipselabs.occ.marsrobot.robot.api.MarsRobot;
import org.eclipselabs.occ.marsrobot.world.api.Environment;
import org.eclipselabs.occ.marsrobot.world.api.World;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@Component(immediate=true)
public class MarsEnvironment implements Environment {

	private World world;
	
	private MarsRobot robot;

	@Reference
	public void setWorld(World world) {
		this.world = world;
	}

	@Reference
	public void setRobot(MarsRobot robot) {
		this.robot = robot;
	}
	
	@Activate
	public void startEnvironment(){
		world.createWall(9, 0, 0, 19, 1).rotate90(1);
		world.createWall(-9, 0, 0, 19, 2).rotate90(1);
		world.createWall(0, 0, 9, 19, 1);
		world.createWall(0, 0, -9, 19, 2);
		
		//create box(es) and arches
		world.createBox(5, 0, 3, 1, 1, 1);//.rotate(0.6d);
		world.createBox(-2, 0, 3, 2, 2, 2);
		world.createArch(-6, 0, -5);
		world.createArch(4, 0, -4).rotate(0.4d);
		
		System.out.println("Start mars world with robot : " + robot.getName());
	}


}
