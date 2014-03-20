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

import com.rwestful.android.constants.Constants;

public class StorageWriteHandler implements HttpRequestHandler {

	private Boolean append = null;

	static final String LOG_TAG = "STORAGE_WRITE_HANDLER";

	public StorageWriteHandler(Boolean append) {
		this.append = append;
	}

	@Override
	public void handle(HttpRequest request, HttpResponse response,
			HttpContext httpContext) throws HttpException, IOException {
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
		final List<String> directorysegments = pathsegments.subList(2, pathsegments.size() - 1);
		Log.i(LOG_TAG, uri.getPathSegments().toString());
		final String filename = uri.getLastPathSegment();
		Log.i(LOG_TAG, uri.getPathSegments().toString());
		Log.i(LOG_TAG, directorysegments.toString());
		Log.i(LOG_TAG, filename);

		// Boom baby.. combined can never be null since it is appended later on
		File combined = new File(File.separator);

		for (String directory : directorysegments) {
			combined = new File(combined, directory);
		}

		final File directory = new File(
				Environment
						.getExternalStoragePublicDirectory(Constants.STORAGE_FOLDER),
				combined.toString());
		
		final File file = new File(directory, filename);
		final List<String> lines = uri.getQueryParameters("line");

		// List String pathsegments = uri.getPathSegments();
		Log.i(LOG_TAG, uri.getLastPathSegment());
		Log.i(LOG_TAG, uri.getQueryParameters("line").toString());
		Log.i(LOG_TAG, command);
		Log.i(LOG_TAG, function);
		Log.i(LOG_TAG, combined.toString());
		Log.i(LOG_TAG, pathsegments.subList(2, pathsegments.size() - 1)
				.toString());

		if (!directory.mkdirs()) {
			Log.e(LOG_TAG, "Directory not created");
		} else {
			Log.i(LOG_TAG, "Directory ensured");
		}

		FileOutputStream fileOutputSteam = null;

		final String newline = System.getProperty("line.separator");

		try {
			fileOutputSteam = new FileOutputStream(file, append);
			for (String line : lines) {
				fileOutputSteam.write(line.getBytes());
				fileOutputSteam.write(newline.getBytes());
			}
		} finally {
			if (fileOutputSteam != null) {
				fileOutputSteam.close();
			}
		}

		EntityTemplate entity = new EntityTemplate(new ContentProducer() {
			public void writeTo(final OutputStream outstream)
					throws IOException {
				OutputStreamWriter writer = null;
				try {
					writer = new OutputStreamWriter(outstream,
							Constants.CHARSET_UTF8);

					writer.write(filename.toString());
					writer.write(newline);
					writer.write(file.toString());
					writer.write(newline);
					writer.write(directory.toString());
				} finally {
					if (writer != null) {
						writer.close();
					}
				}
			}
		});

		entity.setContentType(Constants.CONTENT_TYPE);

		response.setEntity(entity);
	}

}