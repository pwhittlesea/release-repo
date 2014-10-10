package uk.me.thega.model.repository.file;

import java.io.File;

import uk.me.thega.model.repository.Application;
import uk.me.thega.model.util.MetadataHelper;

public class FileApplication implements Application {

	private final File file;

	public FileApplication(final File file) {
		this.file = file;
	}

	/**
	 * Get the file this application is wrapping.
	 * 
	 * @return the file.
	 * @deprecated accessor methods should be used instead.
	 */
	@Deprecated
	public File getFile() {
		return file;
	}

	/**
	 * Get the name of this application.
	 * 
	 * @return the name.
	 */
	@Override
	public String getName() {
		return file.getName();
	}

	@Override
	public Boolean isDiscontinued() {
		return MetadataHelper.isDiscontinued(file.getPath());
	}
}
