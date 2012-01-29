package org.maven.ide.eclipse.gdt.core.buildpartcipant.gwt;

import java.io.File;

import org.apache.maven.plugin.MojoExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GWTI18NBuildParticipant extends GWTBuildParticipant {

    private static final Logger LOGGER = LoggerFactory.getLogger(GWTI18NBuildParticipant.class);

    public GWTI18NBuildParticipant(MojoExecution execution) {
        super(execution, true);
    }

  
    @Override
    protected boolean fileConcerned(File statedFolder, String file) {
        if (file.endsWith(".properties")) {
            LOGGER.debug(file + " triggers the build");
            return true;
        }
        return false;
    }

}
