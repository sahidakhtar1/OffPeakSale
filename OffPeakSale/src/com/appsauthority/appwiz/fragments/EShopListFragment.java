package com.appsauthority.appwiz.fragments;

/* Copyright (C)
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Kevin Irish Antonio <irish.antonio@yahoo.com>, February 2014
 */
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsauthority.appwiz.EShopDetailActivity;
import com.appsauthority.appwiz.adapters.EShopListAdapter;
import com.appsauthority.appwiz.adapters.FilterListAdapter;
import com.appsauthority.appwiz.models.CategoryObject;
import com.appsauthority.appwiz.models.Product;
import com.appsauthority.appwiz.models.ProductResponse;
import com.appsauthority.appwiz.models.Retailer;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.HTTPHandler;
import com.appsauthority.appwiz.utils.Helper;
import com.appsauthority.appwiz.utils.Utils;
import com.offpeaksale.restaurants.R;
import com.google.gson.Gson;

public class EShopListFragment extends Fragment {

	private View view;
	private ProgressDialog progressDialog;
	private ListView listview;
	private TextView tvNoSearchFound;
	private EShopListAdapter adapter;
	public List<Product> productList = new ArrayList<Product>();
	private Context context;
	Retailer retailer;
	List<String> filterOptions;
	FilterListAdapter filterAdapter;
	public CategoryObject category;
	private SharedPreferences spref;

	List<String> listFilterKey;
	int selectedIndex = 0, filterIndex = 0;
	public String searchedKeyWord;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		listFilterKey = new ArrayList<String>();
		
		listFilterKey.add("rate");
		listFilterKey.add("new");
		listFilterKey.add("low");
		listFilterKey.add("high");
		listFilterKey.add("none");

