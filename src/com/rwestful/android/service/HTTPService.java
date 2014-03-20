package com.rwestful.android.service;

import com.rwestful.android.webserver.WebServer;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class HTTPService extends Service {

	private WebServer server = null;

	@Override
	public void onCreate() {
		super.onCreate();

		server = new WebServer(this);
	}

	@Override
	public void onDestroy() {
		server.stopThread();
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		server.startThread();
		return START_STICKY;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}