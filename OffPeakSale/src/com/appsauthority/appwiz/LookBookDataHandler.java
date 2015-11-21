package com.appsauthority.appwiz;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.appauthority.appwiz.interfaces.LookBookCaller;
import com.appsauthority.appwiz.models.LookBookInfoResponse;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.HTTPHandler;
import com.google.gson.Gson;

public class LookBookDataHandler {
	LookBookCaller caller;
	private SharedPreferences spref;

	public LookBookDataHandler(LookBookCaller caller,
			Activity activity) {
		this.caller = caller;
		spref = PreferenceManager.getDefaultSharedPreferences(activity);
	}

	public void getLookBookData() {
		new AsyncGetLookBook().execute();
	}

	private final class AsyncGetLookBook extends
			AsyncTask<Void, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			String lookBookData = spref.getString(
					Constants.KEY_GET_LOOK_BOOK_INFO, "");
			if (!lookBookData.equalsIgnoreCase("")) {
				try {
					Gson gson = new Gson();
					LookBookInfoResponse res = gson.fromJson(lookBookData,
							LookBookInfoResponse.class);

					if (caller != null && res != null) {
						caller.lookBookDataDownloaded(res);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block

					e.printStackTrace();
				}
			}
		}

		@Override
		protected JSONObject doInBackground(Void... params) {

			JSONObject param = new JSONObject();

			try {
				param.put(Constants.PARAM_RETAILER_ID, Constants.RETAILER_ID);
				JSONObject jsonObject = HTTPHandler.defaultHandler().doPost(
						Constants.URL_LOOK_BOOK, param);
				String jsonString = jsonObject.toString();
				if (jsonString != null && !jsonString.equalsIgnoreCase("")) {
					spref.edit()
							.putString(Constants.KEY_GET_LOOK_BOOK_INFO,
									jsonString).commit();
				}

				return jsonObject;

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		}

		@Override
		protected void onPostExecute(JSONObject json) {
			if (json != null) {
				if (json.has("errorCode")) {
					try {
						String errorCode = json.getString("errorCode");
						if (errorCode != null && errorCode.equals("1")) {
							Gson gson = new Gson();
							LookBookInfoResponse res = gson
									.fromJson(json.toString(),
											LookBookInfoResponse.class);
							if (caller != null && res != null) {
								caller.lookBookDataDownloaded(res);
							}
						} else {

						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block

						e.printStackTrace();
					}
				}
			} else {

			}
		}
	}
}
