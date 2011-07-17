package org.maven.ide.eclipse.gdt.gwt;

/**
 * @author NOUGUIER Olivier olivier.nouguier@gmail.com
 */
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.MojoExecution;
import org.codehaus.plexus.util.Scanner;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.eclipse.m2e.core.lifecyclemapping.model.IPluginExecutionMetadata;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.eclipse.m2e.core.project.configurator.MojoExecutionBuildParticipant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.plexus.build.incremental.BuildContext;

public class GWTI18NBuildParticipant extends MojoExecutionBuildParticipant {
	
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GWTI18NBuildParticipant.class);

	public GWTI18NBuildParticipant(IMavenProjectFacade projectFacade,
			MojoExecution execution, IPluginExecutionMetadata executionMetadata) {
		super(execution, true);
	}

	@Override
	public Set<IProject> build(int kind, IProgressMonitor monitor)
			throws Exception {
		IMaven maven = MavenPlugin.getMaven();
		BuildContext buildContext = getBuildContext();

		List<String> bundleFilesAsString = new ArrayList<String>();
		bundleFilesAsString.addAll(getBundles(maven, "i18nMessagesBundles"));
		bundleFilesAsString.addAll(getBundles(maven, "i18nConstantsBundles"));
		bundleFilesAsString.addAll(getBundles(maven, "i18nConstantsWithLookupBundles"));

		List<File> bundleFiles = new ArrayList<File>();

		String sourceDirectory = getMavenProjectFacade().getMavenProject()
				.getBuild().getSourceDirectory();

		List<Resource> resources = getMavenProjectFacade().getMavenProject()
				.getBuild().getResources();

		List<String> searchPath = new ArrayList<String>();
		searchPath.add(sourceDirectory);
		for (Resource resource : resources) {
			searchPath.add(resource.getDirectory());
		}

		for (String bundleFileAsString : bundleFilesAsString) {

			File file = findBundleFile(bundleFileAsString, searchPath);
			if (file != null) {
				bundleFiles.add(file);
			}
		}

		if (determineIfShouldRun(buildContext, bundleFiles)) {

			LOGGER.debug("Executing build participant " + GWTI18NBuildParticipant.class.getName() + " for plugin execution: " + getMojoExecution());
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

	private Collection<? extends String> getBundles(IMaven maven, String typeBundle)
			throws CoreException {
		String bundleFilesAsString[] = maven.getMojoParameterValue(
				getSession(), getMojoExecution(), typeBundle,
				String[].class);
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < bundleFilesAsString.length; i++) {
			String bundle = bundleFilesAsString[i];
			LOGGER.info("Searching bundle (" + typeBundle + "): " + bundle);
			list.add(bundle.replaceAll("\\.", "/") + ".properties");
		}

		return list;
	}

	private File findBundleFile(String bundleFileAsString,
			List<String> searchPath) {
		for (String resourcePath : searchPath) {
			File file = new File(resourcePath, bundleFileAsString);
			if (file != null && file.exists()) {
				LOGGER.info("Find bundle: " + bundleFileAsString + " (" + resourcePath + ")");
				return file;
			}
		}
		return null;
	}

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
