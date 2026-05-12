package org.octri.common.config;

import java.io.IOException;

import org.octri.common.view.ViewUtils;
import org.octri.common.view.ViteManifest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;

import jakarta.annotation.PostConstruct;

/**
 * Configuration for Vite manifest support.
 */
@Configuration
@EnableConfigurationProperties(ViteManifestProperties.class)
@ConditionalOnProperty(value = "octri.common.vite.enabled", havingValue = "true", matchIfMissing = false)
public class ViteManifestConfig {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final ViteManifestProperties properties;
	private final ResourceLoader resourceLoader;

	/**
	 * Constructor.
	 *
	 * @param properties
	 *            - configuration properties
	 * @param resourceLoader
	 *            - resource loader to use to load manifest files
	 */
	public ViteManifestConfig(ViteManifestProperties properties, ResourceLoader resourceLoader) {
		log.info("Enabling Vite manifest support");
		this.properties = properties;
		this.resourceLoader = resourceLoader;
	}

	/**
	 * Loads any Vite manifests found on the classpath, merges their contents, and adds the merged manifest to
	 * {@link ViewUtils}.
	 *
	 * @throws IOException
	 */
	@PostConstruct
	public void loadViteManifests() throws IOException {
		var patternResolver = ResourcePatternUtils.getResourcePatternResolver(resourceLoader);
		var resources = patternResolver.getResources(properties.getManifestPattern());

		if (resources.length == 0) {
			log.warn("No Vite manifest files found with pattern '{}'", properties.getManifestPattern());
			return;
		} else {
			log.debug("Found {} Vite manifest file(s) with pattern '{}'", resources.length,
					properties.getManifestPattern());
		}

		var mergedManifest = ViteManifest.fromResources(resources);
		log.debug("Merged Vite manifest: {}", mergedManifest);
		if (!mergedManifest.isEmpty()) {
			log.info("Adding Vite manifest to ViewUtils.");
			ViewUtils.useViteManifest(mergedManifest, properties.getEntrypointPrefix());
		}
	}

}
