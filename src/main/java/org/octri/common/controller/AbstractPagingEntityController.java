package org.octri.common.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.octri.common.domain.AbstractEntity;
import org.octri.common.view.DataTableResponse;
import org.octri.common.view.ViewUtils;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.persistence.criteria.Predicate;
import jakarta.validation.Valid;

/**
 * Abstract class that provides methods for performing CRUD tasks and paging for AbstractEntity objects. This is tightly
 * coupled with DataTables making server-side requests, and it cannot walk the entity graph to display, search, sort,
 * and filter on any related objects.
 *
 * @param <T>
 *            an entity type extending {@link AbstractEntity}
 * @param <U>
 *            jpaRepository type for accessing the entity
 */
public abstract class AbstractPagingEntityController<T extends AbstractEntity, U extends JpaRepository<T, Long> & JpaSpecificationExecutor<T>> {

	private static final Log log = LogFactory.getLog(AbstractPagingEntityController.class);

	public static class DTSearch {

		public String value;
		public boolean regex;
	}

	public static class DTColumn {

		public String data;
		public String name;
		public boolean searchable;
		public boolean orderable;
		public DTSearch search;
	}

	public static class DTOrder {

		public int column;
		public String dir;
	}

	// cached values
	private String templateFolder;
	private String baseRoute;

	/**
	 * Class of the domain entity. Needed due to java type erasure.
	 *
	 * @return class of the domain entity
	 */
	protected abstract Class<T> domainClass();

	/**
	 * Repository for the given domain entity. This should be autowired into the extending controller.
	 *
	 * @return repository for the domain entity
	 */
	protected abstract U getRepository();

	protected Page<T> getData(List<DTColumn> columns, Pageable pageable, String search) {
		if (search == null || search.isBlank()) {
			return getRepository().findAll(pageable);
		}
		Specification<T> spec = (root, query, builder) -> {
			String like = "%" + search.toLowerCase() + "%";
			List<Predicate> preds = new ArrayList<>();

			for (DTColumn col : columns) {
				if (col.searchable) {
					preds.add(builder.like(
							builder.lower(root.get(col.data).as(String.class)),
							like));
				}
			}

			return builder.or(preds.toArray(new Predicate[0]));
		};

		return getRepository().findAll(spec, pageable);
	}

	@GetMapping("/data")
	@ResponseBody
	public DataTableResponse<T> data(
			@RequestParam("start") int start,
			@RequestParam("length") int length,
			@RequestParam(value = "search[value]", required = false) String search,
			@RequestParam Map<String, String> requestParams) {

		int page = start / length;

		List<DTColumn> columns = parseColumns(requestParams);
		List<DTOrder> order = parseOrder(requestParams);

		DTOrder ord = order.get(0);
		String sortField = columns.get(ord.column).data;
		Sort.Direction dir = ord.dir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		Sort sort = Sort.by(dir, sortField);

		Pageable pageable = PageRequest.of(page, length, sort);

		Page<T> p = getData(columns, pageable, search);

		return new DataTableResponse<>(
				p.getContent(),
				p.getTotalElements(),
				p.getTotalElements());
	}

	/**
	 * Adds common view attributes to the Model passed to the view template.
	 *
	 * @param model
	 *            map containing template data
	 */
	protected void addTemplateAttributes(Map<String, Object> model) {
		model.put("entityName", this.entityName());
		model.put("baseRoute", this.getBaseRoute());
	}

	/**
	 * Renders the entity list view.
	 * 
	 * @param model
	 *            map containing template data
	 * @return the entity list
	 */
	@GetMapping("/")
	public String list(Map<String, Object> model) {
		addTemplateAttributes(model);
		ViewUtils.addPageWebjar(model, "datatables/js/jquery.dataTables.min.js");
		ViewUtils.addPageWebjar(model, "datatables/js/dataTables.bootstrap5.min.js");
		ViewUtils.addPageScript(model, "paged-sorting.js");
		// model.put("entity_list", getRepository().findAll());
		return template("list");
	}

	/**
	 * Renders the details page for the entity with the given ID.
	 * 
	 * @param model
	 *            map containing template data
	 * @param id
	 *            ID of the entity to display
	 * @return the entity details page
	 */
	@GetMapping("/{id}")
	public String show(Map<String, Object> model, @PathVariable Long id) {
		addTemplateAttributes(model);
		model.put("entity", getRepository().findById(id).get());
		return template("show");
	}

	/**
	 * Renders the form to create a new entity.
	 * 
	 * @param model
	 *            map containing template data
	 * @return the new entity form
	 */
	@GetMapping("/new")
	public String newEntity(Map<String, Object> model) {
		addTemplateAttributes(model);
		ViewUtils.addPageScript(model, "form-reset.js");
		model.put("entity", newEntity());
		return template("form");
	}

