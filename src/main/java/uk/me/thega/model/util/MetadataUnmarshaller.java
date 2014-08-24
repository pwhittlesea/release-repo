package uk.me.thega.model.util;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class MetadataUnmarshaller {

	@SuppressWarnings("unchecked")
	public static <T> T un(final File path, final Class<T> clazz) throws JAXBException {
		final JAXBContext jc = JAXBContext.newInstance(clazz);
		final Unmarshaller unmarshaller = jc.createUnmarshaller();
		return (T) unmarshaller.unmarshal(path);
	}

	private MetadataUnmarshaller() {
		// No construction allowed
	}
}
