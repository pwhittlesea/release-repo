package uk.me.thega.model.util;

import java.util.Comparator;

import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import uk.me.thega.model.repository.Version;

public class VersionComparator implements Comparator<Version> {

	@Override
	public int compare(final Version o1, final Version o2) {
		final DefaultArtifactVersion v1 = new DefaultArtifactVersion(o1.getName());
		final DefaultArtifactVersion v2 = new DefaultArtifactVersion(o2.getName());
		return v2.compareTo(v1);
	}

}
