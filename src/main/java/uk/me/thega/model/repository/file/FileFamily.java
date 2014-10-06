package uk.me.thega.model.repository.file;

import java.io.File;

import uk.me.thega.model.repository.Family;

public class FileFamily implements Family {

	private final File file;

	public FileFamily(final File file) {
		this.file = file;
	}

	/**
	 * Get the file this family is wrapping.
	 * 
	 * @return the file.
	 * @deprecated accessor methods should be used instead.
	 */
	@Deprecated
	public File getFile() {
		return file;
	}

	/**
	 * Get the name of this family.
	 * 
	 * @return the name.
	 */
	@Override
	public String getName() {
		return file.getName();
	}
}
