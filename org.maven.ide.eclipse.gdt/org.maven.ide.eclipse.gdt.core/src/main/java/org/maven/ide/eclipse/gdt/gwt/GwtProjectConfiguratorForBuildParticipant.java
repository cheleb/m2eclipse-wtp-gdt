package org.maven.ide.eclipse.gdt.gwt;

import org.apache.maven.plugin.MojoExecution;
import org.eclipse.m2e.core.lifecyclemapping.model.IPluginExecutionMetadata;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.configurator.AbstractBuildParticipant;
import org.eclipse.m2e.jdt.AbstractJavaProjectConfigurator;
import org.maven.ide.eclipse.gdt.gwt.build.GWTGenerateAsyncParticipant;
import org.maven.ide.eclipse.gdt.gwt.build.GWTI18NBuildParticipant;

/**
 * Google Web Toolkit Project configurator.
 * 
 * @author Fred Bricon
 */

public class GwtProjectConfiguratorForBuildParticipant extends AbstractJavaProjectConfigurator {



	

	@Override
	public AbstractBuildParticipant getBuildParticipant(
			IMavenProjectFacade projectFacade, MojoExecution execution,
			IPluginExecutionMetadata executionMetadata) {
		if ("i18n".equals(execution.getGoal())) {
			return new GWTI18NBuildParticipant(projectFacade, execution,
					executionMetadata);
		} else if ("generateAsync".equals(execution.getGoal())) {
			return new GWTGenerateAsyncParticipant(projectFacade, execution,
					executionMetadata);
		}
		return null;
	}

	@Override
	protected String getOutputFolderParameterName() {
		return "generateDirectory";
	}

}
