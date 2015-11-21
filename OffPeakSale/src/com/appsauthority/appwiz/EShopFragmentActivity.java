package com.appsauthority.appwiz;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appsauthority.appwiz.fragments.EShopFragment;
import com.appsauthority.appwiz.models.Product;
import com.appsauthority.appwiz.models.ProductResponse;
import com.appsauthority.appwiz.models.Retailer;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.HTTPHandler;
import com.appsauthority.appwiz.utils.Helper;
import com.appsauthority.appwiz.utils.SQLiteHelper;
import com.appsauthority.appwiz.utils.Utils;
import com.google.gson.Gson;
import com.offpeaksale.restaurants.R;

public class EShopFragmentActivity extends FragmentActivity {

	public String TAG = getClass().getSimpleName();
	private EShopFragment content;
	private Context context;
	private ProgressDialog progressDialog;
	private Activity activity;
	private View viewLineHome, viewLineEShop, viewLineLoyalty,
			viewLineFeedback, viewLineVouchers;
	private TextView textViewHeader;
	private TextView txtCartTotal;
	private LinearLayout cartView;
	private Retailer retailer;
	private SQLiteHelper sqliteHelper;
	private SharedPreferences spref;
	List<Product> productList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		getActionBar().hide();
		context = this;
		activity = this;
		spref = PreferenceManager.getDefaultSharedPreferences(this);
		;

		// sqliteHelper = new SQLiteHelper(context);
		// sqliteHelper.openDataBase();
		retailer = Helper.getSharedHelper().reatiler;
		// sqliteHelper.close();

		if (savedInstanceState != null)
			content = (EShopFragment) getSupportFragmentManager().getFragment(
					savedInstanceState, "mContent");

