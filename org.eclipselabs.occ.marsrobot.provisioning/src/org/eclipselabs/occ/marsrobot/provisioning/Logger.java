package org.eclipselabs.occ.marsrobot.provisioning;

import org.osgi.service.log.LogService;

import aQute.bnd.annotation.component.Component;
import aQute.bnd.annotation.component.Reference;

@Component(immediate=true)
public class Logger {
	
	LogService logService;

	private static Logger singeltonInstance;

	public static Logger getLogger() {
		if (singeltonInstance == null) {
			singeltonInstance = new Logger();
		}
		return singeltonInstance;
	}

	@Reference(dynamic=true)
	public void bindLogService(LogService service) {
		this.logService = service;
	}

	public void unbindLogService(LogService service) {
		if (this.logService == service) {
			this.logService = null;
		}
	}

	protected void log(int level, Object... messge) {
		StringBuilder sb = new StringBuilder();
		for (Object msg : messge) {
			sb.append(msg);
		}
		if (logService != null) {
			logService.log(level, sb.toString());
		} else {
			System.out.println(sb.toString());
		}
	}

	public void logDebug(Object... message) {
		log(LogService.LOG_DEBUG, message);
	}

	public void logError(Object... message) {
		log(LogService.LOG_ERROR, message);
	}

}

