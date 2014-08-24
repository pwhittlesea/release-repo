package uk.me.thega.model.metadata;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import uk.me.thega.model.util.MetadataUnmarshaller;

public class ProductMetadataTest {

	@Test
	public void testUnmarshalling() throws Exception {
		final ProductMetadata family = MetadataUnmarshaller.un(new File("src/test/resources/testProductMetadata.xml"), ProductMetadata.class);
		Assert.assertTrue(family.isDiscontinued());
	}
}
