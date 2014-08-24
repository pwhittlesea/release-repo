package uk.me.thega.model.metadata;

import java.io.File;

import javax.xml.bind.JAXBException;

import uk.me.thega.model.Named;
import uk.me.thega.model.util.MetadataUnmarshaller;

public class MetadataFactory {

	public static ProductMetadata createProductMetadata(final File file) {
		try {
			final ProductMetadata productMetadata = getMetadataFromFolder(file.getPath(), ProductMetadata.class);
			if (productMetadata != null) {
				productMetadata.setName(file.getName());
				return productMetadata;
			} else {
				return null;
			}
		} catch (final JAXBException e) {
			return null;
		}
	}

	private static <T extends Named> T getMetadataFromFolder(final String folder, final Class<T> clazz) throws JAXBException {
		final String metadataPath = folder + "/metadata.xml";
		final File metadataFile = new File(metadataPath);
		if (metadataFile.isFile()) {
			return MetadataUnmarshaller.un(metadataFile, clazz);
		} else {
			return null;
		}

	}
}
