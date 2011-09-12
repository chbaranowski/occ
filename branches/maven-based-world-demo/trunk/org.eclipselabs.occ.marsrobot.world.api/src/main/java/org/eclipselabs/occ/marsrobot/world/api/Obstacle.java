package org.eclipselabs.occ.marsrobot.world.api;

public class Obstacle {

	private String type;
	
	private int xPos;
	private int yPos;
	private int zPos;
	private int xLen;
	private int yLen;
	private int zLen;
	
	private double rotationAngle;

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setxPos(int xPos) {
		this.xPos = xPos;
	}

	public int getxPos() {
		return xPos;
	}

	public void setyPos(int yPos) {
		this.yPos = yPos;
	}

	public int getyPos() {
		return yPos;
	}

	public void setzPos(int zPos) {
		this.zPos = zPos;
	}

	public int getzPos() {
		return zPos;
	}

	public void setxLen(int xLen) {
		this.xLen = xLen;
	}

	public int getxLen() {
		return xLen;
	}

	public void setyLen(int yLen) {
		this.yLen = yLen;
	}

	public int getyLen() {
		return yLen;
	}

	public void setzLen(int zLen) {
		this.zLen = zLen;
	}

	public int getzLen() {
		return zLen;
	}

	public void setRotationAngle(double rotationAngle) {
		this.rotationAngle = rotationAngle;
	}

	public double getRotationAngle() {
		return rotationAngle;
	}
}
