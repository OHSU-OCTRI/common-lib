package org.octri.common.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.octri.common.domain.AbstractEntity;
import org.springframework.core.io.ClassPathResource;

public class ViewUtilsTest {

	private class TestEntity extends AbstractEntity {
	};

	private Map<String, Object> model;

	@BeforeEach
	public void setUp() throws Exception {
		var resource = new ClassPathResource("example-vite-manifest.json");
		var manifest = ViteManifest.fromResources(resource);
		ViewUtils.useViteManifest(manifest, "");
		model = new HashMap<>();
	}

	public void useOctriManifest() throws Exception {
		var resource = new ClassPathResource("example-octri-vite-manifest.json");
		var manifest = ViteManifest.fromResources(resource);
		ViewUtils.useViteManifest(manifest, "src/main/resources/frontend/");
	}

	@Test
	public void testEntityWrapper() {
		var entity = new TestEntity();
		var wrapper = ViewUtils.entityWrapper(entity);
		assertNotNull(wrapper, "It should return a wrapper map");
		assertNotNull(wrapper.get("entity"), "It should contain an entry for the 'entity' key");
		assertEquals(entity, wrapper.get("entity"), "The value should equal the input entity");
	}

	@Test
	public void testAddPageScriptNotInManifest() {
		ViewUtils.addPageScript(model, "table-sorting.js");
		assertNotNull(model.get(ViewUtils.PAGE_SCRIPT_ATTRIBUTE), "The page script array should be present");
		var pageScripts = Arrays.asList((String[]) model.get(ViewUtils.PAGE_SCRIPT_ATTRIBUTE));
		assertTrue(pageScripts.contains("table-sorting.js"), "The page script array should contain the expected file");
	}

	@Test
	public void testAddPageScriptWithEmptyManifest() {
		ViewUtils.useViteManifest(ViteManifest.empty(), "");
		ViewUtils.addPageScript(model, "views/foo.js");
		assertNotNull(model.get(ViewUtils.PAGE_SCRIPT_ATTRIBUTE), "The page script array should be present");
		var pageScripts = Arrays.asList((String[]) model.get(ViewUtils.PAGE_SCRIPT_ATTRIBUTE));
		assertTrue(pageScripts.contains("views/foo.js"), "Should behave the same as files not in the manifest");
	}

	@Test
	public void testAddPageScriptWithManifestEntrypointIsDelegated() {
		assertTrue(model.size() == 0, "The model map should initially be empty");
		// should be delegated to addManifestModule(...)
		ViewUtils.addPageScript(model, "views/foo.js");
		assertNull(model.get(ViewUtils.PAGE_SCRIPT_ATTRIBUTE), "The page script array should not be present");
		assertNotNull(model.get(ViewUtils.PAGE_MODULE_ATTRIBUTE), "The page modules array should be present");
		assertNotNull(model.get(ViewUtils.PAGE_STYLES_ATTRIBUTE), "The page styles array should be present");
		assertNotNull(model.get(ViewUtils.PAGE_MODULE_PRELOAD_ATTRIBUTE),
				"The page module preloads array should be present");
	}

	@Test
	public void testAddPageScriptWithManifestEntrypointUsesEntrypointPrefix() throws Exception {
		useOctriManifest();
		assertTrue(model.size() == 0, "The model map should initially be empty");
		// should be delegated to addManifestModule(...)
		ViewUtils.addPageScript(model, "managed-content.js");
		assertNull(model.get(ViewUtils.PAGE_SCRIPT_ATTRIBUTE), "The page script array should not be present");
		assertNotNull(model.get(ViewUtils.PAGE_MODULE_ATTRIBUTE), "The page modules array should be present");
	}

	@Test
	public void testAddAdminScript() {
		ViewUtils.addAdminScript(model, "translation-management.js");
		assertNotNull(model.get(ViewUtils.ADMIN_SCRIPT_ATTRIBUTE), "The admin script array should be present");
		var adminScripts = Arrays.asList((String[]) model.get(ViewUtils.ADMIN_SCRIPT_ATTRIBUTE));
		assertTrue(adminScripts.contains("translation-management.js"),
				"The admin script array should contain the expected file");
	}

