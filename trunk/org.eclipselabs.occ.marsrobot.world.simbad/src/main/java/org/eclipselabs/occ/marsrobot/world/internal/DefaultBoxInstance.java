package org.eclipselabs.occ.marsrobot.world.internal;

import org.eclipselabs.occ.marsrobot.world.api.BoxInstance;
import org.eclipselabs.occ.marsrobot.world.internal.simbad.sim.Box;


public class DefaultBoxInstance implements BoxInstance {

	private final Box box;

	public DefaultBoxInstance(Box box) {
		this.box = box;
	}

	@Override
	public BoxInstance rotate(double angle) {
		box.rotateY(angle);
		return this;
	}
}
