package org.eclipselabs.occ.marsrobot.vaadin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.felix.service.command.CommandProcessor;
import org.eclipselabs.occ.marsrobot.commands.RobotCommand;
import org.eclipselabs.occ.marsrobot.robot.Robot;
import org.osgi.framework.ServiceReference;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.Command;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

/**
 * 
 * @author osgi
 *
 */
@Component(factory = "vaadin.app", name = "org.eclipselabs.occ.marsrobot.vaadin")
public class MainApplication extends Application {

	private Window main;
	private VerticalLayout mainLayout;
	final Panel contentPanel = new Panel();
	private Table table = new Table();

	private Map<String, RobotCommand> commandRobot = Collections.synchronizedMap(new HashMap<String, RobotCommand>());
	private Map<String, Method> commands = Collections.synchronizedMap(new HashMap<String, Method>());
	private Collection<Robot> robots = Collections.synchronizedCollection(new ArrayList<Robot>());

	@Override
	public void init() {
		main = new Window("MarsRobot Vaadin Application");
		mainLayout = (VerticalLayout) main.getContent();
		mainLayout.setMargin(false);
		// mainLayout.setStyleName("blue");
		setMainWindow(main);

		mainLayout.setSizeFull();
		mainLayout.addComponent(getMenu());

		HorizontalLayout content = new HorizontalLayout();
		contentPanel.setSizeFull();
		content.setSizeFull();
		content.addComponent(contentPanel);

		mainLayout.addComponent(content);
		mainLayout.setExpandRatio(content, 1);

		initTable();
	}

	@SuppressWarnings("serial")
	private MenuBar getMenu() {
		MenuBar menubar = new MenuBar();
		menubar.setWidth("100%");
		MenuItem homeMenu = menubar.addItem("Home", null);

		homeMenu.addItem("Show Commands", new Command() {
			@Override
			public void menuSelected(MenuItem selectedItem) {
				getMainWindow().showNotification("Show Commands");
			}
		});

		return menubar;
	}

	/**
	 * Create table with
	 * CommandName | Robot Combobox | Parameter TextField | Execute Button
	 */
	private void initTable() {
		table.setSizeFull();
		table.setSelectable(true);
		table.addContainerProperty("Commandname", String.class, null);
		table.addContainerProperty("Robots", ComboBox.class, null);
		table.addContainerProperty("Parameter", TextField.class, null);
		table.addContainerProperty("Execute", Button.class, null);
		contentPanel.addComponent(table);
		updateCommandTable();
	}

	private void updateCommandTable() {
		for (String oneMethodName : commands.keySet()) {
			final Method method = commands.get(oneMethodName);
			final ComboBox robotBox = new ComboBox("Robots");
			for (Robot r : robots) {
				robotBox.addItem(r.getName());
			}

			final TextField field = new TextField();
			final RobotCommand cmd = commandRobot.get(oneMethodName);
			Button button = new Button("Execute");
			button.addListener(new Button.ClickListener() {

				@Override
				public void buttonClick(ClickEvent event) {

					String paramsList = (String) field.getValue();
					String[] params = paramsList.split(" ");
					try {
						method.invoke(cmd, transformParameters(robotBox.getValue().toString(), method, params));
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
				}
			});
			table.addItem(new Object[] { oneMethodName, robotBox, field, button }, oneMethodName);
		}
	}

	/**
	 * First method parameter is always method name
	 * 
	 * @param robotName - first method parameter
	 * @param method
	 * @param params
	 * @return
	 */
	private Object[] transformParameters(String robotName, Method method, String[] params) {
		if (params == null || params.length == 0)
			return new Object[0];
		Class<?>[] types = method.getParameterTypes();

		Object[] values = new Object[types.length];
		values[0] = robotName;
		for (int i = 1; i < types.length; i++) {
			Class<?> oneParameterType = types[i];
			if (oneParameterType.equals(String.class)) {
				values[i] = params[i-1];
			} else if (oneParameterType.equals(Double.class) || oneParameterType.equals(double.class)) {
				values[i] = Double.valueOf(params[i-1]);
			} else if (oneParameterType.equals(Integer.class) || oneParameterType.equals(int.class)) {
				values[i] = Integer.valueOf(params[i-1]);
			} else if (oneParameterType.equals(Long.class) || oneParameterType.equals(long.class)) {
				values[i] = Long.valueOf(params[i-1]);
			} else if (oneParameterType.equals(Boolean.class) || oneParameterType.equals(boolean.class)) {
				values[i] = Boolean.valueOf(params[i-1]);
			}
		}
		return values;
	}

	@Reference(multiple = true, optional = false, dynamic = true)
	public void addRobotCommand(RobotCommand cmd, Map properties) {
		String[] methodNames = (String[]) properties.get(CommandProcessor.COMMAND_FUNCTION);

		Map<String, Method> methodsByName = new HashMap<String, Method>();
		Method[] allMethods = cmd.getClass().getMethods();
		for (Method method : allMethods) {
			methodsByName.put(method.getName(), method);
		}
		for (String oneMethodName : methodNames) {
			commands.put(oneMethodName, methodsByName.get(oneMethodName));
			commandRobot.put(oneMethodName, cmd);
		}
		updateCommandTable();
	}

	public void removeRobotCommand(RobotCommand cmd) {
		commands.remove(cmd);
		updateCommandTable();
	}

	@Reference(dynamic = true, multiple = true, optional = true)
	public void addRobot(Robot robot) {
		robots.add(robot);
		updateCommandTable();
	}

	public void removeRobot(Robot robot) {
		robots.remove(robot);
		updateCommandTable();
	}
}
