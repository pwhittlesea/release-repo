package uk.me.thega.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.me.thega.controller.exception.NotFoundException;

@Controller
@RequestMapping("/browse")
public class BrowseController extends AbstractController {

	private static final String BASE_DIR = System.getProperty("user.home") + "/repository";

	@RequestMapping(value = "/{family}", method = RequestMethod.GET)
	public String browseFamilyGet(@PathVariable final String family, final ModelMap model) throws IOException {
		populateFamilyGet(family, model);

		checkAndGetContentsOf(BASE_DIR);
		final File[] products = checkAndGetContentsOf(BASE_DIR + "/" + family);
		final List<String> list = new ArrayList<String>();
		for (int i = 0; i < products.length; i++) {
			if (products[i].isDirectory()) {
				list.add(products[i].getName());
			}
		}
		model.addAttribute("list", list.toArray(new String[list.size()]));

		return "browseFamily";
	}

	@RequestMapping(method = RequestMethod.GET)
	public String browseGet(final ModelMap model) throws IOException {
		populateGet(model);

		final File[] families = checkAndGetContentsOf(BASE_DIR);

		model.addAttribute("oddList", everyOther(0, new ArrayList<String>(), families));
		model.addAttribute("evenList", everyOther(1, new ArrayList<String>(), families));

		return "browse";
	}

	@RequestMapping(value = "/{family}/{product}", method = RequestMethod.GET)
	public String browseProductGet(@PathVariable final String family, @PathVariable final String product, final ModelMap model) throws IOException {
		populateProductGet(family, product, model);

		checkAndGetContentsOf(BASE_DIR);
		checkAndGetContentsOf(BASE_DIR + "/" + family);
		final File[] versions = checkAndGetContentsOf(BASE_DIR + "/" + family + "/" + product);
		final List<String> list = new ArrayList<String>();
		for (int i = 0; i < versions.length; i++) {
			if (versions[i].isDirectory()) {
				list.add(versions[i].getName());
			}
		}
		model.addAttribute("list", list.toArray(new String[list.size()]));

		return "browseProduct";
	}

	@RequestMapping(value = "/{family}/{product}/{version}", method = RequestMethod.GET)
	public String browseVersionGet(@PathVariable final String family, @PathVariable final String product, @PathVariable final String version, final ModelMap model) throws IOException {
		populateVersionGet(family, product, version, model);

		checkAndGetContentsOf(BASE_DIR);
		checkAndGetContentsOf(BASE_DIR + "/" + family);
		checkAndGetContentsOf(BASE_DIR + "/" + family + "/" + product);
		final File[] resources = checkAndGetContentsOf(BASE_DIR + "/" + family + "/" + product + "/" + version);
		final List<String> list = new ArrayList<String>();
		for (int i = 0; i < resources.length; i++) {
			if (resources[i].isFile()) {
				list.add(resources[i].getName());
			}
		}
		model.addAttribute("list", list.toArray(new String[list.size()]));

		return "browseVersion";
	}

	private File[] checkAndGetContentsOf(final String path) throws IOException {
		final Resource resource = new FileSystemResource(path);
		final File dir = resource.getFile();
		if (!dir.isDirectory()) {
			throw new NotFoundException("Not Found");
		}
		return dir.listFiles();
	}

	private String[] everyOther(final int offset, final List<String> outputArray, final File[] inputArray) {
		for (int i = offset; i < inputArray.length; i += 2) {
			if (inputArray[i].isDirectory()) {
				outputArray.add(inputArray[i].getName());
			}
		}
		return outputArray.toArray(new String[outputArray.size()]);
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