package be.abis.exercise.repository;

import be.abis.exercise.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

/*
 * BEFORE (no JPA): CourseRepository was an interface that you had to implement yourself,
 * with MemoryCourseRepository keeping the courses in a hard-coded ArrayList:
 *
 *   public interface CourseRepository {
 *       List<Course> findAllCourses();
 *       Course findCourse(int id);
 *       Course findCourse(String shortTitle);
 *   }
 *
 *   @Component
 *   public class MemoryCourseRepository implements CourseRepository {
 *       private ArrayList<Course> courses = new ArrayList<Course>();
 *       public MemoryCourseRepository(){
 *           courses.add(new Course(7850,"DB2BAS","DB2, an overview",5,550.0));
 *           ...
 *       }
 *       public List<Course> findAllCourses() { return courses; }
 *       public Course findCourse(int id) {
 *           return courses.stream().filter(c->c.getCourseId()==id).findFirst().orElse(null);
 *       }
 *       public Course findCourse(String shortTitle) {
 *           return courses.stream().filter(c->c.getShortTitle().equals(shortTitle)).findFirst().orElse(null);
 *       }
 *   }
 *
 * NOW: Spring Data generates the implementation. findAll(), findById(), save() and deleteById()
 * are provided by JpaRepository; findByShortTitle is a query method derived from its name.
 */
public interface CourseJpaRepository extends JpaRepository<Course, Integer> {

    Course findByShortTitle(String shortTitle);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update courses set caprice = :price where cid = :id", nativeQuery = true)
    int changePrice(@Param("id") int courseId, @Param("price") double price);

}
