package be.abis.exercise.service;

import be.abis.exercise.dto.EnrolmentDTO;
import be.abis.exercise.dto.SessionDTO;
import be.abis.exercise.exception.EnrolException;
import be.abis.exercise.exception.PersonAlreadyExistsException;
import be.abis.exercise.exception.SessionCanNotBeDeletedException;
import be.abis.exercise.form.PersonForm;
import be.abis.exercise.mapper.PersonMapper;
import be.abis.exercise.model.*;
import be.abis.exercise.repository.EnrolmentJpaRepository;
import be.abis.exercise.repository.PersonJpaRepository;
import be.abis.exercise.repository.SessionJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class AbisTrainingService implements TrainingService {

    @Value("Welcome to the Abis Training Service")
    private String welcomeMessage;

    @Autowired
    private PersonService personService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private SessionJpaRepository sessionRepository;

    @Autowired
    private EnrolmentJpaRepository enrolmentRepository;

    @Autowired
    private PersonJpaRepository personRepository;

    @Autowired
    private PersonMapper personMapper;

    @Override
    public String getWelcomeMessage() {
        return welcomeMessage;
    }

    public void setWelcomeMessage(String welcomeMessage) {
        this.welcomeMessage = welcomeMessage;
    }

    public PersonService getPersonService() {
        return personService;
    }

    public CourseService getCourseService() {
        return courseService;
    }

    // BEFORE: public void enrolForSession(Person person, int sessionId) throws EnrolException { }   (empty)
    @Override
    @Transactional(rollbackFor = EnrolException.class)
    public void enrolForSession(PersonForm form, int sessionId) throws EnrolException {
        try {
            // the person (and a new company) is registered first when unknown; this is rolled back if enrolling fails
            Person person = personRepository.findByEmailAddressIgnoreCase(form.getEmailAddress()).orElse(null);
            if (person == null) {
                person = personService.addPerson(personMapper.toEntity(form));
            }

            Session session = sessionRepository.findById(sessionId)
                    .orElseThrow(() -> new EnrolException("session " + sessionId + " does not exist"));
            if (session.isCancelled()) {
                throw new EnrolException("session " + sessionId + " is cancelled");
            }
            if (enrolmentRepository.existsBySessionSessionIdAndEnrolleePersonIdAndCancelFlagIsNull(
                    sessionId, person.getPersonId())) {
                throw new EnrolException(person.getFirstName() + " " + person.getLastName()
                        + " is already enrolled for session " + sessionId);
            }

            Enrolment enrolment = new Enrolment();
            enrolment.setId(new EnrolmentId(sessionId, enrolmentRepository.findMaxEnrolmentNr(sessionId) + 1));
            enrolment.setSession(session);
            enrolment.setEnrollee(person);
            enrolment.setEnrolmentCompany(person.getCompany());
            enrolment.setPricePerDayPayed(session.getCourse().getPricePerDay());
            enrolmentRepository.saveAndFlush(enrolment);
        } catch (EnrolException e) {
            throw e;
        } catch (PersonAlreadyExistsException | RuntimeException e) {
            throw new EnrolException("enrolment failed: " + e.getMessage());
        }
    }

    // BEFORE: public List<Session> findSessionsForCourse(String courseTitle) { return null; }
    @Override
    public List<SessionDTO> findSessionsForCourse(String courseTitle) {
        return sessionRepository.findActiveSessionsForCourse(courseTitle).stream()
                .map(this::toSessionDto)
                .toList();
    }

    // BEFORE: public List<Enrolment> findEnrolments(int personId) { return null; }
    @Override
    public List<EnrolmentDTO> findEnrolments(int personId) {
        // every row is {Person, LocalDate startDate, Course}
        return enrolmentRepository.findEnrolmentsForPerson(personId).stream()
                .map(row -> {
                    Person p = (Person) row[0];
                    Course c = (Course) row[2];
                    String companyName = p.getCompany() != null ? p.getCompany().getName() : null;
                    return new EnrolmentDTO(p.getFirstName(), p.getLastName(), companyName,
                            (LocalDate) row[1], c.getLongTitle());
                })
                .toList();
    }

    @Override
    @Transactional
    public void cancelSession(int sessionId) {
        sessionRepository.cancelSession(sessionId);
    }

    @Override
    @Transactional(rollbackFor = SessionCanNotBeDeletedException.class)
    public void deleteSession(int sessionId) throws SessionCanNotBeDeletedException {
        if (!sessionRepository.existsById(sessionId)) {
            throw new SessionCanNotBeDeletedException("session " + sessionId + " does not exist");
        }
        if (enrolmentRepository.existsByIdSessionId(sessionId)) {
            throw new SessionCanNotBeDeletedException("session " + sessionId + " still has enrolments");
        }
        sessionRepository.deleteById(sessionId);
    }

    private SessionDTO toSessionDto(Session s) {
        Person instructor = s.getInstructor();
        return new SessionDTO(s.getSessionId(),
                s.getStartDate(),
                instructor != null ? instructor.getFirstName() : null,
                instructor != null ? instructor.getLastName() : null,
                s.getKind(),
                s.isCancelled(),
                s.getCourse() != null ? s.getCourse().getLongTitle() : null);
    }

}
