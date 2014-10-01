package uk.me.thega.cron;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import uk.me.thega.model.util.RepositoryFileSystem;
import uk.me.thega.model.util.jira.JiraHelper;

/**
 * Cron scheduler.
 * 
 * @author pwhittlesea
 * 
 */
@Component
public class Scheduler {

	/** Logger. */
	private static final Logger logger = LoggerFactory.getLogger(Scheduler.class);

	/** One Minute. */
	private final long MIN = 1000 * 60;

	@Autowired
	private RepositoryFileSystem fileSystem;

	@Autowired
	private JiraHelper jiraHelper;

	/**
	 * Run Jira caching every 15 mins.
	 */
	@Scheduled(fixedDelay = MIN * 15)
	public void runJiraCache() {
		logger.debug("Scheduler: Running Jira Caching task");
		try {
			for (final File familyFile : fileSystem.families()) {
				final String family = familyFile.getName();
				for (final File productFile : fileSystem.products(family)) {
					final String product = productFile.getName();
					for (final File versionFile : fileSystem.versions(family, product)) {
						final String version = versionFile.getName();
						jiraHelper.cacheChangeLogForVersion(family, product, version);
					}
				}
			}
		} catch (final IOException e) {
			logger.error("Scheduler: Could not finish Jira Cache", e);
		}
	}
}
