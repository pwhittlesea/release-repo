package uk.me.thega.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.me.thega.model.repository.Family;
import uk.me.thega.model.repository.Product;
import uk.me.thega.model.repository.Resource;
import uk.me.thega.model.repository.Version;
import uk.me.thega.model.util.MetadataHelper;
import uk.me.thega.model.util.SizeCalculator;
import uk.me.thega.model.util.jira.JiraHelper;

@Controller
@RequestMapping(UrlMappings.ROOT_BROWSE)
public class BrowseController extends AbstractController {

	@Autowired
	private JiraHelper jiraHelper;

	@RequestMapping(value = UrlMappings.FAMILY, method = RequestMethod.GET)
	public String browseFamilyGet(@PathVariable final String family, final ModelMap model) throws IOException, JAXBException {
		populateFamilyGet(family, model);

		final List<String> list = new ArrayList<String>();
		final Map<String, Boolean> isDiscontinued = new HashMap<String, Boolean>();
		for (final Product product : getRepository().products(family)) {
			final String prodName = product.getName();
			list.add(prodName);
			isDiscontinued.put(prodName, product.isDiscontinued());
		}
		model.addAttribute("products", list);
		model.addAttribute("discontinued", isDiscontinued);

		return "browseFamily";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String browseGet(final ModelMap model) throws IOException {
		populateGet(model);

		final List<String> leftList = new ArrayList<String>();
		final List<String> rightList = new ArrayList<String>();
		int i = 0;

		for (final Family family : getRepository().families()) {
			final String name = family.getName();
			if ((i++ % 2) == 0) {
				leftList.add(name);
			} else {
				rightList.add(name);
			}
		}
		model.addAttribute("leftList", leftList);
		model.addAttribute("rightList", rightList);

		return "browse";
	}

	@RequestMapping(value = UrlMappings.PRODUCT, method = RequestMethod.GET)
	public String browseProductGet(@PathVariable final String family, @PathVariable final String product, final ModelMap model) throws IOException {
		populateProductGet(family, product, model);

		final List<String> list = new ArrayList<String>();

		final List<Version> versions;
		if (product.equals("all")) {
			versions = getRepository().allVersions(family);
		} else {
			versions = getRepository().versions(family, product);
		}

		final Map<String, String> statuses = new HashMap<String, String>();
		for (final Version version : versions) {
			final String name = version.getName();
			if (!list.contains(name)) {
				list.add(name);
				statuses.put(name, version.status());
			}
		}
		model.addAttribute("versions", list);
		model.addAttribute("status", statuses);

		return "browseProduct";
	}

	@RequestMapping(value = UrlMappings.VERSION, method = RequestMethod.GET)
	public String browseVersionGet(@PathVariable final String family, @PathVariable final String product, @PathVariable final String version, final ModelMap model) throws IOException {
		populateVersionGet(family, product, version, model);

		final SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm dd/MM/yy");
		final List<String[]> list = new ArrayList<String[]>();

		final List<Resource> resources;
		if (product.equals("all")) {
			resources = getRepository().allResources(family, version);
		} else {
			resources = getRepository().resources(family, product, version);
		}

		long totalLen = 0;
		final List<String> excluded = MetadataHelper.excludedFiles();
		for (final Resource resource : resources) {
			if (!excluded.contains(resource.getName()) && resource.isFile()) {
				final long len = resource.length();
				final long lastModified = resource.lastModified();

				final String lenSt = SizeCalculator.getStringSizeLengthFile(len);
				final String lastModifiedSt = dateFormatter.format(new Date(lastModified));
				final String name = resource.getName();

				final String[] resourceSpec = { name, lenSt, lastModifiedSt };
				list.add(resourceSpec);

				// Sums
				totalLen += len;
			}
		}
		model.addAttribute("resources", list);
		model.addAttribute("totalSize", SizeCalculator.getStringSizeLengthFile(totalLen));

		// Do the jira!
		final Map<String, String> changeLog = jiraHelper.getCachedChangeLogForVersion(family, product, version);
		final Map<String, String> shortLog = new HashMap<String, String>();

		// Take the first 5 off the change log and add to a short log
		final List<String> tempList = new ArrayList<String>(changeLog.keySet());
		Collections.sort(tempList);
		for (int i = 1; i <= 5; i++) {
			if (i <= tempList.size()) {
				final String key = tempList.get(i);
				shortLog.put(key, changeLog.get(key));
				changeLog.remove(key);
			}
		}

		model.addAttribute("jiraBaseUrl", jiraHelper.getJiraUrl());
		model.addAttribute("jiraShortList", shortLog);
		model.addAttribute("jiraLongList", changeLog);

		final String parentJQL = jiraHelper.getJQLForProduct(family, product);
		final String childJQL = jiraHelper.getJQLForVersion(family, product, version);
		model.addAttribute("parentJQL", parentJQL.trim());
		model.addAttribute("childJQL", childJQL.replace(parentJQL, "").trim());

		return "browseVersion";
	}

	private void populateFamilyGet(final String family, final ModelMap model) {
		populateGet(model);
		model.addAttribute("family", family);
	}

	private void populateProductGet(final String family, final String product, final ModelMap model) {
		populateFamilyGet(family, model);
		model.addAttribute("product", product);
	}

	private void populateVersionGet(final String family, final String product, final String version, final ModelMap model) {
		populateProductGet(family, product, model);
		model.addAttribute("version", version);
	}
}