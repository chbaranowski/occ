package org.eclipselabs.occ.marsrobot.remote.consumer;

import org.apache.felix.service.command.CommandProcessor;
import org.eclipselabs.occ.marsrobot.remote.WorldService;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@Component(
	immediate = true, 
	properties = { 
		CommandProcessor.COMMAND_SCOPE + "=remote console", 
		CommandProcessor.COMMAND_FUNCTION + "=createWall" 
})
public class RemoteConsole implements ConsoleCommands {

	private WorldService worldService;
	
	@Reference
	public void setWorldService(WorldService worldService) {
		this.worldService = worldService;
	}
	
	public void createWall(int x, int y, int z, int length, int height, int rotate){
		worldService.createWall(x, y, z, length, height, rotate);
	}
	
}
