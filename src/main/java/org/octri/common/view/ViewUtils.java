package org.octri.common.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.octri.common.domain.AbstractEntity;

/**
 * Utility methods for generating view-related code.
 *
 * @author lawhead
 *
 */
public class ViewUtils {

	public final static String PAGE_STYLES_ATTRIBUTE = "pageStyles";
	public final static String PAGE_SCRIPT_ATTRIBUTE = "pageScripts";
	public final static String ADMIN_SCRIPT_ATTRIBUTE = "adminScripts";
	public final static String PAGE_WEBJAR_ATTRIBUTE = "pageWebjars";
	public final static String PAGE_MODULE_ATTRIBUTE = "pageModules";
	public final static String PAGE_MODULE_PRELOAD_ATTRIBUTE = "pageModulePreloads";

	private static ViteManifest viteManifest = ViteManifest.empty();
	private static String entryPointPrefix = "";

	/**
	 * Sets the manifest to use to look up assets processed by Vite.
	 * 
	 * @param manifest
	 *            the Vite manifest to use
	 * @param entryPointPrefix
	 *            the path prefix to prepend when searching the manifest for a script
	 */
	public static void useViteManifest(ViteManifest manifest, String entryPointPrefix) {
		ViewUtils.viteManifest = manifest;
		ViewUtils.entryPointPrefix = entryPointPrefix;
	}

	/**
	 * Utility method for creating a map object with a nested entity. Useful for using entity components in different
	 * contexts.
	 *
	 * @param entity
	 * @return
	 */
	public static Map<String, Object> entityWrapper(AbstractEntity entity) {
		Map<String, Object> wrapper = new HashMap<String, Object>();
		wrapper.put("entity", entity);
		return wrapper;
	}

	/**
	 * Add the given script name to the model's pageScripts (see footer.mustache).
	 *
	 * @param model
	 * @param scriptName
	 */
	public static void addPageScript(Map<String, Object> model, String scriptName) {
		if (viteManifest.hasEntry(entryPointPrefix + scriptName)) {
			addManifestModule(model, scriptName);
		} else {
			addArrayProperty(model, PAGE_SCRIPT_ATTRIBUTE, scriptName);
		}
	}

	/**
	 * Add the given script name to the model's adminScripts (see footer.mustache)
	 *
	 * @param model
	 * @param scriptName
	 */
	public static void addAdminScript(Map<String, Object> model, String scriptName) {
		addArrayProperty(model, ADMIN_SCRIPT_ATTRIBUTE, scriptName);
	}

	/**
	 * Adds the given webjar name to the model's pageWebjars (see footer.mustache)
	 *
	 * @param model
	 * @param scriptName
	 */
	public static void addPageWebjar(Map<String, Object> model, String scriptName) {
		addArrayProperty(model, PAGE_WEBJAR_ATTRIBUTE, scriptName);
	}

	/**
	 * Adds the given entrypoint and its dependencies to the module's asset arrays.
	 *
	 * @param model
	 * @param entryFilename
	 */
	public static void addManifestModule(Map<String, Object> model, String entryFilename) {
		var manifestKey = entryPointPrefix + entryFilename;
		var entryChunk = viteManifest.getEntryPoint(manifestKey);
		var importedChunks = viteManifest.getImportedChunks(manifestKey);

		// collect css files from entry and imported chunks
		var importedCss = new ArrayList<>();
		importedCss.addAll(entryChunk.css());
		importedChunks.stream().forEach(chunk -> importedCss.addAll(chunk.css()));
		if (importedCss.size() > 0) {
			addArrayProperty(model, PAGE_STYLES_ATTRIBUTE, importedCss.toArray(new String[0]));
		}

		// add the entrypoint module
		addArrayProperty(model, PAGE_MODULE_ATTRIBUTE, entryChunk.file());

		// preload imported modules
		var importedModules = importedChunks.stream()
				.map(ViteManifest.ViteManifestChunk::file)
				.collect(Collectors.toList());
		if (importedModules.size() > 0) {
			addArrayProperty(model, PAGE_MODULE_PRELOAD_ATTRIBUTE, importedModules.toArray(new String[0]));
		}
	}

	/**
	 * Add one or more values to a model's string array property.
	 *
	 * @param model
	 *            - model to modify
	 * @param key
	 *            - property name
	 * @param values
	 *            - values to add to the Array
	 */
	public static void addArrayProperty(Map<String, Object> model, String key, String... values) {
		var existingArray = model.get(key);
		var newList = existingArray != null ? new ArrayList<String>(Arrays.asList(((String[]) existingArray)))
				: new ArrayList<String>();
		for (var value : values) {
			if (!newList.contains(value)) {
				newList.add(value);
			}
		}
		model.put(key, newList.toArray(new String[0]));
	}
}