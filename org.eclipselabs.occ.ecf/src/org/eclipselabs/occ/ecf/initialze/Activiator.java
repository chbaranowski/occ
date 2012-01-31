package org.eclipselabs.occ.ecf.initialze;

import org.eclipse.ecf.osgi.services.distribution.IDistributionConstants;
import org.eclipse.ecf.provider.jmdns.identity.JMDNSNamespace;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 *	Workaround to use ECF with bndtools to activate the lazy ECF Bundles.
 */
public class Activiator implements BundleActivator {

	JMDNSNamespace jmdnsNamespace;
	
	IDistributionConstants distributionConstants;
	
	@Override
	public void start(BundleContext context) throws Exception {
		System.out.println("Start initialze the lazy ECF Bundles.");
		distributionConstants = new IDistributionConstants() {};
		jmdnsNamespace = new JMDNSNamespace();
		System.out.println("Initialize the ECF Bundles (activate the bundles).");
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		jmdnsNamespace = null;
		distributionConstants = null;
	}

}
