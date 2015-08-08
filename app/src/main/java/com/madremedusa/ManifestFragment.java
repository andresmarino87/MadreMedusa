package com.madremedusa;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class ManifestFragment extends Fragment {
	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static ManifestFragment newInstance() {
		ManifestFragment fragment = new ManifestFragment();
		Bundle args = new Bundle();
		fragment.setArguments(args);
		return fragment;
	}

	public ManifestFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_manifest, container,false);
		final TextView text_part_1=(TextView)rootView.findViewById(R.id.text_part_1);
		final TextView text_part_2=(TextView)rootView.findViewById(R.id.text_part_2);
		final ImageView image=(ImageView)rootView.findViewById(R.id.image);
		final DrawableManager DM=new DrawableManager(getActivity(),getActivity().getResources().getDrawable(R.drawable.ic_launcher));
		text_part_1.setText(R.string.manifest_text_1);
		text_part_2.setText(R.string.manifest_text_2);
		DM.DisplayImage(AppConstant.ManifestImageUrl, image);
		return rootView;
	}
}

