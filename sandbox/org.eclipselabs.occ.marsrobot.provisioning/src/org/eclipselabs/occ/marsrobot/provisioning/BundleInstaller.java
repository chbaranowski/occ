package org.eclipselabs.occ.marsrobot.provisioning;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;

public class BundleInstaller {

	private BundleContext context;
	private Map<Bundle, URL> bundleToURL = new HashMap<Bundle, URL>();
	private Logger logger = Logger.getLogger();

	public BundleInstaller(BundleContext context) {
		this.context = context;
	}

	public Bundle installBundle(URL bundleUrl) throws BundleException {
		Bundle bundle = context.installBundle(bundleUrl.toExternalForm());
		if (bundle != null) {
			bundleToURL.put(bundle, bundleUrl);
		}
		return bundle;
	}

	public int startBundle(Bundle bundle) throws BundleException {
		if (bundle != null) {
			bundle.start();
			return bundle.getState();
		}
		return Bundle.UNINSTALLED;
	}

	public void removeBundle(Bundle bundleToRemove) {
		URL url = bundleToURL.get(bundleToRemove);
		if (url != null) {
			try {
				File fileToRemove = new File(url.toURI());
				if (fileToRemove.exists()) {
					fileToRemove.delete();
					bundleToURL.remove(bundleToRemove);
				}
			} catch (URISyntaxException e) {
				logger.logError("Unable to delete Bundle file ",
						bundleToRemove.getSymbolicName());
			}
		}
	}

	public boolean isAllreadyInstalled(URL bundleUrl) {
		String toFind = bundleUrl.toExternalForm();
		for (URL url : bundleToURL.values()) {
			if (url.toExternalForm().equals(toFind)) {
				return true;
			}
		}
		return false;
	}

}
