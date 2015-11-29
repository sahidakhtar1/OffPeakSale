package com.appsauthority.appwiz;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.appauthority.appwiz.interfaces.OrderHistoryCaller;
import com.appsauthority.appwiz.models.OrderDetailResponseObject;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.HTTPHandler;
import com.google.gson.Gson;

public class OrderDetailDataHandler {
	OrderHistoryCaller caller;
	private SharedPreferences spref;
	String retailerMail;
	String orderId;

	public OrderDetailDataHandler(OrderHistoryCaller caller,
			Activity activity,String retailerMail,String orderId) {
		this.caller = caller;
		this.retailerMail = retailerMail;
		this.orderId = orderId;
		spref = PreferenceManager.getDefaultSharedPreferences(activity);
	}

	public void getOrderDetail() {
		new AyncOrderDetail().execute();
	}

	private final class AyncOrderDetail extends
			AsyncTask<Void, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			String lookBookData = spref.getString(
					orderId, "");
			if (!lookBookData.equalsIgnoreCase("")) {
				try {
					Gson gson = new Gson();
					OrderDetailResponseObject res = gson.fromJson(lookBookData,
							OrderDetailResponseObject.class);

					if (caller != null && res != null) {
						caller.orderDetailDownloaded(res);
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
				param.put("retailerMail", retailerMail);
				param.put("orderId", orderId);
				JSONObject jsonObject = HTTPHandler.defaultHandler().doPost(
						Constants.URL_ORDER_DETAIL, param);
				String jsonString = jsonObject.toString();
				if (jsonString != null && !jsonString.equalsIgnoreCase("")) {
					spref.edit()
							.putString(orderId,
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
							OrderDetailResponseObject res = gson
									.fromJson(json.toString(),
											OrderDetailResponseObject.class);
							if (caller != null && res != null) {
								caller.orderDetailDownloaded(res);
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
