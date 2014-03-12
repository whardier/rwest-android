package com.rwestful.android.webserver;

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


public class HomePageHandler implements HttpRequestHandler {
	private Context context = null;

	public HomePageHandler(Context context){
		this.context = context;
	}

	@Override
	public void handle(HttpRequest request, HttpResponse response, HttpContext httpContext) throws HttpException, IOException {
		String contentType = "text/html";
		final String uriString = request.getRequestLine().getUri();
		Uri uri = Uri.parse(uriString);

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