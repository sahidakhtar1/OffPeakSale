package com.appsauthority.appwiz;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.appauthority.appwiz.interfaces.VoucherDeleteCaller;
import com.appsauthority.appwiz.adapters.VouchersListAdapter;
import com.appsauthority.appwiz.custom.BaseActivity;
import com.appsauthority.appwiz.models.Retailer;
import com.appsauthority.appwiz.models.Voucher;
import com.appsauthority.appwiz.models.VoucherList;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.HTTPHandler;
import com.appsauthority.appwiz.utils.Helper;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.gson.Gson;
import com.offpeaksale.consumer.R;

public class VoucherActivity extends BaseActivity implements
		VoucherDeleteCaller {

	private Activity activity;
	private View viewLineHome, viewLineEShop, viewLineLoyalty,
			viewLineFeedback, viewLineVouchers;
	private TextView textViewHeader;
	private ListView listViewVoucher;
	private VouchersListAdapter adapter;
	private List<Voucher> voucherList = new ArrayList<Voucher>();
	private Context context;
	private AdView adView;
	private LinearLayout linAdMob;
	private Retailer retailer;
	private SharedPreferences spref;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.activity_voucher);
		activity = this;
		context = this;

		spref = PreferenceManager.getDefaultSharedPreferences(this);
		;
		listViewVoucher = (ListView) findViewById(R.id.lv_vouchers);

		retailer = Helper.getSharedHelper().reatiler;
		getVouchers();

		// sqliteHelper.openDataBase();
		// voucherList = sqliteHelper.getVouchers();
		// sqliteHelper.close();

		adapter = new VouchersListAdapter(context, R.layout.row_voucher,
				voucherList, this);

		Log.i(TAG, voucherList.size() + "");

		setViewLines();

		textViewHeader.setText("VOUCHERS");
		textViewHeader.setTypeface(Helper.getSharedHelper().boldFont);

		listViewVoucher.setAdapter(adapter);

		try {
			setHeaderTheme(activity, retailer.getRetailerTextColor(),
					retailer.getHeaderColor());
		} catch (Exception e) {
		}

		new AsyncGetPublisherId().execute();

	}

	public void getVouchers() {

		Gson gson = new Gson();
		VoucherList vouchres;
		String voucherListString = spref.getString(Constants.KEY_VOUCHERS, "");
		if (voucherListString.equals("")) {
			vouchres = new VoucherList();
		} else {
			vouchres = gson.fromJson(voucherListString, VoucherList.class);

		}
		voucherList = (ArrayList<Voucher>) vouchres.getVouchers();
		// voucherListString = gson.toJson(vouchres);
		// spref.edit().putString(Constants.KEY_VOUCHERS,
		// voucherListString).commit();
		// Voucher v= new Voucher();
		// v.setMsg("http://appwizlive.com/uploads/retailer/11/pnimages/deviceimg/pn_14053468485.Eshop1.jpg");
		// v.setType("Image");
		// vouchres.getVouchers().add(v);
		//
		// Voucher v2= new Voucher();
		// v2.setMsg("http://appwizlive.com/uploads/retailer/10/bgupload/edn_promotional_video3.mp4");
		// v2.setType("Video");
		// vouchres.getVouchers().add(v2);
		// vouchres.getVouchers().add(v2);
	}

	public void closePressed(int position) {
		int pos = position;// listViewVoucher.getPositionForView(v);

		voucherList.remove(pos);
		VoucherList vouchres = new VoucherList();
		vouchres.setVouchers(voucherList);
		Gson gson = new Gson();
		String voucherListString = gson.toJson(vouchres);
		spref.edit().putString(Constants.KEY_VOUCHERS, voucherListString)
				.commit();
		adapter.notifyDataSetChanged();

	}

	private void setViewLines() {
		viewLineHome = (View) findViewById(R.id.viewLineHome);
		viewLineEShop = (View) findViewById(R.id.viewLineEShop);
		viewLineLoyalty = (View) findViewById(R.id.viewLineLoyalty);
		viewLineFeedback = (View) findViewById(R.id.viewLineFeedback);
		viewLineVouchers = (View) findViewById(R.id.viewLineVouchers);
		textViewHeader = (TextView) findViewById(R.id.textViewHeader);
		viewLineHome.setVisibility(View.INVISIBLE);
		viewLineEShop.setVisibility(View.INVISIBLE);
		viewLineLoyalty.setVisibility(View.INVISIBLE);
		viewLineFeedback.setVisibility(View.INVISIBLE);
		viewLineVouchers.setVisibility(View.VISIBLE);
		try {
			viewLineVouchers.setBackgroundColor(Color.parseColor("#80"
					+ Helper.getSharedHelper().reatiler.getHeaderColor()));
		} catch (Exception e) {

		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (adView != null) {
			adView.resume();
		}
	}

	@Override
	public void onPause() {
		if (adView != null) {
			adView.pause();
		}
		super.onPause();
	}

	@Override
	public void onDestroy() {

		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}

	private final class AsyncGetPublisherId extends
			AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(Void... params) {

			JSONObject param = new JSONObject();
			try {
				param.put(Constants.PARAM_RETAILER_ID, Constants.RETAILER_ID);
				JSONObject jsonObject = HTTPHandler.defaultHandler().doPost(
						Constants.URL_GET_PUBLISHER_ID, param);

				return jsonObject.getString("publisherId");

			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}

		}

		@Override
		protected void onPostExecute(String id) {

			linAdMob = (LinearLayout) findViewById(R.id.linAdMob);

			adView = new AdView(context);
			adView.setAdSize(AdSize.BANNER);
			adView.setAdUnitId("ca-app-pub-8773946931527913/7091917786");

			linAdMob.addView(adView);

			TelephonyManager tm = (TelephonyManager) getBaseContext()
					.getSystemService(Context.TELEPHONY_SERVICE);

			String deviceid = tm.getDeviceId();

			AdRequest adRequest = new AdRequest.Builder()
					.addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
					.addTestDevice(deviceid).build();

			adView.loadAd(adRequest);
			adView.setAdListener(new AdListener() {
				@Override
				public void onAdLoaded() {
					// TODO Auto-generated method stub
					super.onAdLoaded();
					// Toast.makeText(VoucherActivity.this, "Ad loaded",
					// Toast.LENGTH_LONG).show();
				}

				@Override
				public void onAdFailedToLoad(int errorCode) {
					// TODO Auto-generated method stub
					super.onAdFailedToLoad(errorCode);
					// Toast.makeText(VoucherActivity.this,
					// "failed with error  "+errorCode,
					// Toast.LENGTH_LONG).show();
				}
			});

		}
	}

	@Override
	public void deleteVoucher(int position) {
		// TODO Auto-generated method stub
		closePressed(position);
	}

	@Override
	public void voucherClicked(int position) {
		// TODO Auto-generated method stub
		
	}

}
