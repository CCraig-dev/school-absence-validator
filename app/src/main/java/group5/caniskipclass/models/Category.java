package group5.caniskipclass.models;

import java.util.ArrayList;

/**
 * Created by jcdesimp on 2/25/15.
 */
public class Category {
    private String name;
    private double weight;
    private long id;

    private ArrayList<Assignment> assignments;

    public Category(String name, double weight, long id){

        this.name = name;
        this.weight = weight;
        this.id = id;
    }

    public Category(String name, double weight){

        this.name = name;
        this.weight = weight;
        long id = 0;
    }

    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }

    public ArrayList<String> getAssignmentNames()
    {
        ArrayList<String> names = new ArrayList<String>();
        for (Assignment assignment : assignments)
            names.add(assignment.getName());

        return names;
    }

    public void setId(long id) { this.id = id; }

    public long getId() { return id; }

    public void addAssignment(Assignment assignment){
        assignments.add(assignment);
    }

    public String getName(){
        return name;
    }

    public double getWeight(){
        return weight;
    }

    public void addCategory(Category category){

    }


}
