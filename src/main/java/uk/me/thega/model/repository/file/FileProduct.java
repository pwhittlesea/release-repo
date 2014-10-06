package uk.me.thega.model.repository.file;

import java.io.File;

import uk.me.thega.model.repository.Product;
import uk.me.thega.model.util.MetadataHelper;

public class FileProduct implements Product {

	private final File file;

	public FileProduct(final File file) {
		this.file = file;
	}

	/**
	 * Get the file this product is wrapping.
	 * 
	 * @return the file.
	 * @deprecated accessor methods should be used instead.
	 */
	@Deprecated
	public File getFile() {
		return file;
	}

	/**
	 * Get the name of this product.
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
