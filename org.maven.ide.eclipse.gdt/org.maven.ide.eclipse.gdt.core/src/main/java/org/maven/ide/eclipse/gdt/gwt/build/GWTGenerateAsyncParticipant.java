package org.maven.ide.eclipse.gdt.gwt.build;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.maven.plugin.MojoExecution;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.m2e.core.lifecyclemapping.model.IPluginExecutionMetadata;
import org.eclipse.m2e.core.project.IMavenProjectFacade;

public class GWTGenerateAsyncParticipant extends GWTBuildParticipant {

	public GWTGenerateAsyncParticipant(IMavenProjectFacade projectFacade,
			MojoExecution execution, IPluginExecutionMetadata executionMetadata) {
		super(execution, true);
	}

	
	


	@Override
	protected List<File> getResourcesToScan() throws CoreException {
		List<File> paths = new ArrayList<File>();
		IPath[] compileSourceLocations = getMavenProjectFacade().getCompileSourceLocations();
		for (int i = 0; i < compileSourceLocations.length; i++) {
			IPath iPath = compileSourceLocations[i];
			paths.add(getMavenProjectFacade().getProject().getFolder(iPath).getLocation().toFile());
		}
		return paths;
	}
	

}
