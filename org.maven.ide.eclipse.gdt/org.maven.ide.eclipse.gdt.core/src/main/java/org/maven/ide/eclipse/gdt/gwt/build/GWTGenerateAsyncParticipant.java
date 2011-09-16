package org.maven.ide.eclipse.gdt.gwt.build;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.maven.plugin.MojoExecution;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.m2e.core.lifecyclemapping.model.IPluginExecutionMetadata;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gdt.eclipse.core.JavaASTUtils;

public class GWTGenerateAsyncParticipant extends GWTBuildParticipant {

    private static final Logger LOGGER = LoggerFactory.getLogger(GWTGenerateAsyncParticipant.class);

    public GWTGenerateAsyncParticipant(IMavenProjectFacade projectFacade, MojoExecution execution,
            IPluginExecutionMetadata executionMetadata) {
        super(execution, true);
    }

    /**
     * Return the absolute path for searching (relative) resources. Only java
     * 
     * @return
     */
    @Override
    protected List<IPath> getStatedFolders() {
        List<IPath> iPaths = new ArrayList<IPath>();
        Collections.addAll(iPaths, getMavenProjectFacade().getCompileSourceLocations());
        return iPaths;
    }

    @SuppressWarnings("restriction")
    @Override
    protected boolean fileConcerned(File statedFolder, String file) {
        if (file.endsWith(".java")) {
            String qualifiedName = file.substring(0, file.lastIndexOf(".")).replaceAll("/|\\\\", ".");
            try {
                ITypeBinding binding = JavaASTUtils.resolveType(JavaCore.create(getMavenProjectFacade().getProject()), qualifiedName);
                if(binding==null) {
                    LOGGER.error("Could not resolve: " + qualifiedName);
                    return false;
                }
                if(JavaASTUtils.findTypeInHierarchy(binding, "com.google.gwt.user.client.rpc.RemoteService") != null ) {
                    LOGGER.debug(file + " triggers the build");
                    return true;
                }
                
            } catch (JavaModelException e) {
                LOGGER.error("Could not find type for: " + file, e);
            }

        }
        return false;
    }

}
