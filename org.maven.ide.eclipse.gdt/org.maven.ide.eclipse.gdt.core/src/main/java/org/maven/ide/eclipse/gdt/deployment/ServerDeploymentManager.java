package org.maven.ide.eclipse.gdt.deployment;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.maven.ide.eclipse.gdt.deployment.internal.GdtDeploymentStrategy;

/**
 * Manages server deployment settings.
 *  
 * @author Fred Bricon
 *
 */
public class ServerDeploymentManager {
	
	private ServerDeploymentManager() {
		//No instantiation allowed
	}
	
	/**
	 * Factory method for server deployment strategies.
	 * 
	 * @param project
	 * @return the deployment strategy associated with the project
	 */
	public static ServerDeploymentStrategy createDeployment(IProject project){
		//TODO Should use the appropriate deployment strategy, as defined in the project preferences
		//Strategies should be defined as extension points (for WTP for ex.)
		//Defaut strategy would use GDT built-in server and maven build output
		return new GdtDeploymentStrategy();
	}
	
	/*
	public static List<ServerDeploymentStrategy> getDeploymentStrategies(){
		//TODO retrieve strategies from extension points
		ServerDeploymentStrategy defaultStrategy = new GdtDeploymentStrategy();
		return Arrays.asList(defaultStrategy);
	}
	*/
	
}
