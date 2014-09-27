package uk.me.thega.model.util.jira;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutionException;

import org.apache.commons.io.FileUtils;

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
	public String getChangeLogForVersion(final String family, final String product, final String version) {
		final StringBuilder sb = new StringBuilder();
		try {
			final String familyJql = getJiraConfigFromLocation(false, pathHelper.getFamilyPath(family));
			final String productJql = getJiraConfigFromLocation(false, pathHelper.getProductPath(family, product));
			final String versionJql = getJiraConfigFromLocation(false, pathHelper.getVersionPath(family, product, version));
			final SearchResult searchResult = runJQL(familyJql + " " + productJql + " " + versionJql);

			for (final BasicIssue issue : searchResult.getIssues()) {
				sb.append(getChangeLogForIssue(issue)).append("\n");
			}
		} catch (final Exception e) {
		}
		return sb.toString();
	}

	public boolean isEnabled() {
		final File conf = new File(pathHelper.getRepositoryPath() + File.separator + ".jira");
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
		return issue.getKey() + ": " + realIssue.getSummary();
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

	private String getJiraUrl() throws IOException {
		final String[] jiraConfiguration = getJiraConfiguration();
		return jiraConfiguration[0].trim();
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
		final JiraRestClient restClient = getRestClient();
		final Promise<SearchResult> futureResult = restClient.getSearchClient().searchJql(jql);
		return futureResult.get();
	}

}
