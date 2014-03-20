package com.rwestful.android.web.servers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.http.HttpException;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;

import android.content.Context;
import android.content.SharedPreferences;

import android.app.NotificationManager;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.rwestful.android.R;

import com.rwestful.android.web.handlers.DefaultHandler;
import com.rwestful.android.web.handlers.storage.StorageWriteHandler;

public class HTTPServer extends Thread {

	private final static int NOTIFICATION_ID = 0xA;

	private static final String SERVER_NAME = "rwestful";
	private static final String ALL_PATTERN = "*";
	private static final String STORAGE_WRITE_PATTERN = "/storage/write/*";
	private static final String STORAGE_APPEND_PATTERN = "/storage/append/*";

	private boolean isRunning = false;
	private Context context = null;
	private int serverPort = 0;

	private BasicHttpProcessor httpproc = null;
	private BasicHttpContext httpContext = null;
	private HttpService httpService = null;
	private HttpRequestHandlerRegistry registry = null;

	public HTTPServer(Context context){
		super(SERVER_NAME);

		this.setContext(context);

		serverPort = 4269;
		httpproc = new BasicHttpProcessor();
		httpContext = new BasicHttpContext();

		httpproc.addInterceptor(new ResponseDate());
		httpproc.addInterceptor(new ResponseServer());
		httpproc.addInterceptor(new ResponseContent());
		httpproc.addInterceptor(new ResponseConnControl());

		httpService = new HttpService(httpproc, 
				new DefaultConnectionReuseStrategy(),
				new DefaultHttpResponseFactory());


		registry = new HttpRequestHandlerRegistry();

		//FIXME: Passing the context doesn't seem to work too great from threads without using a handler
		
		registry.register(ALL_PATTERN, new DefaultHandler(context));
		registry.register(STORAGE_WRITE_PATTERN, new StorageWriteHandler(context, false));
		registry.register(STORAGE_APPEND_PATTERN, new StorageWriteHandler(context, true));		
		
		httpService.setHandlerResolver(registry);
	}

	private ServerSocket serverSocket;

	@Override
	public void run() {
		super.run();

		try {	
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);			
			
			serverSocket = new ServerSocket();
					
			serverSocket.setReuseAddress(true);					

			if (prefs.getBoolean("server_listen_localhost_only", true)) {
				serverSocket.bind(new InetSocketAddress("127.0.0.1", serverPort));
			} else {
				serverSocket.bind(new InetSocketAddress(serverPort));
			}
			
			setForegroundNotificationText("http://" + serverSocket.getInetAddress().toString() + ":" + serverSocket.getLocalPort() + "/");
			
			while(isRunning){
				try {
					final Socket socket = serverSocket.accept();

					DefaultHttpServerConnection serverConnection = new DefaultHttpServerConnection();

					serverConnection.bind(socket, new BasicHttpParams());

					httpService.handleRequest(serverConnection, httpContext);

					serverConnection.shutdown();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (HttpException e) {
					e.printStackTrace();
				}
			}

			serverSocket.close();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public synchronized void startThread() {
		isRunning = true;
		super.start();
	}

	public synchronized void stopThread(){
		isRunning = false;
		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Context getContext() {
		return context;
	}

	//FIXME: Move this to a utility class
	private void setForegroundNotificationText(String text) {
		// mId allows you to update the notification later on.
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context);
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		mBuilder.setSmallIcon(R.drawable.ic_launcher);
		mBuilder.setContentTitle(context.getString(R.string.app_name));
		mBuilder.setContentText(text);
		mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
		// Sleeps the thread, simulating an operation
		// Removes the progress bar
	}

}