package org.maven.ide.eclipse.gdt.core.buildpartcipant.gwt;

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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.eclipse.m2e.core.project.configurator.MojoExecutionBuildParticipant;
import org.maven.ide.eclipse.gdt.core.configurator.gwt.GWTConfiguratorConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.plexus.build.incremental.BuildContext;

import com.google.gdt.eclipse.suite.GdtPlugin;

/**
 * Common participant feature, child class have to implement: <li>
 * {@link GWTBuildParticipant#getResourcesToScan()} <li>
 * And might implement: <li> {@link GWTBuildParticipant#getStatedFolders()}</li>
 * 
 * @author Olivier NOUGUIER olivier.nouguier@gmail.com
 * 
 */
public abstract class GWTBuildParticipant extends MojoExecutionBuildParticipant {

    private static final Logger LOGGER = LoggerFactory.getLogger(GWTBuildParticipant.class);

    public GWTBuildParticipant(MojoExecution execution, boolean runOnIncremental) {
        super(execution, runOnIncremental);
    }

    @Override
    public Set<IProject> build(int kind, IProgressMonitor monitor) throws CoreException {
        IMaven maven = MavenPlugin.getMaven();

        BuildContext buildContext = getBuildContext();

        List<File> resourcesToStat = getResourcesToScan();

        if (determineIfShouldRun(buildContext, resourcesToStat)) {

            LOGGER.debug("Executing build participant " + GWTI18NBuildParticipant.class.getName() + " for plugin execution: "
                    + getMojoExecution());
            Set<IProject> result;
            try {

                result = super.build(kind, monitor);
            } catch (Exception e) {
                throw new CoreException(new Status(IStatus.ERROR, GdtPlugin.PLUGIN_ID, e.getLocalizedMessage(), e));
            }

            // tell m2e builder to refresh generated files
            File generated = maven.getMojoParameterValue(getSession(), getMojoExecution(), GWTConfiguratorConstants.GENERATE_DIRECTORY,
                    File.class);
            if (generated != null) {
                buildContext.refresh(generated);
                // Have to touch the project, look like GPE validator are
                // needing this (?)
                getMavenProjectFacade().getProject().touch(monitor);

            }

            return result;
        }
        LOGGER.debug("Nothing to do!");
        return null;

    }

    /**
     * Determines the file(s) or folder(s) that should be stated as input for
     * mojo processing.
     * 
     * @return
     * @throws CoreException
     */
    protected List<File> getResourcesToScan() throws CoreException {
        List<File> paths = new ArrayList<File>();

        List<IPath> iPaths = getStatedFolders();
        for (IPath iPath : iPaths) {
            paths.add(getMavenProjectFacade().getProject().getFolder(iPath).getLocation().toFile());
        }

        return paths;
    }

    /**
     * Return the absolute path for searching (relative) resources. By default,
     * java + resources.
     * 
     * @return
     */
    protected List<IPath> getStatedFolders() {
        List<IPath> searchPath = new ArrayList<IPath>();

        IPath[] sourcesLocations = getMavenProjectFacade().getCompileSourceLocations();

        Collections.addAll(searchPath, sourcesLocations);

        IPath[] resourcesLocations = getMavenProjectFacade().getResourceLocations();

        Collections.addAll(searchPath, resourcesLocations);

        return searchPath;
    }

    /**
     * Check if there is something to do.
     * 
     * @param buildContext
     * @param resourcesToStat
     * @return
     */
    protected boolean determineIfShouldRun(BuildContext buildContext, List<File> resourcesToStat) {

        for (File statedFolder : resourcesToStat) {
            Scanner scanner = buildContext.newScanner(statedFolder, false);

            scanner.scan();
            String[] includedFiles = scanner.getIncludedFiles();
            if (includedFiles != null && includedFiles.length > 0) {
                for (int i = 0; i < includedFiles.length; i++) {
                    if (fileConcerned(statedFolder, includedFiles[i])) {
                        return true;
                    }
                }
            }

        }
        return false;
    }

    protected abstract boolean fileConcerned(File statedFolder, String file);
}
