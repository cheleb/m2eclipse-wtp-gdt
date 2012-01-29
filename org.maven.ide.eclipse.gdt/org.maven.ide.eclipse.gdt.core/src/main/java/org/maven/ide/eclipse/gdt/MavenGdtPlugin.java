package org.maven.ide.eclipse.gdt;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The activator class controls the plug-in life cycle
 */
public class MavenGdtPlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.maven.ide.eclipse.gdt";

	private static final Logger LOGGER = LoggerFactory.getLogger(MavenGdtPlugin.class);
	
	// The shared instance
	private static MavenGdtPlugin plugin;
	
	/**
	 * The constructor
	 */
	public MavenGdtPlugin() {
	}

	/**
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context)  throws GdtException {
		try {
            super.start(context);
        } catch (Exception e) {
            throw new GdtException(e.getLocalizedMessage(), e);
        }
		plugin = this;
		LOGGER.info("Starting MavenGdtPlugin");
	}

	/**
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws GdtException {
	    LOGGER.info("Stopping MavenGdtPlugin");
		plugin = null;
		try {
            super.stop(context);
        } catch (Exception e) {
            throw new GdtException(e.getLocalizedMessage(), e);
        }
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static MavenGdtPlugin getDefault() {
		return plugin;
	}

}
