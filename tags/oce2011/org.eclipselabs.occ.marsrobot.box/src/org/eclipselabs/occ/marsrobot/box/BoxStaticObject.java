package org.eclipselabs.occ.marsrobot.box;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import org.eclipselabs.occ.marsrobot.extender.StaticObject;

import simbad.sim.BaseObject;
import simbad.sim.Box;
import simbad.sim.EnvironmentDescription;

public class BoxStaticObject implements StaticObject {

	@Override
	public BaseObject createStaticSimbadObject(EnvironmentDescription ed) {
		return new Box(new Vector3d(-3, 0, -3), new Vector3f(1, 1, 1), ed);
	}

}
