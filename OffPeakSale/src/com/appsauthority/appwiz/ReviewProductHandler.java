package com.appsauthority.appwiz;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.appauthority.appwiz.interfaces.ReviewProductCaller;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.HTTPHandler;

public class ReviewProductHandler {
	String productId;
	String comments;
	String name;
	ReviewProductCaller caller;
	String emailId;

	public ReviewProductHandler(String productId, String emailId,
			String comments, String name, ReviewProductCaller caller) {
		this.productId = productId;
		this.emailId = emailId;
		this.comments = comments;
		this.name = name;
		this.caller = caller;
	}

	public void submitReview() {
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
				param.put("productId", productId);
				param.put("comments", comments);
				param.put("name", name);
				param.put(Constants.PARAM_EMAIL, emailId);
				JSONObject jsonObject = HTTPHandler.defaultHandler().doPost(
						Constants.URL_SEND_PRODUCT_REVIEW, param);

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

							if (caller != null) {
								caller.reviewSubmitted(true,
										json.getString("errorMessage"));
							}
						} else {
							if (caller != null) {
								caller.reviewSubmitted(false,
										json.getString("errorMessage"));
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block

						e.printStackTrace();
						if (caller != null) {
							caller.reviewSubmitted(false,
									"Unable to submit the review.");
						}
					}
				}
			} else {
				if (caller != null) {
					caller.reviewSubmitted(false,
							"Unable to submit the review.");
				}
			}
		}
	}
}
