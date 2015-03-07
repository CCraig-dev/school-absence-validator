package group5.caniskipclass;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

/**
 * Created by Andre Belanger on 3/7/2015.
 */
public class CategoryListViewAdapter extends BaseExpandableListAdapter {

    private Activity context;
    private Map<String, List<String>> categorizedAssignments;
    private List<String> assignments;

    public CategoryListViewAdapter(Activity context, List<String> assignments,
                                   Map<String, List<String>> categorizedAssignments) {
        this.context = context;
        this.categorizedAssignments = categorizedAssignments;
        this.assignments = assignments;
    }

    @Override
    public int getGroupCount() {
        return assignments.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return categorizedAssignments.get(assignments.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return assignments.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return categorizedAssignments.get(assignments.get(groupPosition)).get(childPosition);
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
        String categoryName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.category_list_item, null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.category_name);
        item.setText(categoryName);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String assignmentName = (String) getChild(groupPosition, childPosition);

        LayoutInflater inflater = context.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.assignment_list_item,null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.assignment_name);

        item.setText(assignmentName);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
