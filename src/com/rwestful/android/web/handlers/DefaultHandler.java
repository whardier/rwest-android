package com.rwestful.android.web.handlers;

import org.apache.http.protocol.HttpRequestHandler;


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
import android.util.Log;

public class DefaultHandler implements HttpRequestHandler {
	
    static final String LOG_TAG = "DEFAULT_HANDLER";

	public DefaultHandler(Context context){
	}

	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext httpContext) throws HttpException, IOException {
		String contentType = "text/html";
		final String uriString = request.getRequestLine().getUri();
	    
		// Do stuff here and create final status codes and stuff

		Log.d(LOG_TAG, "Called");
		
		HttpEntity entity = new EntityTemplate(new ContentProducer() {
			public void writeTo(final OutputStream outstream) throws IOException {
				OutputStreamWriter writer = new OutputStreamWriter(outstream, "UTF-8");

				writer.write(uriString);
				writer.flush();
			}
		});

		((EntityTemplate)entity).setContentType(contentType);

		response.setEntity(entity);
	}

}