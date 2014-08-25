package uk.me.thega.controller;

import org.springframework.ui.ModelMap;

public class AbstractController {

	protected static final String BASE_DIR = System.getProperty("user.home") + "/repository";

	protected static void populateGet(final ModelMap model) {
		model.addAttribute("productName", "Product Name");
		model.addAttribute("company", "Company Name");
	}
}
