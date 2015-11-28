package com.appsauthority.appwiz;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsauthority.appwiz.custom.MyLocation;
import com.appsauthority.appwiz.custom.MyLocation.LocationResult;
import com.appsauthority.appwiz.models.Retailer;
import com.appsauthority.appwiz.models.RetailerStores;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.Helper;
import com.appsauthority.appwiz.utils.SQLiteHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.offpeaksale.consumer.R;

public class BranchLocationActivity extends FragmentActivity implements
		OnInfoWindowClickListener {

	// private String TAG = getClass().getSimpleName();
	private SQLiteHelper sqliteHelper = null;

	Marker userLocation;

	private MyLocation myLocation;
	private Activity activity;
	private GoogleMap map;
	private ArrayList<RetailerStores> stores = new ArrayList<RetailerStores>();
	private TextView textViewHeader;
	private Context context;
	private Retailer retailer;
	private RelativeLayout rootHeader;
	private LatLngBounds.Builder bld;

	private View viewLineHome, viewLineEShop, viewLineLoyalty,
			viewLineFeedback, viewLineVouchers;

	static Boolean ishownAlert = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.activity_branchlocation);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		textViewHeader = (TextView) findViewById(R.id.textViewHeader);
		activity = this;
		context = this;
		getActionBar().hide();
		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map_info)).getMap();
		if (bld == null) {
			bld = new LatLngBounds.Builder();
		}
		
