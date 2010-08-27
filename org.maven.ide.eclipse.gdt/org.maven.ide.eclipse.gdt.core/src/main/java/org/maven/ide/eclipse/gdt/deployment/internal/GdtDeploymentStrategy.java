package org.maven.ide.eclipse.gdt.deployment.internal;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.maven.ide.eclipse.gdt.deployment.ServerDeploymentStrategy;

 
public class GdtDeploymentStrategy implements ServerDeploymentStrategy {

	public void setupDeployment(IProject project) {
		// Don't remember what I wanted to do there
	}

	/**
	 * Setup Launch configuration to use maven generated folders. 
	 */
	public void setupLaunchConfiguration(IProject project) throws CoreException {
		//com.google.gdt.eclipse.suite.* packages not exported. that's really unfortunate
		//Can't use WebAppLaunchUtil
		
		//In order to setup the launch configuration, we need to :
		//- create a launch configuration if one doesn't exists (using project/artifactId name)
		//- use ${project.build.directory.finalName} (or something like that) as war folder (create folder if it doesn't exists)
		//- set "run built in server" to true
		//- set start URL to ...
		
		
	}

}
