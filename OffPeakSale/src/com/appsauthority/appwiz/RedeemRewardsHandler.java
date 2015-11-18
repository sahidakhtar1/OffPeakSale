package com.appsauthority.appwiz;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.appauthority.appwiz.interfaces.RedeemRewadsCaller;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.HTTPHandler;
import com.appsauthority.appwiz.utils.Helper;

public class RedeemRewardsHandler {
	String rewardsPoint;
	RedeemRewadsCaller caller;
	String emailId;

	public RedeemRewardsHandler(String rewrdsPoint, String emailId,
			RedeemRewadsCaller caller) {
		this.rewardsPoint = rewrdsPoint;
		this.emailId = emailId;
		this.caller = caller;
	}

	public void redeemRewards() {
		new AsyncRedeemRewards().execute();
	}

	private final class AsyncRedeemRewards extends
			AsyncTask<Void, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(Void... params) {

			JSONObject param = new JSONObject();

			try {
				param.put(Constants.PARAM_RETAILER_ID, Constants.RETAILER_ID);
				param.put("redeemPoints", rewardsPoint);
				param.put(Constants.PARAM_EMAIL, emailId);
				JSONObject jsonObject = HTTPHandler.defaultHandler().doPost(
						Constants.URL_REDEEM_REWARDS, param);

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
								Helper.getSharedHelper().redeemPoints = rewardsPoint;
								caller.rewadsPointsRedeemed(true,
										"You have redeemed " + rewardsPoint
												+ " points");
							}
						} else {
							if (caller != null) {
								caller.rewadsPointsRedeemed(false,
										json.getString("errorMessage"));
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block

						e.printStackTrace();
						if (caller != null) {
							caller.rewadsPointsRedeemed(false,
									"Unable to reedeem the points.");
						}
					}
				}
			} else {
				if (caller != null) {
					caller.rewadsPointsRedeemed(false,
							"Unable to reedeem the points.");
				}
			}
		}
	}
}
