package com.appsauthority.appwiz;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.appauthority.appwiz.fragments.SlidingMenuActivity;
import com.appauthority.appwiz.interfaces.LookBookCaller;
import com.appauthority.appwiz.interfaces.PayPalCaller;
import com.appsauthority.appwiz.models.LookBookInfoResponse;
import com.appsauthority.appwiz.models.PayPalModelObject;
import com.appsauthority.appwiz.models.PaypalTokenRequest;
import com.appsauthority.appwiz.models.Product;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.HTTPHandler;
import com.appsauthority.appwiz.utils.Helper;
import com.google.gson.Gson;

public class PayPalDataHandler {
	PayPalCaller caller;
	String resturantId;
	String emailId;
	String qty;

	public PayPalDataHandler(PayPalCaller caller, String resturantId,
			String emailId, String qty) {
		this.caller = caller;
		this.resturantId = resturantId;
		this.emailId = emailId;
		this.qty = qty;
	}

	public void getpayPalData() {
		new AsyncGetPayPalData().execute();
	}

	private class AsyncGetPayPalData extends AsyncTask<Void, Void, JSONObject> {

		@Override
		protected void onPreExecute() {

			// showLoadingDialog();

		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		protected JSONObject doInBackground(Void... params) {

			JSONObject result = null;
			JSONObject json = new JSONObject();
			try {

//				json.put(Constants.PARAM_QUANTITY, qty);
				json.put(Constants.PARAM_EMAIL, emailId);
//				json.put(Constants.PARAM_PRODUCTID_FOR_TOKEN, resturantId);
				json.put(Constants.PARAM_RETAILER_ID, Constants.RETAILER_ID);
				JSONArray jObj = new JSONArray();
				JSONObject productJson = new JSONObject();
				productJson.put(Constants.PARAM_PRODUCTID_FOR_TOKEN,
						resturantId);
				productJson.put(Constants.PARAM_QUANTITY, qty);
				jObj.put(productJson);
				json.put("products", jObj);
				
				json.put("discount",
						Helper.getSharedHelper().discountPercent);
				json.put("discountType",
						Helper.getSharedHelper().discountType);
				json.put("discountCode",
						Helper.getSharedHelper().creditCode);
				
				String checkOut_url;
				if (Helper.getSharedHelper().reatiler.enablePay
						.equalsIgnoreCase("1")) {
					checkOut_url = Constants.URL_GET_PAYPAL_TOKEN;
				} else {
					checkOut_url = Constants.URL_GET_VERITRANS_LINK;
				}
				result = HTTPHandler.defaultHandler()
						.doPost(checkOut_url, json);

			} catch (Exception e) {
				// TODO: handle exception
			}

			return result;

		}

		@Override
		protected void onPostExecute(JSONObject result) {

			if (result != null && result.has("errorCode")) {

				try {
					if (result.getString("errorCode").equals("1")) {

						// Bundle bundle = new Bundle();
						//
						// // bundle.putSerializable("product", product);
						// if (Helper.getSharedHelper().reatiler.enablePay
						// .equalsIgnoreCase("1")) {
						// bundle.putString("token", result.getString("token"));
						// } else {
						// bundle.putString("redirectUrl",
						// result.getString("redirectUrl"));
						// }
						//
						// bundle.putString("sucessUrl",
						// result.getString("successUrl"));
						// bundle.putString("cancelUrl",
						// result.getString("cancelUrl"));
						// if (result.has("paypalMode")) {
						// bundle.putString("paypalMode",
						// result.getString("paypalMode"));
						// }

						Gson gson = new Gson();
						PayPalModelObject payPalObj = gson.fromJson(
								result.toString(), PayPalModelObject.class);
						if (caller != null) {
							if (payPalObj != null) {
								caller.payPalDataDownloaded(payPalObj);
							}else{
								caller.payPalDataFailed("Unknow Error");
							}
						}
						

					}else{
						if (caller != null) {
							
							caller.payPalDataFailed(result.getString("errorMessage"));
					}
					}
					
				} catch (JSONException e) {

					e.printStackTrace();
					if (caller != null) {
						
							caller.payPalDataFailed("Unknow Error");
					}

				}
			} else {
				if (caller != null) {
					
					caller.payPalDataFailed("Unknow Error");
			}
			}

		}

	}
}
