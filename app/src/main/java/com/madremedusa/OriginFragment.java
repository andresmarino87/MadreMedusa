package com.madremedusa;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class OriginFragment extends Fragment {
	public static OriginFragment newInstance() {
		OriginFragment fragment = new OriginFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public OriginFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_origin, container,false);
		final DrawableManager DM=new DrawableManager(getActivity(),getActivity().getResources().getDrawable(R.drawable.ic_launcher));
		final TextView text_part_1=(TextView)rootView.findViewById(R.id.text_part_1);
		final TextView text_part_2=(TextView)rootView.findViewById(R.id.text_part_2);
		final TextView text_part_3=(TextView)rootView.findViewById(R.id.text_part_3);
		final TextView text_part_4=(TextView)rootView.findViewById(R.id.text_part_4);
		final TextView text_part_5=(TextView)rootView.findViewById(R.id.text_part_5);
		final ImageView image_1=(ImageView)rootView.findViewById(R.id.image_1);
		final ImageView image_2=(ImageView)rootView.findViewById(R.id.image_2);
		final ImageView image_3=(ImageView)rootView.findViewById(R.id.image_3);
		text_part_1.setText(R.string.origin_text_1);
		text_part_2.setText(R.string.origin_text_2);
		text_part_3.setText(R.string.origin_text_3);
		text_part_4.setText(R.string.origin_text_4);
		text_part_5.setText(R.string.origin_text_5);
		DM.DisplayImage(AppConstant.OriginImageUrl_1, image_1);
		DM.DisplayImage(AppConstant.OriginImageUrl_2, image_2);
		DM.DisplayImage(AppConstant.OriginImageUrl_3, image_3);
		return rootView;
	}
}
