package be.abis.exercise.controller;

import be.abis.exercise.dto.EnrolmentDTO;
import be.abis.exercise.dto.SessionDTO;
import be.abis.exercise.exception.EnrolException;
import be.abis.exercise.exception.SessionCanNotBeDeletedException;
import be.abis.exercise.form.PersonForm;
import be.abis.exercise.service.TrainingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TrainingApiController {

    @Autowired
    TrainingService trainingService;

    // BEFORE: these three methods were stubs returning null / doing nothing:
    //   List<Session> findSessionsForCourse(String courseTitle) { return null; }
    //   List<Enrolment> findEnrolments(int personId) { return null; }
    //   void enrollForSession(@RequestBody Person person, int sessionId) { }
    @GetMapping("sessions/query")
    public List<SessionDTO> findSessionsForCourse(@RequestParam("title") String courseTitle){
        return trainingService.findSessionsForCourse(courseTitle);
    }

    @GetMapping("persons/{id}/enrolments")
    public List<EnrolmentDTO> findEnrolments(@PathVariable("id") int personId)
    {
        return trainingService.findEnrolments(personId);
    }

    @PostMapping("sessions/{id}/enrolments")
    public void enrollForSession(@RequestBody PersonForm person, @PathVariable("id") int sessionId) throws EnrolException {
        trainingService.enrolForSession(person, sessionId);
    }

    @PutMapping("sessions/{id}/cancellation")
    public void cancelSession(@PathVariable("id") int sessionId) {
        trainingService.cancelSession(sessionId);
    }

    @DeleteMapping("sessions/{id}")
    public void deleteSession(@PathVariable("id") int sessionId) throws SessionCanNotBeDeletedException {
        trainingService.deleteSession(sessionId);
    }

}
