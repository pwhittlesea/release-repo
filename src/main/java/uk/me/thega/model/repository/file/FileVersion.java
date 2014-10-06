package uk.me.thega.model.repository.file;

import java.io.File;

import uk.me.thega.model.repository.Version;
import uk.me.thega.model.util.MetadataHelper;

public class FileVersion implements Version {

	private final File file;

	public FileVersion(final File file) {
		this.file = file;
	}

	/**
	 * Get the file this version is wrapping.
	 * 
	 * @return the file.
	 * @deprecated accessor methods should be used instead.
	 */
	@Deprecated
	public File getFile() {
		return file;
	}

	/**
	 * Get the name of this version.
	 * 
	 * @return the name.
	 */
	@Override
	public String getName() {
		return file.getName();
	}

	@Override
	public String status() {
		return MetadataHelper.status(file.getPath());
	}
}
