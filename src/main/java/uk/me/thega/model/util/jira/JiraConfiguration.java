package uk.me.thega.model.util.jira;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.me.thega.model.properties.PropertiesLoader;
import uk.me.thega.model.util.PathHelper;

/**
 * Config should be in a .jira.properties file.
 * 
 * @author pwhittlesea
 * 
 */
class JiraConfiguration {

	/** Configuration key for URL. */
	private static final String URL = "url";

	/** Configuration key for user name. */
	private static final String USERNAME = "username";

	/** Configuration key for password. */
	private static final String PASSWORD = "password";

	/** The logger. */
	private static final Logger logger = LoggerFactory.getLogger(JiraConfiguration.class);

	/** Helper for path resolution. */
	private final PathHelper pathHelper;

	/** The configuration for jira connection. */
	private Map<String, String> jiraConfigurationMap = null;

	/**
	 * Default constructor.
	 * 
	 * @param pathHelper the helper for path resolution.
	 */
	JiraConfiguration(final PathHelper pathHelper) {
		this.pathHelper = pathHelper;
	}

	String getConfigFromLocation(final String path) throws IOException {
		final File jqlFile = new File(path + File.separator + ".jira");
		try {
			return FileUtils.readFileToString(jqlFile).trim();
		} catch (final IOException e) {
			logger.trace("Could not get jira config from " + path);
		}
		return "";
	}

	String getPassword() throws IOException {
		return getJiraConfiguration().get(PASSWORD).trim();
	}

	String getUrl() throws IOException {
		return getJiraConfiguration().get(URL).trim();
	}

	String getUsername() throws IOException {
		return getJiraConfiguration().get(USERNAME).trim();
	}

	boolean isEnabled() {
		String jiraUrl;
		try {
			jiraUrl = getUrl();
		} catch (final IOException e) {
			return false;
		}
		return (jiraUrl != null) && !jiraUrl.equals("null");
	}

	boolean isEnabled(final String family, final String application, final String version) {
		final File conf = new File(pathHelper.getVersionPath(family, application, version) + File.separator + ".jira");
		return conf.isFile();
	}

	boolean isLocked() throws IOException {
		final String locked = getJiraConfiguration().get("locked").trim();
		return Boolean.parseBoolean(locked);
	}

	private Map<String, String> getJiraConfiguration() throws IOException {
		if (jiraConfigurationMap == null) {
			final PropertiesLoader loader = new PropertiesLoader(pathHelper);
			jiraConfigurationMap = loader.read(".jira");

			if (logger.isDebugEnabled() && isEnabled()) {
				final Map<String, String> loggerMap = new HashMap<String, String>();
				loggerMap.putAll(jiraConfigurationMap);
				loggerMap.put(PASSWORD, "<redacted>");
				logger.debug("Using Jira configuration: [{}]", loggerMap);
			}
		}
		return jiraConfigurationMap;
	}
}
