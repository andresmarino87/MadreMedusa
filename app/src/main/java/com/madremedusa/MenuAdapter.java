package com.madremedusa;

import java.util.ArrayList;
import java.util.List;

import com.madremedusa.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class MenuAdapter extends ArrayAdapter<MItem> {
	private int layoutResourceId; 
	private List<MItem> items = new ArrayList<MItem>();
//    private TextView menuItem;
    
    public MenuAdapter(Context context, int textViewResourceId,List<MItem> objects) {
    	super(context, textViewResourceId, objects);
    	this.layoutResourceId=textViewResourceId;
    	this.items = objects;
    	notifyDataSetChanged();
    }

	public int getCount() {
		return this.items.size();
	}

	public MItem getItem(int index) {
		return this.items.get(index);
	}

	@SuppressLint("NewApi")
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			// ROW INFLATION
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(layoutResourceId, parent, false);
		}
		MItem i = getItem(position);
		final TextView menuItem = (TextView) row.findViewById(R.id.menuItem);
		final ImageView icon = (ImageView) row.findViewById(R.id.icon);

		menuItem.setText(i.getTitle());
		icon.setImageResource(i.getThumbnail());
//		menuItem.setCompoundDrawablesRelativeWithIntrinsicBounds(i.getThumbnail(), 0, 0, 0);
        return row;
    }
}
