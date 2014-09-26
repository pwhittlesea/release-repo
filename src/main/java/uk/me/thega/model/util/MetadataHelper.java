package uk.me.thega.model.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public final class MetadataHelper {

	public static boolean isDiscontinued(final String path) {
		final File discontinuedFile = new File(path + File.separator + ".discontinued");
		return discontinuedFile.isFile();
	}

	public static String status(final String path) {
		final File statusFile = new File(path + File.separator + ".status");
		try {
			final String fileContents = FileUtils.readFileToString(statusFile);
			return fileContents.trim();
		} catch (final IOException e) {
			return "n/a";
		}
	}

	private MetadataHelper() {
		// No construction
	}
}
