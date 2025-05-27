package org.octri.common.view;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.octri.common.domain.AbstractEntity;

/**
 * Utility methods for generating view-related code.
 *
 * @author lawhead
 *
 */
public class ViewUtils {

	public final static String PAGE_SCRIPT_ATTRIBUTE = "pageScripts";
	public final static String ADMIN_SCRIPT_ATTRIBUTE = "adminScripts";
	public final static String PAGE_WEBJAR_ATTRIBUTE = "pageWebjars";

	/**
	 * Utility method for creating a map object with a nested entity. Useful for
	 * using entity components in different
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
		addArrayProperty(model, PAGE_SCRIPT_ATTRIBUTE, scriptName);
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

	public static void addPageWebjar(Map<String, Object> model, String scriptName) {
		addArrayProperty(model, PAGE_WEBJAR_ATTRIBUTE, scriptName);
	}

	/**
	 * Add a value to a model's string array property.
	 *
	 * @param model
	 *            - model to modify
	 * @param key
	 *            - property name
	 * @param value
	 *            - value to add to the Array
	 */
	public static void addArrayProperty(Map<String, Object> model, String key, String value) {
		var existingArray = model.get(key);
		if (existingArray != null) {
			var newList = new ArrayList<String>(Arrays.asList(((String[]) existingArray)));
			if (!newList.contains(value)) {
				newList.add(value);
				model.put(key, newList.toArray(new String[0]));
			}
		} else {
			model.put(key, new String[] { value });
		}
	}
}