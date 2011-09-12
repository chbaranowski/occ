package org.eclipselabs.occ.marsrobot.provisioning;


import java.io.File;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleException;

public class DirectoryWatcher implements Runnable {
	private BundleInstaller bundleInstaller;
	private Logger logger = Logger.getLogger();
	
	private String pickupFolder;

	public DirectoryWatcher(BundleInstaller bundleInstaller) {
		this.bundleInstaller = bundleInstaller;
	}

	public void run() {
		try {
			while (true) {
				List<URL> bundleUrls = listBundles();
				bundleUrls = filterNewBundles(bundleUrls);
				for (URL bundleUrl : bundleUrls) {
					Bundle bundle = null;
					try {
						logger.logDebug("install bundle : ", bundleUrl);
						bundle = bundleInstaller.installBundle(bundleUrl);
						logger.logDebug("bundle installed. ", bundleUrl,
								" as ", bundle.getSymbolicName());
					} catch (BundleException e) {
						logger.logError("Bundle couldn't be installed.");
					}
					if (bundle != null) {
						try {
							logger.logDebug("start bundle : ",
									bundle.getSymbolicName());
							bundleInstaller.startBundle(bundle);
							logger.logDebug("bundle started. ",
									bundle.getSymbolicName());
						} catch (BundleException e) {
							logger.logError("Bundle couldn't be installed.");
						}
					}
				}
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			logger.logError("FileInstaller is interrupted.");
		}
	}

	private List<URL> filterNewBundles(List<URL> bundleUrls) {
		List<URL> newBundleUrls = new ArrayList<URL>();
		for (URL bundleUrl : bundleUrls) {
			if (!bundleInstaller.isAllreadyInstalled(bundleUrl)) {
				newBundleUrls.add(bundleUrl);
			}
		}
		return newBundleUrls;
	}

	public List<URL> listBundles() {
		File directory = new File(pickupFolder);
		prepareDirectory(directory);

		String[] bundles = directory.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".jar");
			}
		});
		
		List<URL> bundleUrls = new ArrayList<URL>();
		for (String bundle : bundles) {
			try {
				File file = new File(directory, bundle);
				if (!file.isDirectory()) {
					bundleUrls.add(file.toURI().toURL());
				}
			} catch (MalformedURLException e) {
				logger.logError("Could not find any Bundles.");
			}
		}

		return bundleUrls;

	}

	private void prepareDirectory(File dir) {
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				throw new RuntimeException("Can't create folder " + dir);
			}
			logger.logDebug("Created directory ", dir.getAbsolutePath());
		}
		if (!dir.isDirectory()) {
			throw new RuntimeException(dir + " is not a directory");
		}
	}

	public String getPickupFolder() {
		return pickupFolder;
	}

	public void setPickupFolder(String pickupFolder) {
		this.pickupFolder = pickupFolder;
	}

}
