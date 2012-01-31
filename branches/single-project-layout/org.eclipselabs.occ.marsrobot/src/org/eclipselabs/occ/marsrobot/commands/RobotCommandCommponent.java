package org.eclipselabs.occ.marsrobot.commands;

import java.util.Collection;
import java.util.Vector;

import org.apache.felix.service.command.CommandProcessor;
import org.eclipselabs.occ.marsrobot.robot.Robot;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@Component(immediate = true, properties = { 
		CommandProcessor.COMMAND_SCOPE + "=robot", 
		CommandProcessor.COMMAND_FUNCTION + "=move | rotateY" 
})
public class RobotCommandCommponent implements RobotCommands {

	Collection<Robot> robots = new Vector<Robot>();

	public void move(String robotName, double x, double z) {
		Robot robot = findRobotByName(robotName);
		if (robot != null) {
			robot.move(x, z);
		} else {
			System.out.println("No Robot with name " + robotName + " found!");
		}
	}
	
	public void rotateY(String robotName, double angle){
		Robot robot = findRobotByName(robotName);
		if (robot != null) {
			robot.rotateY(angle);
		} else {
			System.out.println("No Robot with name " + robotName + " found!");
		}
	}

	private Robot findRobotByName(String robotName) {
		for (Robot robot : robots) {
			if (robot.getName().equals(robotName))
				return robot;
		}
		return null;
	}

	@Reference(dynamic = true, multiple = true, optional = true)
	public void addRobot(Robot robot) {
		robots.add(robot);
	}

	public void removeRobot(Robot robot) {
		robots.remove(robot);
	}

}
