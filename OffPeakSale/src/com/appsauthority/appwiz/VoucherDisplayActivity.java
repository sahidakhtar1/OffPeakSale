package com.appsauthority.appwiz;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.appauthority.appwiz.interfaces.ProductDetailCaller;
import com.appsauthority.appwiz.custom.BaseActivity;
import com.appsauthority.appwiz.models.Product;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.Helper;
import com.offpeaksale.consumer.R;

public class VoucherDisplayActivity extends BaseActivity implements ProductDetailCaller {

	private ImageView imageView;
	private VideoView videoView;
	private Intent intent;
	private String msg, type,pid;
	WebView webview;
	RelativeLayout fmVideoView;
	private Boolean isAppInForeGround = false;
	Boolean isLoading = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// super.onCreate(savedInstanceState);
		// setTheme(android.R.style.Theme_Translucent);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voucher_display);
		intent = getIntent();

		msg = intent.getStringExtra("msg");
		type = intent.getStringExtra("type");
		pid = intent.getStringExtra("pid");
		isAppInForeGround = intent.getBooleanExtra(
				Constants.KEY_IS_APP_RUNNING, false);

		imageView = (ImageView) findViewById(R.id.iv_voucher);
		videoView = (VideoView) findViewById(R.id.videoView);
		webview = (WebView) findViewById(R.id.gifWebview);
		fmVideoView = (RelativeLayout) findViewById(R.id.fmVideoView);
		webview.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (!isAppInForeGround) {
					Intent intent = new Intent(getApplicationContext(),
							SplashscreenActivity.class);
					if (pid != null) {
						intent.putExtra("pid", pid);
					}
					startActivity(intent);
					finish();
				}else if(pid != null){
					loadProductDetail(pid);
				}
				return true;
			}
		});

		

		if (type.equalsIgnoreCase("Image")) {
			String imageUrl = msg;

			if (imageUrl.endsWith(".gif")) {
				imageView.setVisibility(View.INVISIBLE);
				fmVideoView.setVisibility(View.GONE);
				webview.setVisibility(View.VISIBLE);
				loadGif();
			} else {
				imageView.setVisibility(View.VISIBLE);
				fmVideoView.setVisibility(View.GONE);
				webview.setVisibility(View.INVISIBLE);
				imageCacheloader.displayImage(msg,
						R.drawable.image_placeholder, imageView);
			}

			videoView.setVisibility(View.INVISIBLE);
		} else {

			videoView.setVideoPath(msg);
			MediaController mediaController = new MediaController(this);
			mediaController.setAnchorView(videoView);
			videoView.setMediaController(mediaController);
			videoView.requestFocus();
			videoView.seekTo(1);
			videoView.start();
			videoView.setVisibility(View.VISIBLE);
			imageView.setVisibility(View.INVISIBLE);
			webview.setVisibility(View.INVISIBLE);
			fmVideoView.setVisibility(View.VISIBLE);
			videoView.setBackgroundColor(Color.BLACK);
			videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					videoView.setBackgroundColor(Color.TRANSPARENT);
				}
			});
		}

//		if (!isAppInForeGround) {
			RelativeLayout voucherRL = (RelativeLayout) findViewById(R.id.voucherRL);
			voucherRL.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!isAppInForeGround) {
						Intent intent = new Intent(getApplicationContext(),
								SplashscreenActivity.class);
						if (pid != null) {
							intent.putExtra("pid", pid);
						}
						startActivity(intent);
						finish();
					}else if(pid != null){
						loadProductDetail(pid);
					}
					
				}
			});
//		}
		try {
			SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(this);;
			String themeColor = spref.getString(Constants.KEY_REATILER_THEME_COLOR, "");
			String textColor = spref.getString(Constants.KEY_RETAILER_TEXT_COLOR, "");
			String fontName = spref.getString(Constants.KEY_RETAILER_FONT, "");
			String boldFontPath = Helper.getSharedHelper()
					.getBoldFontPath(fontName);
			Typeface boldFont = Typeface.createFromAsset(
					getAssets(), boldFontPath);
			Button buttonDismiss = (Button) findViewById(R.id.buttonDismiss);
			
			buttonDismiss
					.setTextColor(Color.parseColor("#"
							+ textColor));

			buttonDismiss.setBackgroundDrawable(getGradientDrawable(themeColor));
			buttonDismiss.setTypeface(boldFont);

		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	void loadProductDetail(String productid){
		if (isLoading) {
			return;
		}
		isLoading = true;
		showLoadingDialog();
		ProductDetailHandler productDetailHandler = new ProductDetailHandler(productid, this);
		productDetailHandler.fetchProductDetails();
	}

	public void dismissPressed(View v) {

		finish();
	}

	public void openAppPressed(View v) {

		Intent intent = new Intent(getApplicationContext(),
				SplashscreenActivity.class);
		if (pid != null) {
			intent.putExtra("pid", pid);
		}
		startActivity(intent);
		finish();
	}

	void loadGif() {

		webview.setVisibility(View.VISIBLE);
		String htnlString = "<!DOCTYPE html><html><body style=\"background-color:transparent;margin: 0; padding: 0\"><img src=\""
				+ msg
				+ "\" alt=\"pageNo\" width=\"100%\" height=\"100%\"></body></html>";
		webview.loadDataWithBaseURL(null, htnlString, "text/html", "UTF-8",
				null);
		webview.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
	}

	class ForegroundCheckTask extends AsyncTask<Context, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Context... params) {
			final Context context = params[0].getApplicationContext();
			return isAppOnForeground(context);
		}

		private boolean isAppOnForeground(Context context) {
			ActivityManager activityManager = (ActivityManager) context
					.getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> appProcesses = activityManager
					.getRunningAppProcesses();
			if (appProcesses == null) {
				return false;
			}

			final String packageName = context.getPackageName();
			for (RunningAppProcessInfo appProcess : appProcesses) {
				if (appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND
						&& appProcess.processName.equals(packageName)) {
					return true;
				}
			}
			return false;
		}
	}

	@Override
	public void productDetailLoaded(Product p) {
		isLoading = false;
		dismissLoadingDialog();
		// TODO Auto-generated method stub
		if (p != null) {
			Intent intent = new Intent(context, EShopDetailActivity.class);
			intent.putExtra("product", p);
			startActivity(intent);
			finish();
		}
		
	}
	@Override
	public void errorOccured() {
		// TODO Auto-generated method stub
		isLoading = false;
		dismissLoadingDialog();
	}
}
