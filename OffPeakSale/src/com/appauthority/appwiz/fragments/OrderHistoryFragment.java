package com.appauthority.appwiz.fragments;

/* Copyright (C)
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Kevin Irish Antonio <irish.antonio@yahoo.com>, February 2014
 */
import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.appauthority.appwiz.interfaces.OrderHistoryCaller;
import com.appsauthority.appwiz.OrderDetailActivity;
import com.appsauthority.appwiz.OrderDetailDataHandler;
import com.appsauthority.appwiz.OrderHistoryDataHandler;
import com.appsauthority.appwiz.ProfileActivity;
import com.appsauthority.appwiz.adapters.OrderHistoryAdapter;
import com.appsauthority.appwiz.models.AllOrdersResponseObject;
import com.appsauthority.appwiz.models.OrderDetailResponseObject;
import com.appsauthority.appwiz.models.OrderHistoryResponseObject;
import com.appsauthority.appwiz.models.OrderObject;
import com.appsauthority.appwiz.models.Retailer;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.Helper;
import com.offpeaksale.consumer.R;

public class OrderHistoryFragment extends Fragment implements
		OrderHistoryCaller {

	private View view;
	private ProgressDialog progressDialog;
	private ListView listview;
	private OrderHistoryAdapter adapter;
	public List<OrderObject> orderHistoryList = new ArrayList<OrderObject>();
	private Context context;
	Retailer retailer;
	private SharedPreferences spref;

	HorizontalScrollView horizontalScrollView;
	int width;
	int selectedTabIndex = 0;
	LinearLayout llTabContainer;
	Button btnLogin;
	RelativeLayout vwLogin;
	AllOrdersResponseObject allOrders;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		context = getActivity();
		retailer = Helper.getSharedHelper().reatiler;
		spref = PreferenceManager.getDefaultSharedPreferences(getActivity());
		if (view != null) {
			ViewGroup parent = (ViewGroup) view.getParent();
			if (parent != null)
				parent.removeView(view);

		} else {
			view = inflater.inflate(R.layout.fragment_order_history, container,
					false);
			listview = (ListView) view.findViewById(R.id.lv_order_history);
			orderHistoryList = new ArrayList<OrderObject>();
			adapter = new OrderHistoryAdapter(context,
					R.layout.row_order_history, orderHistoryList);
			listview.setAdapter(adapter);

		}
		vwLogin = (RelativeLayout) view.findViewById(R.id.vwLogin);
		btnLogin = (Button) view.findViewById(R.id.btnLogin);
		llTabContainer = (LinearLayout) view.findViewById(R.id.llTabContainer);
		horizontalScrollView = (HorizontalScrollView) view
				.findViewById(R.id.horizontalScrollView);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				getOrderDetail(position);

			}
		});

		// showLoadingDialog();
		Boolean isloggedIn = spref.getBoolean(Constants.KEY_IS_USER_LOGGED_IN,
				false);
		if (!isloggedIn) {
			showLoginView();
			vwLogin.setVisibility(View.VISIBLE);

		} else {
			vwLogin.setVisibility(View.GONE);
		}
		btnLogin.setTextColor(Color.parseColor("#"
				+ retailer.getRetailerTextColor()));
		btnLogin.setBackgroundDrawable(Helper.getSharedHelper()
				.getGradientDrawable(retailer.getHeaderColor()));
		initializeTab();
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showLoginView();
			}
		});
		return view;
	}

	void getOrderDetail(int position){
		OrderObject order = orderHistoryList.get(position);
		
		OrderDetailDataHandler orderDetailHandler = new OrderDetailDataHandler(this, this.getActivity(), order.merchantEmail, order.orderId);
		orderDetailHandler.getOrderDetail();
	}
	public void refreshList() {
		Boolean isloggedIn = spref.getBoolean(Constants.KEY_IS_USER_LOGGED_IN,
				false);
		if (isloggedIn) {
			OrderHistoryDataHandler lookbookDatahandler = new OrderHistoryDataHandler(
					this, this.getActivity());
			lookbookDatahandler.getLookBookData();
			vwLogin.setVisibility(View.GONE);
		} else {
			vwLogin.setVisibility(View.VISIBLE);
		}

	}

	void showLoginView() {
		Intent intent = new Intent(getActivity(), ProfileActivity.class);
		intent.putExtra("FROM", "CARTLOGIN");
		startActivity(intent);
	}

	void initializeTab() {

		List<String> tabs = new ArrayList<String>();
		tabs.add("Active");
		tabs.add("Inactive");

		llTabContainer.removeAllViews();
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay()
				.getMetrics(displaymetrics);
		int screewidth = displaymetrics.widthPixels;
		if (tabs.size() > 2) {
			width = (int) (screewidth / 2.5);
			// width = (int) (width - width*.25);
		} else {
			width = (int) (screewidth / 2);
		}

		LayoutInflater inflater = (LayoutInflater) getActivity()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		for (int i = 0; i < tabs.size(); i++) {
			View vwReview = inflater.inflate(R.layout.order_history_tab_item,
					null);
			RelativeLayout tabView = (RelativeLayout) vwReview
					.findViewById(R.id.item);
			TextView tvTabName = (TextView) tabView
					.findViewById(R.id.tvItemName);
			TextView tvPlaceHolder = (TextView) tabView
					.findViewById(R.id.tvPlaceHolder);
			View underLineView = (View) tabView
					.findViewById(R.id.vwTabUnderline);
			tvPlaceHolder.getLayoutParams().width = width;
			tvTabName.setText(tabs.get(i));
			if (i == selectedTabIndex) {
				underLineView.setBackgroundColor(Color.parseColor("#F2"
						+ Helper.getSharedHelper().reatiler.getHeaderColor()));
			} else {
				underLineView.setBackgroundColor(Color.TRANSPARENT);
			}
			llTabContainer.addView(tabView);
			tabView.setTag(i);
			tabView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int tag = (Integer) v.getTag();
					selectedTabIndex = tag;
					initializeTab();
				}
			});
		}

		horizontalScrollView.smoothScrollTo(width * selectedTabIndex, 0);
		
		try {
			adapter.clear();
			if (selectedTabIndex ==0) {
				adapter.addAll(allOrders.active);	
			}else{
				adapter.addAll(allOrders.used);
			}
			adapter.notifyDataSetChanged();
			if(adapter.getCount()==0)
			{
				Toast.makeText(getActivity(), "No Item Found", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		refreshList();
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

	@Override
	public void orderHistoryDownloaded(
			OrderHistoryResponseObject orderHistoryresponseObj) {
		// TODO Auto-generated method stub
		if (orderHistoryresponseObj != null
				&& orderHistoryresponseObj.data != null) {
			adapter.clear();
			orderHistoryList.clear();
			allOrders = orderHistoryresponseObj.data;
			if (selectedTabIndex ==0) {
				adapter.addAll(allOrders.active);	
			}else{
				adapter.addAll(allOrders.used);
			}
			
			adapter.notifyDataSetChanged();
		} else {
			Toast.makeText(getActivity(), "No record found", Toast.LENGTH_LONG)
					.show();
		}
	}

	@Override
	public void orderDetailDownloaded(OrderDetailResponseObject orderDetailObj) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(context, OrderDetailActivity.class);
		intent.putExtra("orderObj", orderDetailObj.data);
		startActivity(intent);
	}

}
