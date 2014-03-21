package com.rwestful.android.web.services;

import com.rwestful.android.MainActivity;
import com.rwestful.android.R;
import com.rwestful.android.web.servers.HTTPServer;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import android.app.Notification;
import android.support.v4.app.NotificationCompat;

import android.preference.PreferenceManager;

public class HTTPService extends Service {

	private final static int NOTIFICATION_ID = 0xA;
	private HTTPServer server = null;

	public SharedPreferences sharedPreferences;

	public static boolean isTestCase = false;
	
	public HTTPService() {
	}

	@Override
	public void onCreate() {

		NotificationCompat.Builder serviceNotificationBuilder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle(getString(R.string.app_name))
				.setContentText("Service is running.. maybe..");

		Intent notificationIntent = new Intent(this, MainActivity.class);
		notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);

		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);

		serviceNotificationBuilder.setContentIntent(pendingIntent);
		Notification notification = serviceNotificationBuilder.build();
		notification.flags |= Notification.FLAG_NO_CLEAR;

		// Horrible workaround for Android bug. 
		// For more info, see http://stackoverflow.com/questions/13358386/service-startforeground-throws-nullpointerexception-when-run-with-servicetestc
		if(!isTestCase) {
			startForeground(NOTIFICATION_ID, notification);
		}

		server = new HTTPServer(this);
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(server != null) {
			server.stopThread();
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		server.startThread();		
		return Service.START_STICKY;
	}    

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		throw new UnsupportedOperationException("Not yet implemented");
	}
}
