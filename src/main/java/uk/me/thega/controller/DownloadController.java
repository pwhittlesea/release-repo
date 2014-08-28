package uk.me.thega.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.activation.MimetypesFileTypeMap;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import uk.me.thega.controller.exception.NotFoundException;

@Controller
@RequestMapping(UrlMappings.ROOT_DOWNLOAD)
public class DownloadController extends AbstractController {

	@RequestMapping(value = UrlMappings.FILE_PATH, method = RequestMethod.GET)
	public void downloadResourceGet(@PathVariable final String family, @PathVariable final String product, @PathVariable final String version, @PathVariable final String fileName, @PathVariable final String extension, final HttpServletResponse response) throws IOException {
		final File download;
		if (product.equals("all")) {
			download = FILE_SYSTEM_UTIL.findResource(family, version, fileName + '.' + extension);
		} else {
			final String fileToDownload = PATH_HELPER.getResourcePath(family, product, version, fileName + '.' + extension);
			download = new File(fileToDownload);
			if (!download.isFile()) {
				throw new NotFoundException("File not found: " + fileToDownload);
			}
		}
		final InputStream is = new FileInputStream(download);
		IOUtils.copy(is, response.getOutputStream());
		response.flushBuffer();
		response.setHeader("Content-Type", new MimetypesFileTypeMap().getContentType(download));
	}
}