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

import uk.me.thega.model.metadata.MetadataFactory;
import uk.me.thega.model.metadata.ProductMetadata;
import uk.me.thega.model.util.SizeCalculator;

@Controller
@RequestMapping(UrlMappings.ROOT_BROWSE)
public class BrowseController extends AbstractController {

	private final static SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm dd/MM/yy");

	static void populateFamilyGet(final String family, final ModelMap model) {
		populateGet(model);
		model.addAttribute("family", family);
	}

	static void populateProductGet(final String family, final String product, final ModelMap model) {
		populateFamilyGet(family, model);
		model.addAttribute("product", product);
	}

	static void populateVersionGet(final String family, final String product, final String version, final ModelMap model) {
		populateProductGet(family, product, model);
		model.addAttribute("version", version);
	}

	@RequestMapping(value = UrlMappings.FAMILY, method = RequestMethod.GET)
	public String browseFamilyGet(@PathVariable final String family, final ModelMap model) throws IOException, JAXBException {
		populateFamilyGet(family, model);

		final List<String> list = new ArrayList<String>();
		final Map<String, ProductMetadata> metadataMap = new HashMap<String, ProductMetadata>();
		for (final File product : getFileSystemUtil().products(family)) {
			final String prodName = product.getName();
			final ProductMetadata metadata = MetadataFactory.createProductMetadata(product);
			if (metadata != null) {
				metadataMap.put(prodName, metadata);
			}
			list.add(prodName);
		}
		model.addAttribute("products", list);
		model.addAttribute("metadata", metadataMap);

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

		for (final File version : versions) {
			final String name = version.getName();
			if (version.isDirectory() && !list.contains(name)) {
				list.add(name);
			}
		}
		model.addAttribute("versions", list);

		return "browseProduct";
	}

	@RequestMapping(value = UrlMappings.VERSION, method = RequestMethod.GET)
	public String browseVersionGet(@PathVariable final String family, @PathVariable final String product, @PathVariable final String version, final ModelMap model) throws IOException {
		populateVersionGet(family, product, version, model);

		final List<String[]> list = new ArrayList<String[]>();

		final List<File> resources;
		if (product.equals("all")) {
			resources = getFileSystemUtil().allResources(family, version);
		} else {
			resources = getFileSystemUtil().resources(family, product, version);
		}

		long totalLen = 0;
		for (final File resource : resources) {
			if (resource.isFile()) {
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
}