package com.rwestful.android.web.handlers.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class StorageWriteHandler implements HttpRequestHandler {
	private Context context = null;
	private Boolean append = null;

	static final String LOG_TAG = "STORAGE_WRITE_HANDLER";

	public StorageWriteHandler(Context context, Boolean append){
		this.context = context;
		this.append = append;
	}

	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext httpContext) throws HttpException, IOException {
		String contentType = "text/html";
		final String uriString = request.getRequestLine().getUri();
		Uri uri = Uri.parse(uriString);

		Log.i(LOG_TAG, uri.getPath());
		Log.i(LOG_TAG, uri.getPathSegments().toString());
		
		final List<String> pathsegments = uri.getPathSegments();
		Log.i(LOG_TAG, uri.getPathSegments().toString());
		final String command = pathsegments.get(0);
		Log.i(LOG_TAG, uri.getPathSegments().toString());
		final String function = pathsegments.get(1);
		Log.i(LOG_TAG, uri.getPathSegments().toString());
		final List<String> directorysegments = pathsegments.subList(2,  pathsegments.size()-1);
		Log.i(LOG_TAG, uri.getPathSegments().toString());
		final String filename = uri.getLastPathSegment();		
		Log.i(LOG_TAG, uri.getPathSegments().toString());
		Log.i(LOG_TAG, directorysegments.toString());
		Log.i(LOG_TAG, filename);

		//FIXME: Handle it when directorysegments has no elements
		File combined = new File(directorysegments.get(0));

		int i = 1;
		
		while ( i < directorysegments.size())
		{
		    combined = new File(combined, directorysegments.get(i));
		    ++i;
		}
		
		final File directory = new File(Environment.getExternalStoragePublicDirectory("stuff"), combined.toString());
		final File file = new File(directory, filename);
		final List<String> lines = uri.getQueryParameters("line");
		
		//List String pathsegments = uri.getPathSegments();
		Log.i(LOG_TAG, uri.getLastPathSegment());
		Log.i(LOG_TAG, uri.getQueryParameters("line").toString());
		Log.i(LOG_TAG, command);
		Log.i(LOG_TAG, function);
		Log.i(LOG_TAG, combined.toString());
		Log.i(LOG_TAG, pathsegments.subList(3, pathsegments.size()-1).toString());
		
		if (!directory.mkdirs()) {
			Log.e(LOG_TAG, "Directory not created");
		} else {
			Log.i(LOG_TAG, "Directory ensured");
		}
		
        FileOutputStream f = new FileOutputStream(file, append);

        final String newline = "\n".toString();
        
		for (int l = 0; l < lines.size(); l++) {
		    String line = lines.get(l);
		    f.write(line.getBytes());
		    f.write(newline.getBytes());
		}
		
		f.close();
		
		HttpEntity entity = new EntityTemplate(new ContentProducer() {
			public void writeTo(final OutputStream outstream) throws IOException {
				OutputStreamWriter writer = new OutputStreamWriter(outstream, "UTF-8");
				//String resp = Utility.openHTMLString(context, R.raw.home);
								
				writer.write(filename.toString());
				writer.write('\n');
				writer.write(file.toString());
				writer.write('\n');				
				writer.write(directory.toString());
				writer.flush();
			}
		});

		((EntityTemplate)entity).setContentType(contentType);

		response.setEntity(entity);
	}

}