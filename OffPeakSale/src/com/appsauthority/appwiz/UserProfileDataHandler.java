package com.appsauthority.appwiz;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.appauthority.appwiz.interfaces.UserProfileCaller;
import com.appsauthority.appwiz.models.Profile;
import com.appsauthority.appwiz.models.UserDetailObject;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.HTTPHandler;
import com.appsauthority.appwiz.utils.Helper;
import com.google.gson.Gson;

public class UserProfileDataHandler {
	UserProfileCaller caller;
	String emailId;

	public UserProfileDataHandler(String emailId, UserProfileCaller caller) {
		this.emailId = emailId;
		this.caller = caller;
		new AsyncUserProfile().execute();
	}

	public void redeemRewards() {

	}

	private final class AsyncUserProfile extends
			AsyncTask<Void, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected JSONObject doInBackground(Void... params) {

			JSONObject param = new JSONObject();

			try {
				param.put(Constants.PARAM_RETAILER_ID, Constants.RETAILER_ID);
				param.put(Constants.PARAM_EMAIL, emailId);
				JSONObject jsonObject = HTTPHandler.defaultHandler().doPost(
						Constants.URL_GET_USER_PROFILE, param);

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
							UserDetailObject userDetail = gson.fromJson(
									json.toString(), UserDetailObject.class);
							Profile  userProfile = userDetail.userProfile;
							
//							Helper.getSharedHelper().rewardPoints = userProfile
//									.getRewardPoints();
							if (caller != null) {
								caller.userProfileFetched("0");
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
