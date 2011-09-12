package org.eclipselabs.occ.marsrobot.world.internal;

import org.eclipselabs.occ.marsrobot.world.api.WallInstance;
import org.eclipselabs.occ.marsrobot.world.internal.simbad.sim.Wall;

public class DefaultWallInstance implements WallInstance {
	
	private final Wall wall;
	private int length;
	private int height;
	private int width;
	private int xPos;
	private int zPos;

	public DefaultWallInstance(Wall wall) {
		this.wall = wall;
	}

	@Override
	public WallInstance rotate90(int times) {
		wall.rotate90(times);
		return this;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getLength() {
		return length;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHeight() {
		return height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getWidth() {
		return width;
	}

	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	public int getxPos() {
		return xPos;
	}

	public void setzPos(int zPos) {
		this.zPos = zPos;
	}

	public int getzPos() {
		return zPos;
	}

}
