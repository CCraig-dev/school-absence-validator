package group5.caniskipclass;

import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class CourseDetailActivity extends ActionBarActivity {

    ArrayList<Category> categoryList;
    List<String> groupList;
    List<String> childList;
    Map<String, List<String>> categorizedAssignments;


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

        categoryList.get(0).addAssignment(new Assignment("Homework 1", 20, 17));
        categoryList.get(0).addAssignment(new Assignment("Homework 2", 40, 23));

        updateList();

    }

    private void updateList() {

        createGroupList();
        createCollection();

        ArrayList<String> cl = new ArrayList<>();

        for(Category cat : categoryList){
            cl.add(cat.getName());
        }


        //ListView lv = (ListView) findViewById(R.id.categorylist);
        //lv.setAdapter(new ArrayAdapter<>(this, R.layout.category_list_item, R.id.category_name, cl));

        ExpandableListView elv = (ExpandableListView) findViewById(R.id.category_list);

        elv.setAdapter(new CategoryListViewAdapter(this, groupList, categorizedAssignments));

        //elv.setAdapter(new ExpandableListAdapter()



    }


    private void createGroupList(){
        groupList = new ArrayList<String>();
        groupList.add("Homework");
        groupList.add("Exams");

    }

    private void createCollection() {

        String[] exams = {"Midterm", "Final"};
        String[] homeworks = {"Homework 1", "Homework 2"};

        categorizedAssignments = new LinkedHashMap<String, List<String>>();

        loadChild(homeworks);
        categorizedAssignments.put(groupList.get(0), childList);

        loadChild(exams);
        categorizedAssignments.put(groupList.get(1), childList);
    }

    private void loadChild(String[] assignments) {
        childList = new ArrayList<String>();

        for (String assignment: assignments)
            childList.add(assignment);
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