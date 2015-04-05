package group5.caniskipclass.views;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import group5.caniskipclass.persistence.CanISkipClassContract;
import group5.caniskipclass.persistence.CanISkipClassDbHelper;
import group5.caniskipclass.CategoryListViewAdapter;
import group5.caniskipclass.CourseList;
import group5.caniskipclass.R;
import group5.caniskipclass.models.Assignment;
import group5.caniskipclass.models.Category;
import group5.caniskipclass.models.Course;


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

        //categoryList = new ArrayList<Category>();

        String categoryQuery = "select * from " + CanISkipClassContract.CategoryEntry.TABLE_NAME + " WHERE " +
                CanISkipClassContract.CategoryEntry.COLUMN_NAME_COURSE_ID + " = " + thisCourse.getId();
        System.out.println("Category Query: " + categoryQuery);

        Cursor c = db.rawQuery(categoryQuery, null);

        System.out.println("db queried");


        c.moveToFirst();

        //HashMap<String, Category> foundCats = new HashMap<>();
        // Loop through the categories
        while(!c.isAfterLast()) {
            Category currentCategory = null;
            String catName = c.getString(c.getColumnIndex(CanISkipClassContract.CategoryEntry.COLUMN_NAME_NAME));

            System.out.println("Category found from db: " + catName);


            int catWeight = c.getInt(c.getColumnIndex(CanISkipClassContract.AssignmentEntry.COLUMN_NAME_WEIGHT));

            int catID = c.getInt(c.getColumnIndex(CanISkipClassContract.CategoryEntry._ID));

            // Loop through the list to see if the category is already in it
            boolean categoryInList = false;
            for (Category category : categoryList){
                if (category.getId() == catID) {
                    categoryInList = true;
                    currentCategory = category;
                }
            }
            // If the category is not in the list, add it
            if (!categoryInList){
                currentCategory = new Category(catName, catWeight, catID);
                categoryList.add(currentCategory);
            }
            //int weight = c.getInt(c.getColumnIndex(CanISkipClassContract.AssignmentEntry.COLUMN_NAME_WEIGHT));

            //String categoryName = c.getString(c.getColumnIndex(CanISkipClassContract.AssignmentEntry.COLUMN_NAME_CATEGORY));

            //if this category hasn't been seen yet, create it
            /*if(!foundCats.containsKey(categoryName)) {
                foundCats.put(categoryName, new Category(categoryName, 0));
            }
            if(grade < 0) {
                foundCats.get(categoryName).addAssignment(new Assignment(name, weight));
            } else {
                foundCats.get(categoryName).addAssignment(new Assignment(name, weight, grade));
            }*/

            // Get all the assignments for the category
            Cursor ac = db.rawQuery("select * from " + CanISkipClassContract.AssignmentEntry.TABLE_NAME + " WHERE " +
                    CanISkipClassContract.AssignmentEntry.COLUMN_NAME_CATEGORY_ID + " = " + catID, null);

            // Make sure we pull back records for the cursor
            if (ac.getCount() > 0) {
                ac.moveToFirst();

                while (!ac.isAfterLast() && !ac.isBeforeFirst()) {

                    String aName = ac.getString(ac.getColumnIndex(CanISkipClassContract.AssignmentEntry.COLUMN_NAME_NAME));
                    // Get the weight index and weight from database
                    int weightIndex = ac.getColumnIndex(CanISkipClassContract.AssignmentEntry.COLUMN_NAME_WEIGHT);
                    int aWeight = ac.getInt(weightIndex);
                    // Get the grade index and grade from the database
                    int gradeIndex = ac.getColumnIndex(CanISkipClassContract.AssignmentEntry.COLUMN_NAME_GRADE);
                    int aGrade = ac.getInt(gradeIndex);

                    // Create new assignment object with query results
                    Assignment assignment = new Assignment(aName, aWeight, aGrade);
                    ArrayList<String> currentAssignments = currentCategory.getAssignmentNames();
                    boolean assignmentInList = false;
                    for (String assignmentName : currentAssignments)
                    {
                        if (assignmentName.equals(aName)) {
                            assignmentInList = true;
                        }
                    }
                    if (!assignmentInList)
                        currentCategory.addAssignment(assignment);

                    ac.moveToNext();
                }
            }
            ac.close();

            c.moveToNext();
        }
            db.close();
        /*for(Category ca : foundCats.values()) {
            categoryList.add(ca);
        }*/


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

    public void OnAddCategoryClicked(View view) {
        final Dialog newCatDialog = new Dialog(this);
        newCatDialog.setContentView(R.layout.dialog_add_category);

        newCatDialog.setTitle("Enter New Category");

        Button ok = (Button) newCatDialog.findViewById(R.id.ok);
        Button cancel = (Button) newCatDialog.findViewById(R.id.cancel);

        final EditText nameField = (EditText) newCatDialog.findViewById(R.id.category_name);
        final EditText weightField = (EditText) newCatDialog.findViewById(R.id.category_weight);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameField.getText().length() == 0 || weightField.getText().length() == 0 ) {
                    new AlertDialog.Builder(v.getContext())
                            .setTitle("Incomplete Fields")
                            .setMessage("Please fill in all fields before submitting.")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                }
                            })
                                    //.setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
                else {
                    String name = (String) nameField.getText().toString();
                    int weight = Integer.parseInt(weightField.getText().toString());
                    System.out.println("Category name: " + name + " weight: " + weight);
                    Category category = new Category(name, weight);
                    thisCourse.addCategory(category, getApplicationContext());
//
//                    ContentValues values = new ContentValues();
//                    values.put(CanISkipClassContract.CategoryEntry.COLUMN_NAME_NAME, name);
//                    values.put(CanISkipClassContract.CategoryEntry.COLUMN_NAME_WEIGHT, weight);
//                    values.put(CanISkipClassContract.CategoryEntry.COLUMN_NAME_COURSE_ID, thisCourse.getId());

                    categoryList.add(category);
                    updateList();
                    newCatDialog.dismiss();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newCatDialog.dismiss();
            }
        });

        newCatDialog.show();
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
        } else if (id == R.id.action_delete) {
            new AlertDialog.Builder(this)
                    .setTitle("Are you sure?")
                    .setMessage("Are you sure you want to delete this course? This action cannot be undone.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                            thisCourse.delete(getApplicationContext());
                            CourseList.getInstance(getApplicationContext()).updateCourseList();
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // cancel deletion

                        }
                    })
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }
}
