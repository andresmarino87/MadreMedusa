package com.madremedusa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CollaboratorAdapter extends BaseAdapter {
    private Context mContext;
    final DrawableManager DM;
    
    public Integer[] titles = {
    	R.string.collaborator_1,
    	R.string.collaborator_2,
    	R.string.collaborator_3,
    	R.string.collaborator_4,
    	R.string.collaborator_5,
    	R.string.collaborator_6,
    	R.string.collaborator_7,
    	R.string.collaborator_8,
    	R.string.collaborator_9,
    	R.string.collaborator_10,
    	R.string.collaborator_11,
    	R.string.collaborator_12,
    	R.string.collaborator_13,
    	R.string.collaborator_14,
    	R.string.collaborator_15,
    	R.string.collaborator_16,
    	R.string.collaborator_17,
    	R.string.collaborator_18
    };

    // Constructor
    public CollaboratorAdapter(Context c){
        mContext = c;
        DM =new DrawableManager(mContext,mContext.getResources().getDrawable(R.drawable.ic_launcher));
    }
 
    @Override
    public int getCount() {
        return titles.length;
    }
 
    @Override
    public Object getItem(int position) {
        return titles[position];
    }
 
    @Override
    public long getItemId(int position) {
        return 0;
    }
 
	@SuppressLint("ViewHolder") @Override
    public View getView(int position, View convertView, ViewGroup parent) {
     	LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View itemView = inflater.inflate(R.layout.item_colaboradores, parent, false);
        final ImageView image= (ImageView)itemView.findViewById(R.id.image);
        final TextView text= (TextView)itemView.findViewById(R.id.text);
        text.setText(titles[position]);
        DM.DisplayImage(AppConstant.collaboratorsUrl[position], image);
        return itemView;
    }
}