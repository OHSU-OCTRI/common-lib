package org.octri.common.controller;

import java.util.Map;

import org.octri.common.domain.AbstractEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Abstract class implementing common functionality for domain entity CRUD operations.
 *
 * @param <T>
 *            an entity type extending {@link AbstractEntity}
 * @param <U>
 *            repository type for accessing the entity
 */
public abstract class AbstractEntityController<T extends AbstractEntity, U extends CrudRepository<T, Long>>
		extends AbstractBaseEntityController<T, U> {

	@GetMapping("/")
	@Override
	public String list(Map<String, Object> model) {
		return super.list(model);
	}

	@GetMapping("/{id}")
	@Override
	public String show(Map<String, Object> model, @PathVariable Long id) {
		return super.show(model, id);
	}

	@GetMapping("/new")
	@Override
	public String newEntity(Map<String, Object> model) {
		return super.newEntity(model);
	}

	@PostMapping("/new")
	@Override
	public String create(Map<String, Object> model, @ModelAttribute("entity") T entity,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		return super.create(model, entity, bindingResult, redirectAttributes);
	}

	@GetMapping("/{id}/edit")
	@Override
	public String edit(Map<String, Object> model, @PathVariable Long id) {
		return super.edit(model, id);
	}

	@PostMapping("/{id}/edit")
	@Override
	public String update(Map<String, Object> model, @PathVariable Long id,
			@ModelAttribute("entity") T entity,
			BindingResult bindingResult,
			RedirectAttributes redirectAttributes) {
		return super.update(model, id, entity, bindingResult, redirectAttributes);
	}

	@GetMapping("/{id}/delete")
	@Override
	public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
		return super.delete(id, redirectAttributes);
	}

}
