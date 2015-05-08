package group5.caniskipclass;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import group5.caniskipclass.models.Course;
import group5.caniskipclass.persistence.CanISkipClassContract;
import group5.caniskipclass.persistence.CanISkipClassDbHelper;
import group5.caniskipclass.views.CourseDetailActivity;

/**
 * Created by Chris on 3/16/2015.
 * This class is a custom adapter to create a text view and button inside a ListView for the
 * main page
 */
public class MainListViewAdapter extends BaseAdapter {

    private Activity context;
    private List<Course> courses;
    private static final String TAG = "Main Page List Adapter";

    public MainListViewAdapter(Activity context, List<Course> courses){
        this.context = context;
        this.courses = courses;
    }
    @Override
    public int getCount() {
        return courses.size();
    }

    @Override
    public Object getItem(int position) {
        return courses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return courses.get(position).getId();
    }

    /**
     * This creates the Custom list view
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Course course = (Course)getItem(position);
        final String courseName = course.getName();
        // This will inflate the template view inside each ListView item
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.main_list_item, parent, false);
        }

        // Set the textView to the course name
        TextView item = (TextView) convertView.findViewById(R.id.classNameView);
        item.setText(courseName);
        item.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){

                GradeCalculation gradeCalc = new GradeCalculation(context);
                System.out.println("Grade: " + gradeCalc.getGrade(course));
                Intent courseDetail = new Intent(context, CourseDetailActivity.class);
                courseDetail.putExtra("position", pos);
                context.startActivity(courseDetail);
            }
        });
        Button skipButton = (Button) convertView.findViewById(R.id.skipButton);
        skipButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GradeCalculation gradeCalc = new GradeCalculation(context);
                double grade = gradeCalc.getGrade(course);
                String desiredCrade = course.getMinimumGrade();
                double numericDesiredGrade = gradeCalc.letterToNumericGrade(desiredCrade);
                double gradeIfSkip = gradeCalc.getGradeIfSkipped(course, grade);
                String message = "";

                Log.d(TAG, "Number of skips: " + course.getNumSkips());
                if (gradeIfSkip > numericDesiredGrade) {
                    message = "You can safely skip this class, do you want to skip? ";
                } else {
                    message = "If you skip this class, you will be below your desired grade of " +
                            desiredCrade + ". Are you sure you want to skip?";
                }
                new AlertDialog.Builder(context)
                        .setTitle("Current Grade")
                        .setMessage(message)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                //String numSkipsCol = CanISkipClassContract.CourseEntry.
                                int numSkips = course.getNumSkips();
                                CanISkipClassDbHelper dbhelp = CanISkipClassDbHelper.getInstance(context);
                                SQLiteDatabase db = dbhelp.getWritableDatabase();
                                ContentValues updateVals = new ContentValues();
                                Log.d(TAG, "Course ID: " + course.getId());
                                String where = CanISkipClassContract.CourseEntry._ID + "=?";
                                updateVals.put(CanISkipClassContract.CourseEntry.COLUMN_NAME_NUM_SKIPS, numSkips++);
                                db.update(CanISkipClassContract.CourseEntry.TABLE_NAME, updateVals, where, new String[]{String.valueOf(course.getId())});
                                course.setNumSkips(numSkips++);
                                Log.d(TAG, "Number of skips after update: " + course.getNumSkips());
                                new AlertDialog.Builder(context)
                                        .setTitle("What to do instead...")
                                        .setMessage(
                                                "Now that you've skipped class, you can:\n" +
                                                        " - Sleep\n" +
                                                        " - Eat Food\n" +
                                                        " - Take a Walk\n" +
                                                        " - Go to the Gym\n" +
                                                        " - Or Whatever You Want..."
                                        )
                                        .setNeutralButton("Ok!", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .show();
                            }
                        })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });
        return convertView;
    }
}
