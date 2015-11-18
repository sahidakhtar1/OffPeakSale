package com.appsauthority.appwiz;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.appauthority.appwiz.interfaces.ReviewProductCaller;
import com.appsauthority.appwiz.utils.Helper;

public class CurrencyConversionHandler {
	String productId;
	String comments;
	String name;
	ReviewProductCaller caller;
	String emailId;

	public CurrencyConversionHandler() {
		new AsyncReviewProduct().execute();
	}

	private final class AsyncReviewProduct extends
			AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected String doInBackground(Void... params) {

			String response = null;
			JSONObject param = new JSONObject();

			try {

				String link = prepareLink();
				response = doGet(link, null);

				return response;

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		}

		@Override
		protected void onPostExecute(String json) {

			String[] currency_lines = json.split("\n");
			for (int i = 0; i < currency_lines.length; i++) {
				String line = currency_lines[i];
				String[] curency_comp = line.split(",");

				if (curency_comp.length > 1) {
					String curency_code = curency_comp[0];
					String conversionValue = curency_comp[1];
					Helper.getSharedHelper().currency_conversion_map.put(
							curency_code.replace("\"", ""), conversionValue);
				}
			}
		}
	}

	String prepareLink() {
		String link = "http://finance.yahoo.com/d/quotes.csv?e=.json&f=c4l1&s=";
		String default_curency = Helper.getSharedHelper().reatiler.defaultCurrency;
		String[] allCurrencies = Helper.getSharedHelper().reatiler.allowedCurrencies
				.split(",");
		for (int i = 0; i < allCurrencies.length; i++) {
			String curecny = allCurrencies[i];
			link = link + default_curency + curecny + "=X,";
		}
		return link;
	}

	String doGet(String url, List<NameValuePair> params) {

		InputStream is = null;
		JSONObject jObj = null;
		String json = "";

		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();
			if (params != null) {
				String paramString = URLEncodedUtils.format(params, "utf-8");
				url += "?" + paramString;
			}

			HttpGet httpGet = new HttpGet(url);
			Log.i("URL REQUEST - GET", url);
			HttpResponse httpResponse = httpClient.execute(httpGet);
			HttpEntity httpEntity = httpResponse.getEntity();
			is = httpEntity.getContent();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			json = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error", "Error converting result " + e.toString());
		}

		return json;

	}
}
