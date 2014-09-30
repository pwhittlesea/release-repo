package uk.me.thega.model.util;

import java.io.File;
import java.util.Comparator;

import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

public class VersionComparator implements Comparator<File> {

	@Override
	public int compare(final File o1, final File o2) {
		final DefaultArtifactVersion v1 = new DefaultArtifactVersion(o1.getName());
		final DefaultArtifactVersion v2 = new DefaultArtifactVersion(o2.getName());
		return v2.compareTo(v1);
	}

}
