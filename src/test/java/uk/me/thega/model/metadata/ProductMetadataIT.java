package uk.me.thega.model.metadata;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import uk.me.thega.model.util.MetadataHelper;
import uk.me.thega.model.util.PathHelper;

/**
 * Tests to ensure that picking up metadata about projects and versions works.
 * 
 * @author pwhittlesea
 * 
 */
public class ProductMetadataIT {

	/** Use the test repo for all these ITs. */
	final PathHelper helper = new PathHelper("src/test/resources/test_repository");

	/**
	 * Test that we can detect a discontinued product.
	 */
	@Test
	public void testDiscontinuedProduct() {
		final String project3 = helper.getProductPath("Family 1", "project3");
		final boolean isProject3Discontinued = MetadataHelper.isDiscontinued(project3);
		assertTrue(isProject3Discontinued);
	}

	/**
	 * Test that we dont label products as discontinued when they are not.
	 */
	@Test
	public void testNotDiscontinuedProduct() {
		final String project4 = helper.getProductPath("Family 1", "project4");
		final boolean isProject3Discontinued = MetadataHelper.isDiscontinued(project4);
		assertFalse(isProject3Discontinued);
	}
}
