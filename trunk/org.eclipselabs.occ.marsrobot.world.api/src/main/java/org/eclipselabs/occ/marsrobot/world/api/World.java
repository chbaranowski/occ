package org.eclipselabs.occ.marsrobot.world.api;


public interface World {
	
	WallInstance createWall(int x, int y, int z, int length, int heigth);
	
	BoxInstance createBox(int x, int y, int z, int xLen, int yLen, int zLen);
	
	ArchInstance createArch(int x, int y, int z);
	
	Subject createSubject(int x, int y, int z, String symbolicName);
	
	Dimension getDimension();
		
}
