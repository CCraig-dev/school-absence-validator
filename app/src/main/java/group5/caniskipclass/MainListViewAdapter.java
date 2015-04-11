package group5.caniskipclass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import group5.caniskipclass.models.Course;
import group5.caniskipclass.views.CourseDetailActivity;

/**
 * Created by Chris on 3/16/2015.
 * This class is a custom adapter to create a text view and button inside a ListView for the
 * main page
 */
public class MainListViewAdapter extends BaseAdapter {

    private Activity context;
    private List<Course> courses;

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
                // todo call a method to determine skippability
                // todo display yes or no screen
            }
        });

        return convertView;
    }
}