	@Test
	public void testAddPageWebjar() {
		ViewUtils.addPageWebjar(model, "datatables/js/dataTables.min.js");
		assertNotNull(model.get(ViewUtils.PAGE_WEBJAR_ATTRIBUTE), "The page webjar array should be present");
		var adminScripts = Arrays.asList((String[]) model.get(ViewUtils.PAGE_WEBJAR_ATTRIBUTE));
		assertTrue(adminScripts.contains("datatables/js/dataTables.min.js"),
				"The page webjars array should contain the expected path");
	}

	@Test
	public void testAddManifestModuleAddsEntryModule() {
		ViewUtils.addManifestModule(model, "views/foo.js");
		var modules = Arrays.asList((String[]) model.get(ViewUtils.PAGE_MODULE_ATTRIBUTE));
		assertTrue(modules.contains("assets/foo-BRBmoGS9.js"), "Module list should contain foo entry file");
	}

	@Test
	public void testAddManifestModuleAddsCssFromEntry() {
		ViewUtils.addManifestModule(model, "views/foo.js");
		var styles = Arrays.asList((String[]) model.get(ViewUtils.PAGE_STYLES_ATTRIBUTE));
		assertTrue(styles.contains("assets/foo-5UjPuW-k.css"), "Styles should include foo's own CSS");
	}

	@Test
	public void testAddManifestModuleAddsCssFromImportedChunks() {
		ViewUtils.addManifestModule(model, "views/foo.js");
		var styles = Arrays.asList((String[]) model.get(ViewUtils.PAGE_STYLES_ATTRIBUTE));
		assertTrue(styles.contains("assets/shared-ChJ_j-JJ.css"),
				"Styles should include CSS from imported shared chunk");
	}

	@Test
	public void testAddManifestModuleAddsPreloadsForImports() {
		ViewUtils.addManifestModule(model, "views/foo.js");
		var preloads = Arrays.asList((String[]) model.get(ViewUtils.PAGE_MODULE_PRELOAD_ATTRIBUTE));
		assertTrue(preloads.contains("assets/shared-B7PI925R.js"), "Preloads should include the imported shared chunk");
	}

	@Test
	public void testAddManifestModuleNoOwnCss() {
		ViewUtils.addManifestModule(model, "views/bar.js");
		var styles = Arrays.asList((String[]) model.get(ViewUtils.PAGE_STYLES_ATTRIBUTE));
		assertFalse(styles.contains("assets/bar-gkvgaI9m.js"), "Styles should not include bar's module file");
		assertTrue(styles.contains("assets/shared-ChJ_j-JJ.css"),
				"Styles should include CSS from imported shared chunk");
	}

	@Test
	public void testAddManifestModuleThrowsForUnknownEntry() {
		assertThrows(IllegalArgumentException.class, () -> ViewUtils.addManifestModule(model, "nonexistent.js"),
				"Unknown entry point should throw IllegalArgumentException");
	}

	@Test
	public void testAddManifestModulePrependsEntrypointPrefix() throws Exception {
		useOctriManifest();
		ViewUtils.addManifestModule(model, "managed-content.js");
		var modules = Arrays.asList((String[]) model.get(ViewUtils.PAGE_MODULE_ATTRIBUTE));
		assertTrue(modules.contains("assets/js/managed-content-CKqkE5xz.js"),
				"The entrypoint prefix should have been prepended to the filename when looking up the chunk");
	}

