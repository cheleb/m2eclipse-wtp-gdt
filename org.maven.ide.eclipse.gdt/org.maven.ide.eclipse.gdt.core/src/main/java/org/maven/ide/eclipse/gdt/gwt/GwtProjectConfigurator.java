package org.maven.ide.eclipse.gdt.gwt;

import org.apache.maven.project.MavenProject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.maven.ide.eclipse.gdt.AbstractGdtProjectConfigurator;
import org.maven.ide.eclipse.gdt.deployment.ServerDeploymentManager;
import org.maven.ide.eclipse.gdt.deployment.ServerDeploymentStrategy;

import com.google.gdt.eclipse.core.sdk.AbstractSdk;
import com.google.gwt.eclipse.core.nature.GWTNature;
import com.google.gwt.eclipse.core.runtime.GWTRuntime;

/**
 * Google Web Toolkit Project configurator.
 * 
 * @author Fred Bricon
 */
@SuppressWarnings("restriction")
public class GwtProjectConfigurator extends AbstractGdtProjectConfigurator {

	private static final String GWT_MAVEN_PLUGIN = "org.codehaus.mojo:gwt-maven-plugin";
	
	@Override
	protected boolean isConfigurable(MavenProject mavenProject) {
		return mavenProject.getPlugin(GWT_MAVEN_PLUGIN) != null;
	}

	@Override
	protected void configureNature(IProject project) throws CoreException {
		if (!GWTNature.isGWTProject(project)) {
			console.logMessage("Adding GWT nature to " + project.getName());
			GWTNature.addNatureToProject(project);
		}
	}

	@Override
	protected void configureDeploymentSettings(IProject project,
			MavenProject mavenProject) throws CoreException {
		ServerDeploymentStrategy deployment = ServerDeploymentManager.createDeployment(project);
		//Setup deployment to server
		deployment.setupDeployment(project);
		//Setup launch configuration 
		deployment.setupLaunchConfiguration(project);
	}

	@Override
	protected AbstractSdk findSDK(IJavaProject javaProject)
			throws JavaModelException {
		return GWTRuntime.findSdkFor(javaProject);
	}

	
}