		new AsyncGetEShopDetails().execute();

	}

	public void homePressed(View v) {
		// Intent intent = new Intent(context, MainActivity.class);
		// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		// startActivity(intent);
	}

	public void eShopPressed(View v) {

	}

	public void loyaltyPressed(View v) {
		Intent intent = new Intent(this, LoyaltyActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	public void feedbackPressed(View v) {
		Intent intent = new Intent(this, FeedbackActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	public void vouchersPressed(View v) {
		Intent intent = new Intent(context, VoucherActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@SuppressWarnings("deprecation")
	public void setHeaderTheme(Activity activity) {
		TextView textViewHeader = (TextView) activity
				.findViewById(R.id.textViewHeader);
		View header = (View) activity.findViewById(R.id.header);
		cartView = (LinearLayout) activity.findViewById(R.id.cartView);

		txtCartTotal = (TextView) activity.findViewById(R.id.txtCartTotal);
		txtCartTotal.setTextColor(Color.parseColor("#"
				+ retailer.getRetailerTextColor()));
		if (Helper.getSharedHelper().enableShoppingCart.equals("1")) {
			cartView.setVisibility(View.VISIBLE);
		} else {
			cartView.setVisibility(View.GONE);
		}

		textViewHeader.setTextColor(Color.parseColor("#"
				+ retailer.getRetailerTextColor()));

		// GradientDrawable gd = new GradientDrawable(
		// GradientDrawable.Orientation.TOP_BOTTOM, new int[] {
		// Color.parseColor("#AA" + retailer.getHeaderColor()),
		// Color.parseColor("#" + retailer.getHeaderColor()) });
		// gd.setCornerRadius(0f);
		ColorDrawable cd = new ColorDrawable(Color.parseColor("#"
				+ retailer.getHeaderColor()));
		header.setBackgroundDrawable(cd);

		if (Helper.getSharedHelper().enableShoppingCart.equals("1")) {
			if (txtCartTotal != null) {
				txtCartTotal.setText(Helper.getSharedHelper().getCartTotal());
			}
		} else {
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
			Log.e(TAG,
					"Could not display progress dialog because the activity that called it is no longer active");
		}
	}

	public void dismissLoadingDialog() {
		try {
			progressDialog.dismiss();
		} catch (Exception ex) {
			Log.e(TAG,
					"Could not dismiss progress dialog because the activity that called it is no longer active");
		}
	}

	private void setViewLines() {
		viewLineHome.setVisibility(View.INVISIBLE);
		viewLineEShop.setVisibility(View.VISIBLE);
		viewLineLoyalty.setVisibility(View.INVISIBLE);
		viewLineFeedback.setVisibility(View.INVISIBLE);
		viewLineVouchers.setVisibility(View.INVISIBLE);
		try {
			viewLineEShop.setBackgroundColor(Color.parseColor("#80"
					+ Helper.getSharedHelper().reatiler.getHeaderColor()));
		} catch (Exception e) {

		}
	}

	public void overflowPressed(View v) {
		RelativeLayout header = (RelativeLayout) findViewById(R.id.header);
		final PopupMenu popupMenu = new PopupMenu(this, header);
		popupMenu.getMenu().add(Menu.NONE, 1, Menu.NONE, "PROFILE");
		popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Intent intent = new Intent(context, ProfileActivity.class);
				startActivity(intent);
				return true;
			}
		});

		popupMenu.show();
	}

	Boolean parseJSON(JSONObject jsonObject) {
		Gson gson = new Gson();
		if (jsonObject.has("currency_code")) {
			try {
				String currencyCode = jsonObject.getString("currency_code")
						.toString();
				Helper.getSharedHelper().reatiler.defaultCurrency = currencyCode;
//				Helper.getSharedHelper().currency_code = currencyCode;
//				Helper.getSharedHelper().getCurrencySymbol();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (jsonObject.has("enableCreditCode")) {
			try {
				String enableCreditCode = jsonObject.getString(
						"enableCreditCode").toString();
				Helper.getSharedHelper().enableCreditCode = enableCreditCode;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (jsonObject.has("enableRating")) {
			try {
				String enableRating = jsonObject.getString("enableRating")
						.toString();
				Helper.getSharedHelper().enableRating = enableRating;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (jsonObject.has("enableShoppingCart")) {
			try {
				String enableShoppingCart = jsonObject.getString(
						"enableShoppingCart").toString();
				Helper.getSharedHelper().enableShoppingCart = enableShoppingCart;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (jsonObject.has("disablePayment")) {
			try {
				String disablePayment = jsonObject.getString("disablePayment")
						.toString();
				Helper.getSharedHelper().disablePayment = disablePayment;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (jsonObject.has("enablePay")) {
			try {
				String enablePay = jsonObject.getString("enablePay").toString();
				Helper.getSharedHelper().reatiler.enablePay = enablePay;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (jsonObject.has("enableVerit")) {
			try {
				String enableVerit = jsonObject.getString("enableVerit").toString();
				Helper.getSharedHelper().reatiler.enableVerit = enableVerit;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (jsonObject.has("enableCOD")) {
			try {
				String enableCOD = jsonObject.getString("enableCOD").toString();
				Helper.getSharedHelper().reatiler.enableCOD = enableCOD;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (jsonObject.has("enableDelivery")) {
			try {
				String enableDelivery = jsonObject.getString("enableDelivery")
						.toString();
				Helper.getSharedHelper().enableDelivery = enableDelivery;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (jsonObject.has("deliveryDays")) {
			try {
				String deliveryDays = jsonObject.getString("deliveryDays")
						.toString();
				Helper.getSharedHelper().deliveryDays = deliveryDays;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (jsonObject.has("enable_shipping")) {
			try {
				String enable_shipping = jsonObject
						.getString("enable_shipping").toString();
				Helper.getSharedHelper().enable_shipping = enable_shipping;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (jsonObject.has("deliveryTimeSlots")) {
			try {
				String deliveryTimeSlots = jsonObject.getString(
						"deliveryTimeSlots").toString();
				Helper.getSharedHelper().reatiler.deliveryTimeSlots = deliveryTimeSlots;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		ProductResponse data = gson.fromJson(jsonObject.toString(),
				ProductResponse.class);

		if (data.getErrorCode().equals("1")) {
			// sqliteHelper.openDataBase();
			// sqliteHelper.deleteCategories();
			// sqliteHelper.close();
			productList = new ArrayList<Product>();
			productList = data.getData();

			for (int index = 0; index < productList.size(); index++) {

				Product product = productList.get(index);
				if (product.getNewPrice().contains(".")) {
					Helper.getSharedHelper().isDecialFromat = true;
					break;
				} else {
					// Helper.getSharedHelper().isDecialFromat = false;
				}
			}

			return true;
		} else {
			return false;
		}
	}

	void loadEShop() {
		// if (content == null) {
		// content = new EShopFragment();
		// }
		//
		// content.categories = (ArrayList<ProductCategory>) categoryList;
		// setContentView(R.layout.fragment_frame);
		// getSupportFragmentManager().beginTransaction()
		// .replace(R.id.container_framelayout, content).commit();
		//
		// try {
		// setHeaderTheme(activity);
		// } catch (Exception e) {
		// }
		//
		// textViewHeader = (TextView) findViewById(R.id.textViewHeader);
		// viewLineHome = (View) findViewById(R.id.viewLineHome);
		// viewLineEShop = (View) findViewById(R.id.viewLineEShop);
		// viewLineLoyalty = (View) findViewById(R.id.viewLineLoyalty);
		// viewLineFeedback = (View) findViewById(R.id.viewLineFeedback);
		// viewLineVouchers = (View) findViewById(R.id.viewLineVouchers);
		// textViewHeader.setText("E-SHOP");
		// setViewLines();
		// setFont();
	}

	void setFont() {
		try {
			textViewHeader.setTypeface(Helper.getSharedHelper().boldFont);
			txtCartTotal.setTypeface(Helper.getSharedHelper().normalFont);
		} catch (Exception e) {

		}
	}

	private final class AsyncGetEShopDetails extends
			AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {

			String eshopConatent = spref.getString(
					Constants.KEY_GET_PRODUCTS_INFO, "");
			if (!eshopConatent.equalsIgnoreCase("")) {
				try {
					JSONObject jsonObject = new JSONObject(eshopConatent);
					Boolean status = parseJSON(jsonObject);
					if (status) {
						// loadEShop();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {

			}
			showLoadingDialog();
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			JSONObject param = new JSONObject();
			if (Utils.hasNetworkConnection(getApplicationContext())) {
				try {
					param.put(Constants.PARAM_RETAILER_ID,
							Constants.RETAILER_ID);
					JSONObject jsonObject = HTTPHandler.defaultHandler()
							.doPost(Constants.URL_GET_PRODUCTS, param);

					if (jsonObject != null) {
						spref.edit()
								.putString(Constants.KEY_GET_PRODUCTS_INFO,
										jsonObject.toString()).commit();
					}
					return parseJSON(jsonObject);
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			} else {
				return false;
			}

		}

		@Override
		protected void onPostExecute(Boolean status) {
			dismissLoadingDialog();

			loadEShop();

		}
	}

	public void cartPressed(View v) {
		if (Helper.getSharedHelper().shoppintCartList.size() > 0) {
			Intent intent = new Intent(this, ShoppingCartActivity.class);

			startActivity(intent);
		} else {
			Toast.makeText(context, "Your shopping cart is empty.",
					Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		if (Helper.getSharedHelper().enableShoppingCart.equals("1")) {
			if (txtCartTotal != null) {
				txtCartTotal.setText(Helper.getSharedHelper().getCartTotal());
			}
		} else {
		}
	}
}