	@Test
	public void testAddArrayPropertySingleValue() {
		ViewUtils.addArrayProperty(model, ViewUtils.PAGE_SCRIPT_ATTRIBUTE, "example.js");
		assertNotNull(model.get(ViewUtils.PAGE_SCRIPT_ATTRIBUTE), "The page script array should be present");
		var pageScripts = Arrays.asList((String[]) model.get(ViewUtils.PAGE_SCRIPT_ATTRIBUTE));
		assertEquals(1, pageScripts.size(), "There should be one value");
		assertEquals("example.js", pageScripts.get(0), "The expected value should be present");
	}

	@Test
	public void testAddArrayPropertyMultipleValues() {
		ViewUtils.addArrayProperty(model, ViewUtils.PAGE_SCRIPT_ATTRIBUTE, "a.js", "b.js");
		assertNotNull(model.get(ViewUtils.PAGE_SCRIPT_ATTRIBUTE), "The page script array should be present");
		var pageScripts = Arrays.asList((String[]) model.get(ViewUtils.PAGE_SCRIPT_ATTRIBUTE));
		assertEquals(2, pageScripts.size(), "There should be two values");
		assertEquals("a.js", pageScripts.get(0), "The expected values should be present");
		assertEquals("b.js", pageScripts.get(1), "The expected values should be present");
	}

	@Test
	public void testAddArrayPropertyDeduplicatesValuesWhenCreatingArray() {
		ViewUtils.addArrayProperty(model, ViewUtils.PAGE_SCRIPT_ATTRIBUTE, "a.js", "b.js", "a.js");
		assertNotNull(model.get(ViewUtils.PAGE_SCRIPT_ATTRIBUTE), "The page script array should be present");
		var pageScripts = Arrays.asList((String[]) model.get(ViewUtils.PAGE_SCRIPT_ATTRIBUTE));
		assertEquals(2, pageScripts.size(), "There should only be two values");
		assertEquals("a.js", pageScripts.get(0), "The expected values should be present");
		assertEquals("b.js", pageScripts.get(1), "The expected values should be present");
	}

	@Test
	public void testAddArrayPropertyDeduplicatesValuesWhenAppending() {
		ViewUtils.addArrayProperty(model, ViewUtils.PAGE_SCRIPT_ATTRIBUTE, "a.js", "b.js");
		assertNotNull(model.get(ViewUtils.PAGE_SCRIPT_ATTRIBUTE), "The page script array should be present");
		var pageScripts = Arrays.asList((String[]) model.get(ViewUtils.PAGE_SCRIPT_ATTRIBUTE));
		assertEquals(2, pageScripts.size(), "There should be two values");
		assertEquals("a.js", pageScripts.get(0), "The expected values should be present");
		assertEquals("b.js", pageScripts.get(1), "The expected values should be present");

		ViewUtils.addArrayProperty(model, ViewUtils.PAGE_SCRIPT_ATTRIBUTE, "a.js");
		pageScripts = Arrays.asList((String[]) model.get(ViewUtils.PAGE_SCRIPT_ATTRIBUTE));
		assertEquals(2, pageScripts.size(), "There should still be two values");
		assertEquals("a.js", pageScripts.get(0), "The expected values should still be present");
		assertEquals("b.js", pageScripts.get(1), "The expected values should still be present");
	}

	@Test
	public void testAddArrayPropertyAppendsToExistingValues() {
		ViewUtils.addArrayProperty(model, ViewUtils.PAGE_SCRIPT_ATTRIBUTE, "a.js", "b.js");
		assertNotNull(model.get(ViewUtils.PAGE_SCRIPT_ATTRIBUTE), "The page script array should be present");
		var pageScripts = Arrays.asList((String[]) model.get(ViewUtils.PAGE_SCRIPT_ATTRIBUTE));
		assertEquals(2, pageScripts.size(), "There should be two values");

		ViewUtils.addArrayProperty(model, ViewUtils.PAGE_SCRIPT_ATTRIBUTE, "c.js");
		pageScripts = Arrays.asList((String[]) model.get(ViewUtils.PAGE_SCRIPT_ATTRIBUTE));
		assertEquals("c.js", pageScripts.get(2), "The new value should be appended to the array");
	}

}
