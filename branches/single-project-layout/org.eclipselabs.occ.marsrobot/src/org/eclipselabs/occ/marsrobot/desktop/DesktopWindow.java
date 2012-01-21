package org.eclipselabs.occ.marsrobot.desktop;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;

public interface DesktopWindow {

	Container getContent();
	
	String getName();
	
	Point getDefaultLocation();
	
	Dimension getSize();
	
}
