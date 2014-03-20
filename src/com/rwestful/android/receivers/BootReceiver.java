package com.rwestful.android.receivers;

import com.rwestful.android.web.services.HTTPService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootReceiver extends BroadcastReceiver {
	
	private Toast toast;
	
	public BootReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO: This method is called when the BroadcastReceiver is receiving
		// an Intent broadcast.
		int duration = Toast.LENGTH_SHORT;
		toast = Toast.makeText(context, "I AM ALIIIIIVE!", duration);
		toast.show();

		context.stopService(new Intent(context, HTTPService.class));
    	context.startService(new Intent(context, HTTPService.class));    	
	}
}
