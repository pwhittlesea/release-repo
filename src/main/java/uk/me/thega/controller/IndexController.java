package uk.me.thega.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/")
public class IndexController extends AbstractController {

	@RequestMapping(method = RequestMethod.GET)
	public String indexGet(final ModelMap model) {
		controllerGet(model);
		model.addAttribute("company", "Company Name");
		// Spring uses InternalResourceViewResolver and return back index.jsp
		return "index";
	}

}