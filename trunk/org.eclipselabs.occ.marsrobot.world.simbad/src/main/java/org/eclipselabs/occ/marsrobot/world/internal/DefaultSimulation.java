package org.eclipselabs.occ.marsrobot.world.internal;

import org.eclipselabs.occ.marsrobot.world.api.Simulation;
import org.eclipselabs.occ.marsrobot.world.internal.simbad.gui.Simbad;
import org.eclipselabs.occ.marsrobot.world.internal.simbad.sim.EnvironmentDescription;

public class DefaultSimulation implements Simulation{

	private final Simbad simbad;
	private final EnvironmentDescription environmentDescription;

	public DefaultSimulation(Simbad simbad, EnvironmentDescription environmentDescription) {
		this.simbad = simbad;
		this.environmentDescription = environmentDescription;
	}
	
	@Override
	public void stop() {
		simbad.simulator.stopSimulation();
	}

	public Simbad getSimbad() {
		return simbad;
	}

	@Override
	public void restart() {
		simbad.start(environmentDescription);
	}

}
