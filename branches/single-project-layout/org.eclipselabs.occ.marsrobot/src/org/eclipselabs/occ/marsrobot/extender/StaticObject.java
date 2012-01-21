package org.eclipselabs.occ.marsrobot.extender;

import simbad.sim.BaseObject;
import simbad.sim.EnvironmentDescription;

public interface StaticObject {

	BaseObject createStaticSimbadObject(EnvironmentDescription ed);
	
}
