package com.f11.testapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.TextView;


import com.f11.testapp.data.AdProvider;
import com.f11.testapp.R;

import java.util.List;

public class AdViewExpandableListAdapter extends BaseExpandableListAdapter {

    private final Context context;
    private final List<AdProvider> groupItems;
    private final OnDeleteRowListener onDeleteRowListener;
    private final OnUpdateRowListener onUpdateRowListener;

    public enum ChildType {
        EDITTEXT, BUTTONS
    }




    public AdViewExpandableListAdapter(
            Context context,
            List<AdProvider> groupItems,
            OnDeleteRowListener onDeleteRowListener,
            OnUpdateRowListener onUpdateRowListener
    ) {
        this.context = context;
        this.groupItems = groupItems;
        this.onDeleteRowListener = onDeleteRowListener;
        this.onUpdateRowListener = onUpdateRowListener;
    }

    @Override
    public int getGroupCount() {
        return groupItems.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 4;
    }


    @Override
    public AdProvider getGroup(int groupPosition) {
        return groupItems.get(groupPosition);
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        AdProvider adProvider = getGroup(groupPosition);
        switch (childPosition) {
            case 0:
                return adProvider.getAlias();
            case 1:
                return adProvider.getName();
            case 2:
                return adProvider.getPrivacyUrl();
            default:
                return "";
        }
    }

    private int getChildHint(int childPosition) {
        switch (childPosition) {
            case 0:
                return R.string.alias;
            case 1:
                return R.string.name;
            case 2:
                return R.string.privacy_url;
            default:
                return -1;
        }
    }

    @Override
    public long getGroupId(int groupPosition) {
        return getGroup(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public int getChildType(int groupPosition, int childPosition) {
        int childrenCount = getChildrenCount(groupPosition);
        if (childPosition == childrenCount - 1) {  // Last child is the buttons layout
            return ChildType.BUTTONS.ordinal();
        } else {
            return ChildType.EDITTEXT.ordinal();
        }
    }

    @Override
    public int getChildTypeCount() {
        return ChildType.values().length;  // Returns the number of enum elements
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View groupView = convertView;
        if (groupView == null) {
            groupView = LayoutInflater.from(context).inflate(R.layout.group_item_layout, parent, false);
        }

        TextView groupText = groupView.findViewById(R.id.group_text);
        AdProvider adProvider = getGroup(groupPosition);
        groupText.setText(adProvider.getAlias());
        return groupView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        int typeOrdinal = getChildType(groupPosition, childPosition);
        ChildType type = ChildType.values()[typeOrdinal];  // Convert ordinal back to enum type

        switch (type) {
            case EDITTEXT:
                ViewHolderEdit editHolder;
                if (convertView == null || !(convertView.getTag() instanceof ViewHolderEdit)) {
                    Log.d("ExpandableListAdapter", "Inflating new EDITTEXT view");
                    convertView = LayoutInflater.from(context).inflate(R.layout.child_item_layout, parent, false);
                    editHolder = new ViewHolderEdit();
                    editHolder.editText = convertView.findViewById(R.id.child_text);
                    convertView.setTag(editHolder);
                } else{
                    Log.d("ExpandableListAdapter", "Recycling EDITTEXT view");
                    editHolder = (ViewHolderEdit) convertView.getTag();
                }

                editHolder.editText.setText((String) getChild(groupPosition, childPosition));
                editHolder.editText.setError(null);
                editHolder.editText.setHint(getChildHint(childPosition));

                break;
            case BUTTONS:
                ViewHolderButtons buttonHolder;
                if (convertView == null || !(convertView.getTag() instanceof ViewHolderButtons)) {
                    Log.d("ExpandableListAdapter", "Inflating new BUTTONS view");
                    convertView = LayoutInflater.from(context).inflate(R.layout.button_layout, parent, false);
                    buttonHolder = new ViewHolderButtons();
                    buttonHolder.updateButton = convertView.findViewById(R.id.update);
                    buttonHolder.deleteButton = convertView.findViewById(R.id.delete);
                    convertView.setTag(buttonHolder);
                } else{
                    Log.d("ExpandableListAdapter", "Recycling BUTTONS view");
                    buttonHolder = (ViewHolderButtons) convertView.getTag();
                }

                buttonHolder.updateButton.setOnClickListener(v -> {
                    handleUpdate(groupPosition, parent);
                });
                buttonHolder.deleteButton.setOnClickListener(v -> {
                    handleDelete(groupPosition);
                });
                break;
        }
        return convertView;
    }

    private void handleDelete(int groupPosition) {
        AdProvider adProvider = getGroup(groupPosition);
        onDeleteRowListener.onDeleteRow(adProvider);
    }


    static class ViewHolderButtons {
        Button updateButton;
        Button deleteButton;
    }

    static class ViewHolderEdit {
        EditText editText;
    }


    private void handleUpdate(int groupPosition, ViewGroup parent) {
        // Get the AdProvider object corresponding to the groupPosition
        AdProvider adProvider = groupItems.get(groupPosition);

        boolean isAnyChildEmpty = false;

        // Get the total number of children (excluding the last child, which is the buttons layout)
        int childrenCount = getChildrenCount(groupPosition) - 1;

        // Loop through the children to retrieve the EditText values and update the model
        for (int i = 0; i < childrenCount; i++) {
            // Get the child view at position i
            View childView = getChildViewByPosition(parent, groupPosition, i);

            // Check if the childView is not null and it contains an EditText
            if (childView != null && childView.getTag() instanceof ViewHolderEdit) {
                ViewHolderEdit editHolder = (ViewHolderEdit) childView.getTag();
                EditText editText = editHolder.editText;

                // Retrieve the text from the EditText and update the corresponding field in the model
                String newText = editText.getText().toString().trim();
                if(newText.isEmpty()) {
                    editText.setError(context.getString(getErrorText(i)));
                    isAnyChildEmpty = true;
                    continue;
                }
                switch (i) {
                    case 0:
                        adProvider.setAlias(newText);
                        break;
                    case 1:
                        adProvider.setName(newText);
                        break;
                    case 2:
                        adProvider.setPrivacyUrl(newText);
                        break;
                }
            }
        }

        if(!isAnyChildEmpty) {
            onUpdateRowListener.onUpdateRow(adProvider);
        }
    }

    private int getErrorText(int childPosition) {
        switch (childPosition) {
            case 0:
                return R.string.alias_cannot_be_blank;
            case 1:
                return R.string.name_cannot_be_blank;
            case 2:
                return R.string.privacy_url_cannot_be_blank;
            default:
                return -1;
        }
    }

    private View getChildViewByPosition(ViewGroup parent, int groupPosition, int childPosition) {
        ExpandableListView listView = (ExpandableListView) parent;
        int flatPosition = listView.getFlatListPosition(ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
        int firstVisiblePosition = listView.getFirstVisiblePosition();
        return listView.getChildAt(flatPosition - firstVisiblePosition);
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public interface OnDeleteRowListener {
        void onDeleteRow(AdProvider tobeDeletedRow);
    }

    public interface OnUpdateRowListener {
        void onUpdateRow(AdProvider toBeUpdatedRow);
    }
}