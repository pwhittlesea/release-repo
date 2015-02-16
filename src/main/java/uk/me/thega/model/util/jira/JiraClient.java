package uk.me.thega.model.util.jira;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.api.JiraRestClientFactory;
import com.atlassian.jira.rest.client.api.domain.BasicIssue;
import com.atlassian.jira.rest.client.api.domain.Issue;
import com.atlassian.jira.rest.client.api.domain.SearchResult;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import com.atlassian.util.concurrent.Promise;

/**
 * Manage cached Jira JQL queries.
 * 
 * JQL queries are built by fetching JQL from .jira files in families, applications and versions in that order.
 * 
 * @author pwhittlesea
 * 
 */
class JiraClient {

	/** The logger. */
	private static final Logger logger = LoggerFactory.getLogger(JiraClient.class);

	/** The client for accessing jira. */
	private JiraRestClient restClient = null;

	private final JiraConfiguration jiraConfiguration;

	JiraClient(final JiraConfiguration jiraConfiguration) {
		this.jiraConfiguration = jiraConfiguration;
	}

	/**
	 * Tear down.
	 */
	void close() {
		logger.debug("Shutting down JiraHelper");
		IOUtils.closeQuietly(restClient);
	}

	/**
	 * Get the change log for a version of a application.
	 * 
	 * This will be in a line separated list.
	 * 
	 * @param jql the jql to run
	 * @return the change log.
	 */
	Map<String, String> getChangeLogForVersion(final String jql) {
		final Map<String, String> keyToInfo = new HashMap<String, String>();

		try {
			final SearchResult searchResult = runJQL(jql);

			for (final BasicIssue issue : searchResult.getIssues()) {
				keyToInfo.put(issue.getKey(), getChangeLogForIssue(issue));
			}
		} catch (final Exception e) {
		}

		return keyToInfo;
	}

	/**
	 * Get the change log for a given {@link BasicIssue}.
	 * 
	 * @param issue the issue to get the change log for.
	 * @return the change log in the form "ISSUE#: SUMMARY"
	 * @throws IOException if configuring the client fails.
	 * @throws URISyntaxException if the URL of the server is wrong.
	 * @throws InterruptedException if talking to Jira fails.
	 * @throws ExecutionException if talking to Jira fails.
	 */
	private String getChangeLogForIssue(final BasicIssue issue) throws IOException, URISyntaxException, InterruptedException, ExecutionException {
		final JiraRestClient restClient = getRestClient();
		final Promise<Issue> realIssueFutureResult = restClient.getIssueClient().getIssue(issue.getKey());
		final Issue realIssue = realIssueFutureResult.get();
		return realIssue.getSummary();
	}

	private JiraRestClient getRestClient() throws URISyntaxException, IOException {
		if (restClient == null) {
			final JiraRestClientFactory factory = new AsynchronousJiraRestClientFactory();
			final String url = jiraConfiguration.getUrl();
			final String username = jiraConfiguration.getUsername();
			final String password = jiraConfiguration.getPassword();
			restClient = factory.createWithBasicHttpAuthentication(new URI(url), username, password);
		}
		return restClient;
	}

	private SearchResult runJQL(final String jql) throws URISyntaxException, IOException, InterruptedException, ExecutionException {
		logger.debug("JiraHelper: running jql [{}]", jql);
		final JiraRestClient restClient = getRestClient();
		final Promise<SearchResult> futureResult = restClient.getSearchClient().searchJql(jql);
		return futureResult.get();
	}

}
