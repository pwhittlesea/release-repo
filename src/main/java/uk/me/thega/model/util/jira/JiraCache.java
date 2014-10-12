package uk.me.thega.model.util.jira;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manage cached Jira JQL queries.
 * 
 * JQL queries are built by fetching JQL from .jira files in families, applications and versions in that order.
 * 
 * @author pwhittlesea
 * 
 */
final class JiraCache {

	/** The logger. */
	private static final Logger logger = LoggerFactory.getLogger(JiraCache.class);

	static Map<String, String> get(final String path) {
		final Map<String, String> map = new HashMap<String, String>();
		final File cacheFile = new File(path);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(cacheFile);
			final byte[] data = new byte[(int) cacheFile.length()];
			fis.read(data);
			fis.close();

			// Extract the cached JSON
			final JSONObject json = new JSONObject(new String(data, "UTF-8"));

			@SuppressWarnings("unchecked")
			final Iterator<String> keys = json.keys();
			while (keys.hasNext()) {
				final String key = keys.next();
				map.put(key, (String) json.get(key));
			}
		} catch (final IOException e) {
			logger.error("Could not get cached content", e);
		} catch (final JSONException e) {
			logger.error("Could not get cached content", e);
		} finally {
			IOUtils.closeQuietly(fis);
		}

		return map;
	}

	static void put(final Map<String, String> issues, final String path) throws IOException {
		final JSONObject json = new JSONObject(issues);
		final String jsonString = json.toString();

		// Create some files here
		logger.trace("JiraHelper: Caching in {}", path);
		final File sourceFile = new File(path + ".temp");
		final File fileToCopy = new File(path);

		// Sample content
		FileUtils.writeStringToFile(sourceFile, jsonString);

		// Now copy from source to copy, the delete source.
		FileUtils.copyFile(sourceFile, fileToCopy);
		FileUtils.deleteQuietly(sourceFile);
	}

	private JiraCache() {
		// no construction
	}

}
