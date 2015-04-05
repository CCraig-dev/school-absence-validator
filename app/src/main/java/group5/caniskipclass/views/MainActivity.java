package group5.caniskipclass.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import group5.caniskipclass.CourseList;
import group5.caniskipclass.MainListViewAdapter;
import group5.caniskipclass.R;
import group5.caniskipclass.models.Course;


public class MainActivity extends ActionBarActivity {

    //todo default course values here, remove at later time and retrieve from storage instead
    //String[] courseList = {"Trends in SE", "Physics", "Calculus", "Computer Science", "English", "Web Development"};
    CourseList courseList;

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
        courseList = CourseList.getInstance(this);
        setContentView(R.layout.activity_main);


        updateList();
    }

    private void updateList() {

        ArrayList<Course> cl = courseList.getCourses();

        ListView lv = (ListView) findViewById(R.id.courselist);

        lv.setAdapter(new MainListViewAdapter(this, cl));
        lv.setClickable(true);
        //set onClick event listener to move to CourseDetailActivity
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent courseDetail = new Intent(view.getContext(), CourseDetailActivity.class);
                courseDetail.putExtra("position", position);
                startActivity(courseDetail);
            }
        });
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
            Intent intent = new Intent(this, AddCourseActivity.class);

            startActivity(intent);
        }

        // This is for the database viewer
        // TODO: remove
        else if (id == R.id.database_view) {
            Intent dbmanager = new Intent(this,AndroidDatabaseManager.class);
            startActivity(dbmanager);
        }

        return super.onOptionsItemSelected(item);
    }




}
