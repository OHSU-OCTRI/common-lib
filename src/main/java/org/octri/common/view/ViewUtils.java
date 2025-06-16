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

	/**
	 * Model attribute used to store the names of JavaScript files attached to the page.
	 */
	public final static String PAGE_SCRIPT_ATTRIBUTE = "pageScripts";

	/**
	 * Model attribute used to store the names of JavaScript files attached to the page if the user is an admin.
	 */
	public final static String ADMIN_SCRIPT_ATTRIBUTE = "adminScripts";

	/**
	 * Model attribute used to store the names of Webjars attached to the page.
	 */
	public final static String PAGE_WEBJAR_ATTRIBUTE = "pageWebjars";

	/**
	 * Utility method for creating a map object with a nested entity. Useful for
	 * using entity components in different contexts.
	 *
	 * @param entity
	 *            an entity
	 * @return a map with an "entity" key that references the given entity
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
	 *            template model data
	 * @param scriptName
	 *            name of javascript to add to page scripts
	 */
	public static void addPageScript(Map<String, Object> model, String scriptName) {
		addArrayProperty(model, PAGE_SCRIPT_ATTRIBUTE, scriptName);
	}

	/**
	 * Add the given script name to the model's adminScripts (see footer.mustache)
	 *
	 * @param model
	 *            template model data
	 * @param scriptName
	 *            name of javascript to add to admin page scripts
	 */
	public static void addAdminScript(Map<String, Object> model, String scriptName) {
		addArrayProperty(model, ADMIN_SCRIPT_ATTRIBUTE, scriptName);
	}

	/**
	 * Add the given Webjar to the model's pageWebjars (see footer.mustache)
	 * 
	 * @param model
	 *            template model data
	 * @param scriptName
	 *            name of the Webjar script to add
	 */
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