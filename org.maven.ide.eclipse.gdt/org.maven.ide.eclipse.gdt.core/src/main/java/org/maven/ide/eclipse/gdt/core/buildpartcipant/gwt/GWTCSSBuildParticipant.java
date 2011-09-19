package org.maven.ide.eclipse.gdt.core.buildpartcipant.gwt;

import java.io.File;

import org.apache.maven.plugin.MojoExecution;
import org.eclipse.m2e.core.lifecyclemapping.model.IPluginExecutionMetadata;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GWTCSSBuildParticipant extends GWTBuildParticipant {

    private static final Logger LOGGER = LoggerFactory.getLogger(GWTCSSBuildParticipant.class);

    public GWTCSSBuildParticipant(IMavenProjectFacade projectFacade, MojoExecution execution, IPluginExecutionMetadata executionMetadata) {
        super(execution, true);
    }

  
    @Override
    protected boolean fileConcerned(File statedFolder, String file) {
        if (file.endsWith(".css")) {
            LOGGER.debug(file + " triggers the build");
            return true;
        }
        return false;
    }

}
