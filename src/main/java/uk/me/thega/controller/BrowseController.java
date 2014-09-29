package uk.me.thega.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.me.thega.model.util.MetadataHelper;
import uk.me.thega.model.util.SizeCalculator;

@Controller
@RequestMapping(UrlMappings.ROOT_BROWSE)
public class BrowseController extends AbstractController {

	@RequestMapping(value = UrlMappings.FAMILY, method = RequestMethod.GET)
	public String browseFamilyGet(@PathVariable final String family, final ModelMap model) throws IOException, JAXBException {
		populateFamilyGet(family, model);

		final List<String> list = new ArrayList<String>();
		final Map<String, Boolean> isDiscontinued = new HashMap<String, Boolean>();
		for (final File product : getFileSystemUtil().products(family)) {
			final String prodName = product.getName();
			list.add(prodName);
			isDiscontinued.put(prodName, MetadataHelper.isDiscontinued(product.getPath()));
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

		for (final File family : getFileSystemUtil().families()) {
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

		final List<File> versions;
		if (product.equals("all")) {
			versions = getFileSystemUtil().allVersions(family);
		} else {
			versions = getFileSystemUtil().versions(family, product);
		}

		final Map<String, String> statuses = new HashMap<String, String>();
		for (final File version : versions) {
			final String name = version.getName();
			if (version.isDirectory() && !list.contains(name)) {
				list.add(name);
				statuses.put(name, MetadataHelper.status(version.getPath()));
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

		final List<File> resources;
		if (product.equals("all")) {
			resources = getFileSystemUtil().allResources(family, version);
		} else {
			resources = getFileSystemUtil().resources(family, product, version);
		}

		long totalLen = 0;
		final List<String> excluded = MetadataHelper.excludedFiles();
		for (final File resource : resources) {
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