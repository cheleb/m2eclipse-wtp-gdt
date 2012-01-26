package org.maven.ide.eclipse.gdt;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.osgi.service.prefs.BackingStoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gdt.eclipse.suite.GdtPlugin;

/**
 * Temporary class to bypass Gdk API lock.
 * @author olivier.nouguier@gmail.com
 *
 */
public class GdtLockedAPIHelper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GdtLockedAPIHelper.class);
	
	public static void setEncodedProblemSeverities(String encodedSeverities) {
		IEclipsePreferences configurationPreferences = getConfigurationPreferences();
		configurationPreferences.put("problemSeverities", encodedSeverities);
		flushPreferences(configurationPreferences);
	}

	
	private static IEclipsePreferences getConfigurationPreferences() {
		
		IEclipsePreferences configurationPrefs = ConfigurationScope.INSTANCE
				.getNode(GdtPlugin.PLUGIN_ID);
		return configurationPrefs;
	}

	private static void flushPreferences(IEclipsePreferences preferences) {
		try {
			preferences.flush();
		} catch (BackingStoreException e) {
			LOGGER.error("", e);
		}
	}

}
