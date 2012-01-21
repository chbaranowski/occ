package org.eclipselabs.occ.marsrobot.sensordevices;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.eclipselabs.occ.marsrobot.desktop.DesktopWindow;
import org.eclipselabs.occ.marsrobot.world.World;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import simbad.sim.Agent;
import simbad.sim.SensorDevice;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;

@Component(immediate = true, 
	properties = {
		"event.topics=" + World.Events.WORLD_EVENT_GROUP + "*"
	}
)
public class SensorDevicesComponent implements DesktopWindow, EventHandler {
	
	JTabbedPane content;
	
	Map<Agent, JPanel> agentToInspectorPanels;
	
	World world;
	
	public SensorDevicesComponent() {
		initialize();
	}
	
	protected void initialize() {
		content = new JTabbedPane();
	}
	
	@Activate
	public void activate(){
		agentToInspectorPanels = new HashMap<Agent, JPanel>();
		Collection<Agent> agents = world.getAgents();
		for (Agent agent : agents) {
			addSensorDevicesPanels(agent);
		}
		content.setVisible(true);
	}
	
	@Deactivate
	public void deactivate(){
		agentToInspectorPanels.clear();
		agentToInspectorPanels = null;
		content.removeAll();
		content.setVisible(false);
	}

	@Override
	public Container getContent() {
		return content;
	}

	@Override
	public String getName() {
		return "Sensor Data";
	}

	@Override
	public Point getDefaultLocation() {
		return new Point(410, 5);
	}

	@Override
	public Dimension getSize() {
		return new Dimension(220, 500);
	}

	@Override
	public void handleEvent(Event event) {
		if(event.getTopic().equals(World.Events.ADD_AGENT_EVENT)){
			Agent agent = (Agent) event.getProperty("agent");
			addSensorDevicesPanels(agent);
		}
		else if(event.getTopic().equals(World.Events.REMOVE_AGENT_EVENT)){
			Agent agent = (Agent) event.getProperty("agent");
			removeSensorDevicesPanels(agent);
		}
		else if(event.getTopic().equals(World.Events.WORLD_CHANGED_EVENT)){
			content.setVisible(false);
			content.repaint();
			content.setVisible(true);
		}
	}

	private void addSensorDevicesPanels(Agent agent) {
		content.setVisible(false);
		JPanel rootInspectorPanel = new JPanel();
		rootInspectorPanel.setLayout(new BoxLayout(rootInspectorPanel, BoxLayout.Y_AXIS));
		agentToInspectorPanels.put(agent, rootInspectorPanel);
		for (Object obj : agent.getSensorList()) {
			SensorDevice sensorDevice = (SensorDevice) obj;
			JLabel label = new JLabel(sensorDevice.getName());
			label.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
			rootInspectorPanel.add(label);
			JPanel inspectorPanel = sensorDevice.createInspectorPanel();
			inspectorPanel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
			rootInspectorPanel.add(inspectorPanel);
		}
		rootInspectorPanel.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT);
		content.addTab(agent.getName(), rootInspectorPanel);
		content.setVisible(true);
	}
	
	private void removeSensorDevicesPanels(Agent agent) {
		JPanel panel = agentToInspectorPanels.remove(agent);
		content.remove(panel);
	}
	
	@Reference(dynamic=true)
	public void setWorld(World world){
		this.world = world;
	}
	
	public void unsetWorld(World world){
		this.world = null;
	}

}
