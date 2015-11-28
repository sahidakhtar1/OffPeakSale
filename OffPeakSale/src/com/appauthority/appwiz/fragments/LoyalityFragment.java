package com.appauthority.appwiz.fragments;

import jim.h.common.android.zxinglib.integrator.IntentIntegrator;
import jim.h.common.android.zxinglib.integrator.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.appsauthority.appwiz.models.Loyalty;
import com.appsauthority.appwiz.models.Retailer;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.HTTPHandler;
import com.appsauthority.appwiz.utils.Helper;
import com.appsauthority.appwiz.utils.ImageCacheLoader;
import com.appsauthority.appwiz.utils.Utils;
import com.offpeaksale.consumer.R;

public class LoyalityFragment extends Fragment {
	private Activity activity;
	private Context context;
	private TextView textViewHeader;

	private String password = "";
	private Button buttonMerchantAdmin, buttonReset;
	private Dialog dialog;
	EditText editTextPassword;

	private ImageButton imageButtonStamp1, imageButtonStamp2,
			imageButtonStamp3, imageButtonStamp4, imageButtonStamp5;
	private int i = 0;
	private boolean isPasswordEntered = false;

	private boolean stamp1, stamp2, stamp3, stamp4, stamp5;
	private TextView textViewTermsCond;
	private Loyalty loyalty;
	TextView howItWorks;

	Boolean isActive = true;

	private ImageView imageViewLoyalty;
	private ImageView buttonFb;
	private LinearLayout llFb, llInstagram;
	private Retailer retailer;
	private ImageCacheLoader imageCacheloader;
	private ProgressDialog progressDialog;

