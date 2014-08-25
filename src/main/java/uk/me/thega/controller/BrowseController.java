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

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.me.thega.controller.exception.NotFoundException;
import uk.me.thega.model.metadata.MetadataFactory;
import uk.me.thega.model.metadata.ProductMetadata;
import uk.me.thega.model.util.SizeCalculator;

@Controller
@RequestMapping(UrlMappings.ROOT_BROWSE)
public class BrowseController extends AbstractController {

	private final static SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm dd/MM/yy");

	private static File[] checkAndGetContentsOf(final String path) throws IOException {
		final Resource resource = new FileSystemResource(path);
		final File dir = resource.getFile();
		if (!dir.isDirectory()) {
			throw new NotFoundException("Not Found");
		}
		return dir.listFiles();
	}

	private static void populateFamilyGet(final String family, final ModelMap model) {
		populateGet(model);
		model.addAttribute("family", family);
	}

	private static void populateProductGet(final String family, final String product, final ModelMap model) {
		populateFamilyGet(family, model);
		model.addAttribute("product", product);
	}

	private static void populateVersionGet(final String family, final String product, final String version, final ModelMap model) {
		populateProductGet(family, product, model);
		model.addAttribute("version", version);
	}

	@RequestMapping(value = UrlMappings.FAMILY, method = RequestMethod.GET)
	public String browseFamilyGet(@PathVariable final String family, final ModelMap model) throws IOException, JAXBException {
		populateFamilyGet(family, model);

		final File[] products = checkAndGetContentsOf(BASE_DIR + "/" + family);
		final List<String> list = new ArrayList<String>();
		final Map<String, ProductMetadata> metadataMap = new HashMap<String, ProductMetadata>();
		for (int i = 0; i < products.length; i++) {
			if (products[i].isDirectory()) {
				final String prodName = products[i].getName();
				final ProductMetadata metadata = MetadataFactory.createProductMetadata(products[i]);
				if (metadata != null) {
					metadataMap.put(prodName, metadata);
				}
				list.add(prodName);
			}
		}
		model.addAttribute("products", list);
		model.addAttribute("metadata", metadataMap);

		return "browseFamily";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String browseGet(final ModelMap model) throws IOException {
		populateGet(model);

		final File[] families = checkAndGetContentsOf(BASE_DIR);
		final List<String> leftList = new ArrayList<String>();
		final List<String> rightList = new ArrayList<String>();
		for (int i = 0; i < families.length; i++) {
			final String name = families[i].getName();
			if ((i % 2) == 0) {
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

		final File[] versions = checkAndGetContentsOf(BASE_DIR + "/" + family + "/" + product);
		final List<String> list = new ArrayList<String>();
		for (int i = 0; i < versions.length; i++) {
			if (versions[i].isDirectory()) {
				list.add(versions[i].getName());
			}
		}
		model.addAttribute("versions", list);

		return "browseProduct";
	}

	@RequestMapping(value = UrlMappings.VERSION, method = RequestMethod.GET)
	public String browseVersionGet(@PathVariable final String family, @PathVariable final String product, @PathVariable final String version, final ModelMap model) throws IOException {
		populateVersionGet(family, product, version, model);

		final File[] resources = checkAndGetContentsOf(BASE_DIR + "/" + family + "/" + product + "/" + version);
		final List<String[]> list = new ArrayList<String[]>();
		long totalLen = 0;
		for (int i = 0; i < resources.length; i++) {
			if (resources[i].isFile()) {
				final long len = resources[i].length();
				final long lastModified = resources[i].lastModified();

				final String lenSt = SizeCalculator.getStringSizeLengthFile(len);
				final String lastModifiedSt = dateFormatter.format(new Date(lastModified));
				final String name = resources[i].getName();

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