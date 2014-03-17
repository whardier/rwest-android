package com.rwestful.android.webserver.handlers;

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

public class HomePageHandler implements HttpRequestHandler {
	
    static final String LOG_TAG = "HOMEPAGE_HANDLER";

	public HomePageHandler(Context context){
	}

	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext httpContext) throws HttpException, IOException {
		String contentType = "text/html";
		final String uriString = request.getRequestLine().getUri();
	    
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