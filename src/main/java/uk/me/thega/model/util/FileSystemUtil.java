package uk.me.thega.model.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.me.thega.controller.exception.NotFoundException;

public class FileSystemUtil {

	protected static final String BASE_DIR = System.getProperty("user.home") + "/repository";

	public static List<File> allResources(final String family, final String version) throws NotFoundException {
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

	public static List<File> allVersions(final String family) throws NotFoundException {
		final List<File> allVersions = new ArrayList<File>();
		for (final File product : products(family)) {
			allVersions.addAll(versions(family, product.getName()));
		}
		return allVersions;
	}

	public static List<File> families() throws NotFoundException {
		return checkAndGetContentsOf(BASE_DIR);
	}

	public static List<File> products(final String family) throws NotFoundException {
		return checkAndGetContentsOf(BASE_DIR + File.separator + family);
	}

	public static List<File> resources(final String family, final String product, final String version) throws NotFoundException {
		return checkAndGetContentsOf(BASE_DIR + File.separator + family + File.separator + product + File.separator + version);
	}

	public static List<File> versions(final String family, final String product) throws NotFoundException {
		return checkAndGetContentsOf(BASE_DIR + File.separator + family + File.separator + product);
	}

	private static List<File> checkAndGetContentsOf(final File dir) throws NotFoundException {
		if (!dir.isDirectory()) {
			throw new NotFoundException("Not Found");
		}
		return Arrays.asList(dir.listFiles());
	}

	private static List<File> checkAndGetContentsOf(final String path) throws NotFoundException {
		return checkAndGetContentsOf(new File(path));
	}
}
