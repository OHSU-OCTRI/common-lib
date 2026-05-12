package org.octri.common.view;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ViteManifestTest {

	private static Resource manifestResource;
	private ViteManifest sharedManifest;

	@BeforeAll
	public static void setup() {
		manifestResource = new ClassPathResource("example-vite-manifest.json");
	}

	@BeforeEach
	public void loadManifest() throws Exception {
		sharedManifest = ViteManifest.fromResources(manifestResource);
	}

	@Test
	public void testEmptyManifest() {
		var manifest = ViteManifest.empty();
		assertTrue(manifest.isEmpty(), "Empty manifest should be empty");
	}

	@Test
	public void testLoadManifest() throws Exception {
		assertTrue(!sharedManifest.isEmpty(), "Manifest should not be empty");
	}

	@Test
	public void testHasEntry() {
		assertTrue(sharedManifest.hasEntry("views/foo.js"),
				"Manifest should contain foo.js entry");
		assertFalse(sharedManifest.hasEntry("views/nonexistent.js"),
				"Manifest should not contain nonexistent.js entry");
	}

	@Test
	public void testGetEntryPoint() {
		var chunk = sharedManifest.getEntryPoint("views/foo.js");
		assertTrue(chunk != null, "Chunk for foo.js should not be null");
		assertTrue(chunk.src().equals("views/foo.js"), "Chunk src should be views/foo.js");
		assertTrue(chunk.file().equals("assets/foo-BRBmoGS9.js"), "Chunk file should be assets/foo-BRBmoGS9.js");
	}

	@Test
	public void testGetMissingEntryPoint() {
		assertThrows(IllegalArgumentException.class, () -> sharedManifest.getEntryPoint("views/nonexistent.js"),
				"Getting a non-existent entry point should throw IllegalArgumentException");
	}

	@Test
	public void testGetNonEntryPoint() {
		assertThrows(IllegalArgumentException.class, () -> sharedManifest.getEntryPoint("baz.js"),
				"Getting a non-entry point should throw IllegalArgumentException");
	}

	@Test
	public void testGetImportedChunks() {
		var expectedFooImport = "assets/shared-B7PI925R.js";
		var importedChunks = sharedManifest.getImportedChunks("views/foo.js");
		assertTrue(importedChunks.size() == 1, "foo.js should have 1 imported chunks");
		var importFiles = importedChunks.stream().map(chunk -> chunk.file()).toList();
		assertTrue(importFiles.contains(expectedFooImport), "Imported chunks should include " + expectedFooImport);
	}

}
