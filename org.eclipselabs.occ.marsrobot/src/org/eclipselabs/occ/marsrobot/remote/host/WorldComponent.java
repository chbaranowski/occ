package org.eclipselabs.occ.marsrobot.remote.host;

import java.io.IOException;
import java.util.Dictionary;

import org.eclipselabs.occ.marsrobot.remote.WorldService;
import org.osgi.framework.BundleContext;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@Component(
	immediate = true, 
	properties = { 
		"service.exported.interfaces=*",
		"service.exported.configs=ecf.generic.server" 
})
public class WorldComponent implements WorldService {

	private static final String WALL_PID = "org.eclipselabs.occ.marsrobot.wall.WallComponent";
	ConfigurationAdmin configurationAdmin;
	private String bundleLocation;

	@Activate
	public void activate(BundleContext context){
		bundleLocation = context.getBundle().getLocation();
	}
	
	@Reference
	public void setConfigurationAdmin(ConfigurationAdmin configurationAdmin) {
		this.configurationAdmin = configurationAdmin;
	}

	@Override
	public void createWall(int x, int y, int z, int length, int height,
			int rotate) {
		System.out.println("Service creates new wall using ConfigurationAdmin");
		try {
			Configuration configuration = configurationAdmin.createFactoryConfiguration(WALL_PID, bundleLocation);
			configuration.update();
			System.out.println("PID is: " + configuration.getPid());
			
			@SuppressWarnings("unchecked")
			Dictionary<String, Object> wallProps = configuration.getProperties();
			wallProps.put("x", x);
			wallProps.put("y", y);
			wallProps.put("z", z);
			wallProps.put("length", length);
			wallProps.put("height", height);
			wallProps.put("rotate", rotate);
			configuration.update(wallProps);
			
			System.out.println("Service created new wall");
		} catch (IOException e) {
			System.out.println("Error on creating configuration for Wall.");
			e.printStackTrace();
		}
	}

}
