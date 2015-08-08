package com.madremedusa;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

public class MagazineViewer extends ActionBarActivity {
	private Bundle extras;
	static private ArrayList<String> magazineIssue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_magazine_viewer);

		ActionBar actionBar = getSupportActionBar();
		actionBar.hide();
		extras = getIntent().getExtras();
		magazineIssue = new ArrayList<String>();
		if(extras != null){
			magazineIssue=extras.getStringArrayList("MagazineIssue");
		}

		final ViewPager pager =(ViewPager)findViewById(R.id.pager);
		FullScreenAdapter adapter=new FullScreenAdapter(this,magazineIssue);
		pager.setAdapter(adapter);
	}
}
