package com.appsauthority.appwiz;

import java.util.ArrayList;

import com.appsauthority.appwiz.models.RetailerStores;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.Helper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.annotations.SerializedName;
import com.offpeaksale.consumer.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

	public MapLayout(Context context, Activity activity,
			ArrayList<RetailerStores> stores) {
		super(context);
		this.context = context;
		this.activity = activity;
		this.stores = stores;
//		init();
	}

	public void init() {

		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.map_view, null, false);
		this.addView(view);

		map = ((MapFragment) activity.getFragmentManager().findFragmentById(
				R.id.map_info)).getMap();
		if (bld == null) {
			bld = new LatLngBounds.Builder();
		}
		try {
			map.addMarker(new MarkerOptions()
					.position(new LatLng(Constants.LAT, Constants.LNG))
					.title("Current Location")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.user_pin)));
			bld.include(new LatLng(Constants.LAT, Constants.LNG));
			

			if(latitude!=null && longitude!=null)
			{
				addMarker(Double.parseDouble(latitude),
					Double.parseDouble(longitude), merchantName, storeAddress,
					storeContact, map, true);
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
					70));

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
				.title(resturantName + "\r\n\n" + branchName + "\r\n\r\n"
						+ "Contact")
				.snippet(contact)
				.icon(BitmapDescriptorFactory
						.fromResource(R.drawable.shoplocation));
		//
		Marker marker = map.addMarker(markerOption);
		if (selected) {
			marker.showInfoWindow();
		}

	}
}