	/**
	 * Saves a new entity.
	 *
	 * @param model
	 *            form model
	 * @param entity
	 *            new entity
	 * @param bindingResult
	 *            binding result
	 * @param redirectAttributes
	 *            redirect attributes
	 * @return redirect to the new entity's details page
	 */
	@PostMapping("/new")
	public String create(Map<String, Object> model,
			@Valid @ModelAttribute("entity") T entity,
			BindingResult bindingResult, RedirectAttributes redirectAttributes) {
		T newEntity = this.getRepository().save(entity);
		model.put("newEntity", newEntity);
		redirectAttributes.addFlashAttribute("successMessage", this.entityName() + " successfully created.");
		return showRedirect(newEntity.getId());
	}

	/**
	 * Renders the form to edit an entity.
	 *
	 * @param model
	 *            template model data
	 * @param id
	 *            entity ID
	 * @return form page to edit the entity with the given ID
	 */
	@GetMapping("/{id}/edit")
	public String edit(Map<String, Object> model, @PathVariable Long id) {
		addTemplateAttributes(model);
		model.put("entity", getRepository().findById(id).get());
		return template("form");
	}

	/**
	 * Saves entity updates.
	 *
	 * @param model
	 *            form model
	 * @param id
	 *            entity ID
	 * @param entity
	 *            updated entity
	 * @param bindingResult
	 *            binding result
	 * @param redirectAttributes
	 *            redirect attributes
	 * @return redirect to the entity's details page
	 */
	@PostMapping("/{id}/edit")
	public String update(Map<String, Object> model, @PathVariable Long id,
			@Valid @ModelAttribute("entity") T entity, BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		this.getRepository().save(entity);
		redirectAttributes.addFlashAttribute("infoMessage", this.entityName() + " updated.");
		return showRedirect(id);
	}

	/**
	 * Deletes the entity with the given ID.
	 *
	 * @param id
	 *            entity ID
	 * @param redirectAttributes
	 *            redirect attributes
	 * @return redirect to the entity list page
	 */
	@GetMapping("/{id}/delete")
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
	 * @return entity name
	 */
	protected String entityName() {
		return this.domainClass().getSimpleName();
	}

	/**
	 * Creates a new instance based on the provided domain entity class.
	 *
	 * @return a new instance of the entity class
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
	 * @return the folder containing templates related to the controller
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
	 *            template name
	 * @return full path to the given template name
	 */
	protected String template(String viewName) {
		return getTemplateFolder() + "/" + viewName;
	}

	/**
	 * Computes the base route. Base routes should be relative to the contextPath. Unless overridden, the value is
	 * acquired from the RequestMapping annotation.
	 *
	 * @return base route relative to the context path
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
	 *            entity ID
	 * @return - route to the entity details page
	 */
	protected String showRoute(Long id) {
		return this.getBaseRoute() + "/" + id;
	}

	/**
	 * @return The string returned from a controller method that will redirect to the listing page.
	 */
	protected String listingRedirect() {
		return "redirect:" + this.getBaseRoute() + "/";
	}

	/**
	 * @param id
	 *            entity ID
	 * @return The string returned from a controller method that will redirect to the listing page.
	 */
	protected String showRedirect(Long id) {
		return "redirect:" + this.showRoute(id);
	}

	/**
	 * Customizes data binding to trim strings and set the date format.
	 *
	 * @param binder
	 *            data binder to customize
	 */
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	private List<DTColumn> parseColumns(Map<String, String> params) {
		List<DTColumn> cols = new ArrayList<>();

		int i = 0;
		while (true) {
			String prefix = "columns[" + i + "]";
			if (!params.containsKey(prefix + "[data]"))
				break;

			DTColumn col = new DTColumn();
			col.data = params.get(prefix + "[data]");
			col.name = params.get(prefix + "[name]");
			col.orderable = Boolean.parseBoolean(params.get(prefix + "[orderable]"));
			col.searchable = Boolean.parseBoolean(params.get(prefix + "[searchable]"));

			// nested search object
			DTSearch search = new DTSearch();
			search.value = params.get(prefix + "[search][value]");
			search.regex = Boolean.parseBoolean(params.get(prefix + "[search][regex]"));
			col.search = search;

			cols.add(col);
			i++;
		}
		return cols;
	}

	private List<DTOrder> parseOrder(Map<String, String> params) {
		List<DTOrder> list = new ArrayList<>();

		int i = 0;
		while (true) {
			String prefix = "order[" + i + "]";
			if (!params.containsKey(prefix + "[column]"))
				break;

			DTOrder o = new DTOrder();
			o.column = Integer.parseInt(params.get(prefix + "[column]"));
			o.dir = params.get(prefix + "[dir]");

			list.add(o);
			i++;
		}
		return list;
	}

}