package nmarin.appgenerators.com.gavica.model;

import java.util.HashMap;
import java.util.List;
import nmarin.appgenerators.com.gavica.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<ElementCheckedCritical>> _listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<ElementCheckedCritical>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final ElementCheckedCritical childElemCheckCrit = (ElementCheckedCritical) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        TextView txtListChild = convertView
                .findViewById(R.id.lblListItem);

        txtListChild.setText(childElemCheckCrit.getElement());
        CheckBox check = convertView.findViewById(R.id.checkBox);
        ImageView imageView = convertView.findViewById(R.id.favourite);
        imageView.setColorFilter(Color.parseColor("#9e9d24"));
        switch (childElemCheckCrit.getChecked()) {
            case 0: // Se deshabilita.
                check.setChecked(false);
                break;
            case 1: // Se habilita.
                check.setChecked(true);
                break;

        }

        switch (childElemCheckCrit.getCritical()) {
            case 0: // Se deshabilita.
                imageView.setVisibility(View.INVISIBLE);
                break;
            case 1: // Se habilita.
                imageView.setVisibility(View.VISIBLE);
                break;

        }



        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
//        try{
//            return this._listDataChild.get(this._listDataHeader.get(groupPosition))
//                    .size();
//        } catch (Exception e){
//            return 0;
//        }
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}