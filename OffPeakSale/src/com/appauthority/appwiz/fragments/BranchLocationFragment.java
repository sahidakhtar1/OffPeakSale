package com.appauthority.appwiz.fragments;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appsauthority.appwiz.custom.MyLocation;
import com.appsauthority.appwiz.custom.MyLocation.LocationResult;
import com.appsauthority.appwiz.models.Retailer;
import com.appsauthority.appwiz.models.RetailerStores;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.Helper;
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

public class BranchLocationFragment extends Fragment implements
		OnInfoWindowClickListener {
	Marker userLocation;

	private MyLocation myLocation;
	private Activity activity;
	private GoogleMap map;
	private ArrayList<RetailerStores> stores = new ArrayList<RetailerStores>();
	// private TextView textViewHeader;
	private Context context;
	private Retailer retailer;
	private LatLngBounds.Builder bld;
	LocationManager locationManager;

	static Boolean ishownAlert = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_branchlocation,
				container, false);
		initilizeView(rootView);
		return rootView;
	}

	void initilizeView(View view) {
		activity = this.getActivity();
		context = this.getActivity();
		map = ((MapFragment) getFragmentManager().findFragmentById(
				R.id.map_info)).getMap();
		if (bld == null) {
			bld = new LatLngBounds.Builder();
		}

		// double prevLat = Constants.LAT;
		// double prevLng = Constants.LNG;

		locationManager = (LocationManager) activity
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

			// try {
			//
			// userLocation = map.addMarker(new MarkerOptions()
			// .position(new LatLng(Constants.LAT, Constants.LNG))
			// .title("Current Location")
			// .icon(BitmapDescriptorFactory
			// .fromResource(R.drawable.user_pin)));
			// bld.include(new LatLng(Constants.LAT, Constants.LNG));
			//
			// // BitmapDescriptorFactory
			// // .fromResource(R.drawable.customerlocation)
			// } catch (Exception e) {
			// // TODO: handle exception
			// // Toast.makeText(this, "1. Error in user location ",
			// // Toast.LENGTH_LONG).show();
			// Log.i("1. Error in user location", e.toString());
			// userLocation = map
			// .addMarker(new MarkerOptions()
			// .position(new LatLng(prevLat, prevLng))
			// .title("Current Location")
			// .icon(BitmapDescriptorFactory
			// .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
			// bld.include(new LatLng(Constants.LAT, Constants.LNG));
			// }
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

		// sqliteHelper = new SQLiteHelper(getApplicationContext());

		// sqliteHelper.openDataBase();
		retailer = Helper.getSharedHelper().reatiler;
		// sqliteHelper.close();

		// sqliteHelper.openDataBase();
		// stores = (ArrayList<RetailerStores>) retailer.getRetailerStores();
		// sqliteHelper.close();

		stores = Helper.getSharedHelper().stores;

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
					// map.animateCamera(CameraUpdateFactory.newLatLngBounds(
					// bld.build(), 70));

					map.setOnCameraChangeListener(null);
				}
			});
			map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
				@Override
				public void onMapLoaded() {
					if (stores != null || userLocation != null) {
						map.moveCamera(CameraUpdateFactory.newLatLngBounds(
								bld.build(), 70));
					}

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
		} catch (Exception e) {
		}
	}

	void getCurrentLocation() {
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
					LocationManager locMan = (LocationManager) activity
							.getSystemService(Context.LOCATION_SERVICE);
					String curLoc = locMan.getBestProvider(criteria, true);
					Location tlocation = locMan.getLastKnownLocation(curLoc);
					if (tlocation != null) {
						Constants.LAT = tlocation.getLatitude();
						Constants.LNG = tlocation.getLongitude();
					}

				}
				// locationManager.removeUpdates(locationResult);
				if (userLocation != null) {
					try {
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {

								userLocation.remove();
								userLocation = map.addMarker(new MarkerOptions()
										.position(
												new LatLng(Constants.LAT,
														Constants.LNG))
										.title("Current Location")
										.icon(BitmapDescriptorFactory
												.fromResource(R.drawable.user_pin)));
								bld.include(new LatLng(Constants.LAT,
										Constants.LNG));
								map.animateCamera(CameraUpdateFactory
										.newLatLngBounds(bld.build(), 70));
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
						map.animateCamera(CameraUpdateFactory.newLatLngBounds(
								bld.build(), 70));
					}
				} else {
					try {
						activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {

								userLocation = map.addMarker(new MarkerOptions()
										.position(
												new LatLng(Constants.LAT,
														Constants.LNG))
										.title("Current Location")
										.icon(BitmapDescriptorFactory
												.fromResource(R.drawable.user_pin)));
								bld.include(new LatLng(Constants.LAT,
										Constants.LNG));
								map.animateCamera(CameraUpdateFactory
										.newLatLngBounds(bld.build(), 70));
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
						map.animateCamera(CameraUpdateFactory.newLatLngBounds(
								bld.build(), 70));
					}
				}
			}
		};

		myLocation
				.getLocation(activity.getApplicationContext(), locationResult);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getCurrentLocation();
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

	@SuppressWarnings("deprecation")
	public void setHeaderTheme(Activity activity) {

		// TextView textViewHeader = (TextView) activity
		// .findViewById(R.id.textViewHeader);
		// View header = (View) activity.findViewById(R.id.header);
		//
		// textViewHeader.setTextColor(Color.parseColor("#"
		// + retailer.getRetailerTextColor()));
		// textViewHeader.setTypeface(Helper.getSharedHelper().boldFont);
		//
		// GradientDrawable gd = new GradientDrawable(
		// GradientDrawable.Orientation.TOP_BOTTOM, new int[] {
		// Color.parseColor("#AA" + retailer.getHeaderColor()),
		// Color.parseColor("#" + retailer.getHeaderColor()) });
		// gd.setCornerRadius(0f);
		// ColorDrawable cd = new ColorDrawable(Color.parseColor("#"
		// + retailer.getHeaderColor()));
		// header.setBackgroundDrawable(cd);

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

}
