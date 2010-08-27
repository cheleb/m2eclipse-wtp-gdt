package org.maven.ide.eclipse.gdt.wtp.deployment;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.jst.server.tomcat.core.internal.TomcatServer;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.IServerListener;
import org.eclipse.wst.server.core.ServerEvent;
import org.eclipse.wst.server.core.internal.Server;
import org.eclipse.wst.server.core.model.ServerDelegate;

import com.google.gwt.eclipse.core.nature.GWTNature;

@SuppressWarnings("restriction")
public class M2EServerListener implements IServerListener {

	public void serverChanged(ServerEvent event) {
		IServer server = event.getServer();
		int eventKind = event.getKind();
		System.out.println("M2EServerListener :" + server.getName()
				+ " content changed " + eventKind + " publish state "
				+ event.getPublishState());

		if ((eventKind & ServerEvent.MODULE_CHANGE) != 0) {
			IModule[] modules = event.getModule();
			if (modules == null)
				return;

			String serverDeploymentDir = getServerDeploymentDirectory(server);
			if (serverDeploymentDir == null) return;
			
			System.out.println(server.getName() + " : " + serverDeploymentDir);

			for (IModule module : modules) {
				IProject project = module.getProject();
				if (isTweakable(project)) {
					try {
						updateLaunchConfig(project, server);
					} catch (CoreException e) {
						e.printStackTrace();
					}
				}
			}

		}
	}

	private String getServerDeploymentDirectory(IServer server) {
		ServerDelegate delegate = (ServerDelegate)server.loadAdapter(ServerDelegate.class, new NullProgressMonitor());
		if (delegate instanceof TomcatServer) {
			TomcatServer tomcat = (TomcatServer) delegate;
			return tomcat.getServerDeployDirectory().toOSString();
		}
		return null;
	}

	private boolean isTweakable(IProject project) {
		return project != null && GWTNature.isGWTProject(project);
	}

	private void updateLaunchConfig(IProject project, IServer server)
			throws CoreException {
		System.out.println(project.getName() + " configuration must be changed");
		ILaunchConfiguration config = server.getLaunchConfiguration(true, new NullProgressMonitor());
		ILaunchConfiguration lwc = config.getWorkingCopy();
	}

}
