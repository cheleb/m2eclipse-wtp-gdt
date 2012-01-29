package org.maven.ide.eclipse.gdt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Unused {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Unused.class); 
	
	/**
	 * Add the GWT SDK Library if not yet in the classpath. It must be added
	 * <b>before</b> the Maven classpath container or the GEP will complains
	 * that the GWT SDK is not found.
	 * 
	 * @param monitor
	 * @param javaProject
	 * @param gwtVersion
	 * @throws JavaModelException
	 */
	void insurePresenceOfGWTDependency(IProgressMonitor monitor,
			IJavaProject javaProject, String gwtVersion)
			throws JavaModelException {

		IClasspathEntry prevClasspathEntries[] = javaProject.getRawClasspath();

		int mavenContainerIndex = -1, gwtContainerIndex = -1;
		List<IClasspathEntry> classpathEntries = new ArrayList<IClasspathEntry>();
		for (int i = 0; i < prevClasspathEntries.length; i++) {
			IClasspathEntry iClasspathEntry = prevClasspathEntries[i];
			if ("com.google.gwt.eclipse.core.GWT_CONTAINER"
					.equals(iClasspathEntry.getPath().segment(0))) {
				gwtContainerIndex = i;
			} else if ("org.eclipse.m2e.MAVEN2_CLASSPATH_CONTAINER"
					.equals(iClasspathEntry.getPath().segment(0))) {
				mavenContainerIndex = i;
			}
			classpathEntries.add(iClasspathEntry);
		}

		boolean updated = false;

		if (gwtContainerIndex == -1) {
			StringBuilder gwtLibrairyPath = new StringBuilder(
					"com.google.gwt.eclipse.core.GWT_CONTAINER");
			if (gwtVersion != null) {
				gwtLibrairyPath.append('/').append(gwtVersion);
			}
			gwtContainerIndex = classpathEntries.size();
			classpathEntries.add(JavaCore.newContainerEntry(new Path(
					gwtLibrairyPath.toString()), null, null, false));
			updated = true;
		}

		if (mavenContainerIndex == -1) {
			LOGGER.warn("Maven Container classpath not found, it will be added!");
			mavenContainerIndex = classpathEntries.size();
			classpathEntries.add(JavaCore.newContainerEntry(new Path(
					"org.eclipse.m2e.MAVEN2_CLASSPATH_CONTAINER"), null, null,
					false));
			updated = true;
		}

		if (mavenContainerIndex < gwtContainerIndex) {
			Collections.swap(classpathEntries, mavenContainerIndex,
					gwtContainerIndex);
			int t = mavenContainerIndex;
			gwtContainerIndex = mavenContainerIndex;
			mavenContainerIndex = t;
			updated = true;
		}

		if (updated) {
			IClasspathEntry newClasspathEntries[] = classpathEntries
					.toArray(new IClasspathEntry[prevClasspathEntries.length + 1]);

			javaProject.setRawClasspath(newClasspathEntries, monitor);
		}
	}

}
