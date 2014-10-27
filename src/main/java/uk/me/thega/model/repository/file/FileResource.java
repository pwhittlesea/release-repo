package uk.me.thega.model.repository.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.activation.FileTypeMap;

import org.apache.commons.io.FileUtils;
import org.springframework.mail.javamail.ConfigurableMimeFileTypeMap;

import uk.me.thega.model.repository.Resource;

/**
 * An implementation of the {@link Resource} interface which is file based.
 *
 * @author pwhittlesea
 *
 */
public class FileResource implements Resource {

	/** The wrapped file. */
	private final File file;

	/**
	 * Default constructor.
	 *
	 * @param file the wrapped file.
	 */
	public FileResource(final File file) {
		this.file = file;
	}

	/**
	 * Get the file this resource is wrapping.
	 *
	 * @return the file.
	 * @deprecated accessor methods should be used instead.
	 */
	@Deprecated
	public File getFile() {
		return file;
	}

	@Override
	public FileInputStream getInputStream() throws IOException {
		return new FileInputStream(file);
	}

	@Override
	public String getMimeType() {
		final FileTypeMap map = new ConfigurableMimeFileTypeMap();
		return map.getContentType(file);
	}

	/**
	 * Get the name of this resource.
	 *
	 * @return the name.
	 */
	@Override
	public String getName() {
		return file.getName();
	}

	@Override
	public boolean isFile() {
		return file.isFile();
	}

	@Override
	public long lastModified() {
		return file.lastModified();
	}

	@Override
	public long length() {
		return FileUtils.sizeOf(file);
	}
}
