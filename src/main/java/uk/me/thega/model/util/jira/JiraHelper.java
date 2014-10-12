package uk.me.thega.model.util.jira;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import uk.me.thega.model.util.PathHelper;

/**
 * Helper to get change logs from a jira server configured in the base of the repo.
 * 
 * JQL queries are built by fetching JQL from .jira files in families, applications and versions in that order.
 * 
 * @author pwhittlesea
 * 
 */
public class JiraHelper {

	/** File name of cache. */
	private static final String JIRA_CACHE = ".jiraCache";

	/** Helper for path resolution. */
	private final PathHelper pathHelper;

	/** The configuration for jira connection. */
	private final JiraConfiguration jiraConfiguration;

	/** Client for jira JQL. */
	private final JiraClient jiraClient;

	/**
	 * Default constructor.
	 * 
	 * @param pathHelper the helper for path resolution.
	 */
	public JiraHelper(final PathHelper pathHelper) {
		this.pathHelper = pathHelper;
		this.jiraConfiguration = new JiraConfiguration(pathHelper);
		this.jiraClient = new JiraClient(jiraConfiguration);
	}

	public void cacheChangeLog(final String family, final String application, final String version) throws IOException {
		if (jiraConfiguration.isEnabled(family, application, version)) {
			final String jql = getJql(family, application, version);
			final String path = pathHelper.getVersionPath(family, application, version) + File.separator + JIRA_CACHE;

			final Map<String, String> changeLog = jiraClient.getChangeLogForVersion(jql);
			JiraCache.put(changeLog, path);
		}
	}

	/**
	 * Tear down.
	 */
	public void close() {
		jiraClient.close();
	}

	public Map<String, String> getChangeLog(final String family, final String application, final String version) {
		final Map<String, String> map = new HashMap<String, String>();
		if (jiraConfiguration.isEnabled(family, application, version)) {
			final String path = pathHelper.getVersionPath(family, application, version) + File.separator + JIRA_CACHE;
			return JiraCache.get(path);
		}
		return map;
	}

	public String getJiraUrl() throws IOException {
		return jiraConfiguration.getUrl();
	}

	public String getJql(final String family) throws IOException {
		return jiraConfiguration.getConfigFromLocation(pathHelper.getFamilyPath(family));
	}

	public String getJql(final String family, final String application) throws IOException {
		final String applicationPath = pathHelper.getApplicationPath(family, application);
		final String familyJql = getJql(family);
		final String applicationJql = jiraConfiguration.getConfigFromLocation(applicationPath);
		return familyJql + " " + applicationJql;
	}

	public String getJql(final String family, final String application, final String version) throws IOException {
		final String versionPath = pathHelper.getVersionPath(family, application, version);
		final String applicationJql = getJql(family, application);
		final String versionJql = jiraConfiguration.getConfigFromLocation(versionPath);
		return applicationJql + " " + versionJql;
	}

	public boolean isEnabled() {
		return jiraConfiguration.isEnabled();
	}

	public boolean isJiraLocked() throws IOException {
		return jiraConfiguration.isLocked();
	}

}
