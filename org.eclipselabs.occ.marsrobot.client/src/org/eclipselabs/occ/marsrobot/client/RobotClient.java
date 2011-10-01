package org.eclipselabs.occ.marsrobot.client;

import org.eclipselabs.occ.marsrobot.robot.Robot;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@Component
public class RobotClient {
	
	Robot robot;
	
	@Reference
	public void setRobot(Robot robot)
	{
		this.robot = robot;
	}
	
	@Activate
	public void init()
	{
		System.out.println(this.robot.getName());
	}

}
