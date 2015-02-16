package uk.me.thega.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.ui.ModelMap;

import uk.me.thega.model.util.jira.JiraHelper;

/**
 * Tests for the {@link BrowseController} class.
 */
public class BrowseControllerTest {

	@Test
	public void testGetJiraDetailsForVersionWith0Changes() throws IOException {
		final String family = "family1";
		final String application = "application2";
		final String version = "version3";

		// Init mocks
		final JiraHelper mockJiraHelper = Mockito.mock(JiraHelper.class);
		final ModelMap mockModel = Mockito.mock(ModelMap.class);

		final Map<String, String> changeMap = new HashMap<String, String>();

		Mockito.when(mockJiraHelper.getChangeLog(family, application, version)).thenReturn(changeMap);
		Mockito.when(mockJiraHelper.getJql(family, application)).thenReturn("parent JQL");
		Mockito.when(mockJiraHelper.getJql(family, application, version)).thenReturn("child JQL");

		final Map<String, String> jiraShortList = new HashMap<String, String>();
		final Map<String, String> jiraLongList = new HashMap<String, String>();
		mockModelAddToMap("jiraShortList", mockModel, jiraShortList);
		mockModelAddToMap("jiraLongList", mockModel, jiraLongList);

		BrowseController.getJiraDetailsForVersion(mockJiraHelper, family, application, version, mockModel);

		Assert.assertEquals(0, jiraShortList.size());
		Assert.assertEquals(0, jiraLongList.size());
	}

	@Test
	public void testGetJiraDetailsForVersionWith1Changes() throws IOException {
		final String family = "family1";
		final String application = "application2";
		final String version = "version3";

		// Init mocks
		final JiraHelper mockJiraHelper = Mockito.mock(JiraHelper.class);
		final ModelMap mockModel = Mockito.mock(ModelMap.class);

		final Map<String, String> changeMap = new HashMap<String, String>();
		changeMap.put("key0", "value0");

		Mockito.when(mockJiraHelper.getChangeLog(family, application, version)).thenReturn(changeMap);
		Mockito.when(mockJiraHelper.getJql(family, application)).thenReturn("parent JQL");
		Mockito.when(mockJiraHelper.getJql(family, application, version)).thenReturn("child JQL");

		final Map<String, String> jiraShortList = new HashMap<String, String>();
		final Map<String, String> jiraLongList = new HashMap<String, String>();
		mockModelAddToMap("jiraShortList", mockModel, jiraShortList);
		mockModelAddToMap("jiraLongList", mockModel, jiraLongList);

		BrowseController.getJiraDetailsForVersion(mockJiraHelper, family, application, version, mockModel);

		Assert.assertEquals(1, jiraShortList.size());
		Assert.assertTrue(jiraShortList.containsKey("key0"));
		Assert.assertTrue(jiraLongList.isEmpty());
	}

	@Test
	public void testGetJiraDetailsForVersionWith4Changes() throws IOException {
		final String family = "family1";
		final String application = "application2";
		final String version = "version3";

		// Init mocks
		final JiraHelper mockJiraHelper = Mockito.mock(JiraHelper.class);
		final ModelMap mockModel = Mockito.mock(ModelMap.class);

		final Map<String, String> changeMap = new HashMap<String, String>();
		for (int i = 0; i < 4; i++) {
			changeMap.put("key" + i, "value" + 1);
		}

		Mockito.when(mockJiraHelper.getChangeLog(family, application, version)).thenReturn(changeMap);
		Mockito.when(mockJiraHelper.getJql(family, application)).thenReturn("parent JQL");
		Mockito.when(mockJiraHelper.getJql(family, application, version)).thenReturn("child JQL");

		final Map<String, String> jiraShortList = new HashMap<String, String>();
		final Map<String, String> jiraLongList = new HashMap<String, String>();
		mockModelAddToMap("jiraShortList", mockModel, jiraShortList);
		mockModelAddToMap("jiraLongList", mockModel, jiraLongList);

		BrowseController.getJiraDetailsForVersion(mockJiraHelper, family, application, version, mockModel);

		Assert.assertEquals(4, jiraShortList.size());
		Assert.assertTrue(jiraShortList.containsKey("key0"));
		Assert.assertTrue(jiraShortList.containsKey("key1"));
		Assert.assertTrue(jiraShortList.containsKey("key2"));
		Assert.assertTrue(jiraShortList.containsKey("key3"));
		Assert.assertTrue(jiraLongList.isEmpty());
	}

