package uk.me.thega.controller;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import uk.me.thega.controller.exception.AccessDeniedException;
import uk.me.thega.model.util.PathHelper;
import uk.me.thega.model.util.RepositoryFileSystem;

public abstract class AbstractController {

	/** The logger. */
	private final static Logger logger = LoggerFactory.getLogger(AbstractController.class);

	@Autowired
	private PathHelper pathHelper;

	@Autowired
	private RepositoryFileSystem fileSystemUtil;

	private String productName = null;

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
	 * Get the util for file system interaction.
	 * 
	 * @return the fileSystemUtil
	 */
	protected RepositoryFileSystem getFileSystemUtil() {
		return fileSystemUtil;
	}

	/**
	 * Get the path helper
	 * 
	 * @return the pathHelper
	 */
	protected PathHelper getPathHelper() {
		return pathHelper;
	}

	protected void populateGet(final ModelMap model) {
		initNamesFromConfig();
		model.addAttribute("productName", productName);
		model.addAttribute("company", companyName);
	}

	/**
	 * Set the model for file system interaction.
	 * 
	 * @param fileSystemUtil the fileSystemUtil to set
	 */
	void setFileSystemUtil(final RepositoryFileSystem fileSystemUtil) {
		this.fileSystemUtil = fileSystemUtil;
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
	 * If the names of the product are not initialised then fetch them from the config.
	 */
	private void initNamesFromConfig() {
		if ((productName == null) || (companyName == null)) {
			// Defaults
			productName = "Product Name";
			companyName = "Company Name";

			try {
				final String configPath = pathHelper.getRepositoryPath() + File.separator + ".config";
				final String configContent = FileUtils.readFileToString(new File(configPath));
				final String[] config = configContent.split(",");

				if (config.length == 2) {
					productName = config[0];
					companyName = config[1];
				}
			} catch (final IOException e) {
				// roll back to defaults
			}
		}
	}
}
