package group5.caniskipclass;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import group5.caniskipclass.models.Assignment;
import group5.caniskipclass.models.Category;
import group5.caniskipclass.models.Course;
import group5.caniskipclass.persistence.CanISkipClassContract;
import group5.caniskipclass.persistence.CanISkipClassDbHelper;

/**
 * Created by Chris on 4/5/2015.
 */
public class GradeCalculation {

    private static final String TAG = "Grade Calculation";
    private HashMap<Category, Long> categories;
    private Context appcontext;
    String catNameCol = CanISkipClassContract.CategoryEntry.COLUMN_NAME_NAME;
    String catWeightCol = CanISkipClassContract.CategoryEntry.COLUMN_NAME_WEIGHT;
    String catCourseIdColumn = CanISkipClassContract.CategoryEntry.COLUMN_NAME_COURSE_ID;
    String catIdColumn = CanISkipClassContract.CategoryEntry._ID;
    String catTable = CanISkipClassContract.CategoryEntry.TABLE_NAME;
    String assignmentTable = CanISkipClassContract.AssignmentEntry.TABLE_NAME;
    String assignmentNameCol = CanISkipClassContract.AssignmentEntry.COLUMN_NAME_NAME;
    String assignmentCatIdCol = CanISkipClassContract.AssignmentEntry.COLUMN_NAME_CATEGORY_ID;
    String assignmentGradeCol = CanISkipClassContract.AssignmentEntry.COLUMN_NAME_GRADE;
    String assignmentWeightCol = CanISkipClassContract.AssignmentEntry.COLUMN_NAME_WEIGHT;
    double totalGrade = 100;


    public GradeCalculation (Context appContext){
        this.appcontext = appContext;
    }

    public double getGrade(Course course){
        Map<List<Assignment>, Double> assignmentPoints = getWeightPerCategory(course);
        double grade = getAssignmentPoints(assignmentPoints);
        grade = grade * 100;
        grade = grade / totalGrade;
        Log.d(TAG, "Grade for course " + course.getName() + " " + grade);
        return Math.round(grade);
    }

    public Map<List<Assignment>, Double> getWeightPerCategory(Course course){
        List<Double> points = new ArrayList<Double>();
        Map<List<Assignment>, Double> assignmentWeightMapping = new HashMap<List<Assignment>, Double>();
        Cursor categories = getCategoryInfo(course);
        if (categories.getCount() > 0) {


            while (categories.moveToNext()) {
                int nameIndex = categories.getColumnIndex(catNameCol);
                int weightIndex = categories.getColumnIndex(catWeightCol);
                int idIndex = categories.getColumnIndex(catIdColumn);
                if (nameIndex == -1 || weightIndex == -1 || idIndex == -1){
                    throw new IllegalStateException("One of your indexes be broken");
                }

                String categoryName = categories.getString(nameIndex);
                double categoryWeight = categories.getLong(weightIndex);
                long categoryId = categories.getLong(idIndex);
                Cursor assignments = getAssignmentsByCategory(categoryId);
                Map<String, Long> gradeMultiplier = new HashMap<String, Long>();
                List<Assignment> assignmentObjects = new ArrayList<Assignment>();
                if (assignments.getCount() > 0) {
                    while (assignments.moveToNext()) {
                        // Get the column indexes for the cursor
                        int assignmentNameIndex = assignments.getColumnIndex(assignmentNameCol);
                        int assignmentWeightIndex = assignments.getColumnIndex(assignmentWeightCol);
                        int assignmentGradeIndex = assignments.getColumnIndex(assignmentGradeCol);
                        // Get the values from the cursor
                        String assignmentName = assignments.getString(assignmentNameIndex);
                        double assignmentGrade = assignments.getDouble(assignmentGradeIndex);
                        double assignmentWeight = assignments.getDouble(assignmentWeightIndex);
                        Assignment assignment = new Assignment(assignmentName, assignmentWeight, assignmentGrade);
                        assignmentObjects.add(assignment);
                    }
                    assignmentWeightMapping.put(assignmentObjects, categoryWeight);
                } else {
                    totalGrade = totalGrade - categoryWeight;
                }
                //categoryWeights.put(categoryName, categoryWeight);
            }
        }
        return assignmentWeightMapping;
    }

    public Cursor getCategoryInfo(Course course){
        CanISkipClassDbHelper dbhelp = CanISkipClassDbHelper.getInstance(appcontext);
        SQLiteDatabase db = dbhelp.getWritableDatabase();
        String [] columns = {catIdColumn, catNameCol, catWeightCol};
        String whereClause = catCourseIdColumn + " = ?";
        String [] whereArgs = {Long.toString(course.getId())};

        return db.query(catTable, columns, whereClause, whereArgs, null, null, null);
    }

    public Cursor getAssignmentsByCategory(long categoryId){
        CanISkipClassDbHelper dbhelp = CanISkipClassDbHelper.getInstance(appcontext);
        SQLiteDatabase db = dbhelp.getWritableDatabase();
        String [] columns = {assignmentNameCol, assignmentGradeCol, assignmentWeightCol};
        String whereClause = catIdColumn + " = ?";
        String [] whereArgs = {Long.toString(categoryId)};
        return db.query(assignmentTable, columns, whereClause, whereArgs, null, null, null);
    }

    public double getAssignmentPoints(Map<List<Assignment>, Double> assignmentWeightMappings){
        double totalPoints = 0;
        for (List<Assignment> assignmentList : assignmentWeightMappings.keySet()){
            double categoryWeight = assignmentWeightMappings.get(assignmentList);
            double actualCategoryWeight = categoryWeight / totalGrade;
            for (Assignment assignment: assignmentList){
                // Get the grade and weight from the assignment object
                double assignmentWeight = assignment.getWeight();
                double assignmentGrade = assignment.getGrade();
                Log.d(TAG, "Assignment weight for " + assignment.getName() + ": " + assignmentWeight);
                Log.d(TAG, "Assignment grade for " + assignment.getName() + ": " + assignmentGrade);
                // Get the actual percentage out of 100 for this assignment
                double actualAssignmentGrade = assignmentGrade / assignmentWeight;
                Log.d(TAG, "Actual assignment grade for " + assignment.getName() + ": " + actualAssignmentGrade);
                // Get the weight towards the total grade for the course
                double actualAssignmentWeight = categoryWeight / assignmentList.size();
                Log.d(TAG, "Actual assignment weight for " + assignment.getName() + ": " + actualAssignmentWeight);
                // Get the number of points towards the course Grade this assignment gives
                double assignmentPoints = actualAssignmentWeight * actualAssignmentGrade;
                Log.d(TAG, "Actual assignment points for " + assignment.getName() + ": " + assignmentPoints);
                totalPoints += assignmentPoints;
                Log.d(TAG, "Total points so far: " + totalPoints);
            }
        }
        return totalPoints;
    }

}
