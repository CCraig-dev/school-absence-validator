package group5.caniskipclass.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

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

    private ArrayList<Category> categories;

    public Category findCategory(String name) {

        return null;
    }


    public void delete(Context appcontext) {

        CanISkipClassDbHelper dbhelp = CanISkipClassDbHelper.getInstance(appcontext);
        SQLiteDatabase db = dbhelp.getWritableDatabase();

        // Define 'where' part of query.
        String cSelection = CanISkipClassContract.CategoryEntry._ID + "=?";
        // Specify arguments in placeholder order.
        String[] cSelectionArgs = { String.valueOf(id) };
        // Issue SQL statement.

        String aSelection = CanISkipClassContract.AssignmentEntry.COLUMN_NAME_CATEGORY_ID + "=?";
        // Specify arguments in placeholder order.
        String[] aSelectionArgs = { String.valueOf(id) };
        // Issue SQL statement.


        db.delete(CanISkipClassContract.AssignmentEntry.TABLE_NAME, aSelection, aSelectionArgs);
        db.delete(CanISkipClassContract.CourseEntry.TABLE_NAME, cSelection, cSelectionArgs);

    }

    public void addAssignment(Assignment newAssignment, String cat, Context appcontext) {



        CanISkipClassDbHelper dbhelp = CanISkipClassDbHelper.getInstance(appcontext);
        SQLiteDatabase db = null;
        try {
            db = dbhelp.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(CanISkipClassContract.AssignmentEntry.COLUMN_NAME_NAME, newAssignment.getName());
            values.put(CanISkipClassContract.AssignmentEntry.COLUMN_NAME_GRADE, newAssignment.getGrade());
            values.put(CanISkipClassContract.AssignmentEntry.COLUMN_NAME_WEIGHT, newAssignment.getWeight());
            values.put(CanISkipClassContract.AssignmentEntry.COLUMN_NAME_CATEGORY_ID, getCategoryId(cat,appcontext));

            long newRowId;
            newRowId = db.insert(
                    CanISkipClassContract.AssignmentEntry.TABLE_NAME,
                    null,
                    values
            );
        } finally {
            if (db != null){
                db.close();
            }
        }

    }

    /**
     * Add a category to this class and commit to the databse
     * @param category Category to be added
     * @param appContext Context you are using to add
     */
    public void addCategory(Category category, Context appContext){
        // get the database for the applicaiton
        CanISkipClassDbHelper dbhelp = CanISkipClassDbHelper.getInstance(appContext);
        SQLiteDatabase db = null;
        try {
            db = dbhelp.getWritableDatabase();

            ContentValues values = new ContentValues();
            // Assign the category to the course
            values.put(CanISkipClassContract.CategoryEntry.COLUMN_NAME_COURSE_ID, id);
            // Set the name and weight using attributes from the passed in Category
            values.put(CanISkipClassContract.CategoryEntry.COLUMN_NAME_NAME, category.getName());
            values.put(CanISkipClassContract.CategoryEntry.COLUMN_NAME_WEIGHT, category.getWeight());

            long newRowId;
            // Do the insert into the database
            newRowId = db.insert(
                    CanISkipClassContract.CategoryEntry.TABLE_NAME,
                    null,
                    values
            );
            category.setId(newRowId);
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public int getCategoryId(String name, Context appcontext) {

        CanISkipClassDbHelper dbhelp = CanISkipClassDbHelper.getInstance(appcontext);
        SQLiteDatabase db = dbhelp.getWritableDatabase();

        Cursor c = db.rawQuery("select * from " + CanISkipClassContract.CategoryEntry.TABLE_NAME + " where " + CanISkipClassContract.CategoryEntry.COLUMN_NAME_NAME + "='" + name+ "'", null);

        c.moveToFirst();

        if(!c.isAfterLast()) {
            int cid = c.getInt(c.getColumnIndex(CanISkipClassContract.CategoryEntry._ID));

            return cid;
        }

        c.close();
        return -1;

    }

   //public int getGrade()

}
