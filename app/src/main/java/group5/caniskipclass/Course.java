package group5.caniskipclass;

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






    public Category findCategory(String name) {

        return null;
    }


}
