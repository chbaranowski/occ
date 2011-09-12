package org.eclipselabs.occ.marsrobot.world.internal;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.eclipselabs.occ.marsrobot.desktop.ApplicationWindow;
import org.eclipselabs.occ.marsrobot.desktop.DesktopWindow;
import org.eclipselabs.occ.marsrobot.world.World;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventAdmin;

import simbad.gui.AgentFollower;
import simbad.sim.Agent;
import simbad.sim.BaseObject;
import simbad.sim.EnvironmentDescription;
import simbad.sim.StaticObject;
import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Modified;
import aQute.bnd.annotation.component.Reference;
import aQute.bnd.annotation.metatype.Configurable;
import aQute.bnd.annotation.metatype.Meta.OCD;

@OCD(name = "World Configuration", description = "The World Configuration.")
interface WorldConfiguration {
	String name();
}

@Component(immediate = true, designate = WorldConfiguration.class)
public class WorldComponent implements World, DesktopWindow {

	EventAdmin eventAdmin;
	
	ApplicationWindow applicationWindow;
	
	WorldConfiguration configuration;

	EnvironmentDescription simbadEnvironment;

	simbad.sim.World simbadWorld;
	
	JPanel worldContent;
	
	JPanel worldPreviewControl;
	
	Collection<Agent> agents = new ArrayList<Agent>();
	
	{
		worldPreviewControl = new JPanel();
		JButton topViewButton = new JButton("top view");
		topViewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				sendWorldChangeViewPointEvent();
				simbadWorld.changeViewPoint(simbad.sim.World.VIEW_FROM_TOP, null);
			}
		});
		worldPreviewControl.add(topViewButton);
		
		JButton sideViewButton = new JButton("side view");
		sideViewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				sendWorldChangeViewPointEvent();
				simbadWorld.changeViewPoint(simbad.sim.World.VIEW_FROM_EAST, null);
			}
		});
		worldPreviewControl.add(sideViewButton);
	}
	
	@Activate
	public void activate(Map<String, Object> props) {
		configuration = Configurable.createConfigurable(WorldConfiguration.class, props);
		simbadEnvironment = new EnvironmentDescription();
		worldContent = new JPanel();
		worldContent.setLayout(new BorderLayout());
		createSimbadWorld();
		worldContent.add(worldPreviewControl, BorderLayout.SOUTH);
	}

	private void createSimbadWorld() {
		simbadWorld = new simbad.sim.World(simbadEnvironment);
		worldContent.add(simbadWorld.getCanvas3D(), BorderLayout.CENTER);
	}

	@Deactivate
	public void deactivate() {
		destroySimbadWorld();
		worldContent = null;
		simbadEnvironment = null;
	}

	private void destroySimbadWorld() {
		worldContent.remove(simbadWorld.getCanvas3D());
		simbadWorld.dispose();
		simbadWorld = null;
	}

	@Modified
	public void modified(Map<String, Object> props) {
		// TODO: Impl
	}

	@Override
	public EnvironmentDescription getEnvironmentDescription() {
		return simbadEnvironment;
	}

	@Override
	public void addObject(BaseObject obj3d) {
		simbadWorld.addObjectToPickableSceneBranch(obj3d);
		if(obj3d instanceof Agent){
			Agent agent = (Agent) obj3d;
			agents.add(agent);
			sendAddAgentEvent(agent);
		}
		else if (obj3d instanceof StaticObject) {
			StaticObject so = (StaticObject) obj3d;
			so.createLocalToVworld();
            so.createTransformedBounds();
		}
		for (Agent agent : agents) {
			agent.updateSensors(1, simbadWorld.getPickableSceneBranch());
		}
		sendWorldChangedEvent();
	}

	private void sendAddAgentEvent(Agent agent) {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("agent", agent);
		Event event = new Event(Events.ADD_AGENT_EVENT, properties);
		eventAdmin.sendEvent(event);
	}

	@Override
	public void detach(BaseObject obj3d) {
		simbadWorld.detach(obj3d);
		if(obj3d instanceof Agent){
			Agent agent = (Agent) obj3d;
			agents.remove(agent);
			sendRemoveAgentEvent(agent);
		}
		for (Agent agent : agents) {
			agent.updateSensors(1, simbadWorld.getPickableSceneBranch());
		}
		sendWorldChangedEvent();
	}

	private void sendRemoveAgentEvent(Agent agent) {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("agent", agent);
		Event event = new Event(Events.REMOVE_AGENT_EVENT, properties);
		eventAdmin.sendEvent(event);
	}

	@Override
	public Container getContent() {
		return worldContent;
	}

	@Override
	public String getName() {
		return configuration.name() + " Preview";
	}

	@Override
	public Point getDefaultLocation() {
		return new Point(5,5);
	}

	@Override
	public Dimension getSize() {
		return new Dimension(400, 400);
	}

	@Override
	public simbad.sim.World getSimbadWorld() {
		return simbadWorld;
	}
	
	@Reference(dynamic=true)
	public void setApplicationWindow(ApplicationWindow applicationWindow){
		this.applicationWindow = applicationWindow;
	}
	
	public void unsetApplicationWindow(ApplicationWindow applicationWindow){
		this.applicationWindow = null;
	}
	
	@Reference(dynamic=true)
	public void setEventAdmin(EventAdmin eventAdmin){
		this.eventAdmin = eventAdmin;
	}
	
	public void unsetEventAdmin(EventAdmin eventAdmin){
		this.eventAdmin = null;
	}
	
	private void sendWorldChangeViewPointEvent(){
		Map<String, Object> properties = new HashMap<String, Object>();
		Event changeEvent = new Event(Events.CHANGE_WORLD_VIEWPOINT_EVENT, properties);
		eventAdmin.sendEvent(changeEvent);
	}

	@Override
	public Collection<Agent> getAgents() {
		return agents;
	}

	@Override
	public void refreshedWorldContent() {
		simbadWorld.stopRendering();
		simbadWorld.startRendering();
		sendWorldChangedEvent();
	}

	private void sendWorldChangedEvent() {
		Map<String, Object> properties = new HashMap<String, Object>();
		Event worldChangedEvent = new Event(World.Events.WORLD_CHANGED_EVENT, properties);
		eventAdmin.sendEvent(worldChangedEvent);
	}

}
