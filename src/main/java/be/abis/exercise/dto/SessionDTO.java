package be.abis.exercise.dto;

import java.time.LocalDate;

public record SessionDTO(int sessionId,
                         LocalDate startDate,
                         String instructorFirstName,
                         String instructorLastName,
                         String kind,
                         boolean cancelled,
                         String courseLongTitle) {
}
