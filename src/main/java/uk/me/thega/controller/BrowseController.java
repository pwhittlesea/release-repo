package uk.me.thega.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/browse")
public class BrowseController extends AbstractController {

	@RequestMapping(value = "/{family}", method = RequestMethod.GET)
	public String browseFamilyGet(@PathVariable final String family, final ModelMap model) {
		browseGet(model);
		model.addAttribute("family", family);
		// Spring uses InternalResourceViewResolver and return back browseFamily.jsp
		return "browseFamily";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String browseGet(final ModelMap model) {
		controllerGet(model);
		// Spring uses InternalResourceViewResolver and return back browse.jsp
		return "browse";
	}

	@RequestMapping(value = "/{family}/{product}", method = RequestMethod.GET)
	public String browseProductGet(@PathVariable final String family, @PathVariable final String product, final ModelMap model) {
		browseFamilyGet(family, model);
		model.addAttribute("product", product);
		// Spring uses InternalResourceViewResolver and return back browseFamily.jsp
		return "browseProduct";
	}

	@RequestMapping(value = "/{family}/{product}/{version}", method = RequestMethod.GET)
	public String browseVersionGet(@PathVariable final String family, @PathVariable final String product, @PathVariable final String version, final ModelMap model) {
		browseProductGet(family, product, model);
		model.addAttribute("version", version);
		// Spring uses InternalResourceViewResolver and return back browseFamily.jsp
		return "browseVersion";
	}

}