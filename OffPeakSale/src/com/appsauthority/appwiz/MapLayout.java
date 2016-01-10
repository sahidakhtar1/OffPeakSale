package com.appsauthority.appwiz;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.appsauthority.appwiz.custom.MyLocation;
import com.appsauthority.appwiz.custom.MyLocation.LocationResult;
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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MapLayout extends RelativeLayout implements
		OnInfoWindowClickListener {

	Context context;
	Activity activity;
	private GoogleMap map;
	private ArrayList<RetailerStores> stores;
	private LatLngBounds.Builder bld;

	public String outletName;

	public String storeAddress;

	public String storeContact;

	public String latitude;

	public String longitude;
	public String merchantName;

	Marker nearestStoreMarker;
	
	Marker currentStoreMarker;
	 Timer timer;
     TimerTask timerTask;
     MyLocation   myLocation = new MyLocation();
 	final Handler handlerlocation = new Handler();
 	Marker currentLocation;
	
	public MapLayout(Context context, Activity activity,
			ArrayList<RetailerStores> stores) {
		super(context);
		this.context = context;
		this.activity = activity;
		this.stores = stores;
		// init();
	}

	public void init() {

		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.map_view, null, false);
		this.addView(view);

		map = ((MapFragment) activity.getFragmentManager().findFragmentById(
				R.id.map_info)).getMap();
//		 map.setMyLocationEnabled(true);
//		 map.getUiSettings().setMyLocationButtonEnabled(false);
		if (bld == null) {
			bld = new LatLngBounds.Builder();
		}
		try {
			currentLocation = map.addMarker(new MarkerOptions()
					.position(new LatLng(Constants.LAT, Constants.LNG))
					.title("Current Location")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.user_pin)));
			bld.include(new LatLng(Constants.LAT, Constants.LNG));

			if (latitude != null && longitude != null) {
				addMarker(Double.parseDouble(latitude),
						Double.parseDouble(longitude), merchantName,
						storeAddress, storeContact, map, true);
				bld.include(new LatLng(Double.parseDouble(latitude),Double.parseDouble(longitude)));
			}
			for (int i = 0; i < stores.size(); i++) {
				if (stores.get(i).getLatitude().equalsIgnoreCase("")
						|| stores.get(i).getLongitude().equalsIgnoreCase("")) {

				} else {
					addMarker(Double.parseDouble(stores.get(i).getLatitude()),
							Double.parseDouble(stores.get(i).getLongitude()),
							merchantName, stores.get(i).getStoreAddress(),
							stores.get(i).getStoreContact(), map, false);
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
			map.animateCamera(CameraUpdateFactory.newLatLngBounds(bld.build(),
					200));
//			if (currentStoreMarker != null) {
//				currentStoreMarker.showInfoWindow();
//			}
			map.getUiSettings().setZoomControlsEnabled(false);

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
					if (stores != null) {
						map.moveCamera(CameraUpdateFactory.newLatLngBounds(
								bld.build(), 200));
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
					tv.setTypeface(Helper.getSharedHelper().boldFont);
					try {
						TextView snippet = (TextView) popup.findViewById(R.id.snippet);

						snippet.setTypeface(Helper.getSharedHelper().normalFont);
//						SpannableString content = new SpannableString(marker
//								.getSnippet());
//					
//						content.setSpan(new UnderlineSpan(), 0,
//								content.length(), 0);

						snippet.setText(marker.getSnippet());

//						Linkify.addLinks(
//								((TextView) popup.findViewById(R.id.snippet)),
//								Linkify.ALL);
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

	@Override
	public void onInfoWindowClick(Marker marker) {
		// TODO Auto-generated method stub
		marker.hideInfoWindow();
		try {
			if (marker.getSnippet().length() > 0) {
				Intent intent = new Intent(Intent.ACTION_CALL);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setData(Uri.parse("tel:" + marker.getSnippet()));
				activity.startActivity(intent);
			}
		} catch (Exception e) {
		}

	}

	private void addMarker(double lat, double lng, String resturantName,
			String branchName, String contact, GoogleMap map, Boolean selected) {
		MarkerOptions markerOption = new MarkerOptions()
				.position(new LatLng(lat, lng))
				.title(resturantName )
				.snippet(branchName + "\r\n\n"+contact)
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.shoplocation));
		//
		Marker marker = map.addMarker(markerOption);
		if (selected) {
//			currentStoreMarker = marker;
			marker.showInfoWindow();
		}

	}
	
	  public void startLocationUpdate() {
	 		//set a new Timer
//	 		timer = new Timer();
//	 		//initialize the TimerTask's job
//	 		
//	 		//schedule the timer, after the first 5000ms the TimerTask will run every 10000ms
//	 		timer.schedule(timerTask, 5000, 10000); //
		  if (myLocation.getLocation(context.getApplicationContext(),
					locationResult)) {

			}
	 	}
	     public void stopLocationUpdate() {
	 		//stop the timer, if it's not already null
	 		if (myLocation != null) {
	 			myLocation.cancelTimer();
	 		}
	 	}
//	     public void initializeTimerTask() {
//	 		
//	 		timerTask = new TimerTask() {
//	 			public void run() {
//	 				handlerlocation.post(new Runnable() {
//	 					public void run() {
//	 						
//	 					}
//	 				});
//	 			}
//	 		};
//	 	}
	     LocationResult locationResult = new LocationResult() {
				@Override
				public void gotLocation(Location location) {
					if (location != null) {
						Constants.LAT = location.getLatitude();
						Constants.LNG = location.getLongitude();
						currentLocation.setPosition(new LatLng(Constants.LAT, Constants.LNG));
						currentLocation.hideInfoWindow();
//						Toast.makeText(context, " fresh Location fetched "+Constants.LAT+" "+Constants.LNG, Toast.LENGTH_LONG).show();

					} else {

						Criteria criteria = new Criteria();
						LocationManager locMan = (LocationManager) context
								.getSystemService(Context.LOCATION_SERVICE);
						String curLoc = locMan.getBestProvider(criteria, true);
						location = locMan.getLastKnownLocation(curLoc);
						if (location != null) { 
							Constants.LAT = location.getLatitude();
							Constants.LNG = location.getLongitude();
							
							currentLocation.setPosition(new LatLng(Constants.LAT, Constants.LNG));
							currentLocation.hideInfoWindow();
//							Toast.makeText(context, "Old Location fetched"+Constants.LAT+" "+Constants.LNG, Toast.LENGTH_LONG).show();
						}else{
//							Toast.makeText(context, "Location not fetched", Toast.LENGTH_LONG).show();

						}

					}
			}
	     };
}
