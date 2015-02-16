package uk.me.thega.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uk.me.thega.controller.exception.NotFoundException;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

@Controller
@RequestMapping(UrlMappings.ROOT_UPLOAD)
public class UploadController extends AbstractController {

    /** The logger. */
    private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

    @RequestMapping(method = RequestMethod.GET)
    public String uploadGet(final ModelMap model) throws NotFoundException {
        populateGet(model);
        model.addAttribute("families", getRepository().families());
        return "upload";
    }

    @RequestMapping(value = "/{family}/{application}/{version}/new", method = RequestMethod.POST)
    @ResponseBody
    public String uploadPost(@PathVariable final String family, @PathVariable final String application, @PathVariable final String version, final MultipartHttpServletRequest request)
            throws IOException {
        final Iterator<String> itr = request.getFileNames();

        while (itr.hasNext()) {
            MultipartFile mpf = request.getFile(itr.next());
            createParentFolder(family, application, version);

            // Create the file on server
            final String serverPath = getPathHelper().getResourcePath(family, application, version, mpf.getOriginalFilename());

            final File serverFile = new File(serverPath);
            final BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(serverFile));

            outputStream.write(mpf.getBytes());
            outputStream.flush();
            outputStream.close();
            logger.info(mpf.getOriginalFilename() + " uploaded to " + family + " -> " + application + " -> " + version);
        }

        return "{}";
    }

    private void createParentFolder(final String family, final String application, final String version) {
        final File dir = new File(getPathHelper().getVersionPath(family, application, version));
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

}
