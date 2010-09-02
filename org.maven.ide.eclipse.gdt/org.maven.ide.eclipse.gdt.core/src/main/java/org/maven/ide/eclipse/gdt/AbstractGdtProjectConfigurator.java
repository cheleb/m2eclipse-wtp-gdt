package org.maven.ide.eclipse.gdt;

import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.maven.ide.eclipse.project.configurator.AbstractProjectConfigurator;
import org.maven.ide.eclipse.project.configurator.ProjectConfigurationRequest;
import org.osgi.service.prefs.BackingStoreException;

import com.google.gdt.eclipse.core.markers.GdtProblemSeverities;
import com.google.gdt.eclipse.core.markers.GdtProblemSeverity;
import com.google.gdt.eclipse.core.markers.ProjectStructureOrSdkProblemType;
import com.google.gdt.eclipse.core.properties.WebAppProjectProperties;
import com.google.gdt.eclipse.core.sdk.AbstractSdk;

@SuppressWarnings("restriction")
public abstract class AbstractGdtProjectConfigurator extends
		AbstractProjectConfigurator {

	private static final String MAVEN_WAR_PLUGIN = "org.apache.maven.plugins:maven-war-plugin";

	private static final String WAR_SOURCE_FOLDER = "src/main/webapp";

	@Override
	public void configure(ProjectConfigurationRequest request,
			IProgressMonitor monitor) throws CoreException {
		MavenProject mavenProject = request.getMavenProject();
		IProject project = request.getProject();
		if (isConfigurable(mavenProject)) {
			checkMissingSDKSeverity(project, monitor);
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
		IPath warSrcDir = getWarSrcDir(project, mavenProject);
		if (warSrcDir != null) {// null if not a web project
			try {
				if (!warSrcDir.equals(WebAppProjectProperties
						.getWarSrcDir(project))) {
					WebAppProjectProperties.setWarSrcDir(project, warSrcDir);
				}
				WebAppProjectProperties.setWarSrcDirIsOutput(project, false);
			} catch (BackingStoreException e) {
				throw new CoreException(new Status(IStatus.ERROR,
						MavenGdtPlugin.PLUGIN_ID,
						"Unable to set war source directory for "
								+ project.getName(), e));
			}
		}
	}

	protected IPath getWarSrcDir(IProject project, MavenProject mavenProject) {
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

		return project.getFolder(warSrcDir).getProjectRelativePath();
	}

	protected void checkMissingSDKSeverity(IProject project,
			IProgressMonitor monitor) {
		assert project != null;
		IJavaProject javaProject = JavaCore.create(project);
		if (javaProject != null) {
			try {
				AbstractSdk sdk = findSDK(javaProject);
				GdtProblemSeverity severity = GdtProblemSeverities
						.getInstance().getSeverity(
								ProjectStructureOrSdkProblemType.NO_SDK);
				if (sdk == null) {

					if (severity == GdtProblemSeverity.ERROR) {
						// TODO Access to the Google Preferences classes is
						// forbidden.
						// Since we can't change the severity via the API,
						// we'll just log a red warning in the console
						console.logError("Warning : you should reduce the severity level for"
								+ " Window > Preferences > Google > Errors/Warnings > "
								+ ProjectStructureOrSdkProblemType.NO_SDK
										.getCategory().getDisplayName()
								+ " > "
								+ ProjectStructureOrSdkProblemType.NO_SDK
										.getDescription());
						insurePresenceOfGWTDependency(monitor, project, null);
						return;
					}

				} else {
					if (severity == GdtProblemSeverity.IGNORE) {
						removePresenceOfGWTDependency(monitor, project, null);
					}
				}
			} catch (JavaModelException e) {
				console.logError("Unable to find GWT SDK for "
						+ project.getName());
				// Swallow the exception for now, since we just want to warn
				// users
				return;
			}
		}
		return;
	}

	protected abstract AbstractSdk findSDK(IJavaProject javaProject)
			throws JavaModelException;

	private void insurePresenceOfGWTDependency(IProgressMonitor monitor,
			IProject project, String gwtVersion) throws JavaModelException {
		IJavaProject javaProject = JavaCore.create(project);
		IClasspathEntry prevClasspathEntries[] = javaProject.getRawClasspath();

		for (int i = 0; i < prevClasspathEntries.length; i++) {
			IClasspathEntry iClasspathEntry = prevClasspathEntries[i];
			if ("com.google.gwt.eclipse.core.GWT_CONTAINER"
					.equals(iClasspathEntry.getPath().segment(0))) {
				return;
			}
		}
		StringBuilder gwtLibrairyPath = new StringBuilder(
				"com.google.gwt.eclipse.core.GWT_CONTAINER");
		if (gwtVersion != null) {
			gwtLibrairyPath.append('/').append(gwtVersion);
		}
		IClasspathEntry newClasspathEntries[] = new IClasspathEntry[prevClasspathEntries.length + 1];
		System.arraycopy(prevClasspathEntries, 0, newClasspathEntries, 0,
				prevClasspathEntries.length);
		IClasspathEntry updated = JavaCore.newContainerEntry(new Path(
				gwtLibrairyPath.toString()), null, null, false);

		newClasspathEntries[prevClasspathEntries.length] = updated;

		javaProject.setRawClasspath(newClasspathEntries, monitor);
	}

	private void removePresenceOfGWTDependency(IProgressMonitor monitor,
			IProject project, String gwtVersion) throws JavaModelException {
		IJavaProject javaProject = JavaCore.create(project);
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
