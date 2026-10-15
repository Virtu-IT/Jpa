package be.abis.exercise.service;

import be.abis.exercise.dto.EnrolmentDTO;
import be.abis.exercise.dto.SessionDTO;
import be.abis.exercise.exception.EnrolException;
import be.abis.exercise.exception.SessionCanNotBeDeletedException;
import be.abis.exercise.form.PersonForm;

import java.util.List;

public interface TrainingService {

    public String getWelcomeMessage();
    public PersonService getPersonService();
    public CourseService getCourseService();


    // BEFORE: enrolForSession(Person person, int sessionId); List<Session> findSessionsForCourse(...);
    //         List<Enrolment> findEnrolments(int personId);
    public void enrolForSession(PersonForm person, int sessionId) throws EnrolException;

    List<SessionDTO> findSessionsForCourse(String courseTitle);

    public List<EnrolmentDTO> findEnrolments(int personId);

    void cancelSession(int sessionId);

    void deleteSession(int sessionId) throws SessionCanNotBeDeletedException;

}
