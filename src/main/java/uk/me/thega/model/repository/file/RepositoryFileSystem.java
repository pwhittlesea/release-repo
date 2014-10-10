package uk.me.thega.model.repository.file;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import uk.me.thega.controller.exception.NotFoundException;
import uk.me.thega.model.repository.Family;
import uk.me.thega.model.repository.Application;
import uk.me.thega.model.repository.Repository;
import uk.me.thega.model.repository.Resource;
import uk.me.thega.model.repository.Version;
import uk.me.thega.model.util.PathHelper;
import uk.me.thega.model.util.VersionComparator;

public class RepositoryFileSystem implements Repository {

	private final PathHelper pathHelper;

	public RepositoryFileSystem(final PathHelper pathHelper) {
		this.pathHelper = pathHelper;
	}

	@Override
	public List<Resource> allResources(final String family, final String versionName) throws NotFoundException {
		final List<Resource> allResources = new ArrayList<Resource>();
		for (final Application application : applications(family)) {
			final String applicationName = application.getName();
			for (final Version version : versions(family, applicationName)) {
				if (version.getName().equals(versionName)) {
					allResources.addAll(resources(family, applicationName, versionName));
				}
			}
		}
		return allResources;
	}

	@Override
	public List<Version> allVersions(final String family) throws NotFoundException {
		final List<Version> allVersions = new ArrayList<Version>();
		for (final Application application : applications(family)) {
			allVersions.addAll(versions(family, application.getName()));
		}
		Collections.sort(allVersions, new VersionComparator());
		return allVersions;
	}

	@Override
	public List<Family> families() throws NotFoundException {
		final List<File> contents = getContentsOf(pathHelper.getRepositoryPath(), false);
		final List<Family> families = new ArrayList<Family>();
		for (final File file : contents) {
			families.add(new FileFamily(file));
		}
		return families;
	}

	@Override
	public Resource findResource(final String family, final String version, final String name) throws NotFoundException {
		for (final Application application : applications(family)) {
			final File potentialMatch = new File(pathHelper.getResourcePath(family, application.getName(), version, name));
			if (potentialMatch.isFile()) {
				return new FileResource(potentialMatch);
			}
		}
		throw new NotFoundException("Could not find resource: family[" + family + "] version[" + version + "] with name " + name);
	}

	@Override
	public Resource getResource(final String family, final String application, final String version, final String name) throws NotFoundException {
		final File resourceFile = new File(pathHelper.getResourcePath(family, application, version, name));
		if (resourceFile.isFile()) {
			return new FileResource(resourceFile);
		}
		throw new NotFoundException("Could not find resource: family[" + family + "] application[" + application + "] version[" + version + "] with name " + name);
	}

	@Override
	public List<Application> applications(final String family) throws NotFoundException {
		final List<File> contents = getContentsOf(pathHelper.getFamilyPath(family), false);
		final List<Application> applications = new ArrayList<Application>();
		for (final File file : contents) {
			applications.add(new FileApplication(file));
		}
		return applications;
	}

	@Override
	public List<Resource> resources(final String family, final String application, final String version) throws NotFoundException {
		final List<File> contents = getContentsOf(pathHelper.getVersionPath(family, application, version), true);
		final List<Resource> resources = new ArrayList<Resource>();
		for (final File file : contents) {
			resources.add(new FileResource(file));
		}
		return resources;
	}

	@Override
	public List<Version> versions(final String family, final String application) throws NotFoundException {
		final List<File> contents = getContentsOf(pathHelper.getApplicationPath(family, application), false);
		final List<Version> versions = new ArrayList<Version>();
		for (final File file : contents) {
			versions.add(new FileVersion(file));
		}
		Collections.sort(versions, new VersionComparator());
		return versions;
	}

	private List<File> getContentsOf(final String path, final boolean allowFiles) throws NotFoundException {
		final File dir = new File(path);
		if (!dir.isDirectory()) {
			throw new NotFoundException("Not Found");
		}
		final List<File> contents = Arrays.asList(dir.listFiles());
		if (!allowFiles) {
			final List<File> prunedContents = new ArrayList<File>();
			for (final File file : contents) {
				if (file.isDirectory()) {
					prunedContents.add(file);
				}
			}
			return prunedContents;
		} else {
			return contents;
		}
	}
}
