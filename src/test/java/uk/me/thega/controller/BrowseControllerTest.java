package uk.me.thega.controller;

import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.ui.ModelMap;

/**
 * Tests for the {@link BrowseController} class.
 */
public class BrowseControllerTest {

	/**
	 * Test that the product name and company name are set as expected.
	 */
	@Test
	public void testNames() {
		final ModelMap mockModelMap = Mockito.mock(ModelMap.class);

		AbstractController.populateGet(mockModelMap);

		Mockito.verify(mockModelMap, Mockito.times(1)).addAttribute("productName", "Product Name");
		Mockito.verify(mockModelMap, Mockito.times(1)).addAttribute("company", "Company Name");
	}
}
