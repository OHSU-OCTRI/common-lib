package org.octri.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Configuration properties for Vite manifest support.
 */
@ConfigurationProperties(prefix = "octri.common.vite")
public class ViteManifestProperties {

	/**
	 * Whether to enable Vite manifest support. Defaults to false.
	 */
	private Boolean enabled = false;

	/**
	 * Pattern to use when searching for manifest resources. Defaults to "classpath:static/.vite/*.json".
	 */
	private String manifestPattern = "classpath:static/.vite/*.json";

	/**
	 * Prefix path where entrypoint scripts are stored, relative to the root of the repository. Used to look up
	 * entrypoints in the manifest. Defaults to "src/main/resources/frontend/".
	 */
	private String entrypointPrefix = "src/main/resources/frontend/";

	/**
	 * Whether Vite manifest support is enabled.
	 *
	 * @return true if enabled, false otherwise
	 */
	public Boolean isEnabled() {
		return enabled;
	}

	/**
	 * Sets whether Vite manifest support is enabled.
	 *
	 * @param enabled
	 *            true to enable Vite manifest support, false to disable it
	 */
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * Gets the pattern used to search for manifest resources.
	 *
	 * @return the currently-configured pattern
	 */
	public String getManifestPattern() {
		return manifestPattern;
	}

	/**
	 * Sets the pattern to use to search for manifest resources.
	 *
	 * @param manifestPattern
	 *            the pattern to use
	 */
	public void setManifestPattern(String manifestPattern) {
		this.manifestPattern = manifestPattern;
	}

	/**
	 * Gets the path prefix of entrypoint scripts.
	 *
	 * @return the currently-configured prefix
	 */
	public String getEntrypointPrefix() {
		return entrypointPrefix;
	}

	/**
	 * Sets the path prefix of entrypoint scripts.
	 *
	 * @param entrypointPrefix
	 *            the path prefix to use
	 */
	public void setEntrypointPrefix(String entrypointPrefix) {
		this.entrypointPrefix = entrypointPrefix;
	}

}
