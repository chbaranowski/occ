package org.eclipselabs.occ.marsrobot.provisioning;

import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Deactivate;

@Component(immediate=true)
public class Provisioner implements BundleListener {

	private static final String PICKUP_DIRECTORY = "provisioning.pickup.directory";

	private Thread thread = null;

	private DirectoryWatcher watcher;

	private BundleInstaller bundleInstaller;

	private Logger logger = Logger.getLogger();
	
	@Activate
	public void start(BundleContext context, Map<String, Object> props) throws Exception {
		String pickupFolder = context.getProperty(PICKUP_DIRECTORY);
		if(pickupFolder != null) {
			this.bundleInstaller = new BundleInstaller(context);
			this.watcher = new DirectoryWatcher(bundleInstaller);
			this.watcher.setPickupFolder(pickupFolder);
			thread = new Thread(watcher);
			thread.start();
			context.addBundleListener(this);
			logger.logDebug("Start Provisioning Bundle");
		}
	}

	@Deactivate
	public void stop(BundleContext context) {
		context.removeBundleListener(this);
		thread.interrupt();
		try {
			thread.join();
		} catch (InterruptedException e) {
			logger.logError("couln't not join thread");
		}
		logger.logDebug("Stop Provisioning Bundle");
	}

	@Override
	public void bundleChanged(BundleEvent event) {
		if (event.getType() == BundleEvent.UNINSTALLED) {
			Bundle bundleToRemove = event.getBundle();
			bundleInstaller.removeBundle(bundleToRemove);
		}
	}

}

