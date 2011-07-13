package org.maven.ide.eclipse.gdt.gwt;

import org.apache.maven.plugin.MojoExecution;
import org.apache.maven.project.MavenProject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.m2e.core.lifecyclemapping.model.IPluginExecutionMetadata;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.configurator.AbstractBuildParticipant;
import org.eclipse.m2e.core.project.configurator.AbstractProjectConfigurator;
import org.eclipse.m2e.core.project.configurator.ProjectConfigurationRequest;
import org.eclipse.m2e.jdt.AbstractJavaProjectConfigurator;
import org.maven.ide.eclipse.gdt.AbstractGdtProjectConfigurator;
import org.maven.ide.eclipse.gdt.deployment.ServerDeploymentManager;
import org.maven.ide.eclipse.gdt.deployment.ServerDeploymentStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gdt.eclipse.core.sdk.AbstractSdk;
import com.google.gwt.eclipse.core.nature.GWTNature;
import com.google.gwt.eclipse.core.runtime.GWTRuntime;

/**
 * Google Web Toolkit I18N Project configurator.
 * 
 * @author Olivier NOUGUIER
 */
@SuppressWarnings("restriction")
public class GwtI18NProjectConfigurator extends AbstractJavaProjectConfigurator {

	private static final String GWT_MAVEN_PLUGIN = "org.codehaus.mojo:gwt-maven-plugin";

	private static final Logger LOGGER = LoggerFactory.getLogger(GwtI18NProjectConfigurator.class);

	
	@Override
	public AbstractBuildParticipant getBuildParticipant(
			IMavenProjectFacade projectFacade, MojoExecution execution,
			IPluginExecutionMetadata executionMetadata) {
		return new GWTI18NBuildParticipant(projectFacade, execution, executionMetadata);
	}
	
	
	@Override
	protected String getOutputFolderParameterName() {
		return "generateDirectory";
	}
	
	
	
	

}
