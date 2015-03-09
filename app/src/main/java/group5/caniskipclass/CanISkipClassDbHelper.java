package group5.caniskipclass;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import group5.caniskipclass.CanISkipClassContract.*;

/**
 * Created by jcdesimp on 3/1/15.
 */
public class CanISkipClassDbHelper extends SQLiteOpenHelper {

    private static CanISkipClassDbHelper sInstance;

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    // Query strings to create the database
    private static final String SQL_CREATE_COURSE_TABLE =
            "CREATE TABLE " + CourseEntry.TABLE_NAME + " (" +
                    CourseEntry._ID + " INTEGER PRIMARY KEY," +
                    CourseEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    CourseEntry.COLUMN_NAME_MIN_GRADE + TEXT_TYPE + COMMA_SEP +
                    CourseEntry.COLUMN_NAME_NUM_ALLOWED_ABSENCE + INTEGER_TYPE +
                    " )";
    private static final String SQL_CREATE_ASSIGNMENT_TABLE =
            "CREATE TABLE " + AssignmentEntry.TABLE_NAME + " (" +
                    AssignmentEntry._ID + " INTEGER PRIMARY KEY," +
                    AssignmentEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    AssignmentEntry.COLUMN_NAME_CATEGORY + TEXT_TYPE + COMMA_SEP +
                    AssignmentEntry.COLUMN_NAME_GRADE + INTEGER_TYPE + COMMA_SEP +
                    AssignmentEntry.COLUMN_NAME_WEIGHT + INTEGER_TYPE +
                    " )"
            ;

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + CourseEntry.TABLE_NAME +
                    "DROP TABLE IF EXISTS " + AssignmentEntry.TABLE_NAME;


    // getter for DBHelper Singleton singleton
    public static CanISkipClassDbHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new CanISkipClassDbHelper(context.getApplicationContext());
        }
        return sInstance;
    }



    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "FeedReader.db";

    public CanISkipClassDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_COURSE_TABLE);
        db.execSQL(SQL_CREATE_ASSIGNMENT_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}