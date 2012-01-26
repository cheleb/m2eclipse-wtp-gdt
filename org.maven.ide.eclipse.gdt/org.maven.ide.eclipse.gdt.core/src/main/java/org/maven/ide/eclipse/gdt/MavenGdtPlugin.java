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
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		LOGGER.info("Starting MavenGdtPlugin");
	}

	/**
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
	    LOGGER.info("Stopping MavenGdtPlugin");
		plugin = null;
		super.stop(context);
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
