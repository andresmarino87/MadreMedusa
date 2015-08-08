package com.madremedusa;

import java.io.IOException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class PostFragment extends Fragment {
	private Intent i;
	private ListView posts;
	private RelativeLayout progressLayout;
	private PostsArrayAdapter adapter;
	private View spinnerProg;
	static private ArrayList<Post> postList;
	static private String tokenForMore="-1";
	static private boolean isLoading=false;
			
	/**
	 * Returns a new instance of this fragment for the given section
	 * number.
	 */
	public static PostFragment newInstance() {
		PostFragment fragment = new PostFragment();
		return fragment;
	}

	public PostFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		postList= new ArrayList<Post>();
		adapter=new PostsArrayAdapter(getActivity(),R.layout.post_item,postList);
		new JSONGetPosts().execute(AppConstant.PostURL);
	}
	            	
	@SuppressLint("InflateParams") @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_post, container, false);
		posts=(ListView)rootView.findViewById(R.id.posts);
		progressLayout=(RelativeLayout)rootView.findViewById(R.id.progressLayout);
        spinnerProg =  ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading_layout, null);
        posts.addFooterView(spinnerProg);
        posts.setAdapter(adapter);
		posts.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				try{
					Post aux=postList.get(arg2);
					i=new Intent(getActivity(), PostActivity.class);
					i.putExtra("thumbnail", aux.getThumbnailUrl());
					i.putExtra("title", aux.getTitle());
					i.putExtra("author", aux.getAuthor());
					i.putExtra("content", aux.getContent());
					startActivity(i);
				}catch(Exception e){
						
				}
			}
		});
		posts.setOnScrollListener(new OnScrollListener(){
        	private int currentFirstVisibleItem=0;
        	private int currentVisibleItemCount=0;
        	private int currentTotalItemCount=0;
        	private int currentScrollState=0;
        	
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        	    this.currentFirstVisibleItem = firstVisibleItem;
        	    this.currentVisibleItemCount = visibleItemCount;
        	    this.currentTotalItemCount = totalItemCount;
			}

			@Override
			public void onScrollStateChanged(AbsListView arg0, int scrollState) {
        	    this.currentScrollState = scrollState;
         	    this.isScrollCompleted();
			}
			
        	private void isScrollCompleted() {
    	        int lastItem = this.currentFirstVisibleItem + this.currentVisibleItemCount;
        		if (this.currentVisibleItemCount > 0 && this.currentScrollState == SCROLL_STATE_IDLE && lastItem == this.currentTotalItemCount) {
        			if(!isLoading){
        				isLoading = true;
        	            spinnerProg.setVisibility(LinearLayout.VISIBLE);
        	            new JSONGetPosts().execute(AppConstant.PostURL+"&pageToken="+tokenForMore);
        				adapter.notifyDataSetChanged();
        			}
        		}
        	}
		});

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(0);
	}
	        
	public class JSONGetPosts extends AsyncTask<String, Void, JSONObject>{
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
//	        	        progressLayout.setVisibility(LinearLayout.VISIBLE);
//	            	    magazines.setVisibility(LinearLayout.GONE);
		}
	            
		@Override
		protected JSONObject doInBackground(String... urls) {
			String title="";
			String author="";
			String thumbnailUrl="";
			String content="";
			JSONObject result=null;
			JSONObject item,aux;
			JSONArray array;
			String url=urls[0];
			try {
				result=Utils.readJsonFromUrl(url);
				tokenForMore=result.getString("nextPageToken");
				array=result.getJSONArray("items");
				for(int i=0;i<array.length();i++){
					item = array.getJSONObject(i);
					title=item.getString("title");
					content=item.getString("content");
					aux=item.getJSONObject("author");
					author=aux.getString("displayName");
					aux=aux.getJSONObject("image");
					thumbnailUrl=aux.getString("url");					
					postList.add(new Post(title,author,thumbnailUrl,content));
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
			isLoading=false;
			progressLayout.setVisibility(LinearLayout.GONE);
			posts.setVisibility(LinearLayout.VISIBLE);
            spinnerProg.setVisibility(LinearLayout.GONE);
			adapter.notifyDataSetChanged();
		}
	}
}
