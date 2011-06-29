package org.maven.ide.eclipse.gdt.gae;

import org.apache.maven.project.MavenProject;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.maven.ide.eclipse.gdt.AbstractGdtProjectConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.appengine.eclipse.core.nature.GaeNature;
import com.google.appengine.eclipse.core.sdk.GaeSdk;
import com.google.gdt.eclipse.core.sdk.AbstractSdk;

@SuppressWarnings("restriction")
public class GaeProjectConfigurator extends AbstractGdtProjectConfigurator {

		private static final String MAVEN_GAE_PLUGIN = "net.kindleit:maven-gae-plugin";

		private static final Logger LOGGER = LoggerFactory.getLogger(GaeProjectConfigurator.class);
		
		@Override
		protected boolean isConfigurable(MavenProject mavenProject) {
			return mavenProject.getPlugin(MAVEN_GAE_PLUGIN) != null;
		}

		@Override
		protected void configureNature(IProject project, IProgressMonitor monitor) throws CoreException {
			if (!GaeNature.isGaeProject(project)) {
				LOGGER.info("Adding GAE nature to " + project.getName());
				GaeNature.addNatureToProject(project);
			}
			configureORM(project);
			configureValidation(project);
		}

		private void configureORM(IProject project) {
			// TODO configure classes to enhance for usage with java ORM
			// need to check maven-gae-plugin capabilities
		}

		private void configureValidation(IProject project) {
			// TODO exclude classes from validation
			// need to check maven-gae-plugin capabilities
		}

		@Override
		protected AbstractSdk findSDK(IJavaProject javaProject)
				throws JavaModelException {
			return GaeSdk.findSdkFor(javaProject);
		}

}