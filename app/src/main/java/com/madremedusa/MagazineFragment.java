package com.madremedusa;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
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
import android.widget.Toast;

public class MagazineFragment extends Fragment {
	private Context context;
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
		context = this.getActivity();
		mags = new ArrayList<Magazine>();
		new JSONGetMagazines().execute(AppConstant.MagazineURLPage);
	}
            	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_magazine, container, false);
		magazines = (ViewPager)rootView.findViewById(R.id.magazines);
		magazines.setPageMargin(-60);
		//magazines.setHorizontalFadingEdgeEnabled(true);
		magazines.setFadingEdgeLength(30);
		progressLayout = (RelativeLayout)rootView.findViewById(R.id.progressLayout);
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
			JSONObject result = null;
			String url = urls[0];
			if(Utils.checkConn(context)) {
				try {
					result = Utils.readJsonFromUrl(url);
					result.put("isConnectionValid", true);
				} catch (IOException e) {
					Log.e(getActivity().getPackageName(), "Error doInBackground IOException " + e);
				} catch (JSONException e) {
					Log.e(getActivity().getPackageName(), "Error doInBackground JSONException " + e);
				}
			}else {
				try {
					result = new JSONObject();
					result.put("isConnectionValid", false);
				}catch(JSONException e){
					Log.e(getActivity().getPackageName(), "Error doInBackground JSONException " + e);
				}
			}
			return result;
		}
		
		@Override
		protected void onPostExecute(JSONObject result) {
			boolean hasConnection=false;
			try{
				hasConnection = result.getBoolean("isConnectionValid");
			}catch(JSONException e){}

			if(hasConnection) {
				JSONObject content, magazine;
				JSONArray array, auxCovers;
				ArrayList<String> auxImages;
				try {
					content = new JSONObject(result.getString("content").replaceAll("\n", ""));
					array = content.getJSONArray("magazines");
					for (int i = 0; i < array.length(); i++) {
						magazine = array.getJSONObject(i);
						auxCovers = magazine.getJSONArray("imagesCovers");
						auxImages = new ArrayList<String>();
						for (int j = 0; j < auxCovers.length(); j++) {
							auxImages.add(auxCovers.getJSONObject(j).getString("image"));
						}
						mags.add(new Magazine(magazine.getString("magazineIssue"), auxImages));
					}
				} catch (JSONException e) {
					Log.e(getActivity().getPackageName(), "Error doInBackground JSONException " + e);
				}
				progressLayout.setVisibility(LinearLayout.GONE);
				magazines.setVisibility(LinearLayout.VISIBLE);
				adapter = new MagazinesArrayAdapter(getActivity(), mags);
				magazines.setAdapter(adapter);
			}else{
				Toast.makeText(context, R.string.no_internet_connection, Toast.LENGTH_LONG).show();
			}
		}
	}
}
