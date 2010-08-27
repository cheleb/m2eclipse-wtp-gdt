package org.maven.ide.eclipse.gdt;

import org.codehaus.plexus.util.StringUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IPath;

/**
 * Utility class providing convenient project related methods.
 * @author Fred Bricon
 *
 */
public class ProjectsUtil {
	
	/**
	 * Return the relative path to a project
	 * @param project
	 * @param path
	 * @return the relative path to a project 
	 */
	public static String getRelativePath(IProject project, String path) {
		if (project == null || StringUtils.isEmpty(path)){
			return path;
		}
		IPath projectLocationPath = project.getLocation();
		String projectLocation = projectLocationPath.toOSString();
		if (path.startsWith(projectLocation)) {
			return path.substring(projectLocation.length());
		}
		return path;
	}
}
