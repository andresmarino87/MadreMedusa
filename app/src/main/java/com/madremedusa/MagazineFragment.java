package com.madremedusa;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class MagazineFragment extends Fragment {
	private ViewPager magazines;
	private RelativeLayout progressLayout;
	private MagazinesArrayAdapter adapter;
	static private ArrayList<Magazine> mags;

	public static MagazineFragment newInstance() {
		MagazineFragment fragment = new MagazineFragment();
		return fragment;
	}

	public MagazineFragment() {}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		mags= new ArrayList<Magazine>();
		new JSONGetMagazines().execute(AppConstant.MagazineURLPage);
	}
            	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_magazine, container, false);
		magazines=(ViewPager)rootView.findViewById(R.id.magazines);
		magazines.setPageMargin(-60);
		//magazines.setHorizontalFadingEdgeEnabled(true);
		magazines.setFadingEdgeLength(30);
		progressLayout=(RelativeLayout)rootView.findViewById(R.id.progressLayout);
		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(0);
	}
        
	public class JSONGetMagazines extends AsyncTask<String, Void, JSONObject>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//                progressLayout.setVisibility(LinearLayout.VISIBLE);
//                magazines.setVisibility(LinearLayout.GONE);
		}
            
		@Override
		protected JSONObject doInBackground(String... urls) {
			JSONObject result=null;
			JSONObject content=null;
			JSONObject magazine=null;
			JSONArray array,auxCovers;
			ArrayList<String> auxImages;
			String url=urls[0];
			try {
				result=Utils.readJsonFromUrl(url);
				content=new JSONObject(result.getString("content").replaceAll("\n", ""));
				array=content.getJSONArray("magazines");
				for(int i=0;i<array.length();i++){
					magazine = array.getJSONObject(i);
					auxCovers= magazine.getJSONArray("imagesCovers");
					auxImages= new ArrayList<String>();
					for(int j=0;j<auxCovers.length();j++){
						auxImages.add(auxCovers.getJSONObject(j).getString("image"));
					}
					mags.add(new Magazine(magazine.getString("magazineIssue"),auxImages));
				}
			} catch (IOException e) {
				Log.e(getActivity().getPackageName(),"Error doInBackground IOException "+e);
			} catch (JSONException e) {
				Log.e(getActivity().getPackageName(),"Error doInBackground JSONException "+e);
			}	
			return result;
		}
		
		@Override
		protected void onPostExecute(JSONObject result) {
			progressLayout.setVisibility(LinearLayout.GONE);
			magazines.setVisibility(LinearLayout.VISIBLE);
			adapter=new MagazinesArrayAdapter(getActivity(),mags);
			magazines.setAdapter(adapter);
		}
	}
}
