package group5.caniskipclass.views;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

import group5.caniskipclass.CourseList;
import group5.caniskipclass.models.Course;
import group5.caniskipclass.persistence.CanISkipClassContract;
import group5.caniskipclass.persistence.CanISkipClassDbHelper;
import group5.caniskipclass.R;
import group5.caniskipclass.models.Assignment;


public class AddAssignmentActivity extends ActionBarActivity {

    int position;
    String category;
    Course inCourse;
    long courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);
        position = getIntent().getExtras().getInt("position");
        courseId = getIntent().getExtras().getLong("courseId");
        inCourse = CourseList.getInstance(this).getCourseById(courseId);
        //category = getIntent().getExtras().getString("category");

        setTitle("Add new Assignment");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_assignment, menu);
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

        if (id == android.R.id.home) {
            Intent courseDetail = new Intent(this, CourseDetailActivity.class);
            courseDetail.putExtra("position", position);
            startActivity(courseDetail);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void confirmAdd(View view) {
        //EditText aCategory = ((EditText) findViewById(R.id.assignment_category));
        Spinner aCategory = ((Spinner) findViewById(R.id.category_spinner));
        EditText aName = ((EditText) findViewById(R.id.assignment_name));
        EditText grade = ((EditText) findViewById(R.id.assignment_grade));
        EditText weight = ((EditText) findViewById(R.id.assignment_weight));


        String selectQuery = "SELECT "+ CanISkipClassContract.CategoryEntry.COLUMN_NAME_NAME+
                " FROM " + CanISkipClassContract.CategoryEntry.TABLE_NAME + " WHERE " +
                CanISkipClassContract.CategoryEntry.COLUMN_NAME_COURSE_ID + " = " + courseId;

        CanISkipClassDbHelper dbhelp = CanISkipClassDbHelper.getInstance(this);
        SQLiteDatabase db = dbhelp.getWritableDatabase();

        String[] fromColumns = {
                CanISkipClassContract.CategoryEntry.COLUMN_NAME_NAME
        };

        int[] toViews = {
                android.R.id.text1
        };

        Cursor c = db.rawQuery(selectQuery, null);
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, c, fromColumns, toViews, 1);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        aCategory.setAdapter(adapter);

        // validate the fields
        if (aName.getText().length() == 0 || grade.getText().length() == 0 || aCategory.getSelectedItem() == null ) {
            new AlertDialog.Builder(this)
                    .setTitle("Incomplete Fields")
                    .setMessage("Please fill in all fields before submitting.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                            //.setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {

            Assignment newAssignment = new Assignment(aName.getText().toString(),
                    Integer.parseInt(weight.getText().toString()),
                    Integer.parseInt(grade.getText().toString()));

            inCourse.addAssignment(newAssignment, aCategory.getSelectedItem().toString(), this);

            finish();
        }
    }
}
