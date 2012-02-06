package org.eclipselabs.occ.derby;

import org.osgi.framework.Bundle;
import org.osgi.service.packageadmin.PackageAdmin;

import aQute.bnd.annotation.component.Activate;
import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@Component
public class DerbyInitializer {

	private PackageAdmin packageAdmin;

	@Reference
	public void setPackageAdmin(PackageAdmin packageAdmin) {
		this.packageAdmin = packageAdmin;
	}
	
	public void unsetPackageAdmin(PackageAdmin packageAdmin) {
		this.packageAdmin = null;
	}
	
	@Activate
	public void activate() throws Exception {
		Bundle[] derbyDbAccessBundles = packageAdmin.getBundles("org.eclipse.gemini.dbaccess.derby", null);
		Bundle derbyDbAccessBundle = derbyDbAccessBundles[0];
		derbyDbAccessBundle.start();
	}
	
}
