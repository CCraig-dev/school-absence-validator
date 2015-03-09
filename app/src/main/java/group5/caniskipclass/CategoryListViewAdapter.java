package group5.caniskipclass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

/**
 * Created by Andre Belanger on 3/7/2015.
 */
public class CategoryListViewAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Map<Category, List<Assignment>> categorizedAssignments;
    private List<Category> categories;
    private int position;

    public CategoryListViewAdapter(Activity context, List<Category> categories,
                                   Map<Category, List<Assignment>> categorizedAssignments,
                                   int position) {
        this.context = context;
        this.categorizedAssignments = categorizedAssignments;
        this.categories = categories;
        this.position = position;
    }

    @Override
    public int getGroupCount() {
        return categories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return categorizedAssignments.get(categories.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categories.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return categorizedAssignments.get(categories.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return (long) groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return (long) childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final Category category = (Category)getGroup(groupPosition);
        final String categoryName = category.getName();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.category_list_item, null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.category_name);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(categoryName);

//        Button addButton = (Button) convertView.findViewById(R.id.add_button);
//
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent addAssignment = new Intent(v.getContext(), AddAssignmentActivity.class);
//                addAssignment.putExtra("position", position);
//                addAssignment.putExtra("category", categoryName);
//                context.startActivity(addAssignment);
//            }
//        });

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final Assignment assignment = (Assignment) getChild(groupPosition, childPosition);

        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.assignment_list_item,null);
        }

        TextView nameItem = (TextView) convertView.findViewById(R.id.assignment_name);
        nameItem.setText(assignment.getName());

        TextView gradeItem = (TextView) convertView.findViewById(R.id.assignment_grade);
        gradeItem.setText(assignment.getGrade() + "/" + assignment.getWeight());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
