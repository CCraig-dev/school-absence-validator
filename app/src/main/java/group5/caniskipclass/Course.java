package group5.caniskipclass;

import java.util.ArrayList;

/**
 * Created by jcdesimp on 2/25/15.
 */
public class Course {

    private String name;
    private double weight;
    private long id;
    private int numAbscence;
    private int numAllowedAbscence;
    private double currentGrade;
    private double minimumGrade;

    public Course(String name, int numAllowedAbscence, double minimumGrade) {
        this.name = name;
        this.numAllowedAbscence = numAllowedAbscence;
        this.minimumGrade = minimumGrade;

        this.id = 0;


    }






    public Category findCategory(String name) {

        return null;
    }


}
