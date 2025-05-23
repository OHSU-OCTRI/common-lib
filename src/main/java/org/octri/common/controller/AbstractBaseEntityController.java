package org.octri.common.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.octri.common.domain.AbstractEntity;
import org.octri.common.view.ViewUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

/**
 * Abstract class that provides methods for performing CRUD tasks for AbstractEntity objects. However, this class does
 * not define the route mappings for these operations. Separating the mappings from the functions gives us more
 * flexibility since route mappings cannot be overridden. The AbstractEntityController subclasses this one and provides
 * default mappings.
 *
 * @param <T>
 * @param <U>
 */
public abstract class AbstractBaseEntityController<T extends AbstractEntity, U extends CrudRepository<T, Long>> {

	private static final Log log = LogFactory.getLog(AbstractBaseEntityController.class);

	// cached values
	private String templateFolder;
	private String baseRoute;

	/**
	 * Class of the domain entity. Needed due to java type erasure.
	 *
	 * @return
	 */
	protected abstract Class<T> domainClass();

	/**
	 * Repository for the given domain entity. This should be autowired into the extending controller.
	 *
	 * @return
	 */
	protected abstract U getRepository();

	/**
	 * Adds common view attributes to the Model passed to the view template.
	 *
	 * @param model
	 */
	protected void addTemplateAttributes(Map<String, Object> model) {
		model.put("entityName", this.entityName());
		model.put("baseRoute", this.getBaseRoute());
	}

	public String list(Map<String, Object> model) {
		addTemplateAttributes(model);
		ViewUtils.addPageWebjar(model, "datatables/js/jquery.dataTables.min.js");
		ViewUtils.addPageWebjar(model, "datatables/js/dataTables.bootstrap5.min.js");
		ViewUtils.addPageScript(model, "table-sorting.js");
		model.put("entity_list", getRepository().findAll());
		return template("list");
	}

	public String show(Map<String, Object> model, @PathVariable Long id) {
		addTemplateAttributes(model);
		model.put("entity", getRepository().findById(id).get());
		return template("show");
	}

	public String newEntity(Map<String, Object> model) {
		addTemplateAttributes(model);
		ViewUtils.addPageScript(model, "form-reset.js");
		model.put("entity", newEntity());
		return template("form");
	}

	public String create(Map<String, Object> model,
			@Valid @ModelAttribute("entity") T entity,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		T newEntity = this.getRepository().save(entity);
		model.put("newEntity", newEntity);
		redirectAttributes.addFlashAttribute("successMessage", this.entityName() + " successfully created.");
		return showRedirect(newEntity.getId());
	}

	public String edit(Map<String, Object> model, @PathVariable Long id) {
		addTemplateAttributes(model);
		model.put("entity", getRepository().findById(id).get());
		return template("form");
	}

	public String update(Map<String, Object> model, @PathVariable Long id,
			@Valid @ModelAttribute("entity") T entity, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		this.getRepository().save(entity);
		redirectAttributes.addFlashAttribute("infoMessage", this.entityName() + " updated.");
		return showRedirect(id);
	}

	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		try {
			getRepository().deleteById(id);
		} catch (DataIntegrityViolationException e) {
			String msg = this.entityName() + " is in use and cannot be deleted.";
			redirectAttributes.addFlashAttribute("errorMessage", msg);
			return showRedirect(id);
		}

		String msg = this.entityName() + " with id " + id + " successfully deleted.";
		redirectAttributes.addFlashAttribute("infoMessage", msg);
		return listingRedirect();
	}

	/**
	 * Name displayed to the user in the UI.
	 *
	 * @return
	 */
	protected String entityName() {
		return this.domainClass().getSimpleName();
	}

	/**
	 * Creates a new instance based on the provided domain entity class.
	 *
	 * @return
	 */
	protected T newEntity() {
		try {
			return this.domainClass().getDeclaredConstructor().newInstance();
		} catch (ReflectiveOperationException e) {
			log.error("Failed to instantiate new entity of type " + this.entityName());
			return null;
		}
	}

	/**
	 * Name of the folder in which the CRUD view templates reside. Can be overridden.
	 *
	 * @return folder name for the view templates.
	 */
	protected String templateFolder() {
		String className = this.domainClass().getSimpleName();

		String regex = "([a-z])([A-Z]+)";
		String replacement = "$1_$2";
		return className.replaceAll(regex, replacement)
				.toLowerCase();
	}

	/**
	 * Get the template folder. If the value has been previously cached, uses that value, otherwise calculates it.
	 *
	 * @return
	 */
	private String getTemplateFolder() {
		if (templateFolder == null) {
			// cache the value
			templateFolder = templateFolder();
		}
		return templateFolder;
	}

	/**
	 * Utility function that returns the full path to the given template.
	 *
	 * @param viewName
	 * @return
	 */
	protected String template(String viewName) {
		return getTemplateFolder() + "/" + viewName;
	}

	/**
	 * Computes the base route. Base routes should be relative to the contextPath. Unless overridden, the value is
	 * acquired from the RequestMapping annotation.
	 *
	 * @return
	 */
	protected String baseRoute() {
		return this.getClass().getAnnotation(RequestMapping.class).value()[0];
	}

	/**
	 * Used to construct urls in the view.
	 *
	 * @return The value of the Controller's RequestMapping; used to construct urls in the view.
	 */
	public String getBaseRoute() {
		if (this.baseRoute == null) {
			baseRoute = baseRoute();
		}
		return baseRoute;
	}

	/**
	 * @param id
	 * @return - route to the entity details page
	 */
	protected String showRoute(Long id) {
		return this.getBaseRoute() + "/" + id;
	}

	/**
	 *
	 * @return The string returned from a controller method that will redirect to the listing page.
	 */
	protected String listingRedirect() {
		return "redirect:" + this.getBaseRoute() + "/";
	}

	/**
	 *
	 * @return The string returned from a controller method that will redirect to the listing page.
	 */
	protected String showRedirect(Long id) {
		return "redirect:" + this.showRoute(id);
	}

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}
}
