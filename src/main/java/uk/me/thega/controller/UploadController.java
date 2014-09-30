package uk.me.thega.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping(UrlMappings.ROOT_UPLOAD)
public class UploadController extends AbstractController {

	@RequestMapping(value = UrlMappings.FILE_PATH, method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void uploadPost(@PathVariable final String family, @PathVariable final String product, @PathVariable final String version, @PathVariable final String fileName, @PathVariable final String extension, @RequestParam("file") final MultipartFile file) throws IOException {
		createParentFolder(family, product, version);

		// Create the file on server
		final File serverFile = new File(getPathHelper().getResourcePath(family, product, version, fileName + '.' + extension));
		final BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverFile));
		stream.write(file.getBytes());
		stream.close();
	}

	@RequestMapping(value = UrlMappings.FILE_PATH, method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	public void uploadPut(@PathVariable final String family, @PathVariable final String product, @PathVariable final String version, @PathVariable final String fileName, @PathVariable final String extension, final HttpServletRequest request, final HttpServletResponse response) throws IOException {
		final InputStream inputStream = request.getInputStream();
		if (inputStream != null) {
			createParentFolder(family, product, version);

			// Create the file on server
			final File serverFile = new File(getPathHelper().getResourcePath(family, product, version, fileName + '.' + extension));
			final BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(serverFile));

			IOUtils.copyLarge(inputStream, outputStream);
			outputStream.flush();
			outputStream.close();
		}
	}

	private void createParentFolder(final String family, final String product, final String version) {
		final File dir = new File(getPathHelper().getVersionPath(family, product, version));
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}
}