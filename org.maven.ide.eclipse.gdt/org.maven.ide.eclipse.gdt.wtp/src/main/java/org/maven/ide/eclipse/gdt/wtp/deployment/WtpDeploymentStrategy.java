package org.maven.ide.eclipse.gdt.wtp.deployment;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jst.server.generic.servertype.definition.Module;
import org.eclipse.wst.server.core.IModule;
import org.eclipse.wst.server.core.IServer;
import org.eclipse.wst.server.core.ServerUtil;
import org.eclipse.wst.server.core.util.PublishAdapter;
import org.maven.ide.eclipse.gdt.deployment.ServerDeploymentStrategy;

public class WtpDeploymentStrategy implements ServerDeploymentStrategy {

	public void setupDeployment(IProject project) {
		// TODO Auto-generated method stub
		
	}

	public void setupLaunchConfiguration(IProject project) {

		IModule module = ServerUtil.getModule(project);
		if (module == null) return;
		
		IServer[] servers = ServerUtil.getServersByModule(module, new NullProgressMonitor());
		if (servers == null || servers.length == 0) return;
		
		IServer server = servers[0];
		System.err.println(server.getName());
		//server.addServerListener(arg0)
		
		
		// TODO Auto-generated method stub
	}

}
