package com.appsauthority.appwiz;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.appauthority.appwiz.fragments.SlidingMenuActivity;
import com.appsauthority.appwiz.custom.BaseActivity;
import com.appsauthority.appwiz.models.Retailer;
import com.appsauthority.appwiz.models.RetailerInfoResponse;
import com.appsauthority.appwiz.models.RetailerStores;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.HTTPHandler;
import com.appsauthority.appwiz.utils.Helper;
import com.appsauthority.appwiz.utils.Utils;
import com.google.gson.Gson;
import com.offpeaksale.consumer.R;

public class SplashscreenActivity extends BaseActivity {

	private ImageView imageViewSplashScreen;
	private TextView textViewOperator;
	private SharedPreferences spref;

	private Retailer retailer;
	String pid;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		Intent intent = getIntent();
		pid = intent.getStringExtra("pid");
		
		setContentView(R.layout.activity_splashscreen);

		textViewOperator = (TextView) findViewById(R.id.textViewOperator);

		imageViewSplashScreen = (ImageView) findViewById(R.id.imageViewSplashScreen);

		spref = PreferenceManager.getDefaultSharedPreferences(this);
		
		Helper.getSharedHelper().currency_code = spref.getString(
				Constants.KEY_USER_CURRECY, "");
		Helper.getSharedHelper().setCurrencySymbol();

		if (spref.contains(Constants.SPLASH_IMG)) {
			imageCacheloader.displayImage(
					spref.getString(Constants.SPLASH_IMG, ""),
					R.drawable.splash_bg, imageViewSplashScreen);
		}

