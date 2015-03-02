package group5.caniskipclass;

import java.util.ArrayList;

/**
 * Created by jcdesimp on 2/25/15.
 */
public class CourseList {

    private ArrayList<Course> courses;

    public CourseList() {
        this.courses = new ArrayList<>();
    }

    public void addNewCourse(Course c) {
        courses.add(c);
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public Course findCourse(long id) {

        return null;
    }

    public void deleteCourse(long id) {

    }

    public void editCourse(long id) {

    }

}
