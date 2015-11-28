package com.appauthority.appwiz.fragments;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appauthority.appwiz.interfaces.AppThemeUpdateListener;
import com.appsauthority.appwiz.BranchLocationActivity;
import com.appsauthority.appwiz.CurrencyConversionHandler;
import com.appsauthority.appwiz.EShopDetailActivity;
import com.appsauthority.appwiz.adapters.BilBoardAdapter;
import com.appsauthority.appwiz.custom.BannerLayout;
import com.appsauthority.appwiz.models.MediaObject;
import com.appsauthority.appwiz.models.Product;
import com.appsauthority.appwiz.models.Retailer;
import com.appsauthority.appwiz.models.RetailerInfoResponse;
import com.appsauthority.appwiz.models.RetailerStores;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.HTTPHandler;
import com.appsauthority.appwiz.utils.Helper;
import com.appsauthority.appwiz.utils.HorizontalListView;
import com.appsauthority.appwiz.utils.ImageCacheLoader;
import com.appsauthority.appwiz.utils.Utils;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.gson.Gson;
import com.offpeaksale.consumer.R;

public class HomeFragment extends Fragment implements OnInitializedListener {

	private Activity activity;
	private Retailer retailer;
	private Button buttonLocateUs;
	private View rootView;
	private RelativeLayout rel;
	private SharedPreferences spref;
	String localPath;
	static Boolean isFirstTime = true;
	String currentVideoURL1 = "";
	String currentVideoURL2 = "";
	Handler handler = new Handler();
	// YouTubePlayerView youtubeplayerview1, youtubeplayerview2;
	int pageCount = 1;

	int visiblePage = 1;
	Runnable r;
	public static final String API_KEY = "AIzaSyD4MwcyOuotA-8_GIXKIeHHlqCKAYLiT4E";
	public static final String VIDEO_ID = "C5pYRoGP520";

	protected Context context;

	private ImageCacheLoader imageCacheloader;

	HorizontalListView homeHorizontalListLayout;
	HorizontalScrollView homeHorizontalListLayout2;
	BilBoardAdapter mCategoryListAdapter;
	List<Product> billBoardItem, billBoardItem2;
	RelativeLayout rlHomeBanner;
	Boolean isHomeBannerFrameset = false;
	LinearLayout llSecondaryBillBoard;

	int billBoardHeight2 = 0;
	List<MediaObject> mediaViewList;
	public AppThemeUpdateListener themeUpdater;

