package be.abis.exercise;

import be.abis.exercise.exception.CourseNotFoundException;
import be.abis.exercise.model.Course;
import be.abis.exercise.service.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CourseServiceTest {

	@Autowired
    CourseService courseService;

	@Test
	public void course7900isWorkshopSQL() {
		Course c = courseService.findCourse(7900);
		assertEquals("SQLWS",c.getShortTitle().toUpperCase());
	}

	@Test
	public void findCourseByShortTitle() {
		Course c = courseService.findCourse("SQLWS");
		assertEquals(7900, c.getCourseId());
	}

	@Test
	public void unknownCourseIsNull() {
		assertNull(courseService.findCourse(1));
	}

	@Test
	public void thereAreSeveralCourses() {
		assertTrue(courseService.findAllCourses().size() > 10);
	}

	// the DML tests run in a transaction that is rolled back, so they leave the DB untouched
	// and are independent of each other (no @Order needed)

	@Test
	@Transactional
	public void addCourse() {
		Course c = courseService.addCourse(new Course(0, "JPATEST", "Test course for JPA", 2, 400.0));
		assertTrue(c.getCourseId() > 0);
		assertEquals("JPATEST", courseService.findCourse("JPATEST").getShortTitle());
	}

	@Test
	@Transactional
	public void updateCourse() {
		Course c = courseService.addCourse(new Course(0, "JPATEST", "Test course for JPA", 2, 400.0));
		Course update = new Course(c.getCourseId(), "JPATEST", "Updated title", 3, 450.0);
		Course updated = courseService.updateCourse(update);
		assertEquals("Updated title", updated.getLongTitle());
		assertEquals(3, courseService.findCourse(c.getCourseId()).getNumberOfDays());
	}

	@Test
	@Transactional
	public void updateUnknownCourseFails() {
		assertThrows(CourseNotFoundException.class,
				() -> courseService.updateCourse(new Course(1, "X", "X", 1, 1.0)));
	}

	@Test
	@Transactional
	public void deleteCourse() {
		Course c = courseService.addCourse(new Course(0, "JPATEST", "Test course for JPA", 2, 400.0));
		courseService.deleteCourse(c.getCourseId());
		assertNull(courseService.findCourse(c.getCourseId()));
	}

	@Test
	public void changePriceOfCourse() {
		// not @Transactional on purpose: the modifying native query runs in its own transaction,
		// so the price is restored explicitly
		double oldPrice = courseService.findCourse(7900).getPricePerDay();
		try {
			courseService.changePrice(7900, 999.0);
			assertEquals(999.0, courseService.findCourse(7900).getPricePerDay());
		} finally {
			courseService.changePrice(7900, oldPrice);
		}
		assertEquals(oldPrice, courseService.findCourse(7900).getPricePerDay());
	}

	@Test
	public void changePriceOfUnknownCourseFails() {
		assertThrows(CourseNotFoundException.class, () -> courseService.changePrice(1, 10.0));
	}

}
