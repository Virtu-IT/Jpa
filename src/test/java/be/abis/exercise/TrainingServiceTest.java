package be.abis.exercise;

import be.abis.exercise.dto.EnrolmentDTO;
import be.abis.exercise.dto.SessionDTO;
import be.abis.exercise.exception.EnrolException;
import be.abis.exercise.exception.SessionCanNotBeDeletedException;
import be.abis.exercise.form.PersonForm;
import be.abis.exercise.model.Course;
import be.abis.exercise.model.Session;
import be.abis.exercise.repository.CourseJpaRepository;
import be.abis.exercise.repository.EnrolmentJpaRepository;
import be.abis.exercise.repository.PersonJpaRepository;
import be.abis.exercise.repository.SessionJpaRepository;
import be.abis.exercise.service.TrainingService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TrainingServiceTest {

    private static final String NEW_MAIL = "jpa.tester@newcompany.be";

    @Autowired
    TrainingService trainingService;

    @Autowired
    SessionJpaRepository sessionRepository;

    @Autowired
    CourseJpaRepository courseRepository;

    @Autowired
    EnrolmentJpaRepository enrolmentRepository;

    @Autowired
    PersonJpaRepository personRepository;

    private Integer createdSessionId;

    @AfterEach
    public void cleanUp() {
        if (createdSessionId != null) {
            enrolmentRepository.findAll().stream()
                    .filter(e -> e.getId().getSessionId() == createdSessionId)
                    .forEach(enrolmentRepository::delete);
            sessionRepository.deleteById(createdSessionId);
            createdSessionId = null;
        }
        personRepository.findByEmailAddressIgnoreCase(NEW_MAIL).ifPresent(personRepository::delete);
    }

    private Session createSession() {
        Session s = new Session();
        s.setStartDate(LocalDate.of(2030, 1, 1));
        s.setKind("p");
        s.setIncome(0);
        s.setCourse(courseRepository.findByShortTitle("SQLWS"));
        s = sessionRepository.save(s);
        createdSessionId = s.getSessionId();
        return s;
    }

    private PersonForm existingPersonForm() {
        PersonForm f = new PersonForm();
        f.setEmailAddress("jan.smiths@abis.be");
        return f;
    }

    private PersonForm newPersonForm() {
        PersonForm f = new PersonForm();
        f.setFirstName("Jpa");
        f.setLastName("Tester");
        f.setBirthDate(LocalDate.of(1990, 1, 1));
        f.setEmailAddress(NEW_MAIL);
        f.setPassword("secret123");
        f.setLanguage("nl");
        return f;
    }

    @Test
    public void welcomeMessage() {
        assertEquals("Welcome to the Abis Training Service", trainingService.getWelcomeMessage());
    }

    @Test
    public void findSessionsForDB2BAS() {
        // session 1 is active, session 10 for the same course is cancelled
        List<SessionDTO> sessions = trainingService.findSessionsForCourse("DB2BAS");
        assertEquals(1, sessions.size());
        SessionDTO s = sessions.get(0);
        assertEquals(1, s.sessionId());
        assertEquals(LocalDate.of(2017, 1, 12), s.startDate());
        assertFalse(s.cancelled());
        assertEquals("JAN", s.instructorFirstName().trim());
        assertEquals("DB2, an overview", s.courseLongTitle());
    }

    @Test
    public void jpqlQueryAlsoReturnsCancelledSessions() {
        assertEquals(2, sessionRepository.findSessionsForCourse("db2bas").size());
    }

    @Test
    public void findEnrolmentsOfPerson() {
        // person 25 has enrolments in sessions 1, 3, 7, 8 and 9; none of them cancelled
        List<EnrolmentDTO> enrolments = trainingService.findEnrolments(25);
        assertEquals(5, enrolments.size());
        assertEquals("FRITS", enrolments.get(0).firstName().trim());
        assertEquals("COVERDAT", enrolments.get(0).companyName().trim());
    }

    @Test
    public void cancelledEnrolmentsAreNotListed() {
        // person 18 has a cancelled enrolment in session 1 and an active one in session 7
        assertEquals(1, trainingService.findEnrolments(18).size());
    }

    @Test
    public void cancelSession() {
        // not @Transactional on purpose: the modifying native query manages its own transaction
        Session s = createSession();
        assertFalse(sessionRepository.findById(s.getSessionId()).orElseThrow().isCancelled());
        trainingService.cancelSession(s.getSessionId());
        assertTrue(sessionRepository.findById(s.getSessionId()).orElseThrow().isCancelled());
    }

    @Test
    public void deleteSessionWithoutEnrolments() throws SessionCanNotBeDeletedException {
        Session s = createSession();
        trainingService.deleteSession(s.getSessionId());
        assertFalse(sessionRepository.existsById(s.getSessionId()));
        createdSessionId = null;
    }

    @Test
    public void deleteSessionWithEnrolmentsFails() {
        assertThrows(SessionCanNotBeDeletedException.class, () -> trainingService.deleteSession(1));
        assertTrue(sessionRepository.existsById(1));
    }

    @Test
    public void enrolExistingPerson() throws EnrolException {
        Session s = createSession();
        trainingService.enrolForSession(existingPersonForm(), s.getSessionId());
        assertEquals(1, enrolmentRepository.findMaxEnrolmentNr(s.getSessionId()));
        // the same person can not enrol twice
        assertThrows(EnrolException.class, () -> trainingService.enrolForSession(existingPersonForm(), s.getSessionId()));
    }

    @Test
    public void enrolNewPerson() throws EnrolException {
        Session s = createSession();
        trainingService.enrolForSession(newPersonForm(), s.getSessionId());
        assertTrue(personRepository.findByEmailAddressIgnoreCase(NEW_MAIL).isPresent());
        assertEquals(1, enrolmentRepository.findMaxEnrolmentNr(s.getSessionId()));
    }

    @Test
    public void enrolForUnknownSessionRollsBackNewPerson() {
        assertThrows(EnrolException.class, () -> trainingService.enrolForSession(newPersonForm(), 99999));
        assertFalse(personRepository.findByEmailAddressIgnoreCase(NEW_MAIL).isPresent());
    }

    @Test
    public void enrolForCancelledSessionRollsBackNewPerson() {
        assertThrows(EnrolException.class, () -> trainingService.enrolForSession(newPersonForm(), 3));
        assertFalse(personRepository.findByEmailAddressIgnoreCase(NEW_MAIL).isPresent());
    }

}
