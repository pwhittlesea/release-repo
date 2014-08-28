package uk.me.thega.model.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import uk.me.thega.controller.exception.NotFoundException;

public class FileSystemUtil {

	private final PathHelper pathHelper;

	public FileSystemUtil(final PathHelper pathHelper) {
		this.pathHelper = pathHelper;
	}

	public List<File> allResources(final String family, final String version) throws NotFoundException {
		final List<File> allResources = new ArrayList<File>();
		for (final File product : products(family)) {
			final String productName = product.getName();
			for (final File versionFile : versions(family, productName)) {
				if (versionFile.isDirectory() && versionFile.getName().equals(version)) {
					allResources.addAll(resources(family, productName, version));
				}
			}
		}
		return allResources;
	}

	public List<File> allVersions(final String family) throws NotFoundException {
		final List<File> allVersions = new ArrayList<File>();
		for (final File product : products(family)) {
			allVersions.addAll(versions(family, product.getName()));
		}
		Collections.sort(allVersions, new VersionComparator());
		return allVersions;
	}

	public List<File> families() throws NotFoundException {
		return getContentsOf(pathHelper.getRepositoryPath());
	}

	public List<File> products(final String family) throws NotFoundException {
		return getContentsOf(pathHelper.getFamilyPath(family));
	}

	public List<File> resources(final String family, final String product, final String version) throws NotFoundException {
		return getContentsOf(pathHelper.getVersionPath(family, product, version));
	}

	public List<File> versions(final String family, final String product) throws NotFoundException {
		final List<File> versions = getContentsOf(pathHelper.getProductPath(family, product));
		Collections.sort(versions, new VersionComparator());
		return versions;
	}

	private List<File> getContentsOf(final String path) throws NotFoundException {
		final File dir = new File(path);
		if (!dir.isDirectory()) {
			throw new NotFoundException("Not Found");
		}
		return Arrays.asList(dir.listFiles());
	}
}
