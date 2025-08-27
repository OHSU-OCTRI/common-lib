package org.octri.common.customizer;

import org.octri.common.domain.AbstractEntity;
import org.octri.common.domain.Identifiable;

/**
 * Interface defining the methods for finding entities that are uniquely identified by a UUID.
 */
public interface IdentifiableEntityFinder<T extends AbstractEntity & Identifiable> {

	Iterable<T> findAll();

	T findByUuid(String uuid);
}