	public HomeFragment(AppThemeUpdateListener caller) {
		this.themeUpdater = caller;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_main, container,
				false);
		initilizeView(rootView);
		return rootView;
	}

	int calculateHeight(int width) {
		int height = 0;
		height = (int) (width / 1.9f);
		return height;
	}

	void initilizeView(View view) {
		activity = this.getActivity();
		context = this.getActivity();
		billBoardItem = new ArrayList<Product>();
		billBoardItem2 = new ArrayList<Product>();

		rlHomeBanner = (RelativeLayout) view.findViewById(R.id.rlHomeBanner);
		homeHorizontalListLayout = (HorizontalListView) view
				.findViewById(R.id.homeHorizontalListLayout);
		homeHorizontalListLayout2 = (HorizontalScrollView) view
				.findViewById(R.id.homeHorizontalListLayout2);
		llSecondaryBillBoard = (LinearLayout) view
				.findViewById(R.id.llSecondaryBillBoard);
		final ViewTreeObserver hvto = rlHomeBanner.getViewTreeObserver();

		hvto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				// TODO Auto-generated method stub
				if (!isHomeBannerFrameset) {
					isHomeBannerFrameset = true;
					int width = rlHomeBanner.getWidth();
					int rlHeight = rlHomeBanner.getHeight();
					int height = calculateHeight(width);
					rlHomeBanner.getLayoutParams().height = height;
					MediaObject media1 = new MediaObject();
					media1.fileType = "image";
					media1.filePath = "http://inceptionlive.com/uploads/retailer/2/bgupload/2_1427222238_home_banner_image.jpg";
					MediaObject media2 = new MediaObject();
					media2.fileType = "youtube";
					media2.filePath = /*
									 * "http://www.youtube.com/watch?v=2zNSgSzhBfM";
									 * //
									 */"https://www.youtube.com/watch?v=5rQuktgLgE0";
					mediaViewList = new ArrayList<MediaObject>();
					mediaViewList.add(media1);
					mediaViewList.add(media2);
					int rlHeight1 = rlHomeBanner.getHeight();
					BannerLayout imageBanner = new BannerLayout(context,
							retailer.home_imgArray, true, height);
					rlHomeBanner.addView(imageBanner);
					final ViewTreeObserver vto = homeHorizontalListLayout
							.getViewTreeObserver();
					vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

						@Override
						public void onGlobalLayout() {
							// TODO Auto-generated method stub
							if (mCategoryListAdapter.maxHeight == -1) {
								int billBoardHeight = homeHorizontalListLayout
										.getHeight();
								mCategoryListAdapter.maxHeight = billBoardHeight;
								homeHorizontalListLayout
										.setAdapter(mCategoryListAdapter);
								mCategoryListAdapter.notifyDataSetChanged();
							}

						}
					});

					final ViewTreeObserver vto2 = homeHorizontalListLayout2
							.getViewTreeObserver();
					vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

						@Override
						public void onGlobalLayout() {
							// TODO Auto-generated method stub
							if (billBoardHeight2 == 0) {
								billBoardHeight2 = homeHorizontalListLayout2
										.getHeight();
								populateSecondaryBillBoard();
							}

						}
					});

				}

			}
		});
		imageCacheloader = new ImageCacheLoader(activity);
		TextView tvPowerby = (TextView) view.findViewById(R.id.tvPowerby);
		// tvPowerby.setTextColor(Color.parseColor("#"
		// + Helper.getSharedHelper().reatiler.getRetailerTextColor()));

		spref = PreferenceManager.getDefaultSharedPreferences(activity);
		buttonLocateUs = (Button) view.findViewById(R.id.buttonLocateUs);
		buttonLocateUs.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				locateUsPressed(arg0);
			}
		});

		rootView = (View) view.findViewById(R.id.rootView);
		rel = (RelativeLayout) view.findViewById(R.id.rel);

		// youtubeplayerview1 = (YouTubePlayerView) firstPage
		// .findViewById(R.id.youtubeplayerview);
		// youtubeplayerview2 = (YouTubePlayerView) secondPage
		// .findViewById(R.id.youtubeplayerview);

		// webview.setClipBounds();
		// webview.setWebViewClient(new WebViewClient(){
		// public void onPageFinished(WebView view, String url) {
		//
		// //webview.setClipBounds();
		// //webview.setLayoutParams(new
		// LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
		// }
		// });

		Log.i("TIMMINGS", "MAIN SCREEN LOADED");
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		initilize();
		populateBillboardproducts();

		mCategoryListAdapter = new BilBoardAdapter(getActivity(), billBoardItem);

		homeHorizontalListLayout
				.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View view,
							int position, long arg3) {

						Product product = (Product) billBoardItem.get(position);

						Intent intent = new Intent(context,
								EShopDetailActivity.class);
						intent.putExtra("product", product);
						startActivity(intent);
					}
				});

		// homeHorizontalListLayout2.setSelection(4);

		LinearLayout llLogo = (LinearLayout) view.findViewById(R.id.llLogo);
		llLogo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri
						.parse(spref.getString(Constants.POWERED_BY, "http://smartcommerce.asia/")));
				startActivity(browserIntent);
			}
		});
		// llLogo.setBackgroundColor(Color.parseColor("#" + "f0"
		// + Helper.getSharedHelper().reatiler.getHeaderColor()));
		// homeHorizontalListLayout2.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
		
		
		checkAppUpdateStatus();

	}

	void checkAppUpdateStatus(){
		PackageInfo pInfo;
		try {
			pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
			String version = pInfo.versionName;
			if (!Helper.getSharedHelper().isAppUpdateAlertShown &&
					retailer.androidVersion != null &&!
					retailer.androidVersion.equalsIgnoreCase(version)  ) {
				new AlertDialog.Builder(this.getActivity())
			    .setTitle("")
			    .setMessage("New version is available in Play Store. Do You want to download it now? ")
			    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			            // continue with delete
			        	final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
			        	try {
			        	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
			        	} catch (android.content.ActivityNotFoundException anfe) {
			        		String appLink;
			        		if (retailer.googlePlayUrl != null) {
								appLink = retailer.googlePlayUrl;
							}else{
								appLink = "https://play.google.com/store/apps/details?id=" + appPackageName;
							}
			        	    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(appLink)));
			        	}
			        }
			     })
			    .setNegativeButton("No", new DialogInterface.OnClickListener() {
			        public void onClick(DialogInterface dialog, int which) { 
			            // do nothing
			        }
			     })
			    
			     .show();
				Helper.getSharedHelper().isAppUpdateAlertShown = true;
			}
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void populateSecondaryBillBoard() {
		try {

			llSecondaryBillBoard.removeAllViews();
			LayoutInflater inflater = (LayoutInflater) getActivity()
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			for (int i = 0; i < billBoardItem2.size(); i++) {
				View convertView = inflater.inflate(R.layout.bill_board_item,
						null);

				TextView name = (TextView) convertView
						.findViewById(R.id.tvProductName);
				ImageView imgProd = (ImageView) convertView
						.findViewById(R.id.imgProd);
				float pixels = TypedValue.applyDimension(
						TypedValue.COMPLEX_UNIT_DIP, 30, getActivity()
								.getResources().getDisplayMetrics());

				int availableHeight = (int) (billBoardHeight2 - pixels);
				int imageWidth = Helper.getSharedHelper().getWidthforHeight(
						availableHeight);
				imgProd.getLayoutParams().width = (int) imageWidth;
				name.getLayoutParams().width = (int) imageWidth;

				Product product = billBoardItem2.get(i);
				imgProd.setTag(i);
				name.setText(product.getShortDescription());
				name.setTypeface(Helper.getSharedHelper().boldFont);
				imageCacheloader.displayImage(product.getImage(),
						R.drawable.image_placeholder, imgProd);
				imgProd.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						// Toast.makeText(getActivity(),
						// "2 -Selected Position " + arg0.getTag(),
						// Toast.LENGTH_LONG).show();
						int tag = (Integer) arg0.getTag();
						Product product = (Product) billBoardItem2.get(tag);

						Intent intent = new Intent(context,
								EShopDetailActivity.class);
						intent.putExtra("product", product);
						startActivity(intent);
					}
				});
				llSecondaryBillBoard.addView(convertView);
