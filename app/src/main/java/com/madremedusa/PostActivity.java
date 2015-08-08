package com.madremedusa;

import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

public class PostActivity extends ActionBarActivity {
	private Bundle extras;
	static private DrawableManager DM;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);
		final ImageView thumbnail = (ImageView)findViewById(R.id.thumbnail);
		final TextView title = (TextView)findViewById(R.id.title);
		final TextView author = (TextView)findViewById(R.id.author);
		final WebView content = (WebView)findViewById(R.id.content);
		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		extras = getIntent().getExtras();
		DM = new DrawableManager(this,this.getResources().getDrawable(R.drawable.ic_launcher));
		if(extras != null){
			actionBar.setTitle(extras.getString("title"));
			DM.DisplayImage(extras.getString("thumbnail"), thumbnail);
			title.setText(extras.getString("title"));
			author.setText(getString(R.string.autor)+extras.getString("author"));
			content.loadDataWithBaseURL(null,extras.getString("content"), "text/html", "UTF-8",null);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.post, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
			default:
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}
}
