package com.madremedusa;

import java.util.ArrayList;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class FullScreenAdapter  extends PagerAdapter{
	private Context context;
	private ArrayList<String> imageUrl;
	private DrawableManager DM;

    
    // constructor
    public FullScreenAdapter(Context context,ArrayList<String> imageUrls) {
        this.context = context;
        this.imageUrl = imageUrls;
//    	this.DM=new DrawableManager(context,context.getResources().getDrawableForDensity(R.drawable.ic_launcher, DisplayMetrics.DENSITY_XXXHIGH));
    	this.DM=new DrawableManager(context,context.getResources().getDrawable(R.drawable.ic_launcher));
    }
 
	@Override
	public int getCount() {
		return this.imageUrl.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		 return view == ((TouchImageView) object);
	}

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
    	TouchImageView imageView=new TouchImageView(context);
        DM.DisplayImage(imageUrl.get(position), imageView);
        ((ViewPager) container).addView(imageView);
        return imageView;
    }
     
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((TouchImageView) object);
    }
}
