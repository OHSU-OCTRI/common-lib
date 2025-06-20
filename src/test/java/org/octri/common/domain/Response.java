package org.octri.common.domain;

import jakarta.persistence.ManyToOne;

/**
 * A test domain class representing a participant response.
 */
public class Response extends AbstractEntity {

	@ManyToOne
	private Participant participant;
	private String response;

	public Response(Long id, Participant participant, String response) {
		this.participant = participant;
		this.response = response;
	}

	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

}