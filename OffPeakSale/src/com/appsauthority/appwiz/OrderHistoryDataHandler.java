package com.appsauthority.appwiz;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.appauthority.appwiz.interfaces.OrderHistoryCaller;
import com.appsauthority.appwiz.models.OrderHistoryResponseObject;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.HTTPHandler;
import com.google.gson.Gson;

public class OrderHistoryDataHandler {
	OrderHistoryCaller caller;
	private SharedPreferences spref;

	public OrderHistoryDataHandler(OrderHistoryCaller caller,
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
					Constants.KEY_GET_ALL_ORDERS, "");
			if (!lookBookData.equalsIgnoreCase("")) {
				try {
					Gson gson = new Gson();
					OrderHistoryResponseObject res = gson.fromJson(lookBookData,
							OrderHistoryResponseObject.class);

					if (caller != null && res != null) {
						caller.orderHistoryDownloaded(res);
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
				String emailId = spref.getString(Constants.KEY_EMAIL, "");
//				emailId = "pendyala.bhargavi@gmail.com";
				param.put(Constants.PARAM_RETAILER_ID, Constants.RETAILER_ID);
				param.put(Constants.PARAM_EMAIL, emailId);
				JSONObject jsonObject = HTTPHandler.defaultHandler().doPost(
						Constants.URL_ORDER_HISTORY, param);
				String jsonString = jsonObject.toString();
				if (jsonString != null && !jsonString.equalsIgnoreCase("")) {
					spref.edit()
							.putString(Constants.KEY_GET_ALL_ORDERS,
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
							OrderHistoryResponseObject res = gson
									.fromJson(json.toString(),
											OrderHistoryResponseObject.class);
							if (caller != null && res != null) {
								caller.orderHistoryDownloaded(res);
							}
						} else {

						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block

						e.printStackTrace();
						if (caller != null ) {
							caller.orderHistoryDownloaded(null);
						}
					}
				}
			} else {
				if (caller != null ) {
					caller.orderHistoryDownloaded(null);
				}
			}
		}
	}
}
