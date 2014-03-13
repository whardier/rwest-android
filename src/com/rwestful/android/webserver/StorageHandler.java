package com.rwestful.android.webserver;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

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

public class StorageHandler implements HttpRequestHandler {
	private Context context = null;
	
    static final String LOG_TAG = "STORAGEHANDLER";

	public StorageHandler(Context context){
		this.context = context;
	}

	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext httpContext) throws HttpException, IOException {
		String contentType = "text/html";
		final String uriString = request.getRequestLine().getUri();
		Uri uri = Uri.parse(uriString);
		File file = new File(Environment.getExternalStoragePublicDirectory("stuff"), uriString);

        Log.i(LOG_TAG, uri.getPath());
        Log.i(LOG_TAG, uri.getPathSegments().toString());
        List String pathsegments = uri.getPathSegments();
        Log.i(LOG_TAG, uri.getLastPathSegment());
        Log.i(LOG_TAG, uri.getQueryParameters("line").toString());
        
	    if (!file.mkdirs()) {
	        Log.e(LOG_TAG, "Directory not created");
	    } else {
	        Log.i(LOG_TAG, "Directory ensured");
	    }
	    
		HttpEntity entity = new EntityTemplate(new ContentProducer() {
			public void writeTo(final OutputStream outstream) throws IOException {
				OutputStreamWriter writer = new OutputStreamWriter(outstream, "UTF-8");
				//String resp = Utility.openHTMLString(context, R.raw.home);

				writer.write(uriString);
				writer.flush();
			}
		});

		((EntityTemplate)entity).setContentType(contentType);

		response.setEntity(entity);
	}

}