package com.rwestful.android;

import com.rwestful.android.service.HTTPService;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.content.Intent;

public class MainActivity extends Activity {

	Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		intent = new Intent(MainActivity.this, HTTPService.class);

		startService(intent);

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		stopService(intent);
	}

}
