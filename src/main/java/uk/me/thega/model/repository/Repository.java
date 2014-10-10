package uk.me.thega.model.repository;

import java.util.List;

import uk.me.thega.controller.exception.NotFoundException;

/**
 * Interface representing the repository allowing multiple implementations.
 * 
 * @author pwhittlesea
 * 
 */
public interface Repository {

	List<Resource> allResources(String family, String version) throws NotFoundException;

	List<Version> allVersions(String family) throws NotFoundException;

	List<Family> families() throws NotFoundException;

	/**
	 * We have a family, version and file name but do not know which application the file was in.
	 * 
	 * Lets hope noone puts two files by the same name in two applications otherwise this will get nasty.
	 * 
	 * @param family the application family.
	 * @param version the application version.
	 * @param name the name of the resource to find.
	 * @return the found resource.
	 * @throws NotFoundException if we cannot find the resource.
	 */
	Resource findResource(String family, String version, String name) throws NotFoundException;

	Resource getResource(String family, String application, String version, String name) throws NotFoundException;

	List<Application> applications(String family) throws NotFoundException;

	List<Resource> resources(String family, String application, String version) throws NotFoundException;

	List<Version> versions(String family, String application) throws NotFoundException;

}
