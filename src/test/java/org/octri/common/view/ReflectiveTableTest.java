package org.octri.common.view;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.octri.common.domain.Participant;
import org.octri.common.domain.Response;

public class ReflectiveTableTest {

	private static final Long PARTICIPANT_ID = 1L;
	private static final String PARTICIPANT_NAME = "John Doe";
	private static final LocalDate PARTICIPANT_START_DATE = LocalDate.of(2025, 07, 01);
	private static final String RESPONSE_1 = "Response 1";
	private static final String RESPONSE_2 = "Response 2";

	private Participant participant;

	@BeforeEach
	public void setup() {
		this.participant = new Participant(PARTICIPANT_ID, PARTICIPANT_NAME, PARTICIPANT_START_DATE);
	}

	@Test
	public void testReflectiveTableStandaloneEntity() {
		ReflectiveTable<Participant> participantTable = new ReflectiveTable<>("Participant", Participant.class,
				List.of(participant));
		assertEquals(2, participantTable.headers().size(), "Expected 2 headers for Participant table");
		assertTrue(participantTable.headers().containsAll(Set.of("name", "startDate")), "Missing expected headers");
		assertEquals(1, participantTable.rows().size(), "Expected 1 row for Participant table");
		assertTrue(
				participantTable.rows().get(0).containsAll(Set.of(PARTICIPANT_NAME, PARTICIPANT_START_DATE.toString())),
				"Row does not contain expected values");
	}

	@Test
	public void testReflectiveAssociatedEntity() {
		Response response = new Response(1L, this.participant, RESPONSE_1);
		ReflectiveTable<Response> responseTable = new ReflectiveTable<>("Response", Response.class,
				List.of(response));
		assertEquals(2, responseTable.headers().size(), "Expected 2 headers for Response table");
		assertTrue(responseTable.headers().containsAll(Set.of("participant", "response")), "Missing expected headers");
		assertEquals(1, responseTable.rows().size(), "Expected 1 row for Response table");
		assertTrue(
				responseTable.rows().get(0).containsAll(Set.of(Long.toString(PARTICIPANT_ID), RESPONSE_1)),
				"Row does not contain expected values");
	}

	@Test
	public void testMultipleEntities() {
		Response response = new Response(1L, this.participant, RESPONSE_1);
		Response response2 = new Response(2L, this.participant, RESPONSE_2);
		ReflectiveTable<Response> responseTable = new ReflectiveTable<>("Response", Response.class,
				List.of(response, response2));
		assertEquals(2, responseTable.rows().size(), "Expected 2 rows for Response table");
		assertTrue(
				responseTable.rows().get(0).containsAll(Set.of(Long.toString(PARTICIPANT_ID), RESPONSE_1)),
				"Row 1 does not contain expected values");
		assertTrue(
				responseTable.rows().get(1).containsAll(Set.of(Long.toString(PARTICIPANT_ID), RESPONSE_2)),
				"Row 2 does not contain expected values");
	}
}
