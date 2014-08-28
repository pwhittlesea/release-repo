package uk.me.thega.model.util;

import java.io.File;

public class PathHelper {

	private final String basePath;

	public PathHelper(final String basePath) {
		this.basePath = basePath;
	}

	public String getFamilyPath(final String name) {
		return getRepositoryPath() + File.separator + name;
	}

	public String getProductPath(final String family, final String name) {
		return getFamilyPath(family) + File.separator + name;
	}

	public String getRepositoryPath() {
		return basePath;
	}

	public String getResourcePath(final String family, final String product, final String version, final String name) {
		return getVersionPath(family, product, version) + File.separator + name;
	}

	public String getVersionPath(final String family, final String product, final String name) {
		return getProductPath(family, product) + File.separator + name;
	}
}
