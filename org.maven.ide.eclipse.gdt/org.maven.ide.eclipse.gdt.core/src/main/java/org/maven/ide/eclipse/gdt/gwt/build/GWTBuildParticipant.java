package org.maven.ide.eclipse.gdt.gwt.build;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.maven.plugin.MojoExecution;
import org.codehaus.plexus.util.Scanner;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.eclipse.m2e.core.project.configurator.MojoExecutionBuildParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.plexus.build.incremental.BuildContext;

/**
 * Common participant feature, child class have to implement: 
 *  <li> {@link GWTBuildParticipant#getResourcesToScan()} <li>
 *  And might implement:
 *  <li> {@link GWTBuildParticipant#getSearchPath()} </li>
 * @author Olivier NOUGUIER olivier.nouguier@gmail.com
 *
 */
public abstract class GWTBuildParticipant extends MojoExecutionBuildParticipant {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(GWTI18NBuildParticipant.class);
	
	public GWTBuildParticipant(MojoExecution execution, boolean runOnIncremental) {
		super(execution, runOnIncremental);
	}

	@Override
	public Set<IProject> build(int kind, IProgressMonitor monitor)
			throws Exception {
		IMaven maven = MavenPlugin.getMaven();
		
		BuildContext buildContext = getBuildContext();

		List<File> bundleFiles = getResourcesToScan();
		
		if (determineIfShouldRun(buildContext, bundleFiles)) {

			LOGGER.debug("Executing build participant "
					+ GWTI18NBuildParticipant.class.getName()
					+ " for plugin execution: " + getMojoExecution());
			Set<IProject> result = super.build(kind, monitor);

			// tell m2e builder to refresh generated files
			File generated = maven.getMojoParameterValue(getSession(),
					getMojoExecution(), "generateDirectory", File.class);
			if (generated != null) {
				buildContext.refresh(generated);
				// Have to touch the project, look like GPE validator are needing this (?)
				getMavenProjectFacade().getProject().touch(monitor);
				buildContext.refresh(getMavenProjectFacade().getProject().getFolder("src").getLocation().toFile());
				
			}

			return result;
		}
		return null;

	}

	/**
	 * Determines the file(s) or folder(s) that should be stated as input for mojo processing.
	 * @return
	 * @throws CoreException
	 */
	protected abstract List<File> getResourcesToScan() throws CoreException;

	/**
	 * Return the absolute path for searching (relative) resources. By default, java + resources.
	 * @return
	 */
	protected List<IPath> getSearchPath() {
		List<IPath> searchPath = new ArrayList<IPath>();

		IPath[] sourcesLocations = getMavenProjectFacade().getCompileSourceLocations();

		Collections.addAll(searchPath, sourcesLocations);
		
		IPath[] resourcesLocations = getMavenProjectFacade().getResourceLocations();
		
		Collections.addAll(searchPath, resourcesLocations);
		
		return searchPath;
	}
	
	/**
	 * Check if there is something to do.
	 * @param buildContext
	 * @param bundleFiles
	 * @return
	 */
	private boolean determineIfShouldRun(BuildContext buildContext,
			List<File> bundleFiles) {

		for (File file : bundleFiles) {
			Scanner scanner = buildContext.newScanner(file);
			scanner.scan();
			String[] includedFiles = scanner.getIncludedFiles();
			if (includedFiles != null && includedFiles.length > 0) {
				return true;
			}
		}

		return false;
	}

}
