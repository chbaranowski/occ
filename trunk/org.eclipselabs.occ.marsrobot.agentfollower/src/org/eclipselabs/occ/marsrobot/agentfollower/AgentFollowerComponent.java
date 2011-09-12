package org.eclipselabs.occ.marsrobot.agentfollower;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.eclipselabs.occ.marsrobot.desktop.DesktopWindow;
import org.eclipselabs.occ.marsrobot.world.World;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import simbad.gui.AgentFollower;
import simbad.sim.Agent;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;
import aQute.bnd.annotation.component.Reference;

@Component(immediate = true, 
	properties = {
		"event.topics=" + World.Events.WORLD_EVENT_GROUP + "*"
	}
)
public class AgentFollowerComponent implements DesktopWindow, EventHandler {

	JPanel contentPanel;
	
	World world;
	
	Map<Agent, JButton> agentToAgentFollowerButton = new HashMap<Agent, JButton>();
	
	AgentFollower agentFollower;
	
	public AgentFollowerComponent() {
		initialize();
	}

	protected void initialize() {
		contentPanel = new JPanel();
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.X_AXIS));
	}

	@Activate
	public void activate() {
		Collection<Agent> agents = world.getAgents();
		for (Agent agent : agents) {
			addAgentFollowerControl(agent);
		}
	}

	private void addAgentFollowerControl(final Agent agent) {
		contentPanel.setVisible(false);
		JButton agentFollowerButton = new JButton("Follow " + agent.getName());
		agentFollowerButton.setAlignmentX(java.awt.Component.LEFT_ALIGNMENT);
		agentFollowerButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				suspendAgentFollower();
				agentFollower = new AgentFollower(world.getSimbadWorld(), agent);
				agentFollower.setViewPointType(simbad.sim.World.VIEW_AGENT_SIDE);
				agentFollower.resume();
			}
		});
		contentPanel.add(agentFollowerButton);
		agentToAgentFollowerButton.put(agent, agentFollowerButton);
		contentPanel.setVisible(true);
	}
	
	private void suspendAgentFollower() {
		if(agentFollower != null){
			agentFollower.suspend();
		}
	}

	@Deactivate
	public void deactivate() {
		suspendAgentFollower();
		Collection<Agent> agents = world.getAgents();
		for (Agent agent : agents) {
			removeAgentFollowerControl(agent);
		}
	}

	private void removeAgentFollowerControl(Agent agent) {
		contentPanel.setVisible(false);
		if(agentFollower != null && agentFollower.agent.equals(agent)){
			suspendAgentFollower();
		}
		JButton button = agentToAgentFollowerButton.get(agent);
		contentPanel.remove(button);
		contentPanel.repaint();
		contentPanel.setVisible(true);
	}

	@Override
	public Container getContent() {
		return contentPanel;
	}

	@Override
	public String getName() {
		return "Follow ";
	}

	@Override
	public Point getDefaultLocation() {
		return new Point(5, 405);
	}

	@Override
	public Dimension getSize() {
		return new Dimension(400, 100);
	}

	@Reference(dynamic = true)
	public void setWorld(World world) {
		this.world = world;
	}
	
	public void unsetWorld(World world) {
		this.world = null;
	}

	@Override
	public void handleEvent(Event event) {
		if(event.getTopic().equals(World.Events.ADD_AGENT_EVENT)){
			Agent agent = (Agent) event.getProperty("agent");
			addAgentFollowerControl(agent);
		}
		else if(event.getTopic().equals(World.Events.REMOVE_AGENT_EVENT)){
			Agent agent = (Agent) event.getProperty("agent");
			removeAgentFollowerControl(agent);
		}
		else if(event.getTopic().equals(World.Events.CHANGE_WORLD_VIEWPOINT_EVENT)){
			suspendAgentFollower();
		}
	}

}
