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
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.appauthority.appwiz.interfaces.OrderHistoryCaller;
import com.appsauthority.appwiz.OrderDetailActivity;
import com.appsauthority.appwiz.OrderHistoryDataHandler;
import com.appsauthority.appwiz.ProfileActivity;
import com.appsauthority.appwiz.ShoppingCartActivity;
import com.appsauthority.appwiz.adapters.OrderHistoryAdapter;
import com.appsauthority.appwiz.models.OrderHistoryResponseObject;
import com.appsauthority.appwiz.models.OrderObject;
import com.appsauthority.appwiz.models.Retailer;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.Helper;
import com.offpeaksale.restaurants.R;

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
			adapter = new OrderHistoryAdapter(context, R.layout.row_order_history,
					orderHistoryList);
			listview.setAdapter(adapter);

		}

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				OrderObject order = orderHistoryList.get(position);
				Intent intent = new Intent(context, OrderDetailActivity.class);
				intent.putExtra("orderObj", order);
				startActivity(intent);
				
			}
		});

		// showLoadingDialog();
		return view;
	}

	public void refreshList() {
		Boolean isloggedIn = spref.getBoolean(Constants.KEY_IS_USER_LOGGED_IN,false);
		if (isloggedIn) {
			OrderHistoryDataHandler lookbookDatahandler = new OrderHistoryDataHandler(
					this, this.getActivity());
			lookbookDatahandler.getLookBookData();
		}else{
			Intent intent = new Intent(getActivity(), ProfileActivity.class);
			intent.putExtra("FROM", "CARTLOGIN");
			startActivity(intent);
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
		if (orderHistoryresponseObj != null && orderHistoryresponseObj.orders != null) {
			adapter.clear();
			orderHistoryList.clear();
			adapter.addAll(orderHistoryresponseObj.orders);
			adapter.notifyDataSetChanged();
		}else{
			Toast.makeText(getActivity(), "No record found", Toast.LENGTH_LONG).show();
		}
	}

}
