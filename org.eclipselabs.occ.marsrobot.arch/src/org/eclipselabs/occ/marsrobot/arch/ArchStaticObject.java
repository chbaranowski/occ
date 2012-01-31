package org.eclipselabs.occ.marsrobot.arch;

import javax.vecmath.Vector3d;

import org.eclipselabs.occ.marsrobot.extender.StaticObject;

import simbad.sim.Arch;
import simbad.sim.BaseObject;
import simbad.sim.EnvironmentDescription;

public class ArchStaticObject implements StaticObject {

	@Override
	public BaseObject createStaticSimbadObject(EnvironmentDescription ed) {
		return new Arch(new Vector3d(3, 0, -3), ed);
	}

}
