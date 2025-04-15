package org.octri.common.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

/**
 * Interface for Views. Similar to a CrudRepository but excludes methods for saving and those for searching using ids.
 */
@NoRepositoryBean
public interface ViewRepository<T, ID> extends Repository<T, ID> {

	/**
	 * Returns the number of entities available.
	 *
	 * @return the number of entities.
	 */
	long count();

	/**
	 * Returns all instances of the type.
	 *
	 * @return all entities
	 */
	Iterable<T> findAll();
}
