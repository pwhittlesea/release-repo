package uk.me.thega.controller;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import uk.me.thega.model.repository.Resource;
import uk.me.thega.model.util.MetadataHelper;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping(UrlMappings.ROOT_DOWNLOAD)
public class DownloadController extends AbstractController {

    @RequestMapping(value = UrlMappings.FILE_PATH, method = RequestMethod.GET)
    public void downloadResourceGet(@PathVariable final String family, @PathVariable final String application, @PathVariable final String version, @PathVariable final String fileName,
            @PathVariable final String extension, final HttpServletResponse response) throws IOException {
        final Resource download;
        if (application.equals("all")) {
            download = getRepository().findResource(family, version, fileName + '.' + extension);
        } else {
            download = getRepository().getResource(family, application, version, fileName + '.' + extension);
        }
        final InputStream is = download.getInputStream();
        response.setContentType(download.getMimeType());
        response.setContentLength(is.available());

        IOUtils.copy(is, response.getOutputStream());
        response.flushBuffer();
    }

    @RequestMapping(value = UrlMappings.VERSION_DOWNLOAD, method = RequestMethod.GET)
    public void downloadVersionGet(@PathVariable final String family, @PathVariable final String application, @PathVariable final String version, @PathVariable final String extension,
            final HttpServletResponse response) throws IOException {
        final List<Resource> download;
        if (application.equals("all")) {
            download = getRepository().allResources(family, version);
        } else {
            download = getRepository().resources(family, application, version);
        }

        response.setContentType("application/zip");

        final ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());

        final List<String> excluded = MetadataHelper.excludedFiles();
        for (final Resource resource : download) {
            if (!excluded.contains(resource.getName())) {
                final FileInputStream is = resource.getInputStream();

                // Add to zip
                zos.putNextEntry(new ZipEntry(resource.getName()));
                IOUtils.copy(is, zos);
                zos.closeEntry();

                is.close();
            }
        }
        zos.flush();
        zos.close();
        response.flushBuffer();
    }

}
