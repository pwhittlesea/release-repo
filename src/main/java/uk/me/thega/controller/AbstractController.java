package uk.me.thega.controller;

import org.springframework.ui.ModelMap;

public class AbstractController {

	protected void controllerGet(final ModelMap model) {
		model.addAttribute("productName", "Product Name");
	}
}
