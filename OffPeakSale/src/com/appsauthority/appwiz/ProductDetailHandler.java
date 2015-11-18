package com.appsauthority.appwiz;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.appauthority.appwiz.interfaces.ProductDetailCaller;
import com.appsauthority.appwiz.models.ProductDetailObject;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.HTTPHandler;
import com.google.gson.Gson;

public class ProductDetailHandler {
	String productId;
	ProductDetailCaller caller;

	public ProductDetailHandler(String productId, ProductDetailCaller caller) {
		this.productId = productId;
		this.caller = caller;
	}

	public void fetchProductDetails() {
		new AsyncReviewProduct().execute();
	}

	private final class AsyncReviewProduct extends
			AsyncTask<Void, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(Void... params) {

			JSONObject param = new JSONObject();

			try {
				param.put(Constants.PARAM_RETAILER_ID, Constants.RETAILER_ID);
				param.put("product_id", productId);
				JSONObject jsonObject = HTTPHandler.defaultHandler().doPost(
						Constants.URL_GET_PRODUCT_DETAIL, param);

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
						Gson gson = new Gson();
						ProductDetailObject res = gson.fromJson(
								json.toString(), ProductDetailObject.class);
						String errorCode = json.getString("errorCode");
						if (errorCode != null && errorCode.equals("1")) {

							if (caller != null) {
								caller.productDetailLoaded(res.data);;
							}
						} else {
							if (caller != null) {
								caller.errorOccured();
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block

						e.printStackTrace();
						if (caller != null) {
							caller.errorOccured();
						}
						
					}
				}
			} else {
				if (caller != null) {
					caller.errorOccured();
				}
			}
		}
	}
}
