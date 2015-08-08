package com.madremedusa;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

public class PostsArrayAdapter  extends ArrayAdapter<Post>{
	private Context context;
	private List<Post> itemsOriginals=new ArrayList<Post>();
	private List<Post> itemsFiltered=new ArrayList<Post>();
	private DrawableManager DM;
	final private int layoutId;
	
	public PostsArrayAdapter(Context context, int textViewResourceId,ArrayList<Post> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
    	this.itemsFiltered = objects;
    	this.itemsOriginals= objects;
		this.layoutId = textViewResourceId;
		this.DM=new DrawableManager(context,context.getResources().getDrawable(R.drawable.ic_launcher));
		notifyDataSetChanged();
	}
	
	public int getCount() {
		return this.itemsFiltered.size();
	}

	public Post getItem(int index) {
		return this.itemsFiltered.get(index);
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(layoutId ,parent, false);
		}

		final Post p = getItem(position);
		final TextView title = (TextView) row.findViewById(R.id.title);
		final TextView author = (TextView) row.findViewById(R.id.author);
		final ImageView thumbnail = (ImageView) row.findViewById(R.id.thumbnail);
	
		title.setText(p.getTitle());
		author.setText(context.getString(R.string.autor)+p.getAuthor());
	    DM.DisplayImage(p.getThumbnailUrl(), thumbnail);
		return row;
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {
			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint, FilterResults results) {
				itemsFiltered = (ArrayList<Post>) results.values;
				if(results.count > 0){
					notifyDataSetChanged();
				}else{
					notifyDataSetInvalidated();
				}
			}

			@SuppressLint("DefaultLocale") @Override
			protected FilterResults performFiltering(CharSequence constraint) {
				FilterResults results = new FilterResults();
				constraint = constraint.toString().toLowerCase();
				if(constraint == null || constraint.length() == 0){
					synchronized(this){
						results.values = itemsOriginals;
						results.count = itemsOriginals.size();
					}		
				}else{
					ArrayList<Post> gefilterteList = new ArrayList<Post>();
					ArrayList<Post> ungefilterteList = new ArrayList<Post>();
					synchronized (this) {
						ungefilterteList.addAll(itemsOriginals);
					}
					for (int i = 0, l = ungefilterteList.size(); i < l; i++) {
						Post p = ungefilterteList.get(i);
						if (p.getTitle().toLowerCase().contains(constraint.toString())) {
							gefilterteList.add(p);
						}
					}
					results.values = gefilterteList;
					results.count = gefilterteList.size();
				}
				return results;
			}
		};
		return filter;
	}
}
