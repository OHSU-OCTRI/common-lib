package org.octri.common.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ViteManifest {

	private static final Logger log = LoggerFactory.getLogger(ViteManifest.class);
	private static final ObjectMapper objectMapper = new ObjectMapper();

	private final Map<String, ViteManifestChunk> chunkMap;

	/**
	 * Represents a chunk in a Vite manifest, which describes the attributes and dependencies of a single asset.
	 *
	 * @see <a href="https://vite.dev/guide/backend-integration">Vite Backend Integration Guide</a>
	 */
	public record ViteManifestChunk(String file, String name, String src, Boolean isEntry, Boolean isDynamicEntry,
			List<String> imports, List<String> css, List<String> dynamicImports, List<String> assets) {

		public ViteManifestChunk {
			if (src == null) {
				src = file;
			}
			if (name == null) {
				name = file;
			}
			if (isEntry == null) {
				isEntry = false;
			}
			if (isDynamicEntry == null) {
				isDynamicEntry = false;
			}
			if (imports == null) {
				imports = List.of();
			}
			if (css == null) {
				css = List.of();
			}
			if (dynamicImports == null) {
				dynamicImports = List.of();
			}
			if (assets == null) {
				assets = List.of();
			}
		}
	}

	public static ViteManifest empty() {
		return new ViteManifest(Map.of());
	}

	public static ViteManifest fromResources(Resource... resources) throws IOException {
		var typeRef = new TypeReference<HashMap<String, ViteManifestChunk>>() {
		};
		var mergedManifest = new HashMap<String, ViteManifestChunk>();

		for (var resource : resources) {
			log.info("Loading Vite manifest from {}", resource.getURI());
			var manifest = objectMapper.readValue(resource.getInputStream(), typeRef);
			for (var entry : manifest.entrySet()) {
				if (mergedManifest.containsKey(entry.getKey())) {
					log.error("Duplicate entry '{}' found in manifest '{}'. Skipping.", entry.getKey(),
							resource.getFilename());
				}

				mergedManifest.put(entry.getKey(), entry.getValue());
			}
		}

		return new ViteManifest(mergedManifest);
	}

	private ViteManifest(Map<String, ViteManifestChunk> chunkMap) {
		this.chunkMap = chunkMap;
	}

	public boolean isEmpty() {
		return chunkMap.isEmpty();
	}

	public boolean hasEntry(String entryFilename) {
		return chunkMap.containsKey(entryFilename);
	}

	public ViteManifestChunk getEntryPoint(String entryFilename) {
		if (!chunkMap.containsKey(entryFilename)) {
			throw new IllegalArgumentException("Entry '" + entryFilename + "' not found in manifest.");
		}
		var chunk = chunkMap.get(entryFilename);
		if (!chunk.isEntry()) {
			throw new IllegalArgumentException("Entry '" + entryFilename + "' is not an entry point.");
		}
		return chunk;
	}

	public List<ViteManifestChunk> getImportedChunks(String entryFilename) {
		var entryChunk = getEntryPoint(entryFilename);
		var visited = new HashSet<String>();
		return getImportedChunks(entryChunk, visited);
	}

	public List<ViteManifestChunk> getImportedChunks(ViteManifestChunk chunk, Set<String> visited) {
		var chunks = new ArrayList<ViteManifestChunk>();
		for (var importName : chunk.imports()) {
			if (visited.contains(importName)) {
				continue;
			}
			visited.add(importName);
			var importee = chunkMap.get(importName);
			chunks.addAll(getImportedChunks(importee, visited));
			chunks.add(importee);
		}

		return chunks;
	}

	@Override
	public String toString() {
		return "ViteManifest [chunkMap=" + chunkMap + "]";
	}

}
