package org.eclipselabs.occ.marsrobot.world.internal;

import org.eclipselabs.occ.marsrobot.world.api.ArchInstance;
import org.eclipselabs.occ.marsrobot.world.internal.simbad.sim.Arch;

public class DefaultArchInstance implements ArchInstance {

	private final Arch arch;

	public DefaultArchInstance(Arch arch) {
		this.arch = arch;
	}
	
	@Override
	public ArchInstance rotate(double angle) {
		arch.rotateY(angle);
		return null;
	}

}
