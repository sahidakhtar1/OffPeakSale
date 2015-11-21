package com.appsauthority.appwiz;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.appauthority.appwiz.interfaces.ForgotPWDCaller;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.HTTPHandler;

public class ForgotPasswordHandler {
	ForgotPWDCaller caller;
	String emailId;

	public ForgotPasswordHandler(String emailId, ForgotPWDCaller caller) {
		this.emailId = emailId;
		this.caller = caller;
	}

	public void getPassword() {
		new AsyncForgotPWD().execute();
	}

	private final class AsyncForgotPWD extends
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
						Constants.URL_USER_FORGOT_PWD, param);

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
								caller.passwordSent(true,
										json.getString("errorMessage"));
							}
						} else {
							if (caller != null) {
								caller.passwordSent(false,
										json.getString("errorMessage"));
							}
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block

						e.printStackTrace();
						if (caller != null) {
							caller.passwordSent(false, "Error Occured.");
						}
					}
				}
			} else {
				if (caller != null) {
					if (caller != null) {
						caller.passwordSent(false, "Error Occured.");
					}
				}
			}
		}
	}
}
