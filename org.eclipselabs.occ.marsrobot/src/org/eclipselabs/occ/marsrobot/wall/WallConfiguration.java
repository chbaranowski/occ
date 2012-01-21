package org.eclipselabs.occ.marsrobot.wall;

import aQute.bnd.annotation.metatype.Meta.OCD;

@OCD(name="Wall", description="Wall Configuration.")
public interface WallConfiguration {
	int x();
	int y();
	int z();
	int length();
	int height();
	int rotate();
}
