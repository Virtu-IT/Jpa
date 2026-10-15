package be.abis.exercise.service;

import be.abis.exercise.model.Course;

import java.util.List;

public interface CourseService {

    public List<Course> findAllCourses();
    public Course findCourse(int id);
    public Course findCourse(String shortTitle);
    // BEFORE: public void addCourse(Course c); public void updateCourse(Course c);
    // NOW: both return the Course
    public Course addCourse(Course c);
    public Course updateCourse(Course c);
    public void deleteCourse(int id);
    public void changePrice(int id, double price);

}
