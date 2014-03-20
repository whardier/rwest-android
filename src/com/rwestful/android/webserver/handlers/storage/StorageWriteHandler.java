package com.rwestful.android.webserver.handlers.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.util.List;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

import com.rwestful.android.constants.Constants;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class StorageWriteHandler implements HttpRequestHandler {
	private Context context;
	private Boolean append;

	private static final String LOG_TAG = "STORAGE_WRITE_HANDLER";

	public StorageWriteHandler(Context context, Boolean append){
		this.context = context;
		this.append = append;
	}

	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext httpContext) throws HttpException, IOException {
		final String uriString = request.getRequestLine().getUri();
		Uri uri = Uri.parse(uriString);

		Log.i(LOG_TAG, uri.getPath());
		Log.i(LOG_TAG, uri.getPathSegments().toString());
		
		final List<String> pathsegments = uri.getPathSegments();
		final String command = pathsegments.get(0);
		final String function = pathsegments.get(1);
		final List<String> directorysegments = pathsegments.subList(3,  pathsegments.size()-1);		
		final String filename = uri.getLastPathSegment();
		
		File combined = null;
		for(String directory : directorysegments) {
			combined = new File(combined, directory);
		}
		
		final File directory = new File(Environment.getExternalStoragePublicDirectory(Constants.STORAGE_FOLDER), combined.toString());
		final File file = new File(directory, filename);
		final List<String> lines = uri.getQueryParameters("line");
		
		//List String pathsegments = uri.getPathSegments();
		Log.i(LOG_TAG, uri.getLastPathSegment());
		Log.i(LOG_TAG, uri.getQueryParameters("line").toString());
		Log.i(LOG_TAG, command);
		Log.i(LOG_TAG, function);
		Log.i(LOG_TAG, combined.toString());
		Log.i(LOG_TAG, pathsegments.subList(3, pathsegments.size()-1).toString());
		
		String directorymessage = (!directory.mkdirs()) ? "Directory not created" : "Directory ensured";
		Log.e(LOG_TAG, directorymessage);
		
		FileOutputStream fileOutputSteam = null;
		final String newline = System.getProperty("line.separator");
		try {
			fileOutputSteam = new FileOutputStream(file, append);
	        for(String line : lines) {
	        	fileOutputSteam.write(line.getBytes());
	        	fileOutputSteam.write(newline.getBytes());
	        }
		} finally {
			if(fileOutputSteam != null) {
				fileOutputSteam.close();
			}
		}
		
		EntityTemplate entity = new EntityTemplate(new ContentProducer() {
			public void writeTo(final OutputStream outstream) throws IOException {
				OutputStreamWriter writer = null;
				try {
					writer = new OutputStreamWriter(outstream, Constants.CHARSET_UTF8);
					//String resp = Utility.openHTMLString(context, R.raw.home);
									
					writer.write(filename.toString());
					writer.write(newline);
					writer.write(file.toString());
					writer.write(newline);				
					writer.write(directory.toString());
				} finally {
					if(writer != null) {
						writer.close();
					}
				}
			}
		});

		entity.setContentType(Constants.CONTENT_TYPE);
		response.setEntity(entity);
	}

}