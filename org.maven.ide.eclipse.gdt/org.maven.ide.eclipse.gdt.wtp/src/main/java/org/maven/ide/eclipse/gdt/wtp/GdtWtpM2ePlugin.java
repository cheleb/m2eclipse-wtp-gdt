package org.maven.ide.eclipse.gdt.wtp;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.ui.IStartup;
import org.eclipse.wst.server.core.ServerCore;
import org.maven.ide.eclipse.gdt.wtp.deployment.M2EServerLifecycleListener;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class GdtWtpM2ePlugin extends Plugin implements IStartup {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.maven.ide.eclipse.gdt.wtp";

	// The shared instance
	private static GdtWtpM2ePlugin plugin;
	
	private M2EServerLifecycleListener listener;
	
	/**
	 * The constructor
	 */
	public GdtWtpM2ePlugin() {
		super();
	}

	/**
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		listener = new M2EServerLifecycleListener();
		listener.initializeServers();
		ServerCore.addServerLifecycleListener(listener);
	}

	/**
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		System.out.println("stopping Activator");
		plugin = null;
		if (listener != null) {//Could it be null for some reason?
			ServerCore.removeServerLifecycleListener(listener);
			listener.finalizeServers();
			listener = null;
		}
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static GdtWtpM2ePlugin getDefault() {
		return plugin;
	}

	public void earlyStartup() {
		// No need to implement anything here.
	}

}
