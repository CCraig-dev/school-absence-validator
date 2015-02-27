package group5.caniskipclass;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;


public class AddCourseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);


        // populate the grades dropdown
        Spinner gradeChoices = (Spinner) findViewById(R.id.grades_spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.letter_grades_pm, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        gradeChoices.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_course, menu);
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


    public void confirmAdd(View view) {

        // Confirm
        //todo add the new course
        EditText cName = ((EditText) findViewById(R.id.course_name));
        Spinner minGrade = ((Spinner) findViewById(R.id.grades_spinner));
        EditText allowedAbsences = ((EditText) findViewById(R.id.allowed_absences));


        // validate the fields
        if (cName.getText().length() == 0 || minGrade.getSelectedItem() == null || allowedAbsences.getText().length() == 0  ) {
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

            Course newCourse = new Course(cName.getText().toString(),
                    minGrade.getSelectedItem().toString(),
                    Integer.parseInt(allowedAbsences.getText().toString()));
            //todo add course to database



            finish();
        }





    }
}
