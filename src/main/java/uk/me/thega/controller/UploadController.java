package uk.me.thega.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uk.me.thega.controller.exception.NotFoundException;

import java.util.Iterator;

@Controller
@RequestMapping(UrlMappings.ROOT_UPLOAD)
public class UploadController extends AbstractController {

    @RequestMapping(method = RequestMethod.GET)
    public String uploadGet(final ModelMap model) throws NotFoundException {
        populateGet(model);
        model.addAttribute("families", getRepository().families());
        return "upload";
    }

    @RequestMapping(value = "/{family}/{application}/{version}/new", method = RequestMethod.POST)
    public @ResponseBody String uploadPost(@PathVariable final String family, @PathVariable final String application, @PathVariable final String version, final MultipartHttpServletRequest request) {
        Iterator<String> itr =  request.getFileNames();

        while (itr.hasNext()) {
            MultipartFile mpf = request.getFile(itr.next());
            System.out.println(mpf.getOriginalFilename() + " uploaded to " + family + "->" + application + "->" + version);
        }

        return "{}";
    }

}
