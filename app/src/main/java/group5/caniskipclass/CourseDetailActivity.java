package group5.caniskipclass;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class CourseDetailActivity extends ActionBarActivity {

    ArrayList<Category> categoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        int position = getIntent().getExtras().getInt("position");

        // Set title of activity to course name
        CourseList courseList = CourseList.getInstance(this);
        String courseName = courseList.getCourseNames().get(position);
        setTitle(courseName);

        categoryList = new ArrayList<Category>();
        categoryList.add(new Category("Homework", 50));
        categoryList.add(new Category("Exams", 50));
        
        updateList();

    }

    private void updateList() {

        ArrayList<String> cl = new ArrayList<>();

        for(Category cat : categoryList){
            cl.add(cat.getName());
        }


        ListView lv = (ListView) findViewById(R.id.categorylist);

        lv.setAdapter(new ArrayAdapter<>(this, R.layout.category_list_item, R.id.category_name, cl));

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_course_detail, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }
}
