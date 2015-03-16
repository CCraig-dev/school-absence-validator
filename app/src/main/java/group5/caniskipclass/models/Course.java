package group5.caniskipclass.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import group5.caniskipclass.persistence.CanISkipClassContract;
import group5.caniskipclass.persistence.CanISkipClassDbHelper;

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

    public void delete(Context appcontext) {

        CanISkipClassDbHelper dbhelp = CanISkipClassDbHelper.getInstance(appcontext);
        SQLiteDatabase db = dbhelp.getWritableDatabase();

        // Define 'where' part of query.
        String cSelection = CanISkipClassContract.CourseEntry._ID + "=?";
        // Specify arguments in placeholder order.
        String[] cSelectionArgs = { String.valueOf(id) };
        // Issue SQL statement.

        String aSelection = CanISkipClassContract.AssignmentEntry.COLUMN_NAME_CLASS_ID + "=?";
        // Specify arguments in placeholder order.
        String[] aSelectionArgs = { String.valueOf(id) };
        // Issue SQL statement.


        db.delete(CanISkipClassContract.AssignmentEntry.TABLE_NAME, aSelection, aSelectionArgs);
        db.delete(CanISkipClassContract.CourseEntry.TABLE_NAME, cSelection, cSelectionArgs);

    }

    public void addAssignment(Assignment newAssignment, String cat, Context appcontext) {



        CanISkipClassDbHelper dbhelp = CanISkipClassDbHelper.getInstance(appcontext);
        SQLiteDatabase db = dbhelp.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CanISkipClassContract.AssignmentEntry.COLUMN_NAME_NAME, newAssignment.getName());
        values.put(CanISkipClassContract.AssignmentEntry.COLUMN_NAME_GRADE, newAssignment.getGrade());
        values.put(CanISkipClassContract.AssignmentEntry.COLUMN_NAME_WEIGHT, newAssignment.getWeight());
        values.put(CanISkipClassContract.AssignmentEntry.COLUMN_NAME_CATEGORY, cat);
        values.put(CanISkipClassContract.AssignmentEntry.COLUMN_NAME_CLASS_ID, id);

        long newRowId;
        newRowId = db.insert(
                CanISkipClassContract.AssignmentEntry.TABLE_NAME,
                null,
                values
        );

    }


}
