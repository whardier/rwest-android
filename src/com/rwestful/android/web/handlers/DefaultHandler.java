package com.rwestful.android.web.handlers;

import org.apache.http.protocol.HttpRequestHandler;


import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.entity.ContentProducer;
import org.apache.http.entity.EntityTemplate;
import org.apache.http.protocol.HttpContext;

import android.content.Context;

import com.rwestful.android.constants.Constants;

public class DefaultHandler implements HttpRequestHandler {

	static final String LOG_TAG = "DEFAULT_HANDLER";

	public DefaultHandler(){
	}

	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext httpContext) throws HttpException, IOException {
		final String uriString = request.getRequestLine().getUri();
	
		EntityTemplate entity = new EntityTemplate(new ContentProducer() {
			public void writeTo(final OutputStream outstream) throws IOException {
				OutputStreamWriter writer = null;
				try {
					writer = new OutputStreamWriter(outstream, Constants.CHARSET_UTF8);
					writer.write(uriString);
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