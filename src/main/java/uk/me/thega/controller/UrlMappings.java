package uk.me.thega.controller;

/**
 * Class storing the url mappings used.
 * 
 * @author pwhittlesea
 * 
 */
final class UrlMappings {
	static final String FAMILY = "/{family}";
	static final String FILE_PATH = "/{family}/{product}/{version}/{fileName}.{extension}";
	static final String PRODUCT = "/{family}/{product}";
	static final String ROOT_BROWSE = "/browse";
	static final String ROOT_DOWNLOAD = "/download";
	static final String ROOT_INDEX = "/";
	static final String ROOT_UPLOAD = "/upload";
	static final String VERSION = "/{family}/{product}/{version}";
}
