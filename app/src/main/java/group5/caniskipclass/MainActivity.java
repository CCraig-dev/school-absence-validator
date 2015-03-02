package group5.caniskipclass;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import group5.caniskipclass.CanISkipClassContract.*;


public class MainActivity extends ActionBarActivity {

    //todo default course values here, remove at later time and retrieve from storage instead
    //String[] courseList = {"Trends in SE", "Physics", "Calculus", "Computer Science", "English", "Web Development"};
    //CourseList courseList = new CourseList();

    ArrayList<String> courseList = new ArrayList<>();


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            updateList();
        }
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        updateList();



    }

    private void updateList() {

        CanISkipClassDbHelper dbhelp = CanISkipClassDbHelper.getInstance(this);
        SQLiteDatabase db = dbhelp.getWritableDatabase();

        String[] projection = {
                CourseEntry._ID,
                CourseEntry.COLUMN_NAME_NAME
        };

        courseList.clear();

        String sortOrder = CourseEntry.COLUMN_NAME_NAME + " DESC";


        Cursor c = db.rawQuery("select * from " + CourseEntry.TABLE_NAME, null);


        c.moveToFirst();

        while(!c.isAfterLast()) {
            String name = c.getString(c.getColumnIndex(CourseEntry.COLUMN_NAME_NAME));
            courseList.add(name);
            //System.out.println("OHNO!");
            c.moveToNext();

        }

        ListView lv = (ListView) findViewById(R.id.courselist);

        lv.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.course_name, courseList));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_add) {
            //todo handle adding a course
            Intent intent = new Intent(this, AddCourseActivity.class);

            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /*public CourseList getCourseList() {
        return courseList;
    }*/
}