		if (spref.contains(Constants.POWERED_BY)) {
			textViewOperator.setText(spref.getString(Constants.POWERED_BY, ""));
		}
		// Voucher voucher = new Voucher();
		// voucher.setMsg("http://appwizlive.com/uploads/retailer/1/loyalty/Azzura_Erdinger_Loyalty.jpg");
		// voucher.setType("Image");
		// addVouchers(voucher);
		new AsyncWorker().execute();
	}

	// public void addVouchers(Voucher v) {
	//
	// Gson gson = new Gson();
	// VoucherList vouchres;
	// String voucherListString = spref.getString(Constants.KEY_VOUCHERS, "");
	// if (voucherListString.equals("")) {
	// vouchres = new VoucherList();
	// } else {
	// vouchres = gson.fromJson(voucherListString, VoucherList.class);
	//
	// }
	// vouchres.getVouchers().add(v);
	// voucherListString = gson.toJson(vouchres);
	// spref.edit().putString(Constants.KEY_VOUCHERS,
	// voucherListString).commit();
	// }
	private class AsyncWorker extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {

			// try {
			// sqliteHelper.createDataBase();
			//
			// } catch (Exception e) {
			// Log.v("DBInfo", "Failed to create DB");
			// e.printStackTrace();
			// }

			if (Utils.hasNetworkConnection(getApplicationContext())) {
				JSONObject obj = new JSONObject();
				Boolean status = false;
				try {
					obj.put(Constants.PARAM_RETAILER_ID, Constants.RETAILER_ID);

					JSONObject jsonObject = HTTPHandler.defaultHandler()
							.doPost(Constants.URL_GET_RETAILER_INFO, obj);
					String jsonString = jsonObject.toString();
					if (jsonString != null && !jsonString.equalsIgnoreCase("")) {
						spref.edit()
								.putString(Constants.KEY_GET_RETAILER_INFO,
										jsonString).commit();
						;
					} else {
						jsonString = spref.getString(
								Constants.KEY_GET_RETAILER_INFO, "");
						if (jsonString.equalsIgnoreCase("")) {

						} else {
							jsonObject = new JSONObject(jsonString);
						}

					}

					Gson gson = new Gson();
					RetailerInfoResponse res = gson.fromJson(
							jsonObject.toString(), RetailerInfoResponse.class);
					List<RetailerStores> stores = new ArrayList<RetailerStores>();

					if (res.getErrorCode().equals("1")) {

						retailer = res.getRetailerData();
						stores = res.getRetailerData().getRetailerStores();
						Helper.getSharedHelper().stores = (ArrayList<RetailerStores>) stores;
						Helper.getSharedHelper().reatiler = retailer;
						Helper.getSharedHelper().isSSL = res.isSSL;
						Helper.getSharedHelper().termsConditions = retailer.termsConditions;
						CurrencyConversionHandler cch = new CurrencyConversionHandler();
						status = true;

					} else {
						status = false;
					}
					res = null;

					return status;
				} catch (Exception e) {
					e.printStackTrace();
					return processOfflineData();
				}

			} else {

				// Boolean status = false;
				// String jsonString = spref.getString(
				// Constants.KEY_GET_RETAILER_INFO, "");
				// if (jsonString.equalsIgnoreCase("")) {
				// return status;
				// }
				// try {
				// JSONObject jsonObject = new JSONObject(jsonString);
				// Gson gson = new Gson();
				// RetailerInfoResponse res = gson.fromJson(
				// jsonObject.toString(), RetailerInfoResponse.class);
				// List<RetailerStores> stores = new
				// ArrayList<RetailerStores>();
				//
				// if (res.getErrorCode().equals("1")) {
				//
				// retailer = res.getRetailerData();
				// stores = res.getRetailerData().getRetailerStores();
				// Helper.getSharedHelper().stores = (ArrayList<RetailerStores>)
				// stores;
				// Helper.getSharedHelper().reatiler = retailer;
				// Helper.getSharedHelper().isSSL = res.isSSL;
				// status = true;
				//
				// } else {
				// status = false;
				// }
				// res = null;
				//
				// return status;
				// } catch (JSONException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				return processOfflineData();
			}

		}

		@Override
		protected void onPostExecute(Boolean result) {

			if (result) {

				retailer = Helper.getSharedHelper().reatiler;

				try {
					if (retailer.getBackdropColor1().length() > 0) {
						Constants.BACKDROP1 = Color.parseColor("#"
								+ retailer.getBackdropColor1());
					}
				} catch (Exception e) {
				}

				try {
					if (retailer.getBackdropColor2().length() > 0) {
						Constants.BACKDROP2 = Color.parseColor("#"
								+ retailer.getBackdropColor2());
					}
				} catch (Exception e) {
				}

				Helper.getSharedHelper().enableShoppingCart = retailer.enableShoppingCart;
				Helper.getSharedHelper().enableRating = retailer.enableRating;
				Helper.getSharedHelper().enableDelivery = retailer.enableDelivery;
				Helper.getSharedHelper().enable_shipping = retailer.enable_shipping;
				Helper.getSharedHelper().deliveryDays = retailer.deliveryDays;
				Helper.getSharedHelper().enableCreditCode = retailer.enableCreditCode;
				// Helper.getSharedHelper().currency_code =
				// retailer.defaultCurrency;
				// Helper.getSharedHelper().getCurrencySymbol();
				textViewOperator.setText(retailer.getPoweredBy());
				imageCacheloader.displayImage(retailer.getSplashImage(),
						R.drawable.image_placeholder, imageViewSplashScreen);

				spref.edit()
						.putString(Constants.SPLASH_IMG,
								retailer.getSplashImage()).commit();
				spref.edit()
						.putString(Constants.POWERED_BY,
								retailer.getPoweredBy()).commit();
				String normlFontPath = Helper.getSharedHelper()
						.getRegularFontPath(retailer.getSiteFont());
				Typeface nonmarFont = Typeface.createFromAsset(getAssets(),
						normlFontPath);
				Helper.getSharedHelper().normalFont = nonmarFont;

				String boldFontPath = Helper.getSharedHelper().getBoldFontPath(
						retailer.getSiteFont());
				Typeface boldFont = Typeface.createFromAsset(getAssets(),
						boldFontPath);
				Helper.getSharedHelper().boldFont = boldFont;

				if(spref.getBoolean("ProductTour", false)==true)
				{
					Intent intent = new Intent(getApplicationContext(),
							SlidingMenuActivity.class);
					if (pid != null) {
						intent.putExtra("pid", pid);
					}
					startActivity(intent);
				}else
				{
					Intent intent = new Intent(getApplicationContext(),
							ProductTourActivity.class);
					startActivity(intent);
				}
				
				finish();
				Log.i("TIMMINGS", "SPLASH FINISHED");

				// new Handler().postDelayed(new Runnable() {
				// @Override
				// public void run() {
				// String normlFontPath =
				// Helper.getSharedHelper().getRegularFontPath(retailer.getSiteFont());
				// Typeface nonmarFont = Typeface.createFromAsset(getAssets(),
				// normlFontPath);
				// Helper.getSharedHelper().normalFont = nonmarFont;
				//
				// String boldFontPath =
				// Helper.getSharedHelper().getBoldFontPath(retailer.getSiteFont());
				// Typeface boldFont = Typeface.createFromAsset(getAssets(),
				// boldFontPath);
				// Helper.getSharedHelper().boldFont = boldFont;
				//
				// Intent intent = new Intent(getApplicationContext(),
				// MainActivity.class);
				// startActivity(intent);
				// finish();
				// }
				// }, 2000);

			} else {

				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						String siteFont = "";
						try {
							siteFont = retailer.getSiteFont();
						} catch (Exception e) {
							// TODO: handle exception
						}

						String normlFontPath = Helper.getSharedHelper()
								.getRegularFontPath(siteFont);
						Typeface nonmarFont = Typeface.createFromAsset(
								getAssets(), normlFontPath);
						Helper.getSharedHelper().normalFont = nonmarFont;

						String boldFontPath = Helper.getSharedHelper()
								.getBoldFontPath(siteFont);
						Typeface boldFont = Typeface.createFromAsset(
								getAssets(), boldFontPath);
						Helper.getSharedHelper().boldFont = boldFont;

						Intent intent = new Intent(getApplicationContext(),
								SlidingMenuActivity.class);
						if (pid != null) {
							intent.putExtra("pid", pid);
						}
						startActivity(intent);
						finish();
					}
				}, 5000);
			}
		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	Boolean processOfflineData() {
		Boolean status = false;
		String jsonString = spref
				.getString(Constants.KEY_GET_RETAILER_INFO, "");
		if (jsonString.equalsIgnoreCase("")) {
			return false;
		}
		try {
			JSONObject jsonObject = new JSONObject(jsonString);
			Gson gson = new Gson();
			RetailerInfoResponse res = gson.fromJson(jsonObject.toString(),
					RetailerInfoResponse.class);
			List<RetailerStores> stores = new ArrayList<RetailerStores>();

			if (res.getErrorCode().equals("1")) {

				retailer = res.getRetailerData();
				stores = res.getRetailerData().getRetailerStores();
				Helper.getSharedHelper().stores = (ArrayList<RetailerStores>) stores;
				Helper.getSharedHelper().reatiler = retailer;
				Helper.getSharedHelper().isSSL = res.isSSL;
				Helper.getSharedHelper().termsConditions = retailer.termsConditions;
				status = true;

			} else {
				status = false;
			}
			res = null;

			return status;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return status;
	}

}
