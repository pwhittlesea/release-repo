package uk.me.thega.model.properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import uk.me.thega.model.util.PathHelper;

/**
 * Test the {@link PropertiesLoader} class.
 * 
 * @author pwhittlesea
 * 
 */
public class PropertiesLoaderIT {

	/** Use the test repo for all these ITs. */
	final PathHelper helper = new PathHelper("target/test/test_repository");

	/**
	 * Ensure that a non-present file is created.
	 * 
	 * @throws Exception the unexpected exception.
	 */
	@Test
	public void testPropertiesAreCreatedWhenAbsent() throws Exception {
		final File fakeProperties = new File(helper.getRepositoryPath() + File.separator + ".fake.properties");
		fakeProperties.mkdirs();
		fakeProperties.delete();

		final PropertiesLoader loader = new PropertiesLoader(helper);
		loader.read(".fake");
		assertTrue("Expected .fake.properties to be created", fakeProperties.isFile());

	}

	/**
	 * Ensure that when a non-present file is created then it is initialised correctly.
	 * 
	 * @throws Exception the unexpected exception.
	 */
	@Test
	public void testPropertiesAreInitialisedInAFileWhenCreated() throws Exception {
		final Map<String, String> defaultFakeProperties = new HashMap<String, String>();
		defaultFakeProperties.put("fake1", "Text A");
		defaultFakeProperties.put("fake12", "Txt B");

		final File fakeProperties = new File(helper.getRepositoryPath() + File.separator + ".fake.properties");
		fakeProperties.mkdirs();
		fakeProperties.delete();

		final PropertiesLoader loader = new PropertiesLoader(helper);
		final Map<String, String> properties = loader.read(".fake");
		assertEquals("Expected the result to equal the defaults", defaultFakeProperties, properties);
	}
}
