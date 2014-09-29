package uk.me.thega.cron;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import uk.me.thega.controller.exception.NotFoundException;
import uk.me.thega.model.util.PathHelper;
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
	private PathHelper pathHelper;

	/**
	 * Run Jira caching every 15 mins.
	 */
	@Scheduled(fixedDelay = MIN * 15)
	public void runJiraCache() {
		logger.debug("Scheduler: Running Jira Caching task");
		final JiraHelper jiraHelper = new JiraHelper(pathHelper);
		final RepositoryFileSystem fileSystem = new RepositoryFileSystem(pathHelper);
		try {
			for (final File familyFile : fileSystem.families()) {
				final String family = familyFile.getName();
				for (final File productFile : fileSystem.products(family)) {
					final String product = productFile.getName();
					for (final File versionFile : fileSystem.versions(family, product)) {
						final String version = versionFile.getName();
						if (jiraHelper.isEnabled(family, product, version)) {
							final Map<String, String> issues = jiraHelper.getChangeLogForVersion(family, product, version);
							final JSONObject json = new JSONObject(issues);
							final String path = pathHelper.getVersionPath(family, product, version) + File.separator + ".jiraCache";

							// replace file
							final File outputFile = new File(path);
							outputFile.delete();
							outputFile.createNewFile();

							// write cache
							final FileWriter fileWriter = new FileWriter(outputFile, false);
							fileWriter.write(json.toString());
							fileWriter.close();
						}
					}
				}
			}
		} catch (final NotFoundException e) {
			logger.error("Scheduler: Could not finish Jira Cache", e);
		} catch (final FileNotFoundException e) {
			logger.error("Scheduler: Could not finish Jira Cache", e);
		} catch (final IOException e) {
			logger.error("Scheduler: Could not finish Jira Cache", e);
		}
	}
}
