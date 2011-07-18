package org.maven.ide.eclipse.gdt;

import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.m2e.core.project.configurator.ProjectConfigurationRequest;
import org.eclipse.m2e.jdt.AbstractJavaProjectConfigurator;
import org.osgi.service.prefs.BackingStoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gdt.eclipse.core.markers.GdtProblemSeverities;
import com.google.gdt.eclipse.core.markers.GdtProblemSeverity;
import com.google.gdt.eclipse.core.markers.ProjectStructureOrSdkProblemType;
import com.google.gdt.eclipse.core.properties.WebAppProjectProperties;
import com.google.gdt.eclipse.core.sdk.AbstractSdk;

@SuppressWarnings("restriction")
public abstract class AbstractGdtProjectConfigurator extends
		AbstractJavaProjectConfigurator {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(AbstractGdtProjectConfigurator.class);

	private static final String MAVEN_WAR_PLUGIN = "org.apache.maven.plugins:maven-war-plugin";

	private static final String WAR_SOURCE_FOLDER = "src/main/webapp";

	@Override
	public void configure(ProjectConfigurationRequest request,
			IProgressMonitor monitor) {
		MavenProject mavenProject = request.getMavenProject();
		IProject project = request.getProject();
		IJavaProject javaProject = JavaCore.create(project);
		try {
			if (isConfigurable(mavenProject)) {
				checkMissingSDKSeverity(javaProject, monitor);
				configureNature(project, monitor);

				if (isWebapp(mavenProject)) {
					configureWebapp(project, mavenProject);
					boolean messWithLaunchConfig = true;// TODO read from
														// Preferences
					if (messWithLaunchConfig) {
						configureDeploymentSettings(project, mavenProject);
					}
				}
			}
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	protected abstract boolean isConfigurable(MavenProject mavenProject);

	protected abstract void configureNature(IProject project,
			IProgressMonitor monitor) throws CoreException;

	protected void configureDeploymentSettings(IProject project,
			MavenProject mavenProject) throws CoreException {
		// do nothing
	}

	protected boolean isWebapp(MavenProject mavenProject) {
		return "war".equals(mavenProject.getArtifact().getType());
	}

	protected void configureWebapp(IProject project, MavenProject mavenProject)
			throws CoreException {
		assert project != null;
		assert mavenProject != null;
		IFolder warSrcDir = getWarSrcDir(project, mavenProject);
		if (warSrcDir != null) {// null if not a web project
			try {
				if (!warSrcDir.equals(WebAppProjectProperties
						.getWarSrcDir(project))) {
					WebAppProjectProperties.setWarSrcDir(project,
							warSrcDir.getProjectRelativePath());
				}
				WebAppProjectProperties.setWarSrcDirIsOutput(project, false);

				WebAppProjectProperties.setLastUsedWarOutLocation(project,
						project.getFolder("target/m2e-wtp/web-resources")
								.getRawLocation());

			} catch (BackingStoreException e) {
				throw new CoreException(new Status(IStatus.ERROR,
						MavenGdtPlugin.PLUGIN_ID,
						"Unable to set war source directory for "
								+ project.getName(), e));
			}
		}
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
			Xpp3Dom[] warSourceDirectory = config
					.getChildren("warSourceDirectory");
			if (warSourceDirectory != null && warSourceDirectory.length > 0) {
				// first one wins
				String dir = warSourceDirectory[0].getValue();
				warSrcDir = ProjectsUtil.getRelativePath(project, dir);
			}
		}
		warSrcDir = (warSrcDir == null) ? WAR_SOURCE_FOLDER : warSrcDir;

		return project.getFolder(warSrcDir);
	}

	protected void checkMissingSDKSeverity(IJavaProject javaProject,
			IProgressMonitor monitor) {

		if (javaProject != null) {
			try {
				AbstractSdk sdk = findSDK(javaProject);
				GdtProblemSeverity severity = GdtProblemSeverities
						.getInstance().getSeverity(
								ProjectStructureOrSdkProblemType.NO_SDK);
				if (sdk == null) {

					if (severity == GdtProblemSeverity.ERROR) {
						LOGGER.info("We ignore the absence of lib as they are provided by maven");
						GdtProblemSeverities.getInstance().setSeverity(
								ProjectStructureOrSdkProblemType.NO_SDK,
								GdtProblemSeverity.IGNORE);

						LOGGER.info("We are reducing the severity level for"
								+ " Window > Preferences > Google > Errors/Warnings > "
								+ ProjectStructureOrSdkProblemType.NO_SDK
										.getCategory().getDisplayName()
								+ " > "
								+ ProjectStructureOrSdkProblemType.NO_SDK
										.getDescription());
						GdtLockedAPIHelper.setEncodedProblemSeverities(GdtProblemSeverities
								.getInstance().toPreferenceString());
						return;
					}

					//insurePresenceOfGWTDependency(monitor, javaProject, null);

				} else {
					if (severity == GdtProblemSeverity.IGNORE) {
						removePresenceOfGWTDependency(monitor, javaProject,
								null);
					}
				}
			} catch (JavaModelException e) {
				LOGGER.error("Unable to find GWT SDK for "
						+ javaProject.getElementName());
				// Swallow the exception for now, since we just want to warn
				// users
				return;
			}
		}
		return;
	}

	
	protected abstract AbstractSdk findSDK(IJavaProject javaProject)
			throws JavaModelException;
	
	
	
	/**
	 * Remove the GWT SDK Library if found in the classpath.
	 * 
	 * @param monitor
	 * @param javaProject
	 * @param gwtVersion
	 * @throws JavaModelException
	 */
	private void removePresenceOfGWTDependency(IProgressMonitor monitor,
			IJavaProject javaProject, String gwtVersion)
			throws JavaModelException {
		IClasspathEntry prevClasspathEntries[] = javaProject.getRawClasspath();
		IClasspathEntry newClasspathEntries[] = new IClasspathEntry[prevClasspathEntries.length - 1];
		boolean found = false;
		for (int i = 0; i < prevClasspathEntries.length; i++) {
			IClasspathEntry iClasspathEntry = prevClasspathEntries[i];
			if ("com.google.gwt.eclipse.core.GWT_CONTAINER"
					.equals(iClasspathEntry.getPath().segment(0))) {
				found = true;
			} else {
				newClasspathEntries[i] = iClasspathEntry;
			}
		}

		if (found) {
			javaProject.setRawClasspath(newClasspathEntries, monitor);
		}
	}
	
	
}
