package com.rwestful.android.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;

import com.rwestful.android.constants.Constants;
import com.rwestful.android.web.services.HTTPService;


import android.content.Intent;
import android.test.ServiceTestCase;

public class TestService extends ServiceTestCase<HTTPService> {

	private static HTTPService httpService;
	private static Intent intent;
	private static final String port = "4269";
	private static final String host = "localhost";
	private static final String storageWritePattern = "/storage/write/";
	private static final File storageLocation = new File("/storage/emulated/legacy/" + Constants.STORAGE_FOLDER);
	private static File storedFile;
	private static final String storageUrl = "http://" + host + ":" + port + storageWritePattern;
	
	public TestService() {
		super(HTTPService.class);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		intent = new Intent(getSystemContext(), HTTPService.class);
		HTTPService.isTestCase = true;
		startService(intent);
		httpService = getService();
	}
	
	@Override
	public void tearDown() {
		// Clean up test directories
		deleteDirectories(storageLocation);
		
		shutdownService();
	}

	@Test
	public void testStorageWrite() throws URISyntaxException {
		HttpClient client = new DefaultHttpClient();
		String storageDirectoryLocation = "hello/happy/hamster/of/love/and/";
		String storageFileName = "stuff.txt";
		List<String> fileContents = new ArrayList<String>();
		fileContents.add("hihihi");
		fileContents.add("howareyou");
		fileContents.add("ilikepie");
		String urlParameters = convertFileContentsToUrlParameter(fileContents);
		
		URI uri = new URI(storageUrl + storageDirectoryLocation + storageFileName + urlParameters);
		HttpUriRequest httpGet = new HttpGet(uri);
		
		try {
			client.execute(httpGet);
			
			File storedLocation = new File(storageLocation, storageDirectoryLocation);
			storedFile = new File(storedLocation, storageFileName);
			if(!storedFile.exists()) {
				fail("Stored file not created: " + storedFile.getAbsolutePath());
			}
			
			BufferedReader bufferedReader = null;
			List<String> readContents = new ArrayList<String>();
			try {
				bufferedReader = new BufferedReader(new FileReader(storedFile));
				for(String line; (line= bufferedReader.readLine()) != null;) {
					readContents.add(line);
				}
			} finally {
				if(bufferedReader != null) {
					bufferedReader.close();
				}
			}
			
			// Make sure that the contents of the written file equal the contents passed to the url
			assertTrue(Arrays.deepEquals(fileContents.toArray(), readContents.toArray()));
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			fail();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
		
	}
	
	private String convertFileContentsToUrlParameter(List<String> fileContents) {
		String urlParameters = "";
		for(String line : fileContents) {
			urlParameters += (urlParameters == "") ? "?" : "&";
			urlParameters += "line=" + line;
		}
		return urlParameters;
	}
	
	private boolean deleteDirectories(File directory) {
		if(directory.isDirectory()) {
			String[] children = directory.list();
			for(int i = 0; i < children.length; i++) {
				boolean success = deleteDirectories(new File(directory, children[i]));
				if(!success) {
					return false;
				}
			}
		}
		return directory.delete();
	}
	
}