//				llSecondaryBillBoard.removeAllViews();
			}
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					homeHorizontalListLayout2
							.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
				}
			}, 500);
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	// int getWidthforHeight(int imgHeight) {
	//
	// int width = (int) (1.67f * imgHeight);
	//
	// return width;
	// }

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (isFirstTime) {

		} else {
			new AsyncWorker().execute();
		}
		isFirstTime = false;
		// copyToClipBoard();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	void initilize() {

		retailer = Helper.getSharedHelper().reatiler;
		if (retailer == null) {

			return;
		}
		spref.edit()
				.putString(Constants.KEY_REATILER_THEME_COLOR,
						retailer.getHeaderColor()).commit();
		spref.edit()
				.putString(Constants.KEY_RETAILER_TEXT_COLOR,
						retailer.getRetailerTextColor()).commit();
		spref.edit()
				.putString(Constants.KEY_RETAILER_FONT, retailer.getSiteFont())
				.commit();
		GradientDrawable g = new GradientDrawable(
				GradientDrawable.Orientation.TOP_BOTTOM, new int[] {
						Color.WHITE, Color.WHITE });
		g.setCornerRadius(10);
		rel.setBackgroundDrawable(g);

		try {

			// buttonLocateUs.setTextColor(Color.parseColor("#"
			// + retailer.getRetailerTextColor()));
			//
			// // buttonLocateUs.setBackgroundColor(Color.parseColor("#" +
			// retailer
			// // .getHeaderColor()));
			// buttonLocateUs.setBackgroundDrawable(Helper.getSharedHelper()
			// .getGradientDrawable(retailer.getHeaderColor()));

		} catch (Exception e) {
		}

		setFont();
	}

	void setFont() {
		try {
			buttonLocateUs.setTypeface(Helper.getSharedHelper().boldFont);
		} catch (Exception e) {

		}
	}

	@SuppressWarnings("deprecation")
	public static Drawable drawableFromUrl(String url) throws IOException {
		Bitmap x;

		HttpURLConnection connection = (HttpURLConnection) new URL(url)
				.openConnection();
		connection.connect();
		InputStream input = connection.getInputStream();

		x = BitmapFactory.decodeStream(input);
		return new BitmapDrawable(x);
	}

	public void locateUsPressed(View v) {

		Intent intent = new Intent(activity.getApplicationContext(),
				BranchLocationActivity.class);
		startActivity(intent);

		// Intent i = new Intent(this,
		// VoucherDisplayActivity.class);
		// i.putExtra(Constants.KEY_PUSH_MSG,
		// "https://www.youtube.com/watch?v=-MrpfTpnFn8");
		// i.putExtra(Constants.KEY_PUSH_TYPE, "Video");
		// startActivity(i);
	}

	private class AsyncWorker extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {

			if (Utils.hasNetworkConnection(activity.getApplicationContext())) {
				JSONObject obj = new JSONObject();
				Boolean status = false;
				try {
					obj.put(Constants.PARAM_RETAILER_ID, Constants.RETAILER_ID);

					JSONObject jsonObject = HTTPHandler.defaultHandler()
							.doPost(Constants.URL_GET_RETAILER_INFO, obj);
					String jsonString = jsonObject.toString();
					if (jsonString != null && !jsonString.equalsIgnoreCase("")) {
						spref.edit()
								.putString(Constants.KEY_GET_RETAILER_INFO,
										jsonString).commit();
						;
					} else {
						jsonString = spref.getString(
								Constants.KEY_GET_RETAILER_INFO, "");
						if (jsonString.equalsIgnoreCase("")) {

						} else {
							jsonObject = new JSONObject(jsonString);
						}

					}

					Gson gson = new Gson();
					RetailerInfoResponse res = gson.fromJson(
							jsonObject.toString(), RetailerInfoResponse.class);
					List<RetailerStores> stores = new ArrayList<RetailerStores>();

					if (res.getErrorCode().equals("1")) {

						retailer = res.getRetailerData();
						stores = res.getRetailerData().getRetailerStores();
						Helper.getSharedHelper().reatiler = retailer;
						Helper.getSharedHelper().isSSL = res.isSSL;
						Helper.getSharedHelper().termsConditions = retailer.termsConditions;
						CurrencyConversionHandler cch = new CurrencyConversionHandler();
						status = true;

					} else {
						status = false;
					}
					res = null;

					return status;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}

			} else {

				Boolean status = false;
				String jsonString = spref.getString(
						Constants.KEY_GET_RETAILER_INFO, "");
				if (jsonString.equalsIgnoreCase("")) {
					return status;
				}
				try {
					JSONObject jsonObject = new JSONObject(jsonString);
					Gson gson = new Gson();
					RetailerInfoResponse res = gson.fromJson(
							jsonObject.toString(), RetailerInfoResponse.class);
					List<RetailerStores> stores = new ArrayList<RetailerStores>();

					if (res.getErrorCode().equals("1")) {

						retailer = res.getRetailerData();
						stores = res.getRetailerData().getRetailerStores();
						Helper.getSharedHelper().stores = (ArrayList<RetailerStores>) stores;
						Helper.getSharedHelper().reatiler = retailer;
						Helper.getSharedHelper().isSSL = res.isSSL;
						status = true;

					} else {
						status = false;
					}
					res = null;

					return status;
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return false;
			}

		}

		@Override
		protected void onPostExecute(Boolean result) {

			if (result) {

				retailer = Helper.getSharedHelper().reatiler;

				try {
					if (retailer.getBackdropColor1().length() > 0) {
						Constants.BACKDROP1 = Color.parseColor("#"
								+ retailer.getBackdropColor1());
					}
				} catch (Exception e) {
				}

				try {
					if (retailer.getBackdropColor2().length() > 0) {
						Constants.BACKDROP2 = Color.parseColor("#"
								+ retailer.getBackdropColor2());
					}
				} catch (Exception e) {
				}

				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						String normlFontPath = Helper.getSharedHelper()
								.getRegularFontPath(retailer.getSiteFont());
						Typeface nonmarFont = Typeface.createFromAsset(
								activity.getAssets(), normlFontPath);
						Helper.getSharedHelper().normalFont = nonmarFont;

						String boldFontPath = Helper.getSharedHelper()
								.getBoldFontPath(retailer.getSiteFont());
						Typeface boldFont = Typeface.createFromAsset(
								activity.getAssets(), boldFontPath);
						Helper.getSharedHelper().boldFont = boldFont;
						Helper.getSharedHelper().enableShoppingCart = retailer.enableShoppingCart;
						Helper.getSharedHelper().enableRating = retailer.enableRating;
						Helper.getSharedHelper().enableDelivery = retailer.enableDelivery;
						Helper.getSharedHelper().enable_shipping = retailer.enable_shipping;
						Helper.getSharedHelper().deliveryDays = retailer.deliveryDays;
						Helper.getSharedHelper().enableCreditCode = retailer.enableCreditCode;
						// Helper.getSharedHelper().currency_code =
						// retailer.defaultCurrency;
						// Helper.getSharedHelper().getCurrencySymbol();
						initilize();
						populateBillboardproducts();
						if (themeUpdater != null) {
							themeUpdater.updateAppTheme();
						}
						if (mCategoryListAdapter.maxHeight == -1) {

						} else {

							mCategoryListAdapter.notifyDataSetChanged();
							populateSecondaryBillBoard();
						}
					}
				}, 20);
				spref.edit()
						.putString(Constants.SPLASH_IMG,
								retailer.getSplashImage()).commit();
				spref.edit()
						.putString(Constants.POWERED_BY,
								retailer.getPoweredBy()).commit();

			} else {

			}
		}

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}

	void updatePageIndictor() {
		// View firstPage = (View)
		// pageIndicatorView.findViewById(R.id.firstPage);
		// View secondPage = (View) pageIndicatorView
		// .findViewById(R.id.secondPage);
		// if (visiblePage == 1) {
		// firstPage.setBackgroundResource(R.drawable.page_selected);
		// secondPage.setBackgroundResource(R.drawable.page_unselected);
		// } else {
		// firstPage.setBackgroundResource(R.drawable.page_unselected);
		// secondPage.setBackgroundResource(R.drawable.page_selected);
		// }
	}

	@Override
	public void onInitializationFailure(Provider arg0,
			YouTubeInitializationResult arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitializationSuccess(Provider provider,
			YouTubePlayer player, boolean arg2) {
		// TODO Auto-generated method stub
		// if (provider.equals(youtubeplayerview1)) {
		// player.cueVideo(VIDEO_ID);
		// } else {
		// player.cueVideo("o7VVHhK9zf0");
		// }

	}

	void populateBillboardproducts() {
		billBoardItem.clear();
		billBoardItem2.clear();
		
		for (int i = 0; retailer.products != null && i < retailer.products.size() && i < 5; i++) {
			billBoardItem.add(retailer.products.get(i));
		}
		for (int i = 5;retailer.products != null && i < retailer.products.size(); i++) {
			billBoardItem2.add(retailer.products.get(i));
		}

		// Product p = new Product();
		// p.setName("Dummy");
		// p.setImage("http://inceptionlive.com/uploads/products/2_1422204529_orig_2._Home_Multimedia_Message_xhdpi8.jpg");
		// Product p2 = new Product();
		// p2.setName("Dummy");
		// p2.setImage("http://appwizlive.com/uploads/products/1_1425400096_waterproof_coaster-2.jpg");
		// billBoardItem.add(p);
		// billBoardItem.add(p2);
		// billBoardItem.add(p);
		// billBoardItem.add(p2);
		//
		// billBoardItem.add(p);
		//
		// billBoardItem2.add(p2);
		// billBoardItem2.add(p);
		// billBoardItem2.add(p2);
		// billBoardItem2.add(p);
		// billBoardItem2.add(p2);
		// billBoardItem2.add(p);
		// billBoardItem2.add(p2);
		// billBoardItem2.add(p);

	}

}
