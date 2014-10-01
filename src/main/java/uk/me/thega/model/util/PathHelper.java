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

	public String getProductPath(final String family, final String product) {
		return getFamilyPath(family) + File.separator + product;
	}

	public String getRepositoryPath() {
		return basePath;
	}

	public String getResourcePath(final String family, final String product, final String version, final String resource) {
		return getVersionPath(family, product, version) + File.separator + resource;
	}

	public String getVersionPath(final String family, final String product, final String version) {
		return getProductPath(family, product) + File.separator + version;
	}
}
