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

import com.appauthority.appwiz.interfaces.LookBookAdapterCaller;
import com.appauthority.appwiz.interfaces.LookBookCaller;
import com.appsauthority.appwiz.LookBookDataHandler;
import com.appsauthority.appwiz.LookBookLikeDataHandler;
import com.appsauthority.appwiz.adapters.LookbookListAdapter;
import com.appsauthority.appwiz.models.LookBookInfoResponse;
import com.appsauthority.appwiz.models.LookBookObject;
import com.appsauthority.appwiz.models.Retailer;
import com.appsauthority.appwiz.utils.Helper;
import com.offpeaksale.consumer.R;

public class LookBookFragment extends Fragment implements LookBookCaller, LookBookAdapterCaller{

	private View view;
	private ProgressDialog progressDialog;
	private ListView listview;
	private LookbookListAdapter adapter;
	public List<LookBookObject> lookbookList = new ArrayList<LookBookObject>();
	private Context context;
	Retailer retailer;
	private SharedPreferences spref;

	int selectedIndex = -1, filterIndex = 0;

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
			view = inflater.inflate(R.layout.fragment_lookbook_list, container,
					false);
			listview = (ListView) view.findViewById(R.id.lv_lookbook);
			lookbookList = new ArrayList<LookBookObject>();
			adapter = new LookbookListAdapter(context, R.layout.row_lookbook, lookbookList,this);
			listview.setAdapter(adapter);

		}

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == selectedIndex) {
					selectedIndex = -1;
					
				}else{
					selectedIndex = position;
				}
				adapter.selectedIndex = selectedIndex;
				adapter.notifyDataSetChanged();
			}
		});

		LookBookDataHandler lookbookDatahandler = new LookBookDataHandler( this, this.getActivity());
		lookbookDatahandler.getLookBookData();
//		showLoadingDialog();
		return view;
	}

	public void refreshList() {

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
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
	public void lookBookDataDownloaded(LookBookInfoResponse lookbookresponseObj) {
		// TODO Auto-generated method stub
		if (lookbookresponseObj.data != null) {
			adapter.clear();
			lookbookList.clear();
			adapter.addAll(lookbookresponseObj.data);
			adapter.notifyDataSetChanged();
		}
//		dismissLoadingDialog();
		
	}

	@Override
	public void lookBookitemLiked(int itemIndex, String likeCount,String errorMsg) {
		// TODO Auto-generated method stub
		Toast.makeText(this.getActivity(), errorMsg, Toast.LENGTH_LONG).show();
		try {
			LookBookObject  obj = lookbookList.get(itemIndex);
			obj.likesCnt = likeCount;
			adapter.notifyDataSetChanged();
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}

	@Override
	public void lookBookitemLikedFailed(String errorMsg) {
		// TODO Auto-generated method stub
		Toast.makeText(this.getActivity(), errorMsg, Toast.LENGTH_LONG).show();
	}

	@Override
	public void lookBookitemLikeTapped(int itemIndex) {
		// TODO Auto-generated method stub
		try {
			LookBookObject  obj = lookbookList.get(itemIndex);
			LookBookLikeDataHandler likeDataHandler = new LookBookLikeDataHandler(this, obj.id, itemIndex);
			likeDataHandler.likeItem();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
