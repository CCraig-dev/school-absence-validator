package group5.caniskipclass;

/**
 * Created by jcdesimp on 2/25/15.
 *
 */
public class Assignment {

    private double weight;
    private double grade;
    private String name;
    private String category;
    private boolean isGraded;

    public Assignment(String name, double weight)
    {
        this.name = name;
        this.weight = weight;

        isGraded = false;
    }

    public Assignment(String name, double weight, double grade)
    {
        this.name = name;
        this.weight = weight;
        this.grade = grade;

        isGraded = true;
    }

    public String getName(){ return name; }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }
}
