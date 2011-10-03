package org.eclipselabs.occ.marsrobot.remoteextender.service.cxf;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;

import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

import org.eclipselabs.occ.marsrobot.remoteextender.service.api.WallService;

/**
 * Implements adding new walls to the world for remote services by exporting a
 * SOAP interface using Apache CXF.
 */
@Component(properties = {
		"service.exported.interfaces=*",
		"service.exported.configs=org.apache.cxf.ws",
		"org.apache.cxf.ws.address=http://localhost:9090/wallservice" })
public class WallServiceImpl implements WallService {

	ConfigurationAdmin configurationAdmin;

	@Reference
	public void setConfigurationAdmin(ConfigurationAdmin configurationAdmin) {
		this.configurationAdmin = configurationAdmin;
	}

	@Override
	public void createWall(
			int x,
			int y,
			int z,
			int length,
			int height,
			int rotate) throws Exception {
		System.out.println("Service creates new wall using ConfigurationAdmin");

		Configuration configuration = getFactoryConfiguration();
		System.out.println("PID is: " + configuration.getPid());

		Dictionary<String, Integer> wallProperties = toDictionary(x, y, z,
				length, height, rotate);
		configuration.update(wallProperties);

		System.out.println("Service created new wall");
	}

	private Configuration getFactoryConfiguration() throws IOException {
		Configuration configuration = configurationAdmin
				.createFactoryConfiguration(
						"org.eclipselabs.occ.marsrobot.wall.WallComponent",
						null);
		return configuration;
	}

	private Dictionary<String, Integer> toDictionary(int x, int y, int z,
			int length, int height, int rotate) {
		Dictionary<String, Integer> wallProps = new Hashtable<String, Integer>();
		wallProps.put("x", x);
		wallProps.put("y", y);
		wallProps.put("z", z);
		wallProps.put("length", length);
		wallProps.put("height", height);
		wallProps.put("rotate", rotate);
		return wallProps;
	}

}
