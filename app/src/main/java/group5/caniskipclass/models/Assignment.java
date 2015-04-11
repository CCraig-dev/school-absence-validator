package group5.caniskipclass.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import group5.caniskipclass.persistence.CanISkipClassContract;
import group5.caniskipclass.persistence.CanISkipClassDbHelper;

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
    private long id;

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
        this.id = 0;
    }

    public String getName(){ return name; }

    public double getWeight() { return weight; }
    public double getGrade() {return grade;}

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setGrade(double grade) {
        this.grade = grade;
    }

    public void setID(long id) { this.id = id; }

    public long getID() { return id; }

    public void delete(Context appcontext) {

        CanISkipClassDbHelper dbhelp = CanISkipClassDbHelper.getInstance(appcontext);
        SQLiteDatabase db = dbhelp.getWritableDatabase();


        String aSelection = CanISkipClassContract.AssignmentEntry._ID + "=?";
        // Specify arguments in placeholder order.
        String[] aSelectionArgs = { String.valueOf(id) };
        // Issue SQL statement.

        db.delete(CanISkipClassContract.AssignmentEntry.TABLE_NAME, CanISkipClassContract.AssignmentEntry.COLUMN_NAME_NAME + "='" + name + "'", null);
        //db.delete(CanISkipClassContract.AssignmentEntry.TABLE_NAME, aSelection, aSelectionArgs);

    }
}
