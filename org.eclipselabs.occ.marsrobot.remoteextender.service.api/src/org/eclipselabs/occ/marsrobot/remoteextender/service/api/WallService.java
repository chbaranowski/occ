package org.eclipselabs.occ.marsrobot.remoteextender.service.api;

/**
 * Allows placing new walls in the world.
 */
public interface WallService {

	/**
	 * Create new wall by adding required arguments for position and dimension.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @param length
	 * @param height
	 * @param rotate
	 * @throws Exception
	 */
	public abstract void createWall(int x, int y, int z, int length,
			int height, int rotate) throws Exception;

}