package uk.me.thega.model.util.jira;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uk.me.thega.model.util.PathHelper;

import com.atlassian.jira.rest.client.JiraRestClient;
import com.atlassian.jira.rest.client.JiraRestClientFactory;
import com.atlassian.jira.rest.client.domain.BasicIssue;
import com.atlassian.jira.rest.client.domain.Issue;
import com.atlassian.jira.rest.client.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;

/**
 * Helper to get change logs from a jira server configured in the base of the repo.
 * 
 * Config should be in the format URL,user,pass in a .jira file.
 * 
 * JQL queries are built by fetching JQL from .jira files in families, products and versions in that order.
 * 
 * @author pwhittlesea
 * 
 */
public class JiraHelper {

	/** The logger. */
	private static final Logger logger = LoggerFactory.getLogger(JiraHelper.class);

	/** Helper for path resolution. */
	private final PathHelper pathHelper;

	/** The configuration for jira connection. */
	private String[] jiraConfiguration = null;

	/** The client for accessing jira. */
	private JiraRestClient restClient = null;

	/**
	 * Default constructor.
	 * 
	 * @param pathHelper
	 *            the helper for path resolution.
	 */
	public JiraHelper(final PathHelper pathHelper) {
		this.pathHelper = pathHelper;
	}

	public void cacheChangeLogForVersion(final String family, final String product, final String version) throws IOException {
		if (isEnabled(family, product, version)) {
			final Map<String, String> issues = getChangeLogForVersion(family, product, version);
			final JSONObject json = new JSONObject(issues);
			final String jsonString = json.toString();
			final String path = getJiraCachePath(family, product, version);

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
	}

	public Map<String, String> getCachedChangeLogForVersion(final String family, final String product, final String version) {
		final Map<String, String> map = new HashMap<String, String>();
		if (isEnabled(family, product, version)) {
			final File cacheFile = new File(getJiraCachePath(family, product, version));
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

		}
		return map;
	}

	/**
	 * Get the change log for a version of a product.
	 * 
	 * This will be in a line separated list.
	 * 
	 * @param family
	 *            the family of the product.
	 * @param product
	 *            the product to get the change log for.
	 * @param version
	 *            the version of the product.
	 * @return the change log.
	 */
	public Map<String, String> getChangeLogForVersion(final String family, final String product, final String version) {
		// This seems a complex data type for storage, it may come in handy later.
		final Map<String, String> keyToInfo = new HashMap<String, String>();

		try {
			final String familyJql = getJiraConfigFromLocation(false, pathHelper.getFamilyPath(family));
			final String productJql = getJiraConfigFromLocation(false, pathHelper.getProductPath(family, product));
			final String versionJql = getJiraConfigFromLocation(false, pathHelper.getVersionPath(family, product, version));
			final SearchResult searchResult = runJQL(familyJql + " " + productJql + " " + versionJql);

			for (final BasicIssue issue : searchResult.getIssues()) {
				keyToInfo.put(issue.getKey(), getChangeLogForIssue(issue));
			}
		} catch (final Exception e) {
		}

		return keyToInfo;
	}

	public String getJiraUrl() throws IOException {
		final String[] jiraConfiguration = getJiraConfiguration();
		return jiraConfiguration[0].trim();
	}

	public boolean isEnabled() {
		final File conf = new File(pathHelper.getRepositoryPath() + File.separator + ".jira");
		return conf.isFile();
	}

	public boolean isEnabled(final String family, final String product, final String version) {
		final File conf = new File(pathHelper.getVersionPath(family, product, version) + File.separator + ".jira");
		return conf.isFile();
	}

	/**
	 * Get the change log for a given {@link BasicIssue}.
	 * 
	 * @param issue
	 *            the issue to get the change log for.
	 * @return the change log in the form "ISSUE#: SUMMARY"
	 * @throws IOException
	 *             if configuring the client fails.
	 * @throws URISyntaxException
	 *             if the URL of the server is wrong.
	 * @throws InterruptedException
	 *             if talking to Jira fails.
	 * @throws ExecutionException
	 *             if talking to Jira fails.
	 */
	private String getChangeLogForIssue(final BasicIssue issue) throws IOException, URISyntaxException, InterruptedException, ExecutionException {
		final JiraRestClient restClient = getRestClient();
		final Promise<Issue> realIssueFutureResult = restClient.getIssueClient().getIssue(issue.getKey());
		final Issue realIssue = realIssueFutureResult.get();
		return realIssue.getSummary();
	}

	private String getJiraCachePath(final String family, final String product, final String version) {
		return pathHelper.getVersionPath(family, product, version) + File.separator + ".jiraCache";
	}

	private String getJiraConfigFromLocation(final boolean canFail, final String path) throws IOException {
		final File jqlFile = new File(path + File.separator + ".jira");
		try {
			return FileUtils.readFileToString(jqlFile).trim();
		} catch (final IOException e) {
			if (canFail) {
				throw e;
			}
		}
		return "";
	}

	private String[] getJiraConfiguration() throws IOException {
		if (jiraConfiguration == null) {
			final String jiraConfContent = getJiraConfigFromLocation(true, pathHelper.getRepositoryPath());
			jiraConfiguration = jiraConfContent.split(",");
		}
		return jiraConfiguration;
	}

	private String getJiraPassword() throws IOException {
		final String[] jiraConfiguration = getJiraConfiguration();
		return jiraConfiguration[2].trim();
	}

	private String getJiraUsername() throws IOException {
		final String[] jiraConfiguration = getJiraConfiguration();
		return jiraConfiguration[1].trim();
	}

	private JiraRestClient getRestClient() throws URISyntaxException, IOException {
		if (restClient == null) {
			final JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
			final URI jiraServerUri = new URI(getJiraUrl());
			restClient = factory.createWithBasicHttpAuthentication(jiraServerUri, getJiraUsername(), getJiraPassword());
		}
		return restClient;
	}

	private SearchResult runJQL(final String jql) throws URISyntaxException, IOException, InterruptedException, ExecutionException {
		logger.trace("JiraHelper: running jql [{}]", jql);
		final JiraRestClient restClient = getRestClient();
		final Promise<SearchResult> futureResult = restClient.getSearchClient().searchJql(jql);
		return futureResult.get();
	}

}
