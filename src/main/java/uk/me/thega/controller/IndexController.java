package uk.me.thega.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(UrlMappings.ROOT_INDEX)
public class IndexController extends AbstractController {

    @RequestMapping(method = RequestMethod.GET)
    public String indexGet(final ModelMap model) {
        populateGet(model);
        return "index";
    }

}
