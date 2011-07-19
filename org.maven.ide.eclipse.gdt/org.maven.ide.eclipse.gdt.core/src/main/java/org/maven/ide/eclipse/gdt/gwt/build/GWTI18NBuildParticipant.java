package org.maven.ide.eclipse.gdt.gwt.build;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.maven.plugin.MojoExecution;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;
import org.eclipse.m2e.core.lifecyclemapping.model.IPluginExecutionMetadata;
import org.eclipse.m2e.core.project.IMavenProjectFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GWTI18NBuildParticipant extends GWTBuildParticipant {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(GWTI18NBuildParticipant.class);

	public GWTI18NBuildParticipant(IMavenProjectFacade projectFacade,
			MojoExecution execution, IPluginExecutionMetadata executionMetadata) {
		super(execution, true);
	}

	private Collection<? extends String> getBundles(IMaven maven,
			String typeBundle) throws CoreException {
		String bundleFilesAsString[] = maven.getMojoParameterValue(
				getSession(), getMojoExecution(), typeBundle, String[].class);
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < bundleFilesAsString.length; i++) {
			String bundle = bundleFilesAsString[i];
			LOGGER.debug("Searching bundle (" + typeBundle + "): " + bundle);
			list.add(bundle.replaceAll("\\.", "/") + ".properties");
		}

		return list;
	}

	private File findBundleFile(String bundleFileAsString,
			List<IPath> searchPath) {
		for (IPath resourcePath : searchPath) {
			File file = new File(getMavenProjectFacade().getProject()
					.getFolder(resourcePath).getLocation().toFile(),
					bundleFileAsString);
			if (file != null && file.exists()) {
				LOGGER.debug("Find bundle: " + bundleFileAsString + " ("
						+ resourcePath + ")");
				return file;
			}
		}
		return null;
	}

	@Override
	protected List<File> getResourcesToScan() throws CoreException {

		IMaven maven = MavenPlugin.getMaven();
		List<String> bundleFilesAsString = new ArrayList<String>();

		bundleFilesAsString.addAll(getBundles(maven, "i18nMessagesBundles"));
		bundleFilesAsString.addAll(getBundles(maven, "i18nConstantsBundles"));
		bundleFilesAsString.addAll(getBundles(maven,
				"i18nConstantsWithLookupBundles"));

		List<File> bundleFiles = new ArrayList<File>();

		List<IPath> searchPath = getSearchPath();

		for (String bundleFileAsString : bundleFilesAsString) {

			File file = findBundleFile(bundleFileAsString, searchPath);
			if (file != null) {
				bundleFiles.add(file);
			}
		}
		return bundleFiles;

	}

}
