package uk.me.thega.cron;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import uk.me.thega.model.repository.Application;
import uk.me.thega.model.repository.Family;
import uk.me.thega.model.repository.Repository;
import uk.me.thega.model.repository.Version;
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
	private Repository repository;

	@Autowired
	private JiraHelper jiraHelper;

	/**
	 * Run Jira caching every 15 mins.
	 */
	@Scheduled(fixedDelay = MIN * 15)
	public void runJiraCache() {
		if (jiraHelper.isEnabled()) {
			logger.debug("Scheduler: Running Jira Caching task");
			try {
				for (final Family family : repository.families()) {
					final String familyName = family.getName();
					for (final Application application : repository.applications(familyName)) {
						final String applicationName = application.getName();
						for (final Version version : repository.versions(familyName, applicationName)) {
							jiraHelper.cacheChangeLog(familyName, applicationName, version.getName());
						}
					}
				}
			} catch (final IOException e) {
				logger.error("Scheduler: Could not finish Jira Cache", e);
			}
		}
	}
}
