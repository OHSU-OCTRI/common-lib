package org.octri.common.domain;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Version;

/**
 * Abstract superclass for database entities.
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AbstractEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Unique identifier.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column
	protected Long id;

	/**
	 * Optimistic locking version.
	 */
	@Version
	protected Integer version;

	/**
	 * Timestamp when the record was created.
	 */
	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	protected Date createdAt;

	/**
	 * Timestamp when the record was last modified.
	 */
	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	protected Date updatedAt;

	/**
	 * Username of the last user to modify the record.
	 */
	@LastModifiedBy
	protected String updatedBy;

	/**
	 * Gets the record's unique identifier.
	 *
	 * @return the unique ID
	 */
	public Long getId() {
		return id;
	}

	/**
	 * Sets the record's unique identifier.
	 *
	 * @param id
	 *            the unique ID
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Gets the record's optimistic locking version.
	 *
	 * @return optimistic locking version
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * Sets the record's optimistic locking version.
	 *
	 * @param version
	 *            optimistic locking version
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * Gets the timestamp when the record was created.
	 *
	 * @return initial creation timestamp
	 */
	public Date getCreatedAt() {
		return createdAt;
	}

	/**
	 * Sets the timestamp when the record was created.
	 *
	 * @param createdAt
	 *            initial creation timestamp
	 */
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	/**
	 * Gets the timestamp when the record was last updated.
	 *
	 * @return last update timestamp
	 */
	public Date getUpdatedAt() {
		return updatedAt;
	}

	/**
	 * Sets the timestamp when the record was last updated.
	 *
	 * @param updatedAt
	 *            last update timestamp
	 */
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	/**
	 * Gets the username of the user who last updated the record.
	 *
	 * @return username of the last updater
	 */
	public String getUpdatedBy() {
		return updatedBy;
	}

	/**
	 * Sets the username of the user who last updated the record.
	 *
	 * @param updatedBy
	 *            username of the last updater
	 */
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		result = prime * result + ((createdAt == null) ? 0 : createdAt.hashCode());
		result = prime * result + ((updatedAt == null) ? 0 : updatedAt.hashCode());
		result = prime * result + ((updatedBy == null) ? 0 : updatedBy.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractEntity other = (AbstractEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		if (createdAt == null) {
			if (other.createdAt != null)
				return false;
		} else if (!createdAt.equals(other.createdAt))
			return false;
		if (updatedAt == null) {
			if (other.updatedAt != null)
				return false;
		} else if (!updatedAt.equals(other.updatedAt))
			return false;
		if (updatedBy == null) {
			if (other.updatedBy != null)
				return false;
		} else if (!updatedBy.equals(other.updatedBy))
			return false;
		return true;
	}

}
