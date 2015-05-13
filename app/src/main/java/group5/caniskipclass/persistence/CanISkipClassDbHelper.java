package group5.caniskipclass.persistence;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import group5.caniskipclass.models.Category;
import group5.caniskipclass.persistence.CanISkipClassContract.*;

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
                    CourseEntry.COLUMN_NAME_NUM_ALLOWED_ABSENCE + INTEGER_TYPE + COMMA_SEP +
                    CourseEntry.COLUMN_NAME_LOSS_FOR_SKIP + INTEGER_TYPE + COMMA_SEP +
                    CourseEntry.COLUMN_NAME_NUM_SKIPS + INTEGER_TYPE + COMMA_SEP +
                    CourseEntry.COLUMN_NAME_PROFESSOR_NAME + TEXT_TYPE +
                    " )";
    private static final String SQL_CREATE_ASSIGNMENT_TABLE =
            "CREATE TABLE " + AssignmentEntry.TABLE_NAME + " (" +
                    AssignmentEntry._ID + " INTEGER PRIMARY KEY," +
                    AssignmentEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    AssignmentEntry.COLUMN_NAME_GRADE + INTEGER_TYPE + COMMA_SEP +
                    AssignmentEntry.COLUMN_NAME_WEIGHT + INTEGER_TYPE + COMMA_SEP +
                    AssignmentEntry.COLUMN_NAME_CATEGORY_ID + INTEGER_TYPE + COMMA_SEP +
                    "FOREIGN KEY (" + AssignmentEntry.COLUMN_NAME_CATEGORY_ID + ") REFERENCES " + CategoryEntry.TABLE_NAME + " (" + CategoryEntry._ID + ")"+
                    " )";
    private static final String SQL_CREATE_CATEGORY_TABLE =
            "CREATE TABLE " + CategoryEntry.TABLE_NAME + " (" +
                    CategoryEntry._ID + " INTEGER PRIMARY KEY," +
                    CategoryEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                    CategoryEntry.COLUMN_NAME_WEIGHT + TEXT_TYPE + COMMA_SEP +
                    CategoryEntry.COLUMN_NAME_COURSE_ID + INTEGER_TYPE + COMMA_SEP +
                    "FOREIGN KEY (" + CategoryEntry.COLUMN_NAME_COURSE_ID + ") REFERENCES " + CourseEntry.TABLE_NAME + " (" + CourseEntry._ID + ")" +
                    " )";

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
    public static final String DATABASE_NAME = "CanSkipClass.db";

    public CanISkipClassDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_COURSE_TABLE);
        db.execSQL(SQL_CREATE_ASSIGNMENT_TABLE);
        db.execSQL(SQL_CREATE_CATEGORY_TABLE);
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

    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "mesage" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);


        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);


            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {


                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){

            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }


    }
}