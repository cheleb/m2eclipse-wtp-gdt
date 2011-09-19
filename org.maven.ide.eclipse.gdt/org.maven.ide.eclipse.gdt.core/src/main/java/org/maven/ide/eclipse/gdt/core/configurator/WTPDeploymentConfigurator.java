package org.maven.ide.eclipse.gdt.core.configurator;

import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.m2e.core.project.configurator.AbstractProjectConfigurator;
import org.eclipse.m2e.core.project.configurator.ProjectConfigurationRequest;
import org.maven.ide.eclipse.gdt.MavenGdtPlugin;
import org.maven.ide.eclipse.gdt.ProjectsUtil;
import org.maven.ide.eclipse.wtp.MavenWtpConstants;
import org.maven.ide.eclipse.wtp.ProjectUtils;
import org.osgi.service.prefs.BackingStoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gdt.eclipse.core.properties.WebAppProjectProperties;

public class WTPDeploymentConfigurator extends AbstractProjectConfigurator {

    private static final Logger LOGGER = LoggerFactory.getLogger(WTPDeploymentConfigurator.class);

    private static final String MAVEN_WAR_PLUGIN = "org.apache.maven.plugins:maven-war-plugin";

    private static final String WAR_SOURCE_FOLDER = "src/main/webapp";

    @Override
    public void configure(ProjectConfigurationRequest configurationRequest, IProgressMonitor monitor) throws CoreException {
        IProject project = configurationRequest.getProject();
        MavenProject mavenProject = configurationRequest.getMavenProject();
        configureWebapp(project, mavenProject);

    }

    protected IFolder getWarSrcDir(IProject project, MavenProject mavenProject) {
        assert project != null;
        assert mavenProject != null;
        Plugin warPlugin = mavenProject.getPlugin(MAVEN_WAR_PLUGIN);
        if (warPlugin == null) {
            return null;
        }
        Xpp3Dom config = (Xpp3Dom) warPlugin.getConfiguration();
        String warSrcDir = null;
        if (config != null) {
            Xpp3Dom[] warSourceDirectory = config.getChildren("warSourceDirectory");
            if (warSourceDirectory != null && warSourceDirectory.length > 0) {
                // first one wins
                String dir = warSourceDirectory[0].getValue();
                warSrcDir = ProjectsUtil.getRelativePath(project, dir);
            }
        }
        warSrcDir = (warSrcDir == null) ? WAR_SOURCE_FOLDER : warSrcDir;

        return project.getFolder(warSrcDir);
    }

    protected void configureWebapp(IProject project, MavenProject mavenProject) throws CoreException {
        assert project != null;
        assert mavenProject != null;
        IFolder warSrcDir = getWarSrcDir(project, mavenProject);
        if (warSrcDir != null) {// null if not a web project
            try {
                if (!warSrcDir.equals(WebAppProjectProperties.getWarSrcDir(project))) {
                    WebAppProjectProperties.setWarSrcDir(project, warSrcDir.getProjectRelativePath());
                }
                WebAppProjectProperties.setWarSrcDirIsOutput(project, false);

                IPath wtpTargetFolder = ProjectUtils.getM2eclipseWtpFolder(mavenProject, project).append(
                        MavenWtpConstants.WEB_RESOURCES_FOLDER);
                // String wtpTargetFolder = "target/m2e-wtp/web-resources";

                WebAppProjectProperties.setLastUsedWarOutLocation(project, project.getFolder(wtpTargetFolder).getRawLocation());

            } catch (BackingStoreException e) {
                throw new CoreException(new Status(IStatus.ERROR, MavenGdtPlugin.PLUGIN_ID, "Unable to set war source directory for "
                        + project.getName(), e));
            }
        }
    }
}