		context = getActivity();
		retailer = Helper.getSharedHelper().reatiler;
		spref = PreferenceManager.getDefaultSharedPreferences(getActivity());
		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);

		} else {
			view = inflater.inflate(R.layout.fragment_eshop_list, container,
					false);
			listview = (ListView) view.findViewById(R.id.lv_eshop);
			tvNoSearchFound = (TextView) view.findViewById(R.id.tvNoSearchFound);
			tvNoSearchFound.setVisibility(View.GONE);

		}

		try {
			Bundle bundle = getArguments();
			if (bundle.containsKey("ID")) {
				int id = bundle.getInt("ID");

				Log.i("ID", id + "");
			}
			if (bundle.containsKey("ProductList")) {
				productList = (ArrayList<Product>) bundle
						.getSerializable("ProductList");
				// loadListView(0);
			}
		} catch (Exception e) {
		}

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				ImageView image = (ImageView) view.findViewById(R.id.iv_eshop);
				int width = image.getWidth();
				int height = image.getHeight();
				Product product = (Product) parent.getItemAtPosition(position);

				Intent intent = new Intent(context, EShopDetailActivity.class);
				intent.putExtra("product", product);
				startActivity(intent);
			}
		});
		loadListView(0);
		TextView tvFilter = (TextView) view.findViewById(R.id.tvFilter);
		tvFilter.setTypeface(Helper.getSharedHelper().normalFont);
		RelativeLayout rlFilter = (RelativeLayout) view
				.findViewById(R.id.rlFilter);
		rlFilter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showFilterDialog();
			}
		});
		filterOptions = new ArrayList<String>();
		
		filterOptions.add("Popularity");
		filterOptions.add("Latest");
		filterOptions.add("Lowest Price");
		filterOptions.add("Highest Price");
		filterOptions.add("None");
		if (searchedKeyWord != null) {
			rlFilter.setVisibility(View.GONE);
		}
		return view;
	}

	public void refreshList() {
		if (adapter != null) {
			adapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new AsyncAllProducts().execute();
	}

	public void loadListView(int id) {
		// sqLiteHelper.openDataBase();
		// productList = sqLiteHelper.getProducts(id);
		// sqLiteHelper.close();

		adapter = new EShopListAdapter(context, R.layout.row_eshop, productList);

		listview.setAdapter(adapter);

	}

	void showFilterDialog() {

		try {
			DisplayMetrics metrics = getResources().getDisplayMetrics();
			int width = metrics.widthPixels;

			final Dialog dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setCancelable(false);
			dialog.setContentView(R.layout.dialog_filter);

			dialog.getWindow().setLayout((6 * width) / 7,
					LayoutParams.WRAP_CONTENT);

			RelativeLayout btnClose = (RelativeLayout) dialog
					.findViewById(R.id.btnClose);
			TextView tvFilterTitle = (TextView) dialog
					.findViewById(R.id.tvFilterTitle);
			ListView lvFilter = (ListView) dialog.findViewById(R.id.lvFilter);

			View lineTop = (View) dialog.findViewById(R.id.lineTop);
			lineTop.setBackgroundColor(Color.parseColor("#"
					+ retailer.getHeaderColor()));
			Button btnFilter = (Button) dialog.findViewById(R.id.btnFilter);

			btnFilter.setBackgroundDrawable(Helper.getSharedHelper()
					.getGradientDrawable(retailer.getHeaderColor()));

			btnFilter.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					filterIndex = selectedIndex;
					new AsyncAllProducts().execute();
					dialog.dismiss();
				}
			});

			btnClose.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					dialog.dismiss();
				}
			});
			Helper.getSharedHelper().filterIndex = filterIndex;
			btnFilter.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));

			tvFilterTitle.setTypeface(Helper.getSharedHelper().boldFont);

			filterAdapter = new FilterListAdapter(getActivity(),
					R.layout.filter_list_item, filterOptions);
			lvFilter.setAdapter(filterAdapter);

			lvFilter.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					// TODO Auto-generated method stub
					selectedIndex = position;
					Helper.getSharedHelper().filterIndex = selectedIndex;
					filterAdapter.notifyDataSetChanged();
				}
			});
			;

			dialog.show();
		} catch (Exception e) {
		}

	}

	private final class AsyncAllProducts extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog();
			String eshopConatent = null;
			if (category != null) {
				eshopConatent = spref.getString(category.id, "");
			}

			if (searchedKeyWord == null && filterIndex == 0 
					&& eshopConatent!= null
					&& !eshopConatent.equalsIgnoreCase("")) {
				try {
					JSONObject jsonObject = new JSONObject(eshopConatent);
					Boolean status = parseJSON(jsonObject);
					if (status) {
						adapter.clear();
						adapter.addAll(productList);
						adapter.notifyDataSetChanged();
						dismissLoadingDialog();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {

			}
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			JSONObject param = new JSONObject();
			if (Utils.hasNetworkConnection(getActivity()
					.getApplicationContext())) {
				try {
					param.put(Constants.PARAM_RETAILER_ID,
							Constants.RETAILER_ID);
					if (category != null) {
						param.put(Constants.PARAM_CATEGORY_ID, category.id);
					}
//					if (filterIndex > 0) {
						param.put(Constants.PARAM_FILTER_ID,
								listFilterKey.get(filterIndex));
//					}
					if (searchedKeyWord != null) {
						param.put(Constants.PARAM_KEYWORD_ID, searchedKeyWord);
					}
					JSONObject jsonObject = HTTPHandler.defaultHandler()
							.doPost(Constants.URL_GET_ALL_PRODUCTS, param);

					if (searchedKeyWord == null && jsonObject != null
							&& filterIndex == 0) {
						spref.edit()
								.putString(category.id, jsonObject.toString())
								.commit();
					}
					if (jsonObject != null) {
						return parseJSON(jsonObject);
					}
					return false;
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
			if (status) {
				adapter.clear();
				adapter.addAll(productList);
				adapter.notifyDataSetChanged();
				tvNoSearchFound.setVisibility(View.GONE);
				listview.setVisibility(View.VISIBLE);
			}
			if (productList.size() == 0 && searchedKeyWord != null) {
				tvNoSearchFound.setVisibility(View.VISIBLE);
				listview.setVisibility(View.GONE);
				
			}
			dismissLoadingDialog();
		}
	}

	Boolean parseJSON(JSONObject jsonObject) {

		Gson gson = new Gson();
		if (jsonObject.has("currency_code")) {
			try {
				String currencyCode = jsonObject.getString("currency_code")
						.toString();
				Helper.getSharedHelper().reatiler.defaultCurrency = currencyCode;
				// Helper.getSharedHelper().currency_code = currencyCode;
				// Helper.getSharedHelper().getCurrencySymbol();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (jsonObject.has("enableRewards")) {
			try {
				String enableRewards = jsonObject.getString("enableRewards")
						.toString();
				Helper.getSharedHelper().reatiler.enableRewards = enableRewards;
				// Helper.getSharedHelper().currency_code = currencyCode;
				// Helper.getSharedHelper().getCurrencySymbol();
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

		ProductResponse data = gson.fromJson(jsonObject.toString(),
				ProductResponse.class);
		if (data == null) {
			return false;
		}

		if (data.getErrorCode() != null && data.getErrorCode().equals("1")) {

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
}
