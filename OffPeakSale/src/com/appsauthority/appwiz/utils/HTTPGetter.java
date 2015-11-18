package com.appsauthority.appwiz.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.util.Log;

public class HTTPGetter {

	private String TAG = "Getter";

	public String executeHttpGet(String URL) throws Exception {
		BufferedReader bufferedReader = null;
		String page = "";
		URL = URL.trim();

		try {
			// set timeout in milliseconds until a connection is established and
			// until data is retrieved
			HttpParams httpParameters = new BasicHttpParams();
			int timeoutConnection = 30000; // 30sec timeout
			HttpConnectionParams.setConnectionTimeout(httpParameters,
					timeoutConnection);
			int timeoutSocket = 30000; // 30sec timeout
			HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
			HttpClient client = new DefaultHttpClient(httpParameters);

			// HttpClient client = new DefaultHttpClient();
			client.getParams().setParameter(CoreProtocolPNames.USER_AGENT,
					"android");
			HttpGet request = new HttpGet();
//			request.setHeader("Content-Type", "text/plain; charset=utf-8");
			request.setHeader("Accept", "application/json");
			request.setHeader("Content-type", "application/json");
			request.setURI(new URI(URL));
			HttpResponse response = client.execute(request);
			bufferedReader = new BufferedReader(new InputStreamReader(response
					.getEntity().getContent()));
			StringBuffer stringBuffer = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");

			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line + NL);
				// System.out.print(stringBuffer);
			}
			bufferedReader.close();
			page = stringBuffer.toString();
			// System.out.println(page+" page");

			return page;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					Log.d(TAG, e.toString());
				}
			}
		}
	}

}
