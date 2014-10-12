package uk.me.thega.model.properties;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;

import uk.me.thega.model.util.PathHelper;

public class PropertiesLoader {

	/** Repository. */
	private final PathHelper helper;

	public PropertiesLoader(final PathHelper helper) {
		this.helper = helper;
	}

	public Map<String, String> read(final String file) throws IOException {
		final File propertiesFile = new File(helper.getRepositoryPath() + File.separator + file + ".properties");
		ensureExists(propertiesFile);
		return load(propertiesFile);
	}

	private void ensureExists(final File propertiesFile) throws IOException {
		if (!propertiesFile.exists()) {
			if (!propertiesFile.createNewFile()) {
				throw new IOException("Could not create " + propertiesFile);
			}
			loadDefaults(propertiesFile);
		}
	}

	private Map<String, String> load(final File propertiesFile) throws IOException {
		final Map<String, String> map = new HashMap<String, String>();

		InputStream fis = null;
		try {
			fis = new FileInputStream(propertiesFile);
			final Properties properties = new Properties();
			properties.load(fis);
			for (final Entry<Object, Object> entry : properties.entrySet()) {
				map.put((String) entry.getKey(), (String) entry.getValue());
			}
		} finally {
			IOUtils.closeQuietly(fis);
		}
		return map;
	}

	private void loadDefaults(final File propertiesFile) throws IOException, FileNotFoundException {
		final ClassPathResource defaults = new ClassPathResource("defaults" + File.separator + "default-" + propertiesFile.getName());

		BufferedOutputStream out = null;
		InputStream in = null;
		try {
			in = defaults.getInputStream();
			out = new BufferedOutputStream(new FileOutputStream(propertiesFile));

			final Properties defaultProperties = new Properties();
			defaultProperties.load(in);
			defaultProperties.store(out, "Defaults");
		} finally {
			IOUtils.closeQuietly(in);
			IOUtils.closeQuietly(out);
		}
	}
}
