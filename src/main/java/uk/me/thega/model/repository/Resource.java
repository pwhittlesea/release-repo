package uk.me.thega.model.repository;

import java.io.FileInputStream;
import java.io.IOException;

public interface Resource {

	FileInputStream getInputStream() throws IOException;

	String getMimeType();

	String getName();

	boolean isFile();

	long lastModified();

	long length();

}
