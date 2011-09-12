package org.eclipselabs.occ.marsrobot.world;

import java.util.Collection;

import simbad.sim.Agent;
import simbad.sim.BaseObject;
import simbad.sim.EnvironmentDescription;

public interface World {
	
	interface Events {
		
		String WORLD_EVENT_GROUP = "org/eclipselabs/occ/marsrobot/world/";
	
		String ADD_AGENT_EVENT = WORLD_EVENT_GROUP + "AddAgentEvent";
	
		String REMOVE_AGENT_EVENT = WORLD_EVENT_GROUP + "RemoveAgentEvent";
	
		String CHANGE_WORLD_VIEWPOINT_EVENT = WORLD_EVENT_GROUP + "ChangeWorldViewPointEvent";
		
		String WORLD_CHANGED_EVENT = WORLD_EVENT_GROUP + "WorldChangedEvent";
		
	}
	
	EnvironmentDescription getEnvironmentDescription();
	
	void addObject(BaseObject obj3d);
	
	void detach(BaseObject obj3d);
	
	simbad.sim.World getSimbadWorld();
	
	Collection<Agent> getAgents();
	
	void refreshedWorldContent();
	
}
