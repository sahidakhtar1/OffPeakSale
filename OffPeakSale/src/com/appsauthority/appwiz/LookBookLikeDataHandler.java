package com.appsauthority.appwiz;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;

import com.appauthority.appwiz.interfaces.LookBookCaller;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.HTTPHandler;

public class LookBookLikeDataHandler {
	LookBookCaller caller;
	String id;
	int index;

	public LookBookLikeDataHandler(LookBookCaller caller,
			 String id, int index) {
		this.caller = caller;
		this.id = id;
	}

	public void likeItem() {
		new AsyncLikeLookBookItem().execute();
	}

	private final class AsyncLikeLookBookItem extends
			AsyncTask<Void, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			
		}

		@Override
		protected JSONObject doInBackground(Void... params) {

			JSONObject param = new JSONObject();

			try {
				param.put(Constants.PARAM_ID, id);
				param.put(Constants.PARAM_RETAILER_ID, Constants.RETAILER_ID);
				JSONObject jsonObject = HTTPHandler.defaultHandler().doPost(
						Constants.URL_LOOK_BOOK_LIKE, param);
				

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
							String likeCount;
							if (json.has("likesCnt")) {
								likeCount = json.getString("likesCnt");
								
							}else{
								likeCount = "0";
							}
							String errorMessage = "Like submitted successfully";
							if(json.has("errorMessage")){
								errorMessage = json.getString("errorMessage");
							}
							if (caller != null) {
								caller.lookBookitemLiked(index, likeCount, errorMessage);
							}
						} else {

							if (caller != null) {
								caller.lookBookitemLikedFailed("Error orrcured");
							}
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
