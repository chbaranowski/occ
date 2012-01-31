package org.eclipselabs.occ.marsrobot.remoteextender.client;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

import org.eclipselabs.occ.marsrobot.remoteextender.service.api.WallService;

/**
 * Places new walls in the world by calling the exported remote extender service
 * API.
 * 
 * @see WallService
 */
@Component
public class WallClientComponent {

	private WallService wallService;

	@Reference
	public void setWallService(WallService wallService) {
		this.wallService = wallService;
	}

	@Activate
	public void createWall() throws Exception {
		System.out
				.println("Client creates new wall by delegating to (remote?) wall service");
		// TODO Add a GUI for placing new walls
		wallService.createWall(1, 1, 0, 8, 4, 1);
	}

}
