package com.rwestful.android.listeners;

import com.rwestful.android.MainActivity;
import com.rwestful.android.SettingsActivity;
import android.content.Intent;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;

public class OnSettingsMenuItemClickListener implements OnMenuItemClickListener{

	private final MainActivity mainActivity;
	
	public OnSettingsMenuItemClickListener(MainActivity mainActivity){
		this.mainActivity = mainActivity;
	}

	@Override
	public boolean onMenuItemClick(MenuItem item) {
		this.mainActivity.startActivity(new Intent(mainActivity, SettingsActivity.class));
		return true;
	}

}