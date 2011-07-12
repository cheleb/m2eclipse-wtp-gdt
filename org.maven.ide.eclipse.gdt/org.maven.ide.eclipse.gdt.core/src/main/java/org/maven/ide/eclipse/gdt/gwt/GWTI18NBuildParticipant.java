package org.maven.ide.eclipse.gdt.gwt;

import java.io.File;
import java.util.Set;

import org.apache.maven.plugin.MojoExecution;
import org.codehaus.plexus.util.Scanner;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.eclipse.m2e.core.lifecyclemapping.model.IPluginExecutionMetadata;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.configurator.MojoExecutionBuildParticipant;
import org.sonatype.plexus.build.incremental.BuildContext;

public class GWTI18NBuildParticipant extends MojoExecutionBuildParticipant {

	public GWTI18NBuildParticipant(IMavenProjectFacade projectFacade,
			MojoExecution execution, IPluginExecutionMetadata executionMetadata) {
		super(execution, true);
	}

	@Override
	public Set<IProject> build(int kind, IProgressMonitor monitor)
			throws Exception {
		IMaven maven = MavenPlugin.getMaven();
		BuildContext buildContext = getBuildContext();

		File bundleFiles[] = maven.getMojoParameterValue(getSession(),
				getMojoExecution(), "i18nConstantsBundles", File[].class);

		if (determineIfShouldRun(buildContext, bundleFiles)) {

			Set<IProject> result = super.build(kind, monitor);

			// tell m2e builder to refresh generated files
			File generated = maven.getMojoParameterValue(getSession(),
					getMojoExecution(), "generateDirectory", File.class);
			if (generated != null) {
				buildContext.refresh(generated);
			}

			return result;
		}
		return null;

	}

	private boolean determineIfShouldRun(BuildContext buildContext,
			File[] bundleFiles) {

		if (bundleFiles != null && bundleFiles.length > 0) {
			for (File file : bundleFiles) {
				Scanner scanner = buildContext.newScanner(file);
				scanner.scan();
				String[] includedFiles = scanner.getIncludedFiles();
				if (includedFiles != null && includedFiles.length > 0) {
					return true;
				}
			}
		}
		return false;
	}

}
