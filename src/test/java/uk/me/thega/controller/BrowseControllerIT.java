package uk.me.thega.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.ModelMap;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/testApplicationContext.xml" })
public class BrowseControllerIT {

	private static ModelMap createMockModelMap(final Map<String, Object> map) {
		final ModelMap mockModelMap = Mockito.mock(ModelMap.class);
		Mockito.when(mockModelMap.addAttribute(Mockito.anyString(), Mockito.anyObject())).then(new Answer<Void>() {
			@Override
			public Void answer(final InvocationOnMock invocation) throws Throwable {
				final Object[] arguments = invocation.getArguments();
				map.put((String) arguments[0], arguments[1]);
				return null;
			}
		});
		return mockModelMap;
	}

	@Autowired
	private BrowseController controller;

	@SuppressWarnings("unchecked")
	@Test
	public void testFamilyGet() throws Exception {
		final Map<String, Object> loadedObjects = new HashMap<String, Object>();
		final ModelMap mockModelMap = createMockModelMap(loadedObjects);

		controller.browseGet(mockModelMap);

		Assert.assertTrue("Expected a leftList", loadedObjects.containsKey("leftList"));
		Assert.assertTrue("Expected a rightList", loadedObjects.containsKey("rightList"));

		final List<String> leftList = (List<String>) loadedObjects.get("leftList");
		Assert.assertEquals("Expected one element on the left", 1, leftList.size());
		Assert.assertEquals("Expected Family 1", "Family 1", leftList.get(0));

		final List<String> rightList = (List<String>) loadedObjects.get("rightList");
		Assert.assertEquals("Expected one element on the right", 1, rightList.size());
		Assert.assertEquals("Expected family2", "family2", rightList.get(0));

	}
}
