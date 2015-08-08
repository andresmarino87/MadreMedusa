package com.madremedusa;

import java.util.ArrayList;

import com.madremedusa.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MagazinesArrayAdapter extends PagerAdapter{
	private Context context;
	private ArrayList<Magazine> items;
	private Intent i;
	private DrawableManager DM;

	@SuppressLint("NewApi") public MagazinesArrayAdapter(Context context, ArrayList<Magazine> objects){
        this.context = context;
        items=objects;
//    	this.DM=new DrawableManager(context,context.getResources().getDrawableForDensity(R.drawable.ic_launcher, DisplayMetrics.DENSITY_XXHIGH));
    	this.DM=new DrawableManager(context,context.getResources().getDrawable(R.drawable.ic_launcher));

    }
	public int getItemPosition(Object object) {
	    return POSITION_NONE;
	}

    public int getCount() {
        return items.size();
    }

	@Override
	public boolean isViewFromObject(View view, Object object) {
		 return view == ((View) object);
	}
	
    @SuppressLint("NewApi") @Override
    public Object instantiateItem(ViewGroup container, int position) {
		View item;
		final Magazine m=items.get(position);
		final ImageView coverImage;
		final TextView coverTitle; 
		
    	LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
   		item = inflater.inflate(R.layout.cover_item, container,false);
    	coverImage=(ImageView)item.findViewById(R.id.cover_image);    
    	coverTitle=(TextView)item.findViewById(R.id.cover_title);    
        coverTitle.setText(m.getCoverName());
	    DM.DisplayImage(m.getCover(), coverImage);

        item.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
	        	i= new Intent(context, MagazineViewer.class);
	        	i.putExtra("MagazineIssue", m.getImageUrl());
	        	context.startActivity(i);
			}
		});
        ((ViewPager) container).addView(item);
        return item;
    }
     
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((View) object);
    }
}
