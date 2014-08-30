package uk.me.thega.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;

import uk.me.thega.model.util.PathHelper;
import uk.me.thega.model.util.RepositoryFileSystem;

public abstract class AbstractController {

	static void populateGet(final ModelMap model) {
		model.addAttribute("productName", "Product Name");
		model.addAttribute("company", "Company Name");
	}

	@Autowired
	private PathHelper pathHelper;

	@Autowired
	private RepositoryFileSystem fileSystemUtil;

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

	/**
	 * Set the model for file system interaction.
	 * 
	 * @param fileSystemUtil
	 *            the fileSystemUtil to set
	 */
	void setFileSystemUtil(final RepositoryFileSystem fileSystemUtil) {
		this.fileSystemUtil = fileSystemUtil;
	}

	/**
	 * Set the path helper
	 * 
	 * @param pathHelper
	 *            the pathHelper to set
	 */
	void setPathHelper(final PathHelper pathHelper) {
		this.pathHelper = pathHelper;
	}
}
