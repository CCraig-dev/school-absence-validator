package group5.caniskipclass;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class CourseDetailActivity extends ActionBarActivity {

    private int position;
    private Course thisCourse;
    private ArrayList<Category> categoryList;
    private Map<Category, List<Assignment>> categorizedAssignments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);

        position = getIntent().getExtras().getInt("position");

        // Set title of activity to course name
        CourseList courseList = CourseList.getInstance(this);
        thisCourse = courseList.getCourses().get(position);
        String courseName = thisCourse.getName();
        setTitle(courseName);
        categoryList = new ArrayList<Category>();



        updateList();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            updateList();
        }
        super.onWindowFocusChanged(hasFocus);
    }

    private void updateList() {

        CanISkipClassDbHelper dbhelp = CanISkipClassDbHelper.getInstance(CourseDetailActivity.this);
        SQLiteDatabase db = dbhelp.getWritableDatabase();

        //ListView lv = (ListView) findViewById(R.id.categorylist);
        //lv.setAdapter(new ArrayAdapter<>(this, R.layout.category_list_item, R.id.category_name, cl));

        categoryList = new ArrayList<Category>();


        Cursor c = db.rawQuery("select * from " + CanISkipClassContract.AssignmentEntry.TABLE_NAME + " WHERE " +
                CanISkipClassContract.AssignmentEntry.COLUMN_NAME_CLASS_ID + " = " + thisCourse.getId(), null);
        System.out.println(thisCourse.getId());


        c.moveToFirst();

        HashMap<String, Category> foundCats = new HashMap<>();

        while(!c.isAfterLast()) {
            String name = c.getString(c.getColumnIndex(CanISkipClassContract.AssignmentEntry.COLUMN_NAME_NAME));
            System.out.println("Assign class id: " + c.getInt(c.getColumnIndex(CanISkipClassContract.AssignmentEntry.COLUMN_NAME_CLASS_ID)));
            int grade;
            if (c.isNull(c.getColumnIndex(CanISkipClassContract.AssignmentEntry.COLUMN_NAME_GRADE))) {
                grade = -1;
            } else {
                grade = c.getInt(c.getColumnIndex(CanISkipClassContract.AssignmentEntry.COLUMN_NAME_GRADE));

            }

            int weight = c.getInt(c.getColumnIndex(CanISkipClassContract.AssignmentEntry.COLUMN_NAME_WEIGHT));

            String categoryName = c.getString(c.getColumnIndex(CanISkipClassContract.AssignmentEntry.COLUMN_NAME_CATEGORY));

            // if this category hasn't been seen yet, create it
            if(!foundCats.containsKey(categoryName)) {
                foundCats.put(categoryName, new Category(categoryName, 0));
            }
            if(grade < 0) {
                foundCats.get(categoryName).addAssignment(new Assignment(name, weight));
            } else {
                foundCats.get(categoryName).addAssignment(new Assignment(name, weight, grade));
            }

            c.moveToNext();

        }

        c.close();
        for(Category ca : foundCats.values()) {
            categoryList.add(ca);
        }
//        categoryList.add(new Category("Homework", 50));
//        categoryList.add(new Category("Exams", 50));
//
//        categoryList.get(0).addAssignment(new Assignment("Homework 1", 20, 17));
//        categoryList.get(0).addAssignment(new Assignment("Homework 2", 40, 23));
//
//        categoryList.get(1).addAssignment(new Assignment("Midterm", 100, 77));
//        categoryList.get(1).addAssignment(new Assignment("Final Exam", 40, 88));

        createCollection();


        ExpandableListView elv = (ExpandableListView) findViewById(R.id.category_list);

        elv.setAdapter(new CategoryListViewAdapter(this, categoryList, categorizedAssignments, position));
    }

    private void createCollection() {

        categorizedAssignments = new LinkedHashMap<Category, List<Assignment>>();

        for (Category category : categoryList) {
            categorizedAssignments.put(category, category.getAssignments());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_course_detail, menu);
        getMenuInflater().inflate(R.menu.course_detail_activity_actions, menu);
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
            Intent intent = new Intent(this, AddAssignmentActivity.class);
            intent.putExtra("courseId", thisCourse.getId());
            intent.putExtra("nocat", true);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
