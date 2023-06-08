package com.abhishek.resoluteai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ListItem> items;

    public CustomListAdapter(Context context, ArrayList<ListItem> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.custom_list_item, parent, false);
        }

        // Get the current item
        ListItem currentItem = items.get(position);

        // Set the values for the TextViews in the custom layout
        TextView textViewDate = convertView.findViewById(R.id.textViewDate);
        textViewDate.setText(currentItem.getDate());

        TextView textViewTime = convertView.findViewById(R.id.textViewTime);
        textViewTime.setText(currentItem.getTime());

        TextView textViewMessage = convertView.findViewById(R.id.textViewMessage);
        textViewMessage.setText(currentItem.getMessage());

        return convertView;
    }
}

