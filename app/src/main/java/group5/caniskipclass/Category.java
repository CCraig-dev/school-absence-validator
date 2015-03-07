package group5.caniskipclass;

import java.util.ArrayList;

/**
 * Created by jcdesimp on 2/25/15.
 */
public class Category {
    private String name;
    private double weight;

    private ArrayList<Assignment> assignemnts;

    public Category(String n, double w){
        name = n;
        weight = w;
    }

    public ArrayList<Assignment> getAssignemnts() {
        return assignemnts;
    }

    public void AddAssignment(Assignment assignment){
        assignemnts.add(assignment);
    }

    public String getName(){
        return name;
    }

    public double getWeight(){
        return weight;
    }
}
