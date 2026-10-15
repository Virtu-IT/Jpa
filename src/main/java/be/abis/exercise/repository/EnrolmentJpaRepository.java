package be.abis.exercise.repository;

import be.abis.exercise.model.Enrolment;
import be.abis.exercise.model.EnrolmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EnrolmentJpaRepository extends JpaRepository<Enrolment, EnrolmentId> {

    // calls the @NamedQuery "Enrolment.findEnrolmentsForPerson"; every row is {Person, LocalDate, Course}
    List<Object[]> findEnrolmentsForPerson(@Param("personId") int personId);

    boolean existsByIdSessionId(int sessionId);

    boolean existsBySessionSessionIdAndEnrolleePersonIdAndCancelFlagIsNull(int sessionId, int personId);

    @Query("select coalesce(max(e.id.enrolmentInSession), 0) from Enrolment e where e.id.sessionId = :sessionId")
    int findMaxEnrolmentNr(@Param("sessionId") int sessionId);

}