//		double prevLat = Constants.LAT;
//		double prevLng = Constants.LNG;

		LocationManager locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		
		myLocation = new MyLocation();
		Boolean isLocationOn;
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			// Do what you need if enabled...
			isLocationOn = true;
		} else if (locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			// Do what you need if enabled...
			isLocationOn = true;
		} else {
			isLocationOn = false;
		}
		
		if (isLocationOn) {

//			try {
//
//				userLocation = map.addMarker(new MarkerOptions()
//						.position(new LatLng(Constants.LAT, Constants.LNG))
//						.title("Current Location")
//						.icon(BitmapDescriptorFactory
//								.fromResource(R.drawable.user_pin)));
//				bld.include(new LatLng(Constants.LAT, Constants.LNG));
//
//				// BitmapDescriptorFactory
//				// .fromResource(R.drawable.customerlocation)
//			} catch (Exception e) {
//				// TODO: handle exception
//				// Toast.makeText(this, "1. Error in user location ",
//				// Toast.LENGTH_LONG).show();
//				Log.i("1. Error in user location", e.toString());
//				userLocation = map
//						.addMarker(new MarkerOptions()
//								.position(new LatLng(prevLat, prevLng))
//								.title("Current Location")
//								.icon(BitmapDescriptorFactory
//										.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
//				bld.include(new LatLng(Constants.LAT, Constants.LNG));
//			}
		} else {

			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("Information")
					.setMessage("Enable location services")
					.setCancelable(true)
					.setNegativeButton("Cancel",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dialog.cancel();
								}
							})
					.setPositiveButton("Enable",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									try {
										Intent gpsOptionsIntent = new Intent(
												android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
										startActivity(gpsOptionsIntent);
									} catch (Exception e) {
										// e.printStackTrace();
									}

								}
							});
			builder.show();

			ishownAlert = true;
		}
		ImageView imageViewOverflow = (ImageView) findViewById(R.id.imageViewOverflow);
		imageViewOverflow.setImageResource(R.drawable.backbutton);
		imageViewOverflow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();

			}
		});
		// sqliteHelper = new SQLiteHelper(getApplicationContext());

		// sqliteHelper.openDataBase();
		retailer = Helper.getSharedHelper().reatiler;
		// sqliteHelper.close();

		try {

			setHeaderTheme(activity);
		} catch (Exception e) {
		}

		// sqliteHelper.openDataBase();
		// stores = (ArrayList<RetailerStores>) retailer.getRetailerStores();
		// sqliteHelper.close();

		stores = Helper.getSharedHelper().stores;

		textViewHeader.setText(retailer.getRetailerName());

		try {
			for (int i = 0; i < stores.size(); i++) {
				if (stores.get(i).getLatitude().equalsIgnoreCase("")
						|| stores.get(i).getLongitude().equalsIgnoreCase("")) {

				} else {
					addMarker(Double.parseDouble(stores.get(i).getLatitude()),
							Double.parseDouble(stores.get(i).getLongitude()),
							stores.get(i).getStoreAddress(), stores.get(i)
									.getStoreContact(), map);
				}

			}

			ArrayList<LatLng> list = new ArrayList<LatLng>();

			for (int i = 0; i < stores.size(); i++) {
				if (stores.get(i).getLatitude().equalsIgnoreCase("")
						|| stores.get(i).getLongitude().equalsIgnoreCase("")) {

				} else {
					list.add(new LatLng(Double.parseDouble(stores.get(i)
							.getLatitude()), Double.parseDouble(stores.get(i)
							.getLongitude())));
				}

			}

			for (LatLng item : list) {
				bld.include(item);
			}

		} catch (Exception e) {
			// e.printStackTrace();
		}

		// map.setInfoWindowAdapter(new PopupAdapter(getLayoutInflater()));
		try {
			map.setOnCameraChangeListener(new OnCameraChangeListener() {

				@Override
				public void onCameraChange(CameraPosition arg0) {
					map.animateCamera(CameraUpdateFactory.newLatLngBounds(
							bld.build(), 70));

					map.setOnCameraChangeListener(null);
				}
			});
			map.setInfoWindowAdapter(new InfoWindowAdapter() {

				@Override
				public View getInfoWindow(Marker marker) {
					// View vMapInfo = ((Activity)
					// context).getLayoutInflater().inflate(R.layout.popup,
					// null);
					View popup = ((Activity) context).getLayoutInflater()
							.inflate(R.layout.popup, null);

					TextView tv = (TextView) popup.findViewById(R.id.title);
					tv.setText(marker.getTitle());
					try {
						tv = (TextView) popup.findViewById(R.id.snippet);

						SpannableString content = new SpannableString(marker
								.getSnippet());
						content.setSpan(new UnderlineSpan(), 0,
								content.length(), 0);

						tv.setText(content);

						Linkify.addLinks(
								((TextView) popup.findViewById(R.id.snippet)),
								Linkify.ALL);
					} catch (Exception e) {

					}
					return (popup);
				}

				@Override
				public View getInfoContents(Marker arg0) {
					// View v =
					// getLayoutInflater().inflate(R.layout.map_info_layout,
					// null);
					return null;

				}
			});
			map.setOnInfoWindowClickListener(this);
			setViewLines();
		} catch (Exception e) {
		}

	}

	void getCurrentLocation(){
		if (bld == null) {
			bld = new LatLngBounds.Builder();
		}
		LocationResult locationResult = new LocationResult() {
			@Override
			public void gotLocation(Location location) {

				if (location != null) {

					Constants.LAT = location.getLatitude();
					Constants.LNG = location.getLongitude();
				} else {

					Criteria criteria = new Criteria();
					LocationManager locMan = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
					String curLoc = locMan.getBestProvider(criteria, true);
					Location tlocation = locMan.getLastKnownLocation(curLoc);
					if (tlocation != null) {
						Constants.LAT = tlocation.getLatitude();
						Constants.LNG = tlocation.getLongitude();
					}

				}
				if (userLocation != null) {
					try {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								userLocation.remove();
								userLocation = map
										.addMarker(new MarkerOptions()
												.position(
														new LatLng(
																Constants.LAT,
																Constants.LNG))
												.title("Current Location")
												.icon(BitmapDescriptorFactory
														.fromResource(R.drawable.user_pin)));
								bld.include(new LatLng(Constants.LAT, Constants.LNG));
								map.animateCamera(CameraUpdateFactory.newLatLngBounds(
										bld.build(), 70));
							}
						});

					} catch (Exception e) {
						// TODO: handle exception
						// Toast.makeText(BranchLocationActivity.this,
						// "2. Error in user location",
						// Toast.LENGTH_LONG).show();
						Log.i("2. Error in user location", e.toString());
						userLocation = map
								.addMarker(new MarkerOptions()
										.position(
												new LatLng(Constants.LAT,
														Constants.LNG))
										.title("Current Location")
										.icon(BitmapDescriptorFactory
												.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
						bld.include(new LatLng(Constants.LAT, Constants.LNG));
					}
				} else {
					try {
						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								userLocation = map
										.addMarker(new MarkerOptions()
												.position(
														new LatLng(
																Constants.LAT,
																Constants.LNG))
												.title("Current Location")
												.icon(BitmapDescriptorFactory
														.fromResource(R.drawable.user_pin)));
								bld.include(new LatLng(Constants.LAT, Constants.LNG));
								map.animateCamera(CameraUpdateFactory.newLatLngBounds(
										bld.build(), 70));
							}
						});

					} catch (Exception e) {
						// TODO: handle exception
						// System.out.println(e.getLocalizedMessage());
						// Toast.makeText(BranchLocationActivity.this,
						// "3. Error in user location",
						// Toast.LENGTH_LONG).show();
						Log.i("3. Error in user location", e.toString());
						userLocation = map
								.addMarker(new MarkerOptions()
										.position(
												new LatLng(Constants.LAT,
														Constants.LNG))
										.title("Current Location")
										.icon(BitmapDescriptorFactory
												.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
						bld.include(new LatLng(Constants.LAT, Constants.LNG));
					}
				}
			}
		};
		
		myLocation.getLocation(getApplicationContext(), locationResult);
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getCurrentLocation();
	}

	private void setViewLines() {
		viewLineHome = (View) findViewById(R.id.viewLineHome);
		viewLineEShop = (View) findViewById(R.id.viewLineEShop);
		viewLineLoyalty = (View) findViewById(R.id.viewLineLoyalty);
		viewLineFeedback = (View) findViewById(R.id.viewLineFeedback);
		viewLineVouchers = (View) findViewById(R.id.viewLineVouchers);
		viewLineHome.setVisibility(View.VISIBLE);
		viewLineEShop.setVisibility(View.INVISIBLE);
		viewLineLoyalty.setVisibility(View.INVISIBLE);
		viewLineFeedback.setVisibility(View.INVISIBLE);
		viewLineVouchers.setVisibility(View.INVISIBLE);
		try {
			viewLineHome.setBackgroundColor(Color.parseColor("#80"
					+ Helper.getSharedHelper().reatiler.getHeaderColor()));
		} catch (Exception e) {

		}
	}

	private void addMarker(double lat, double lng, String branchName,
			String contact, GoogleMap map) {
		map.addMarker(new MarkerOptions()
				.position(new LatLng(lat, lng))
				.title(retailer.getRetailerName() + "\r\n\n" + branchName
						+ "\r\n\r\n" + "Contact")
				.snippet(contact)
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.shoplocation)/*
															 * BitmapDescriptorFactory
															 * .defaultMarker(
															 * BitmapDescriptorFactory
															 * .HUE_RED)
															 */));

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

	@SuppressWarnings("deprecation")
	public void setHeaderTheme(Activity activity) {

		TextView textViewHeader = (TextView) activity
				.findViewById(R.id.textViewHeader);
		View header = (View) activity.findViewById(R.id.header);

		textViewHeader.setTextColor(Color.parseColor("#"
				+ retailer.getRetailerTextColor()));
		textViewHeader.setTypeface(Helper.getSharedHelper().boldFont);

		GradientDrawable gd = new GradientDrawable(
				GradientDrawable.Orientation.TOP_BOTTOM, new int[] {
						Color.parseColor("#AA" + retailer.getHeaderColor()),
						Color.parseColor("#" + retailer.getHeaderColor()) });
		gd.setCornerRadius(0f);
		ColorDrawable cd = new ColorDrawable(Color.parseColor("#"
				+ retailer.getHeaderColor()));
		header.setBackgroundDrawable(cd);

		// header.setBackgroundDrawable(gd);

	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		marker.hideInfoWindow();
		try {
			if (marker.getSnippet().length() > 0) {
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setData(Uri.parse("tel:" + marker.getSnippet()));
				startActivity(intent);
			}
		} catch (Exception e) {
		}
	}

	public void homePressed(View v) {
//		Intent intent = new Intent(this, MainActivity.class);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		startActivity(intent);
	}

	public void eShopPressed(View v) {
//		Intent intent = new Intent(this, EShopFragmentActivity.class);
//		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//		startActivity(intent);
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
		Intent intent = new Intent(this, VoucherActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

}