	private SharedPreferences spref;
	Handler handler = new Handler();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_loyalty, container,
				false);
		initialize(rootView);

		return rootView;
	}

	void initialize(View view) {
		activity = this.getActivity();
		context = this.getActivity();
		imageCacheloader = new ImageCacheLoader(activity);

		spref = PreferenceManager.getDefaultSharedPreferences(activity);
		retailer = Helper.getSharedHelper().reatiler;

		imageButtonStamp1 = (ImageButton) view.findViewById(R.id.imageButton1);
		imageButtonStamp2 = (ImageButton) view.findViewById(R.id.imageButton2);
		imageButtonStamp3 = (ImageButton) view.findViewById(R.id.imageButton3);
		imageButtonStamp4 = (ImageButton) view.findViewById(R.id.imageButton4);
		imageButtonStamp5 = (ImageButton) view.findViewById(R.id.imageButton5);

		textViewTermsCond = (TextView) view.findViewById(R.id.tv_termscond);
		buttonMerchantAdmin = (Button) view
				.findViewById(R.id.btn_merchant_admin);
		buttonMerchantAdmin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				merchantAdminPressed(arg0);
			}
		});
		buttonReset = (Button) view.findViewById(R.id.btn_reset);
		buttonReset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				resetPressed(v);
			}
		});
		imageViewLoyalty = (ImageView) view.findViewById(R.id.iv_loyalty);
		llFb = (LinearLayout) view.findViewById(R.id.llFb);
		llInstagram = (LinearLayout) view.findViewById(R.id.llInstagram);
		buttonFb = (ImageView) view.findViewById(R.id.btn_fb);
		llFb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				likePressed(v);
			}
		});
		llInstagram.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				instagramPressed(v);
			}
		});
		howItWorks = (TextView) view.findViewById(R.id.tv_terms);

		try {

			buttonMerchantAdmin.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));
			buttonMerchantAdmin.setBackgroundDrawable(Helper.getSharedHelper()
					.getGradientDrawable(retailer.getHeaderColor()));

			buttonReset.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));

			buttonReset.setBackgroundDrawable(Helper.getSharedHelper()
					.getGradientDrawable(retailer.getHeaderColor()));
		} catch (Exception e) {
		}

		// setViewLines();
		String eshopConatent = spref.getString(Constants.KEY_GET_LOYALITY_INFO,
				"");
		if (!eshopConatent.equalsIgnoreCase("")) {
			try {
				JSONObject jsonObject = new JSONObject(eshopConatent);
				Boolean status = parseLoyality(jsonObject);
				if (status) {
					// loadEShop();
					textViewTermsCond.setText(loyalty.getTermsCond());
					imageCacheloader.displayImage(loyalty.getLoyaltyImage(),
							R.drawable.image_placeholder, imageViewLoyalty);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {

		}

		loadStamps();

		if (Utils.hasNetworkConnection(context)) {
			new AsyncGetLoyalty().execute();
		}

		imageButtonStamp1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isActive = true;
				if (isPasswordEntered) {
					i++;
					Handler handler = new Handler();
					Runnable r = new Runnable() {

						@Override
						public void run() {
							if (i == 1) {
								if (stamp1) {
									imageButtonStamp1
											.setImageResource(R.drawable.coupon_default);
									stamp1 = false;
									Utils.setStatusLoyaltyStamp1(context, 0);
								} else {
									imageButtonStamp1
											.setImageResource(R.drawable.coupon_selected);
									stamp1 = true;
									Utils.setStatusLoyaltyStamp1(context, 1);
								}
								Log.i("Single", "1");
							}
							i = 0;
						}
					};
					if (i == 1 && !stamp2) {
						handler.postDelayed(r, 250);
					} else if (i == 2 && !stamp2) {
						imageButtonStamp1
								.setImageResource(R.drawable.coupon_default);
						stamp1 = false;
						Utils.setStatusLoyaltyStamp1(context, 0);
						Log.i("Double", "2");
						i = 0;
					}
				}
			}

		});

		imageButtonStamp2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isActive = true;
				if (isPasswordEntered) {
					i++;
					Handler handler = new Handler();
					Runnable r = new Runnable() {

						@Override
						public void run() {
							if (i == 1) {
								if (stamp2) {
									imageButtonStamp2
											.setImageResource(R.drawable.coupon_default);
									stamp2 = false;
									Utils.setStatusLoyaltyStamp2(context, 0);
								} else {
									imageButtonStamp2
											.setImageResource(R.drawable.coupon_selected);
									stamp2 = true;
									Utils.setStatusLoyaltyStamp2(context, 1);
									Log.i("Single", "1");
								}
							}
							i = 0;
						}
					};
					if (i == 1 && stamp1 && !stamp3) {
						handler.postDelayed(r, 250);
					} else if (i == 2 && !stamp3) {
						imageButtonStamp2
								.setImageResource(R.drawable.coupon_default);
						stamp2 = false;
						Utils.setStatusLoyaltyStamp2(context, 0);
						Log.i("Double", "2");
						i = 0;
					}
				}
			}

		});

		imageButtonStamp3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isActive = true;
				if (isPasswordEntered) {
					i++;
					Handler handler = new Handler();
					Runnable r = new Runnable() {

						@Override
						public void run() {
							if (i == 1) {
								if (stamp3) {
									imageButtonStamp3
											.setImageResource(R.drawable.coupon_default);
									stamp3 = false;
									Utils.setStatusLoyaltyStamp3(context, 0);
									Log.i("Single", "1");
								} else {

									imageButtonStamp3
											.setImageResource(R.drawable.coupon_selected);
									stamp3 = true;
									Utils.setStatusLoyaltyStamp3(context, 1);
									Log.i("Single", "1");
								}
							}
							i = 0;
						}
					};
					if (i == 1 && stamp1 && stamp2 && !stamp4) {
						handler.postDelayed(r, 250);
					} else if (i == 2 && !stamp4) {
						imageButtonStamp3
								.setImageResource(R.drawable.coupon_default);
						stamp3 = false;
						Utils.setStatusLoyaltyStamp3(context, 0);
						Log.i("Double", "2");
						i = 0;
					}
				}
			}

		});

		imageButtonStamp4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isActive = true;
				if (isPasswordEntered) {
					i++;
					Handler handler = new Handler();
					Runnable r = new Runnable() {

						@Override
						public void run() {
							if (i == 1) {
								if (stamp4) {
									imageButtonStamp4
											.setImageResource(R.drawable.coupon_default);
									stamp4 = false;
									Utils.setStatusLoyaltyStamp4(context, 0);
									Log.i("Single", "1");
								} else {
									imageButtonStamp4
											.setImageResource(R.drawable.coupon_selected);
									stamp4 = true;
									Utils.setStatusLoyaltyStamp4(context, 1);
									Log.i("Single", "1");
								}

							}
							i = 0;
						}
					};
					if (i == 1 && stamp3 && stamp2 && stamp1 && !stamp5) {
						handler.postDelayed(r, 250);
					} else if (i == 2 && !stamp5) {
						imageButtonStamp4
								.setImageResource(R.drawable.coupon_default);
						stamp4 = false;
						Utils.setStatusLoyaltyStamp4(context, 0);
						Log.i("Double", "2");
						i = 0;
					}
				}
			}

		});

		imageButtonStamp5.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isActive = true;
				if (isPasswordEntered) {
					i++;
					Handler handler = new Handler();
					Runnable r = new Runnable() {

						@Override
						public void run() {
							if (i == 1) {
								if (stamp5) {
									imageButtonStamp5
											.setImageResource(R.drawable.coupon_default);
									stamp5 = false;
									Utils.setStatusLoyaltyStamp5(context, 0);
									Log.i("Single", "1");
								} else {
									imageButtonStamp5
											.setImageResource(R.drawable.coupon_selected);
									stamp5 = true;
									Utils.setStatusLoyaltyStamp5(context, 1);
									Log.i("Single", "1");
								}

							}
							i = 0;
						}
					};
					if (i == 1 && stamp4 && stamp3 && stamp2 && stamp1) {
						handler.postDelayed(r, 250);
					} else if (i == 2) {
						imageButtonStamp5
								.setImageResource(R.drawable.coupon_default);
						stamp5 = false;
						Utils.setStatusLoyaltyStamp5(context, 0);
						Log.i("Double", "2");
						i = 0;
					}
				}
			}

		});
		setFont();
		setSocialIcon();

	}

	void setSocialIcon() {
		// if (Helper.getSharedHelper().reatiler.fbIconDisplay.equals("1")) {
		//
		// loyalty.setFbUrl(Helper.getSharedHelper().reatiler.fbUrl);
		// } else {
		// loyalty.setFbUrl("");
		// }
		// if (Helper.getSharedHelper().reatiler.instagramDisplay.equals("1")) {
		//
		// loyalty.setInstagramUrl(Helper.getSharedHelper().reatiler.instagramUrl);
		// } else {
		// loyalty.setInstagramUrl("");
		// }
		try {
			if (Helper.getSharedHelper().reatiler.fbIconDisplay
					.equalsIgnoreCase("0")) {
				llFb.setVisibility(View.GONE);

			} else {

				llFb.setVisibility(View.VISIBLE);
			}
			if (Helper.getSharedHelper().reatiler.instagramDisplay
					.equalsIgnoreCase("0")) {
				llInstagram.setVisibility(View.GONE);
			} else {
				llInstagram.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			llFb.setVisibility(View.GONE);
			llInstagram.setVisibility(View.GONE);
		}
	}

	void setFont() {
		try {
			textViewHeader.setTypeface(Helper.getSharedHelper().boldFont);
			buttonMerchantAdmin.setTypeface(Helper.getSharedHelper().boldFont);
			buttonReset.setTypeface(Helper.getSharedHelper().boldFont);

			howItWorks.setTypeface(Helper.getSharedHelper().boldFont);
			textViewTermsCond.setTypeface(Helper.getSharedHelper().normalFont);
		} catch (Exception e) {

		}

	}

	Boolean parseLoyality(JSONObject jsonObject) {
		try {
			if (jsonObject.getString("errorCode").equals("1")) {
				loyalty = new Loyalty();

				loyalty.setLoyaltyImage(jsonObject.getString("loyaltyImage"));
				loyalty.setTermsCond(jsonObject.getString("termsCond"));
				loyalty.setFbIconDisplay(jsonObject.getString("fbIconDisplay"));
				loyalty.setInstagramDisplay(jsonObject
						.getString("instagramDisplay"));

				isActive = true;

				isActive = true;

				// sqliteHelper.openDataBase();
				// sqliteHelper.deleteLoyalty();
				// sqliteHelper.close();
				//
				// sqliteHelper.openDataBase();
				// sqliteHelper.insertOrReplaceLoyalty(loyalty);
				// sqliteHelper.close();

				return true;
			} else {
				return false;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	private void loadStamps() {
		if (Utils.getStatusLoyaltyStamp1(context) == 0) {
			imageButtonStamp1.setImageResource(R.drawable.coupon_default);
			stamp1 = false;
		} else {
			imageButtonStamp1.setImageResource(R.drawable.coupon_selected);
			stamp1 = true;
		}

		if (Utils.getStatusLoyaltyStamp2(context) == 0) {
			imageButtonStamp2.setImageResource(R.drawable.coupon_default);
			stamp2 = false;
		} else {
			imageButtonStamp2.setImageResource(R.drawable.coupon_selected);
			stamp2 = true;
		}

		if (Utils.getStatusLoyaltyStamp3(context) == 0) {
			imageButtonStamp3.setImageResource(R.drawable.coupon_default);
			stamp3 = false;
		} else {
			imageButtonStamp3.setImageResource(R.drawable.coupon_selected);
			stamp3 = true;
		}

		if (Utils.getStatusLoyaltyStamp4(context) == 0) {
			imageButtonStamp4.setImageResource(R.drawable.coupon_default);
			stamp4 = false;
		} else {
			imageButtonStamp4.setImageResource(R.drawable.coupon_selected);
			stamp4 = true;
		}

		if (Utils.getStatusLoyaltyStamp5(context) == 0) {
			imageButtonStamp5.setImageResource(R.drawable.coupon_default);
			stamp5 = false;
		} else {
			imageButtonStamp5.setImageResource(R.drawable.coupon_selected);
			stamp5 = true;
		}
	}

	private final class AsyncGetLoyalty extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			if (loyalty != null) {

			} else {
				showLoadingDialog();
			}

		}

		@Override
		protected Boolean doInBackground(Void... params) {

			if (Utils.hasNetworkConnection(activity.getApplicationContext())) {
				JSONObject param = new JSONObject();
				try {
					param.put(Constants.PARAM_RETAILER_ID,
							Constants.RETAILER_ID);
					JSONObject jsonObject = HTTPHandler.defaultHandler()
							.doPost(Constants.URL_GET_LOYALTY, param);

					if (jsonObject != null) {
						spref.edit()
								.putString(Constants.KEY_GET_LOYALITY_INFO,
										jsonObject.toString()).commit();
					}
					// if (jsonObject.getString("errorCode").equals("1")) {
					// Loyalty loyalty = new Loyalty();
					//
					// loyalty.setLoyaltyImage(jsonObject
					// .getString("loyaltyImage"));
					// loyalty.setTermsCond(jsonObject.getString("termsCond"));
					// loyalty.setFbIconDisplay(jsonObject
					// .getString("fbIconDisplay"));
					//
					// if (jsonObject.getString("fbIconDisplay").equals("1")) {
					// loyalty.setFbUrl(jsonObject.getString("fbUrl"));
					// } else {
					// loyalty.setFbUrl("");
					// }
					//
					// sqliteHelper.openDataBase();
					// sqliteHelper.deleteLoyalty();
					// sqliteHelper.close();
					//
					// sqliteHelper.openDataBase();
					// sqliteHelper.insertOrReplaceLoyalty(loyalty);
					// sqliteHelper.close();
					//
					//
					// } else {
					// return false;
					// }
					return parseLoyality(jsonObject);

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			}
			return false;

		}

		@Override
		protected void onPostExecute(Boolean status) {
			// sqliteHelper.openDataBase();
			// loyalty = sqliteHelper.getLoyalty();
			// sqliteHelper.close();

			textViewTermsCond.setText(loyalty.getTermsCond());
			imageCacheloader.displayImage(loyalty.getLoyaltyImage(),
					R.drawable.image_placeholder, imageViewLoyalty);

			dismissLoadingDialog();
		}
	}

	public void showLoadingDialog() {
		try {
			if (progressDialog == null || !progressDialog.isShowing()) {
				progressDialog = new ProgressDialog(context);
				progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				progressDialog.setMessage("Loading...");
				// progressDialog.setCancelable(false);
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.show();
			}
		} catch (Exception ex) {
			Log.e("",
					"Could not display progress dialog because the activity that called it is no longer active");
		}
	}

	public void dismissLoadingDialog() {
		try {
			progressDialog.dismiss();
		} catch (Exception ex) {
			Log.e("",
					"Could not dismiss progress dialog because the activity that called it is no longer active");
		}
	}

	void resetPressed(View v) {
		isActive = true;
		if (isPasswordEntered) {
			Utils.setStatusLoyaltyStamp1(context, 0);
			Utils.setStatusLoyaltyStamp2(context, 0);
			Utils.setStatusLoyaltyStamp3(context, 0);
			Utils.setStatusLoyaltyStamp4(context, 0);
			Utils.setStatusLoyaltyStamp5(context, 0);

			imageButtonStamp1.setImageResource(R.drawable.coupon_default);
			imageButtonStamp2.setImageResource(R.drawable.coupon_default);
			imageButtonStamp3.setImageResource(R.drawable.coupon_default);
			imageButtonStamp4.setImageResource(R.drawable.coupon_default);
			imageButtonStamp5.setImageResource(R.drawable.coupon_default);

			stamp1 = false;
			stamp2 = false;
			stamp3 = false;
			stamp4 = false;
			stamp5 = false;
		}
	}

	void merchantAdminPressed(View v) {
		isActive = true;
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		int width = metrics.widthPixels;
		try {
			dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.dialog_merchant_password);

			dialog.getWindow().setLayout(width - 14, LayoutParams.WRAP_CONTENT);
			// dialog.getWindow().set(width-20,
			// LayoutParams.WRAP_CONTENT);

			// dialog.getWindow().setLayout((6 * width) / 7,
			// LayoutParams.WRAP_CONTENT);

			Button buttonEnter = (Button) dialog.findViewById(R.id.btn_enter);
			Button btn_Scan = (Button) dialog.findViewById(R.id.btn_Scan);
			Button buttonSaveAndClose = (Button) dialog
					.findViewById(R.id.btn_saveclose);

			editTextPassword = (EditText) dialog
					.findViewById(R.id.edt_password);

			TextView textViewTitle = (TextView) dialog
					.findViewById(R.id.textViewTitle);
			editTextPassword.setBackgroundDrawable(Helper.getSharedHelper()
					.getGradientDrawableEditText(retailer.getHeaderColor()));
			editTextPassword.getBackground().setAlpha(30);
			editTextPassword.setText(password);
			buttonEnter.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));
			buttonEnter.setBackgroundDrawable(Helper.getSharedHelper()
					.getGradientDrawable(retailer.getHeaderColor()));

			buttonSaveAndClose.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));
			buttonSaveAndClose.setBackgroundDrawable(Helper.getSharedHelper()
					.getGradientDrawable(retailer.getHeaderColor()));

			textViewTitle.setBackgroundDrawable(Helper.getSharedHelper()
					.getGradientDrawableNoRad(retailer.getHeaderColor()));
			textViewTitle.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));

			buttonEnter.setTypeface(Helper.getSharedHelper().normalFont);
			buttonSaveAndClose.setTypeface(Helper.getSharedHelper().normalFont);

			textViewTitle.setTypeface(Helper.getSharedHelper().boldFont);
			editTextPassword.setTypeface(Helper.getSharedHelper().normalFont);
			buttonEnter.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					isActive = true;
					password = editTextPassword.getText().toString();
					authenticateAdmin();
				}
			});

			buttonSaveAndClose.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					isActive = true;
					isPasswordEntered = false;
					password = "";
					dialog.dismiss();
					// handler.removeCallbacks(this);
				}
			});
			btn_Scan.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Helper.getSharedHelper().isScanAdminPWD = true;
					IntentIntegrator.initiateScan(getActivity(),
							R.layout.capture, R.id.viewfinder_view,
							R.id.preview_view, true);
				}
			});
		} catch (Exception e) {
		}

		dialog.show();
	}

	void authenticateAdmin() {
		if (Utils.hasNetworkConnection(context)) {
			if (isPasswordEntered) {
				dialog.dismiss();
			} else {
				new AsyncGetPassword().execute();
			}

		} else {

			Log.i("", Utils.getPasswordImage(context) + "");

			if (password.equalsIgnoreCase(Utils.getPasswordImage(context))) {
				isPasswordEntered = !isPasswordEntered;
				dialog.dismiss();
			} else {
				Toast.makeText(context,
						"Incorrect password. Please try again.",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void pwdScanned(String pwd) {
		isActive = true;
		password = pwd;
		authenticateAdmin();
		dialog.dismiss();
//		editTextPassword.setText(pwd);
		
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case IntentIntegrator.REQUEST_CODE:
			IntentResult scanResult = IntentIntegrator.parseActivityResult(
					requestCode, resultCode, data);
			if (scanResult == null) {
				return;
			}
			final String result = scanResult.getContents();
			if (result != null) {
				handler.post(new Runnable() {
					@Override
					public void run() {
//						editTextPassword.setText(result);
					}
				});
			}
			break;
		default:
		}
	}

	private final class AsyncGetPassword extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog();
		}

		@Override
		protected String doInBackground(Void... params) {
			if (Utils.hasNetworkConnection(activity.getApplicationContext())) {

				JSONObject param = new JSONObject();
				try {
					param.put(Constants.PARAM_RETAILER_ID,
							Constants.RETAILER_ID);
					param.put(Constants.PARAM_PASSWORD, password);
					JSONObject jsonObject = HTTPHandler.defaultHandler()
							.doPost(Constants.URL_VERIFY_LOYALTY_PASS, param);

					return jsonObject.getString("errorCode");

				} catch (Exception e) {
					e.printStackTrace();
					return "";
				}
			}

			return "";
		}

		@Override
		protected void onPostExecute(String errCode) {

			dismissLoadingDialog();

			if (errCode.equalsIgnoreCase("1")) {
				dialog.dismiss();

				startTimer();
				Utils.setPassword(context, password);

				if (isPasswordEntered) {
					if (stamp1) {
						Utils.setStatusLoyaltyStamp1(context, 1);
					} else {
						Utils.setStatusLoyaltyStamp1(context, 0);
					}

					if (stamp2) {
						Utils.setStatusLoyaltyStamp2(context, 1);
					} else {
						Utils.setStatusLoyaltyStamp2(context, 0);
					}

					if (stamp3) {
						Utils.setStatusLoyaltyStamp3(context, 1);
					} else {
						Utils.setStatusLoyaltyStamp3(context, 0);
					}

					if (stamp4) {
						Utils.setStatusLoyaltyStamp4(context, 1);
					} else {
						Utils.setStatusLoyaltyStamp4(context, 0);
					}

					if (stamp5) {
						Utils.setStatusLoyaltyStamp5(context, 1);
					} else {
						Utils.setStatusLoyaltyStamp5(context, 0);
					}
				}

				isPasswordEntered = !isPasswordEntered;

				Log.i("Password Result", "Success");

			} else {

				Toast.makeText(context,
						"Incorrect password. Please try again.",
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	private final class AsyncGetPasswordSaveNClose extends
			AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog();
		}

		@Override
		protected String doInBackground(Void... params) {

			if (Utils.hasNetworkConnection(activity.getApplicationContext())) {
				isActive = true;
				JSONObject param = new JSONObject();
				try {
					param.put(Constants.PARAM_RETAILER_ID,
							Constants.RETAILER_ID);
					param.put(Constants.PARAM_PASSWORD, password);
					isActive = true;
					JSONObject jsonObject = HTTPHandler.defaultHandler()
							.doPost(Constants.URL_VERIFY_LOYALTY_PASS, param);
					isActive = true;
					return jsonObject.getString("errorCode");

				} catch (Exception e) {
					e.printStackTrace();
					return "";
				}
			}
			return "";

		}

		@Override
		protected void onPostExecute(String errCode) {

			isActive = true;
			dismissLoadingDialog();

			if (errCode.equalsIgnoreCase("1")) {
				dialog.dismiss();

				Utils.setPassword(context, password);

				if (isPasswordEntered) {
					if (stamp1) {
						Utils.setStatusLoyaltyStamp1(context, 1);
					} else {
						Utils.setStatusLoyaltyStamp1(context, 0);
					}

					if (stamp2) {
						Utils.setStatusLoyaltyStamp2(context, 1);
					} else {
						Utils.setStatusLoyaltyStamp2(context, 0);
					}

					if (stamp3) {
						Utils.setStatusLoyaltyStamp3(context, 1);
					} else {
						Utils.setStatusLoyaltyStamp3(context, 0);
					}

					if (stamp4) {
						Utils.setStatusLoyaltyStamp4(context, 1);
					} else {
						Utils.setStatusLoyaltyStamp4(context, 0);
					}

					if (stamp5) {
						Utils.setStatusLoyaltyStamp5(context, 1);
					} else {
						Utils.setStatusLoyaltyStamp5(context, 0);
					}
				}

				isPasswordEntered = !isPasswordEntered;

				Log.i("Password Result", "Success");
				password = "";

			} else {

				Toast.makeText(context,
						"Incorrect password. Please try again.",
						Toast.LENGTH_SHORT).show();
			}

		}
	}

	void startTimer() {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				if (isActive) {
					isActive = false;
					handler.postDelayed(this, 50000);
				}// run the runnable in a minute again
				else {
					isPasswordEntered = false;
					password = "";
					dialog.dismiss();
					handler.removeCallbacks(this);
				}

			}
		};
		handler.postDelayed(r, 60000);
	}

	public void likePressed(View v) {
		isActive = true;
		try {
			String url = Helper.getSharedHelper().reatiler.fbUrl;

			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			// i.setData(Uri.parse("http://www.facebook.com"));
			startActivity(i);
		} catch (Exception e) {
		}
	}

	public void instagramPressed(View v) {
		isActive = true;
		try {
			String url = Helper.getSharedHelper().reatiler.instagramUrl;
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse(url));
			// i.setData(Uri.parse("http://www.facebook.com"));
			startActivity(i);
		} catch (Exception e) {
		}
	}

}
