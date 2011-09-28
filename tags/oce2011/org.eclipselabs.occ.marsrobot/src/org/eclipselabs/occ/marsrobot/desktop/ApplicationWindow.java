package org.eclipselabs.occ.marsrobot.desktop;

import javax.swing.JComponent;
import javax.swing.JFrame;

public interface ApplicationWindow {
	
	JComponent getApplicationComponent();
	
	JFrame getApplicationFrame();

}
