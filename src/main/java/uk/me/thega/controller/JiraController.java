package uk.me.thega.controller;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import uk.me.thega.model.util.jira.JiraHelper;

@Controller
@RequestMapping(UrlMappings.ROOT_JIRA)
public class JiraController extends AbstractController {

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public String isJiraEnabled() {
		final JiraHelper jiraHelper = new JiraHelper(getPathHelper());
		return Boolean.toString(jiraHelper.isEnabled());
	}

	@RequestMapping(value = UrlMappings.VERSION, method = RequestMethod.GET)
	@ResponseBody
	public String metadataForVersionGet(@PathVariable final String family, @PathVariable final String product, @PathVariable final String version, final ModelMap model) throws IOException {
		final JiraHelper jiraHelper = new JiraHelper(getPathHelper());
		final Map<String, String> details = jiraHelper.getCachedChangeLogForVersion(family, product, version);
		final StringBuilder sb = new StringBuilder();
		final String url = jiraHelper.getJiraUrl();
		for (final String issueKey : details.keySet()) {
			sb.append("<a href=\"").append(url).append("/browse/").append(issueKey).append("\">").append(issueKey).append("</a>: ");
			sb.append(details.get(issueKey)).append("<br>");
		}
		return sb.toString();
	}
}
