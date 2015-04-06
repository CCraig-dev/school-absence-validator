package group5.caniskipclass;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import group5.caniskipclass.models.Assignment;
import group5.caniskipclass.models.Category;
import group5.caniskipclass.models.Course;
import group5.caniskipclass.persistence.CanISkipClassContract;
import group5.caniskipclass.persistence.CanISkipClassDbHelper;

/**
 * Created by Chris on 4/5/2015.
 */
public class GradeCalculation {

    private HashMap<Category, Long> categories;
    private Context appcontext;
    String nameColumn = CanISkipClassContract.CategoryEntry.COLUMN_NAME_NAME;
    String weightColumn = CanISkipClassContract.CategoryEntry.COLUMN_NAME_WEIGHT;
    String categoryCourseIdColumn = CanISkipClassContract.CategoryEntry._ID;
    String categoryIdColumn = CanISkipClassContract.CategoryEntry.COLUMN_NAME_COURSE_ID;
    String categoryTable = CanISkipClassContract.CourseEntry.TABLE_NAME;

    public GradeCalculation (Context appContext){
        this.appcontext = appContext;
    }

    public double getGrade(Course course){
        categories = getWeightPerCategory(course);
        return 0;
    }

    public HashMap<Category, Long> getWeightPerCategory(Course course){
        HashMap<Category, Long> categoryWeights = new HashMap<Category, Long>();

        Cursor categories = getCategoryInfo(course);
        while (categories.moveToNext()){
            int nameIndex = categories.getColumnIndex(nameColumn);
            int weightIndex = categories.getColumnIndex(weightColumn);
            int idIndex = categories.getColumnIndex(categoryCourseIdColumn);
            String categoryName = categories.getString(nameIndex);
            Long categoryWeight = categories.getLong(weightIndex);
            //categoryWeights.put(categoryName, categoryWeight);
        }
        return categoryWeights;
    }

    public Cursor getCategoryInfo(Course course){
        CanISkipClassDbHelper dbhelp = CanISkipClassDbHelper.getInstance(appcontext);
        SQLiteDatabase db = dbhelp.getWritableDatabase();
        String [] columns = {nameColumn, weightColumn};
        String whereClause = "courseIdColumn = ?";
        String [] whereArgs = {Long.toString(course.getId())};

        return db.query(categoryTable, columns, whereClause, whereArgs, null, null, null);
    }
}
