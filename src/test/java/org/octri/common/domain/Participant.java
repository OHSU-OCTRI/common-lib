package org.octri.common.domain;

import java.time.LocalDate;

/**
 * A test domain class representing a participant.
 */
public class Participant extends AbstractEntity {

	private String name;
	private LocalDate startDate;

	public Participant(Long id, String name, LocalDate startDate) {
		this.id = id;
		this.name = name;
		this.startDate = startDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	@Override
	public String toString() {
		return "Participant [id=" + id + ", name=" + name + ", startDate=" + startDate + "]";
	}
}
