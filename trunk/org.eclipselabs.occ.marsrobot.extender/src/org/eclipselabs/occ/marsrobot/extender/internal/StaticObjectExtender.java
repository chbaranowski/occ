package org.eclipselabs.occ.marsrobot.extender.internal;

import java.util.HashMap;
import java.util.Map;

import org.eclipselabs.occ.marsrobot.extender.StaticObject;
import org.eclipselabs.occ.marsrobot.world.World;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.util.tracker.BundleTracker;
import org.osgi.util.tracker.BundleTrackerCustomizer;

import simbad.sim.BaseObject;

public class StaticObjectExtender implements BundleTrackerCustomizer {

	final World world;

	final BundleTracker bundleTracker;

	final Map<Bundle, BaseObject> bundleToStaticObject = new HashMap<Bundle, BaseObject>();

	public StaticObjectExtender(World world, BundleContext context) {
		this.world = world;
		this.bundleTracker = new BundleTracker(context, Bundle.ACTIVE, this);
	}

	@Override
	public Object addingBundle(Bundle bundle, BundleEvent event) {
		Object staticObjectClassName = bundle.getHeaders().get("StaticObject-Class");
		if (staticObjectClassName != null) {
			try {
				Class<?> loadClass = bundle.loadClass(staticObjectClassName.toString());
				Object obj = loadClass.newInstance();
				if (obj instanceof StaticObject) {
					StaticObject staticObject = (StaticObject) obj;
					BaseObject staticBaseObject = staticObject.createStaticSimbadObject(world.getEnvironmentDescription());
					bundleToStaticObject.put(bundle, staticBaseObject);
					world.addObject(staticBaseObject);
					return bundle;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	@Override
	public void modifiedBundle(Bundle bundle, BundleEvent event, Object object) {
		removedBundle(bundle, event, object);
		addingBundle(bundle, event);
	}

	@Override
	public void removedBundle(Bundle bundle, BundleEvent event, Object object) {
		BaseObject baseObject = bundleToStaticObject.remove(bundle);
		if (baseObject != null) {
			world.detach(baseObject);
		}
	}

	public void close() {
		bundleTracker.close();
	}

	public void open() {
		bundleTracker.open();
	}

}