	@Test
	public void testGetJiraDetailsForVersionWith5Changes() throws IOException {
		final String family = "family1";
		final String application = "application2";
		final String version = "version3";

		// Init mocks
		final JiraHelper mockJiraHelper = Mockito.mock(JiraHelper.class);
		final ModelMap mockModel = Mockito.mock(ModelMap.class);

		final Map<String, String> changeMap = new HashMap<String, String>();
		for (int i = 0; i < 5; i++) {
			changeMap.put("key" + i, "value" + 1);
		}

		Mockito.when(mockJiraHelper.getChangeLog(family, application, version)).thenReturn(changeMap);
		Mockito.when(mockJiraHelper.getJql(family, application)).thenReturn("parent JQL");
		Mockito.when(mockJiraHelper.getJql(family, application, version)).thenReturn("child JQL");

		final Map<String, String> jiraShortList = new HashMap<String, String>();
		final Map<String, String> jiraLongList = new HashMap<String, String>();
		mockModelAddToMap("jiraShortList", mockModel, jiraShortList);
		mockModelAddToMap("jiraLongList", mockModel, jiraLongList);

		BrowseController.getJiraDetailsForVersion(mockJiraHelper, family, application, version, mockModel);

		Assert.assertEquals(5, jiraShortList.size());
		Assert.assertTrue(jiraShortList.containsKey("key0"));
		Assert.assertTrue(jiraShortList.containsKey("key1"));
		Assert.assertTrue(jiraShortList.containsKey("key2"));
		Assert.assertTrue(jiraShortList.containsKey("key3"));
		Assert.assertTrue(jiraShortList.containsKey("key4"));
		Assert.assertTrue(jiraLongList.isEmpty());
	}

	@Test
	public void testGetJiraDetailsForVersionWith6Changes() throws IOException {
		final String family = "family1";
		final String application = "application2";
		final String version = "version3";

		// Init mocks
		final JiraHelper mockJiraHelper = Mockito.mock(JiraHelper.class);
		final ModelMap mockModel = Mockito.mock(ModelMap.class);

		final Map<String, String> changeMap = new HashMap<String, String>();
		for (int i = 0; i < 6; i++) {
			changeMap.put("key" + i, "value" + 1);
		}

		Mockito.when(mockJiraHelper.getChangeLog(family, application, version)).thenReturn(changeMap);
		Mockito.when(mockJiraHelper.getJql(family, application)).thenReturn("parent JQL");
		Mockito.when(mockJiraHelper.getJql(family, application, version)).thenReturn("child JQL");

		final Map<String, String> jiraShortList = new HashMap<String, String>();
		final Map<String, String> jiraLongList = new HashMap<String, String>();
		mockModelAddToMap("jiraShortList", mockModel, jiraShortList);
		mockModelAddToMap("jiraLongList", mockModel, jiraLongList);

		BrowseController.getJiraDetailsForVersion(mockJiraHelper, family, application, version, mockModel);

		Assert.assertEquals(5, jiraShortList.size());
		Assert.assertTrue(jiraShortList.containsKey("key0"));
		Assert.assertTrue(jiraShortList.containsKey("key1"));
		Assert.assertTrue(jiraShortList.containsKey("key2"));
		Assert.assertTrue(jiraShortList.containsKey("key3"));
		Assert.assertTrue(jiraShortList.containsKey("key4"));
		Assert.assertEquals(1, jiraLongList.size());
		Assert.assertTrue(jiraLongList.containsKey("key5"));
	}

	private void mockModelAddToMap(final String attribute, final ModelMap mockModel, final Map<String, String> map) {
		Mockito.when(mockModel.addAttribute(Mockito.eq(attribute), Mockito.any())).thenAnswer(new Answer<ModelMap>() {

			@SuppressWarnings("unchecked")
			@Override
			public ModelMap answer(final InvocationOnMock invocation) throws Throwable {
				map.putAll((Map<String, String>) invocation.getArguments()[1]);
				return null;
			}
		});
	}
}
