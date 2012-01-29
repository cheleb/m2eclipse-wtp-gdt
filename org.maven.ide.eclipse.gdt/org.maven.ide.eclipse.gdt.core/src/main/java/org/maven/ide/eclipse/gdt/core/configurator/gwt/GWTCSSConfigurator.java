package org.maven.ide.eclipse.gdt.core.configurator.gwt;

import org.apache.maven.plugin.MojoExecution;
import org.eclipse.m2e.core.lifecyclemapping.model.IPluginExecutionMetadata;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.configurator.AbstractBuildParticipant;
import org.eclipse.m2e.jdt.AbstractJavaProjectConfigurator;
import org.maven.ide.eclipse.gdt.core.buildpartcipant.gwt.GWTCSSBuildParticipant;

/**
 * Google Web Toolkit Project configurator.
 * 
 * @author Olivier NOUGUIER olivier.nouguier@gmail.com
 */

public class GWTCSSConfigurator extends AbstractJavaProjectConfigurator {

    @Override
    public AbstractBuildParticipant getBuildParticipant(IMavenProjectFacade projectFacade, MojoExecution execution,
            IPluginExecutionMetadata executionMetadata) {

        return new GWTCSSBuildParticipant(execution);

    }

    @Override
    protected String getOutputFolderParameterName() {
        return GWTConfiguratorConstants.GENERATE_DIRECTORY;
    }

}
