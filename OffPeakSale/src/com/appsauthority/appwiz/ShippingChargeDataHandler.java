package com.appsauthority.appwiz;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.appauthority.appwiz.interfaces.ShippingChargeCaller;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.HTTPHandler;
import com.appsauthority.appwiz.utils.Helper;

public class ShippingChargeDataHandler {
	String countryId;
	ShippingChargeCaller caller;

	public ShippingChargeDataHandler(String countryId,
			ShippingChargeCaller caller) {
		this.countryId = countryId;
		this.caller = caller;
	}

	public void getShippingCharge() {
		new AsyncGetShipingCharge().execute();
	}

	private final class AsyncGetShipingCharge extends
			AsyncTask<Void, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			// sqliteHelper.openDataBase();
			// profile = sqliteHelper.getProfile();
			// sqliteHelper.close();
			JSONObject param = new JSONObject();

			try {
				param.put(Constants.PARAM_RETAILER_ID, Constants.RETAILER_ID);
				param.put("country_id", countryId);
				JSONObject jsonObject = HTTPHandler.defaultHandler().doPost(
						Constants.URL_SHIPPING_CHARGES, param);

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
							if (json.has("free_amt")) {
								Helper.getSharedHelper().freeAmount = Float
										.parseFloat(json.getString("free_amt"));
							}
							if (json.has("ship_amt")) {
								Helper.getSharedHelper().shippingCharge = Float
										.parseFloat(json.getString("ship_amt"));
								;
							}

							if (caller != null) {
								caller.shippingChargeUpdated();
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
