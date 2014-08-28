package uk.me.thega.controller;

import org.springframework.ui.ModelMap;

import uk.me.thega.model.util.RepositoryFileSystem;
import uk.me.thega.model.util.PathHelper;

public class AbstractController {

	protected static final PathHelper PATH_HELPER = new PathHelper(System.getProperty("user.home") + "/repository");

	protected static final RepositoryFileSystem FILE_SYSTEM_UTIL = new RepositoryFileSystem(PATH_HELPER);

	protected static void populateGet(final ModelMap model) {
		model.addAttribute("productName", "Product Name");
		model.addAttribute("company", "Company Name");
	}
}
