package uk.me.thega.controller;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import uk.me.thega.controller.exception.AccessDeniedException;
import uk.me.thega.model.properties.PropertiesLoader;
import uk.me.thega.model.repository.Repository;
import uk.me.thega.model.util.PathHelper;

public abstract class AbstractController {

	/** The logger. */
	private final static Logger logger = LoggerFactory.getLogger(AbstractController.class);

	@Autowired
	private PathHelper pathHelper;

	@Autowired
	private Repository repository;

	private String applicationName = null;

	private String companyName = null;

	/**
	 * Handle permission exception.
	 * 
	 * @param req the request.
	 * @param exception the exception.
	 * @return the view.
	 */
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(reason = "Access Denied", value = HttpStatus.FORBIDDEN)
	public ModelAndView handleError(final HttpServletRequest req, final AccessDeniedException exception) {
		logger.debug("Gracefully handling: {}", exception.getMessage());
		final ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("exception", exception);
		modelAndView.setViewName("error");
		return modelAndView;
	}

	/**
	 * Get the path helper
	 * 
	 * @return the pathHelper
	 */
	protected PathHelper getPathHelper() {
		return pathHelper;
	}

	/**
	 * Get the repository object.
	 * 
	 * @return the respository
	 */
	protected Repository getRepository() {
		return repository;
	}

	protected void populateGet(final ModelMap model) {
		initNamesFromConfig();
		model.addAttribute("applicationName", applicationName);
		model.addAttribute("company", companyName);
	}

	/**
	 * Set the repository.
	 * 
	 * @param repository the repository to set
	 */
	void setFileSystemUtil(final Repository repository) {
		this.repository = repository;
	}

	/**
	 * Set the path helper
	 * 
	 * @param pathHelper the pathHelper to set
	 */
	void setPathHelper(final PathHelper pathHelper) {
		this.pathHelper = pathHelper;
	}

	/**
	 * If the names of the application are not initialised then fetch them from the config.
	 */
	private void initNamesFromConfig() {
		if ((applicationName == null) || (companyName == null)) {
			try {
				final PropertiesLoader loader = new PropertiesLoader(pathHelper);
				final Map<String, String> properties = loader.read(".config");
				applicationName = properties.get("applicationName");
				companyName = properties.get("companyName");
			} catch (final IOException e) {
				// roll back to defaults
				applicationName = "n/a";
				companyName = "n/a";
			}
		}
	}
}
