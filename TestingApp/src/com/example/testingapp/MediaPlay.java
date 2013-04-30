package com.example.testingapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class MediaPlay extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_media_play);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_media_play, menu);
		return true;
	}

}
