package group5.caniskipclass;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import group5.caniskipclass.models.Course;
import group5.caniskipclass.persistence.CanISkipClassContract;
import group5.caniskipclass.persistence.CanISkipClassDbHelper;

/**
 * Created by jcdesimp on 2/25/15.
 */
public class CourseList {

    // singleton instance
    private static CourseList sInstance;

    private ArrayList<Course> courses;
    private Context appcontext;

    public static CourseList getInstance(Context appcontext) {
        if (sInstance == null) {
            sInstance =  new CourseList(appcontext);
        }
        return sInstance;
    }


    private CourseList(Context appcontext) {
        this.appcontext = appcontext;
        this.courses = new ArrayList<>();
        updateCourseList();
    }

    public void updateCourseList() {
        CanISkipClassDbHelper dbhelp = CanISkipClassDbHelper.getInstance(appcontext);
        SQLiteDatabase db = dbhelp.getWritableDatabase();

        courses.clear();

        String sortOrder = CanISkipClassContract.CourseEntry.COLUMN_NAME_NAME + " DESC";


        Cursor c = db.rawQuery("select * from " + CanISkipClassContract.CourseEntry.TABLE_NAME, null);


        c.moveToFirst();

        while(!c.isAfterLast()) {
            String name = c.getString(c.getColumnIndex(CanISkipClassContract.CourseEntry.COLUMN_NAME_NAME));
            int id = c.getInt(c.getColumnIndex(CanISkipClassContract.CourseEntry._ID));
            String minGrade = c.getString(c.getColumnIndex(CanISkipClassContract.CourseEntry.COLUMN_NAME_MIN_GRADE));
            int numAllowedAbsences = c.getInt(c.getColumnIndex(CanISkipClassContract.CourseEntry.COLUMN_NAME_NUM_ALLOWED_ABSENCE));
            int percentLostForSkip = c.getInt(c.getColumnIndex(CanISkipClassContract.CourseEntry.COLUMN_NAME_LOSS_FOR_SKIP));
            int numSkips = c.getInt(c.getColumnIndex(CanISkipClassContract.CourseEntry.COLUMN_NAME_NUM_SKIPS));
            Course nc = new Course(name, minGrade, numAllowedAbsences, percentLostForSkip, numSkips);
            nc.setId(id);
            courses.add(nc);
            c.moveToNext();
        }

        c.close();
    }

    public void addNewCourse(Course newCourse) {


        CanISkipClassDbHelper dbhelp = CanISkipClassDbHelper.getInstance(appcontext);
        SQLiteDatabase db = dbhelp.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(CanISkipClassContract.CourseEntry.COLUMN_NAME_NAME, newCourse.getName());
        values.put(CanISkipClassContract.CourseEntry.COLUMN_NAME_MIN_GRADE, newCourse.getMinimumGrade());
        values.put(CanISkipClassContract.CourseEntry.COLUMN_NAME_NUM_ALLOWED_ABSENCE, newCourse.getNumAllowedAbsence());
        values.put(CanISkipClassContract.CourseEntry.COLUMN_NAME_LOSS_FOR_SKIP, newCourse.getPercentLostForSkip());
        values.put(CanISkipClassContract.CourseEntry.COLUMN_NAME_NUM_SKIPS, newCourse.getNumSkips());

        long newRowId;
        newRowId = db.insert(
                CanISkipClassContract.CourseEntry.TABLE_NAME,
                null,
                values
        );

        updateCourseList();
    }

    public ArrayList<String> getCourseNames() {
        ArrayList<String> courseNames = new ArrayList<>();

        for(Course c : courses) {
            courseNames.add(c.getName());
        }

        return courseNames;

    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public Course getCourseById(long id) {

        CanISkipClassDbHelper dbhelp = CanISkipClassDbHelper.getInstance(appcontext);
        SQLiteDatabase db = dbhelp.getWritableDatabase();

        Cursor c = db.rawQuery("select * from " + CanISkipClassContract.CourseEntry.TABLE_NAME + " where _id=" + id, null);

        c.moveToFirst();

        if(!c.isAfterLast()) {
            String name = c.getString(c.getColumnIndex(CanISkipClassContract.CourseEntry.COLUMN_NAME_NAME));
            int cid = c.getInt(c.getColumnIndex(CanISkipClassContract.CourseEntry._ID));
            String minGrade = c.getString(c.getColumnIndex(CanISkipClassContract.CourseEntry.COLUMN_NAME_MIN_GRADE));
            int numAllowedAbsences = c.getInt(c.getColumnIndex(CanISkipClassContract.CourseEntry.COLUMN_NAME_NUM_ALLOWED_ABSENCE));
            int percentLostForSkip = c.getInt(c.getColumnIndex(CanISkipClassContract.CourseEntry.COLUMN_NAME_LOSS_FOR_SKIP));
            Course nc = new Course(name, minGrade, numAllowedAbsences, percentLostForSkip);
            nc.setId(cid);
            c.close();

            return nc;
        }

        c.close();
        return null;



    }


}
