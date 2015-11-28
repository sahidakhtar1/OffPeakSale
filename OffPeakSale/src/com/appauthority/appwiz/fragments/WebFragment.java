package com.appauthority.appwiz.fragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.appsauthority.appwiz.models.Retailer;
import com.appsauthority.appwiz.utils.Helper;
import com.offpeaksale.consumer.R;

public class WebFragment extends Fragment {
	public String url;
	private Retailer retailer;
	WebView w;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_web, container,
				false);
		initialize(rootView);
		return rootView;
	}

	void initialize(View view) {
		retailer = Helper.getSharedHelper().reatiler;

		RelativeLayout header = (RelativeLayout) view.findViewById(R.id.header);
		LinearLayout bottonView = (LinearLayout) view
				.findViewById(R.id.bottonView);
		header.setVisibility(View.GONE);
		bottonView.setVisibility(View.GONE);
		View lineBot = (View) view.findViewById(R.id.lineBot);
		View lineTop = (View) view.findViewById(R.id.lineTop);
		Button btn_agree = (Button) view.findViewById(R.id.btn_agree);

		try {

			lineTop.setBackgroundColor(Color.parseColor("#"
					+ retailer.getHeaderColor()));
			lineBot.setBackgroundColor(Color.parseColor("#"
					+ retailer.getHeaderColor()));
			btn_agree.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));
			btn_agree.setBackgroundDrawable(Helper.getSharedHelper()
					.getGradientDrawable(retailer.getHeaderColor()));

		} catch (Exception e) {

		}

		// url = "http://appwizlive.com/";

		w = (WebView) view.findViewById(R.id.webView);
		w.setWebViewClient(new WebViewClient());
		w.getSettings().setJavaScriptEnabled(true);
		if (!url.startsWith("http://")) {
			url = "http://" + url;
		}
		// url =
		// "https://www.google.com/calendar/embed?src=appwizlive@gmail.com&mode=Agenda";
		 w.loadUrl(url);
//		String calenderText = "<html><body><iframe src=\"https://www.google.com/calendar/embed?src=sahidakhtar1@gmail.com&ctz=Asia/Calcutta\" style=\"border: 0\" width=\"800\" height=\"600\" frameborder=\"0\" scrolling=\"yes\"></iframe></body></html>";
//		w.loadData(calenderText, "text/html", "UTF-8");
//		Map<String, String> extraHeaders = new HashMap<String, String>();
//		extraHeaders.put("X-Frame-Options", "SAMEORIGIN");
//		w.loadUrl(calenderText, extraHeaders);
	}

	public void agreePressed(View v) {

	}

	public Boolean canGoback() {
		Boolean canGoBack = w.canGoBack();
		if (canGoBack) {
			w.goBack();
		}
		return canGoBack;
	}
}
