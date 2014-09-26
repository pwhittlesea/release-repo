package uk.me.thega.model.util;

import java.io.File;

public final class MetadataHelper {

	public static boolean isDiscontinued(final String path) {
		final File discontinuedFile = new File(path + File.separator + ".discontinued");
		return discontinuedFile.isFile();
	}

	private MetadataHelper() {
		// No construction
	}
}
