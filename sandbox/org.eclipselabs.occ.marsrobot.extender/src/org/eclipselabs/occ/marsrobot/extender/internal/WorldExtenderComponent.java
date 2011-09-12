package org.eclipselabs.occ.marsrobot.extender.internal;

import org.eclipselabs.occ.marsrobot.world.World;
import org.osgi.framework.BundleContext;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;

@Component(immediate=true)
public class WorldExtenderComponent {

	StaticObjectExtender staticObjectExtender;
	
	World world;
	
	@Activate
	public void activate(BundleContext context){
		staticObjectExtender = new StaticObjectExtender(world, context);
		staticObjectExtender.open();
	}
	
	@Deactivate
	public void deactivate(){
		staticObjectExtender.close();
		staticObjectExtender = null;
	}
	
	@Reference(dynamic=true)
	public void setWorld(World world){
		this.world = world;
	}
	
	public void unsetWorld(World world){
		this.world = null;
	}
	
}
