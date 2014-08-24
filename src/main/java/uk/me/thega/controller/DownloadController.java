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
@RequestMapping("/download")
public class DownloadController extends AbstractController {

	private static final String BASE_DIR = System.getProperty("user.home") + "/repository";

	@RequestMapping(value = "/{family}/{product}/{version}/{file}.{extension}", method = RequestMethod.GET)
	public void downloadExtensionGet(@PathVariable final String family, @PathVariable final String product, @PathVariable final String version, @PathVariable final String file, @PathVariable final String extension, final HttpServletResponse response) throws IOException {
		final String fileToDownload = BASE_DIR + '/' + family + '/' + product + '/' + version + '/' + file + '.' + extension;
		final File download = new File(fileToDownload);
		if (!download.isFile()) {
			throw new NotFoundException("File not found: " + fileToDownload);
		}
		final InputStream is = new FileInputStream(download);
		IOUtils.copy(is, response.getOutputStream());
		response.flushBuffer();
		response.setHeader("Content-Type", new MimetypesFileTypeMap().getContentType(download));
	}
}