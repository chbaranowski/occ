package org.eclipselabs.occ.marsrobot.desktop.internal;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import org.eclipselabs.occ.marsrobot.desktop.ApplicationWindow;
import org.eclipselabs.occ.marsrobot.desktop.DesktopWindow;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;

@Component(immediate=true)
public class Desktop implements ApplicationWindow {
	
	JFrame desktopFrame = new JFrame();
	JDesktopPane desktop = new JDesktopPane();
	
	Map<DesktopWindow, JInternalFrame> desktopWindowToInternalFrame = new HashMap<DesktopWindow, JInternalFrame>();
	
	public Desktop() {
		initialize();
	}
	
	protected void initialize(){
		desktopFrame.setSize(800, 600);
		desktop.setFocusable(true);
		desktopFrame.getContentPane().add(desktop);
	}
	
	@Activate
	public void activate() {
		desktopFrame.setVisible(true);
	}
	
	@Deactivate
	public void deactivate() {
		desktopFrame.setVisible(false);
	}
	
	@Reference(multiple=true, optional=true, dynamic=true)
	public void addDesktopWindow(DesktopWindow window) {
		JInternalFrame desktopFrame = new JInternalFrame(window.getName());
		JPanel conentPane = new JPanel();
		conentPane.setLayout(new BorderLayout());
		desktopFrame.setContentPane(conentPane);
		desktopFrame.setSize(window.getSize());
		desktopFrame.setLocation(window.getDefaultLocation());
		desktopFrame.setResizable(true);
		conentPane.add(window.getContent(), BorderLayout.CENTER);
		desktop.add(desktopFrame);
		desktopFrame.show();
		desktopWindowToInternalFrame.put(window, desktopFrame);
	}
	
	public void removeDesktopWindow(DesktopWindow window) {
		JInternalFrame internalFrame = desktopWindowToInternalFrame.remove(window);
		internalFrame.setVisible(false);
		internalFrame.hide();
		desktop.remove(internalFrame);
		internalFrame.dispose();
		internalFrame = null;
	}

	@Override
	public JComponent getApplicationComponent() {
		return desktop;
	}

	@Override
	public JFrame getApplicationFrame() {
		return desktopFrame;
	}
	
}
