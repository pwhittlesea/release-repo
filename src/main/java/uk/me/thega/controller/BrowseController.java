package uk.me.thega.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.me.thega.controller.exception.NotFoundException;
import uk.me.thega.model.Named;
import uk.me.thega.model.metadata.ProductMetadata;
import uk.me.thega.model.util.MetadataUnmarshaller;

@Controller
@RequestMapping("/browse")
public class BrowseController extends AbstractController {

	private static final String BASE_DIR = System.getProperty("user.home") + "/repository";

	@RequestMapping(value = "/{family}", method = RequestMethod.GET)
	public String browseFamilyGet(@PathVariable final String family, final ModelMap model) throws IOException, JAXBException {
		populateFamilyGet(family, model);

		final File[] products = checkAndGetContentsOf(BASE_DIR + "/" + family);
		final List<ProductMetadata> list = new ArrayList<ProductMetadata>();
		for (int i = 0; i < products.length; i++) {
			if (products[i].isDirectory()) {
				list.add(getMetadataFromFolder(products[i], ProductMetadata.class));
			}
		}
		model.addAttribute("list", list.toArray(new ProductMetadata[list.size()]));

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

	@RequestMapping(value = "/{family}/{product}", method = RequestMethod.GET)
	public String browseProductGet(@PathVariable final String family, @PathVariable final String product, final ModelMap model) throws IOException {
		populateProductGet(family, product, model);

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

	private <T extends Named> T getMetadataFromFolder(final File folder, final Class<T> clazz) throws JAXBException {
		final T metadata = MetadataUnmarshaller.un(folder.getPath() + "/metadata.xml", clazz);
		metadata.setName(folder.getName());
		return metadata;

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