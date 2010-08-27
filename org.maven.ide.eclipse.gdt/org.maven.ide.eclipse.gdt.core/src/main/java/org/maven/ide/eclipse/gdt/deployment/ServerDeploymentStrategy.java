package org.maven.ide.eclipse.gdt.deployment;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;

/**
 * Management of deploymeent settings for web projects
 * 
 * @author Fred Bricon
 */
public interface ServerDeploymentStrategy {
	
	/**
	 * Manage server deployment settings for the given project 
	 * @param project
	 * @throws CoreException
	 */
	void setupDeployment(IProject project) throws CoreException;
	
	/**
	 * Creates and setup a server launch configuration for the project.
	 * @param project
	 * @throws CoreException
	 */ 
	 void setupLaunchConfiguration(IProject project) throws CoreException;
	
}
