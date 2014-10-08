package uk.me.thega.model.util;

import java.io.File;

/**
 * Helper to ensure that certain parts of the repository can be found without having to worry about OS specifics.
 * 
 * @author pwhittlesea
 * 
 */
public class PathHelper {

	/** The base path of the repo. */
	private final String basePath;

	public PathHelper(final String basePath) {
		this.basePath = basePath;
	}

	public String getFamilyPath(final String family) {
		return getRepositoryPath() + File.separator + family;
	}

	public String getApplicationPath(final String family, final String application) {
		return getFamilyPath(family) + File.separator + application;
	}

	public String getRepositoryPath() {
		return basePath;
	}

	public String getResourcePath(final String family, final String application, final String version, final String resource) {
		return getVersionPath(family, application, version) + File.separator + resource;
	}

	public String getVersionPath(final String family, final String application, final String version) {
		return getApplicationPath(family, application) + File.separator + version;
	}
}
