package com.appsauthority.appwiz.custom;

import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.offpeaksale.consumer.R;

public class PopupAdapter implements InfoWindowAdapter {
	LayoutInflater inflater = null;

	public PopupAdapter(LayoutInflater inflater) {
		this.inflater = inflater;
	}

	@Override
	public View getInfoWindow(Marker marker) {
		return (null);
	}

	@Override
	public View getInfoContents(Marker marker) {
		View popup = inflater.inflate(R.layout.popup, null);

		TextView tv = (TextView) popup.findViewById(R.id.title);
		tv.setText(marker.getTitle());
		try {
			tv = (TextView) popup.findViewById(R.id.snippet);

			SpannableString content = new SpannableString(marker.getSnippet());
			content.setSpan(new UnderlineSpan(), 0, content.length(), 0);

			tv.setText(content);

			Linkify.addLinks(((TextView) popup.findViewById(R.id.snippet)),
					Linkify.ALL);
		} catch (Exception e) {

		}
		return (popup);
	}
}
