package group5.caniskipclass.models;

/**
 * Created by jcdesimp on 2/25/15.
 */
public class Course {

    private String name;
    private double weight;
    private long id;
    private int numAbscence;
    private int numAllowedAbsence;
    private double currentGrade;
    private String minimumGrade;

    public Course(String name, String minimumGrade, int numAllowedAbsence) {
        this.name = name;
        this.numAllowedAbsence = numAllowedAbsence;
        this.minimumGrade = minimumGrade;

        this.id = 0;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getNumAbscence() {
        return numAbscence;
    }

    public int getNumAllowedAbsence() {
        return numAllowedAbsence;
    }

    public double getCurrentGrade() {
        return currentGrade;
    }

    public String getMinimumGrade() {
        return minimumGrade;
    }

    public Category findCategory(String name) {

        return null;
    }

    public void addAssignment(Assignment a) {

    }


}
