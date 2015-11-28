package com.appsauthority.appwiz;

/* Copyright (C)
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Kevin Irish Antonio <irish.antonio@yahoo.com>, February 2014
 */

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.appauthority.appwiz.fragments.SlidingMenuActivity;
import com.appsauthority.appwiz.custom.BaseActivity;
import com.appsauthority.appwiz.models.Product;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.Helper;
import com.appsauthority.appwiz.utils.ServiceHandler;
import com.appsauthority.appwiz.utils.Utils;
import com.offpeaksale.consumer.R;

public class PaypalActivity extends BaseActivity {
	private Bundle bundleArgs = null;
	private Product product;
	private Context context;
	private String message;
	private String successUrl;
	private String cancelUrl;
	String token;
	String transactionId, paypal_transactionId;
	private String placeOrderUrl;
	private String grandTotal;

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.activity_paypal);
		context = this;
		bundleArgs = getIntent().getExtras();

		token = bundleArgs.getString("token");
		product = (Product) bundleArgs.getSerializable("product");

		successUrl = bundleArgs.getString("sucessUrl");
		cancelUrl = bundleArgs.getString("cancelUrl");

		String paypalMode = bundleArgs.getString("paypalMode");
		String urlSTring;
		String url;
		if (Helper.getSharedHelper().reatiler.enableVerit.equalsIgnoreCase("1")) {
			urlSTring = bundleArgs.getString("redirectUrl");
			url = urlSTring;
		} else {
			if (paypalMode.equalsIgnoreCase("Sandbox")) {
				urlSTring = Constants.URL_PAYPAL_SANDBOX;
			} else {
				urlSTring = Constants.URL_PAYPAL_LIVE;
			}
			url = urlSTring + token;
		}
		WebView w = (WebView) findViewById(R.id.webView);

		w.getSettings().setJavaScriptEnabled(true);
		w.loadUrl(url);

		w.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				boolean shouldOverride = false;
				Log.i("URL", url);
				if (url.contains("place_order.php?")
						|| url.contains("veritrans_order.php?")) {
					placeOrderUrl = url;
					new AsyncGetMessageSuccess().execute();
					shouldOverride = true;
				} else if (url.equalsIgnoreCase(successUrl)) {
					// --- Payment Success --- //
					new AsyncGetMessageSuccess().execute();
					shouldOverride = true;

				} else if (url.equalsIgnoreCase(cancelUrl)) {
					// --- Payment Failed --- //

					// Intent intent = new Intent(context,
					// EShopDetailActivity.class);
					// intent.putExtra("product", product);
					// intent.putExtra("FROM", "PAYPAL");
					// intent.putExtra("status", "fail");
					// startActivity(intent);
					finish();
					shouldOverride = true;
				}

				return shouldOverride;
			}

			@Override
			public WebResourceResponse shouldInterceptRequest(WebView view,
					String url) {
				// TODO Auto-generated method stub
				// if (url.contains("place_order.php?")) {
				// placeOrderUrl = url;
				// } else
				if (url.equalsIgnoreCase(successUrl)) {
					// --- Payment Success --- //
					new AsyncGetMessageSuccess().execute();

				} else if (url.equalsIgnoreCase(cancelUrl)) {
					// --- Payment Failed --- //

					// Intent intent = new Intent(context,
					// EShopDetailActivity.class);
					// intent.putExtra("product", product);
					// intent.putExtra("FROM", "PAYPAL");
					// intent.putExtra("status", "fail");
					// startActivity(intent);
					finish();
				}
				if (url.contains("cancel_return")) {
					finish();
				}
				return super.shouldInterceptRequest(view, url);
			}

			public void onLoadResource(WebView view, String url) {
				if (url.startsWith(cancelUrl))
					showLoadingDialog();

			}

			public void onPageFinished(WebView view, String url) {

				dismissLoadingDialog();
			}
		});
	}

	private class AsyncGetMessageSuccess extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog();
		}

		@Override
		protected Boolean doInBackground(Void... arg0) {

			if (Utils.hasNetworkConnection(context)) {

				Boolean status = false;
				try {
					ServiceHandler jsonParser = new ServiceHandler();
					if (placeOrderUrl == null) {
						placeOrderUrl = successUrl;
					}
					String json = jsonParser.makeServiceCall(placeOrderUrl,
							ServiceHandler.GET);
					if (json != null) {

						JSONObject obj = new JSONObject(json);

						message = obj.getString("errorMessage");
						try {
							if (obj.get("transactionId") != null) {
								transactionId = obj.getString("transactionId");
							}
							if (obj.get("paypal_transactionId") != null) {
								paypal_transactionId = obj
										.getString("paypal_transactionId");
							}
						} catch (Exception e) {
							// TODO: handle exception
						}

						status = true;
					} else {
						status = false;
					}
				} catch (Exception e) {
					return false;
				}
				return status;
			} else {

				return false;
			}

		}

		@Override
		protected void onPostExecute(Boolean result) {

			dismissLoadingDialog();
			String amount = bundleArgs.getString("grandTotal");
//			if (Helper.getSharedHelper().enableShoppingCart.equals("1")) {
//				Float shipping = Helper.getSharedHelper().shippingCharge;
//				amount = Helper.getSharedHelper().getCartTotalAmount();
//				float total = Float.parseFloat(amount);
//				if (total >= Helper.getSharedHelper().freeAmount
//						|| Helper.getSharedHelper().deliveryOptionSelectedIndex == 1) {
//
//				} else {
//					total = total + shipping;
//				}
//				total = total
//						- Float.parseFloat(Helper.getSharedHelper().redeemPoints);
//				amount = Float.toString(total);
//				Helper.getSharedHelper().shoppintCartList.clear();
//			} else {
//				int qty = Integer.parseInt(product.getQty());
//				float unitPrice = Float.parseFloat(product.getNewPrice());
//				float total = qty * unitPrice;
//				float discount = total
//						* Float.parseFloat(Helper.getSharedHelper().discountPercent)
//						/ 100;
//				total = total - discount;
//				Float shipping = Helper.getSharedHelper().shippingCharge;
//				if (total >= Helper.getSharedHelper().freeAmount
//						&& Helper.getSharedHelper().deliveryOptionSelectedIndex == 0) {
//
//				} else {
//					total = total + shipping;
//				}
//				total = total
//						- Float.parseFloat(Helper.getSharedHelper().redeemPoints);
//				amount = Float.toString(total);
//			}

			String paymetTitle = "Payment success\nEmail confirmation sent\n\n";
			String grandTotal = "Grand Total: "
					+ Helper.getSharedHelper().currency_symbol + " " + amount
					+ "\n\n";
			String earnedRewards = "";
			if (Helper.getSharedHelper().reatiler.enableRewards
					.equalsIgnoreCase("1")) {
				earnedRewards = "Credit Points Earned: "
						+ Helper.getSharedHelper().rewardPountsEarned + "\n\n";
			} else {

			}
			String id = transactionId;
			if (id == null && token != null) {
				id = token;
			}

			String orderNo = "";
			if (id != null) {
				if (Helper.getSharedHelper().reatiler.enableVerit.equals("1")) {
					orderNo = "\nVeritrans Id: " + id;
				} else {
					orderNo = "\nPaypal Id: " + paypal_transactionId;
				}

			}
			String transactionIdMSG = "";
			if (transactionId != null) {
				transactionIdMSG = "Transaction Id:" + transactionId;
			}
			String msg = "Dear Customer,\nYour purchase is sucessful.\nYou will receive E-Mail confirmation shortly.";

			String toasttext = paymetTitle + grandTotal +transactionIdMSG
					+ orderNo;// + msg;
			Toast.makeText(context, toasttext, Toast.LENGTH_LONG).show();

			Helper.getSharedHelper().shoppintCartList.clear();
			Intent intent = new Intent(context, SlidingMenuActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra(Constants.KEY_FROM_PAYPAL, true);
			startActivity(intent);
			Helper.getSharedHelper().discountPercent = "0";
			Helper.getSharedHelper().redeemPoints = "0";
			finish();

		}
	}
	// public void onPageStarted (WebView view, String url, Bitmap favicon){
	// if (url.equals(cancelUrl)) {
	// finish();
	// }
	// }

}
