package be.abis.exercise.dto;

import java.time.LocalDate;

public record EnrolmentDTO(String firstName,
                           String lastName,
                           String companyName,
                           LocalDate startDate,
                           String courseTitle) {
}
