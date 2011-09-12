package org.eclipselabs.occ.marsrobot.wall;

import java.util.Map;

import javax.vecmath.Vector3d;

import org.eclipselabs.occ.marsrobot.world.World;

import simbad.sim.EnvironmentDescription;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Modified;
import aQute.bnd.annotation.component.Reference;
import aQute.bnd.annotation.metatype.Configurable;
import aQute.bnd.annotation.metatype.Meta.OCD;

@OCD(name="Wall", description="Wall Configuration.")
interface WallConfiguration {
	int x();
	int y();
	int z();
	int length();
	int height();
	int rotate();
}

@Component(designateFactory=WallConfiguration.class, immediate=true)
public class WallComponent {
	
	WallConfiguration configuration;
	
	World world;
	
	simbad.sim.Wall simbadWall;
	
	@Activate
	public void activate(Map<String, Object> props) {
		configuration = Configurable.createConfigurable(WallConfiguration.class, props);
		EnvironmentDescription environment = world.getEnvironmentDescription();
		simbadWall = new simbad.sim.Wall(getPosition(), getLength(), getHeight(), environment);
		simbadWall.rotate90(getRotateTimes());
		world.addObject(simbadWall);
	}
	
	@Deactivate
	public void deactivate(){
		world.detach(simbadWall);
		simbadWall = null;
	}

	@Modified
	public void modified(Map<String, Object> props) {
		deactivate();
		activate(props);
	}
	
	public Vector3d getPosition() {
		return new Vector3d(configuration.x(),configuration.y(),configuration.z());
	}

	public float getHeight() {
		return configuration.height();
	}

	public float getLength() {
		return configuration.length();
	}

	public int getRotateTimes() {
		return configuration.rotate();
	}

	@Reference(dynamic=true)
	public void setWorld(World world) {
		this.world = world;
	}
	
	public void unsetWorld(World world) {
		this.world = null;
	}

}
