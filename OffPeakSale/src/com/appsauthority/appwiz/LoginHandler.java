package com.appsauthority.appwiz;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.appauthority.appwiz.interfaces.UserLoginCaller;
import com.appsauthority.appwiz.models.Profile;
import com.appsauthority.appwiz.models.UserDetailObject;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.HTTPHandler;
import com.appsauthority.appwiz.utils.Helper;
import com.google.gson.Gson;

public class LoginHandler {
	String password;
	UserLoginCaller caller;
	String emailId;

	public LoginHandler(String emailId,String password, 
			UserLoginCaller caller) {
		this.password = password;
		this.emailId = emailId;
		this.caller = caller;
	}

	public void login() {
		new AsyncLogin().execute();
	}

	private final class AsyncLogin extends
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
				param.put(Constants.PARAM_USER_PASSWORD, password);
				JSONObject jsonObject = HTTPHandler.defaultHandler().doPost(
						Constants.URL_USER_LOGIN, param);

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
								Gson gson = new Gson();
								UserDetailObject userDetail = gson.fromJson(
										json.toString(), UserDetailObject.class);
								Profile  userProfile = userDetail.userProfile;
//								Helper.getSharedHelper().rewardPoints = userProfile
//										.getRewardPoints();
								caller.loggedIn(true,
										"Sucessfully logged in",userProfile);
							}
						} else {
							if (caller != null) {
								caller.loggedIn(false,
										json.getString("errorMessage"),null);
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block

						e.printStackTrace();
						if (caller != null) {
							caller.loggedIn(false,
									"Unable to log in",null);
						}
					}
				}
			} else {
				if (caller != null) {
					caller.loggedIn(false,
							"Unable to log in",null);
				}
			}
		}
	}
}
