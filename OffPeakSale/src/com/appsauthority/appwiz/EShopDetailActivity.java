package com.appsauthority.appwiz;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import jim.h.common.android.zxinglib.integrator.IntentIntegrator;
import jim.h.common.android.zxinglib.integrator.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import com.appauthority.appwiz.fragments.SlidingMenuActivity;
import com.appauthority.appwiz.interfaces.PayPalCaller;
import com.appauthority.appwiz.interfaces.ReviewProductCaller;
import com.appsauthority.appwiz.custom.BannerLayout;
import com.appsauthority.appwiz.custom.BaseActivity;
import com.appsauthority.appwiz.models.PayPalModelObject;
import com.appsauthority.appwiz.models.Product;
import com.appsauthority.appwiz.models.ProductOption;
import com.appsauthority.appwiz.models.Retailer;
import com.appsauthority.appwiz.models.RetailerStores;
import com.appsauthority.appwiz.utils.BezierTranslationAnimation;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.HTTPHandler;
import com.appsauthority.appwiz.utils.Helper;
import com.appsauthority.appwiz.utils.HorizontalPager;
import com.appsauthority.appwiz.utils.Utils;
import com.google.android.gms.internal.bn;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayer.Provider;
import com.google.android.youtube.player.YouTubePlayerView;
import com.offpeaksale.consumer.R;

public class EShopDetailActivity extends BaseActivity implements
		OnInitializedListener, OnItemClickListener, ReviewProductCaller,
		PayPalCaller {

	private enum SHARINGTYPE {
		EMAIL, FACEBOOK, INSTAGRAM, WECHAT, WHATSAPP
	};

	private Boolean enableUnderLine = true;
	private TextView textViewHeader;
	private Product product;
	private Activity activity;
	private TextView textViewName, textViewShortDesc, textViewDetail,
			textViewHowItWorks, textViewNewPrice, textView_testimonial,
			tv_testimonial_txt, tvReadReviews, tvRewardsPoints;
	private ImageView imageView;
	private Button buttonBuy, btnSubmit, btnEnquiry;
	private RelativeLayout relProductDet, relProductHowItWorks, relTestimonial;
	private EditText editTextQty;
	private View list_left1, list_bot1, vwFirstOptionUnderline,
			vwSecondOptionUnderline;
	private View list_left2, list_bot2, list_left3, list_bot3,
			vwReadReaviewUnderline;
	private View lineTop, lineBot;
	private RatingBar productRatingBar;
	ImageView btnShare, btnLeftShare;
	LinearLayout buyLL, enquiryLL;
	TextView tvEnterCode;

	TextView tvQty;

	private LinearLayout ratingLL;

	private Retailer retailer;

	private TextView txtCartTotal;
	private LinearLayout cartView;

	TextView tvDiscountInfo;
	ImageView resultImage;
	EditText edtCoupon;

	// LinearLayout secondPage;
	// private HorizontalPager mPager;
	YouTubePlayerView youtubeplayerview;
	private VideoView videoView;
	Runnable r;
	BannerLayout imageBanner;

	int pageCount = 1;

	int visiblePage = 1;

	String localPath;
	WebView webview;
	Handler handler = new Handler();
	LinearLayout llFirstOption, llSecondOption;

	TextView tvFirstOptinLbl, tvSeconoptionLbl, tvFirstOptionValue,
			tvSecondOptionValue;

	List<String> firstList, secondList;
	int selectedOptionIndex = 0;
	ListPopupWindow listPopupWindow;

	RelativeLayout rlFirstOption, rlSecondOption;
	Spinner sp_option1, sp_option2;

	ImageView btnSearch, imageScan, imageViewOverflow;
	AutoCompleteTextView etSearch;

	LinearLayout llTabContainer;

	HorizontalScrollView horizontalScrollView;
	int width;
	View underLineReview, underlineProductDetail, underlineHowItWorks;

	ViewFlipper viewFlipper;
	LinearLayout vwChildReviews, vwChildProductDetails, vwChildHowItWorks,
			llMapView;
	RelativeLayout rlBannerlayout;

	ImageView imgAnimationImage;

	EditText edt_comments, edt_Name, edt_email;

	Button btn_submitReview;

	Product addedProduct;

	private SharedPreferences spref;

	TextView tvOldPrice, tvNewPrice, tvDistance, tvAddress, tvQtyIndicator,
			tvDiscountValue, tvDiscountlbl;
	ToggleButton favToggle;
	LinearLayout llDiscountInfo;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.activity_eshop_detail);
		Intent intent = getIntent();
		product = (Product) intent.getSerializableExtra("product");
		activity = this;

		retailer = Helper.getSharedHelper().reatiler;
		spref = PreferenceManager.getDefaultSharedPreferences(activity);
		tvQtyIndicator = (TextView) findViewById(R.id.tvQtyIndicator);
		favToggle = (ToggleButton) findViewById(R.id.favToggle);
		tvDiscountValue = (TextView) findViewById(R.id.tvDiscountValue);
		tvDiscountlbl = (TextView) findViewById(R.id.tvDiscountLbl);
		llDiscountInfo = (LinearLayout) findViewById(R.id.llDiscountInfo);

		tvDiscountValue.setTypeface(Helper.getSharedHelper().normalFont);
		tvDiscountlbl.setTypeface(Helper.getSharedHelper().normalFont);
		tvDiscountValue.setText(product.offpeakDiscount);

		// mPager = (HorizontalPager) findViewById(R.id.horizontal_pager);
		// mPager.setOnScreenSwitchListener(onScreenSwitchListener);
		// secondPage = (LinearLayout) findViewById(R.id.secondPage);
		// videoView = (VideoView) secondPage.findViewById(R.id.videoView);
		// imageView2 = (ImageView) secondPage.findViewById(R.id.imageView);
		// webview = (WebView) secondPage.findViewById(R.id.gifWebview);
		// youtubeplayerview = (YouTubePlayerView) secondPage
		// .findViewById(R.id.youtubeplayerview);

		viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
		vwChildReviews = (LinearLayout) viewFlipper
				.findViewById(R.id.vwReviews);
		vwChildProductDetails = (LinearLayout) viewFlipper
				.findViewById(R.id.vwProductDetails);
		vwChildHowItWorks = (LinearLayout) viewFlipper
				.findViewById(R.id.vwHowItWorks);
		vwChildReviews.setTag(0);
		vwChildProductDetails.setTag(1);
		vwChildHowItWorks.setTag(2);
		llMapView = (LinearLayout) findViewById(R.id.llMapView);

		MapLayout mapLayout = new MapLayout(activity, activity, product.outlets);
		mapLayout.merchantName = product.getShortDescription();
		mapLayout.outletName = product.outletName;
		mapLayout.storeAddress = product.storeAddress;
		mapLayout.storeContact = product.storeContact;
		mapLayout.latitude = product.latitude;
		mapLayout.longitude = product.longitude;
		mapLayout.init();
		llMapView.addView(mapLayout);

		edt_comments = (EditText) findViewById(R.id.edt_comments);
		edt_Name = (EditText) findViewById(R.id.edt_Name);
		edt_email = (EditText) findViewById(R.id.edt_email);
		btn_submitReview = (Button) findViewById(R.id.btn_submitReview);
		btn_submitReview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String comment = edt_comments.getText().toString();
				String name = edt_Name.getText().toString();
				String email = edt_email.getText().toString();
				if (comment == null || comment.length() == 0) {
					Toast.makeText(context, "Enter your comments.",
							Toast.LENGTH_SHORT).show();
				} else if (name == null || name.length() == 0) {
					Toast.makeText(context, "Enter your  name.",
							Toast.LENGTH_SHORT).show();
				} else if (email == null
						|| !Helper.getSharedHelper().isEmailValid(email)) {
					Toast.makeText(context, "Enter your email id.",
							Toast.LENGTH_SHORT).show();
				} else {

					ReviewProductHandler rph = new ReviewProductHandler(product
							.getId(), email, comment, name,
							EShopDetailActivity.this);
					rph.submitReview();
				}

			}
		});

		imgAnimationImage = (ImageView) findViewById(R.id.imgAnimationImage);

		rlBannerlayout = (RelativeLayout) findViewById(R.id.rlBannerlayout);

		llFirstOption = (LinearLayout) findViewById(R.id.llFirstOption);
		llSecondOption = (LinearLayout) findViewById(R.id.llSecondOptionOption);
		btnSearch = (ImageView) findViewById(R.id.btnSearch);
		imageScan = (ImageView) findViewById(R.id.imageScan);
		btnSearch.setVisibility(View.VISIBLE);
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				if (etSearch.getVisibility() == View.VISIBLE) {
					etSearch.setVisibility(View.GONE);
					imageScan.setVisibility(View.GONE);
					textViewHeader.setVisibility(View.VISIBLE);
					btnLeftShare.setVisibility(View.VISIBLE);
					imageViewOverflow.setVisibility(View.VISIBLE);
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(etSearch.getWindowToken(),
							InputMethodManager.RESULT_UNCHANGED_SHOWN);
				} else {
					etSearch.setVisibility(View.VISIBLE);
					imageScan.setVisibility(View.VISIBLE);
					textViewHeader.setVisibility(View.GONE);
					btnLeftShare.setVisibility(View.GONE);
					imageViewOverflow.setVisibility(View.GONE);
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.showSoftInput(btnSearch,
							InputMethodManager.SHOW_IMPLICIT);
				}
			}
		});
		imageScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				etSearch.setVisibility(View.GONE);
				imageScan.setVisibility(View.GONE);
				textViewHeader.setVisibility(View.VISIBLE);
				btnLeftShare.setVisibility(View.VISIBLE);
				imageViewOverflow.setVisibility(View.VISIBLE);
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(etSearch.getWindowToken(),
						InputMethodManager.RESULT_UNCHANGED_SHOWN);
				Helper.getSharedHelper().isScanAdminPWD = false;
				IntentIntegrator.initiateScan(EShopDetailActivity.this,
						R.layout.capture, R.id.viewfinder_view,
						R.id.preview_view, true);
			}
		});
		etSearch = (AutoCompleteTextView) findViewById(R.id.etSearch);
		etSearch.setTextColor(Color.parseColor("#"
				+ retailer.getRetailerTextColor()));
		etSearch.setTypeface(Helper.getSharedHelper().normalFont);
		etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					// Toast.makeText(
					// EShopDetailActivity.this,
					// "Searched text is " + etSearch.getText().toString(),
					// Toast.LENGTH_LONG).show();
					String keyWord = etSearch.getText().toString().trim();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(etSearch.getWindowToken(),
							InputMethodManager.RESULT_UNCHANGED_SHOWN);
					searchText(keyWord);

					return true;
				}
				return false;
			}
		});

		tvOldPrice = (TextView) findViewById(R.id.tv_eshop_old_price);
		tvNewPrice = (TextView) findViewById(R.id.tv_eshop_new_price);
		tvDistance = (TextView) findViewById(R.id.tvDistance);
		tvAddress = (TextView) findViewById(R.id.tvAddress);
		tvFirstOptinLbl = (TextView) llFirstOption
				.findViewById(R.id.tv_optionLable);
		tvSeconoptionLbl = (TextView) llSecondOption
				.findViewById(R.id.tv_optionLable);
		// vwFirstOptionRight1 = (View) llFirstOption
		// .findViewById(R.id.list_right1);
		// vwSecondOptionRight1 = (View) llSecondOption
		// .findViewById(R.id.list_right1);
		rlFirstOption = (RelativeLayout) llFirstOption
				.findViewById(R.id.relValue);
		sp_option1 = (Spinner) llFirstOption.findViewById(R.id.sp_option);
		sp_option2 = (Spinner) llFirstOption.findViewById(R.id.sp_option);

		tvFirstOptionValue = (TextView) llFirstOption
				.findViewById(R.id.tv_optonsValue);
		tvSecondOptionValue = (TextView) llSecondOption
				.findViewById(R.id.tv_optonsValue);
		vwFirstOptionUnderline = (View) llFirstOption
				.findViewById(R.id.list_underline);
		vwSecondOptionUnderline = (View) llSecondOption
				.findViewById(R.id.list_underline);
		rlSecondOption = (RelativeLayout) llSecondOption
				.findViewById(R.id.relValue);

		list_bot1 = (View) findViewById(R.id.list_bot1);
		list_bot2 = (View) findViewById(R.id.list_bot2);
		list_bot3 = (View) findViewById(R.id.list_bot3);
		list_left1 = (View) findViewById(R.id.list_left1);
		list_left2 = (View) findViewById(R.id.list_left2);
		list_left3 = (View) findViewById(R.id.list_left3);
		vwReadReaviewUnderline = (View) findViewById(R.id.vwReadReaviewUnderline);

		imageViewOverflow = (ImageView) findViewById(R.id.imageViewOverflow);
		// imageViewOverflow.setImageResource(R.drawable.backbutton);
		imageViewOverflow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();

			}
		});

		textViewHeader = (TextView) findViewById(R.id.textViewHeader);
		imageView = (ImageView) findViewById(R.id.iv_eshop_detail_image);
		textViewDetail = (TextView) findViewById(R.id.tv_product_detail_txt);
		textViewName = (TextView) findViewById(R.id.tv_eshop_detail_name);
		tvRewardsPoints = (TextView) findViewById(R.id.tvRewardsPoints);
		textViewShortDesc = (TextView) findViewById(R.id.tv_eshop_detail_short_desc);
		textViewHowItWorks = (TextView) findViewById(R.id.tv_how_it_works_txt);
		tv_testimonial_txt = (TextView) findViewById(R.id.tv_testimonial_txt);
		tvReadReviews = (TextView) findViewById(R.id.tvReadReviews);
		editTextQty = (EditText) findViewById(R.id.edt_qty);
		lineBot = (View) findViewById(R.id.lineBot);
		lineTop = (View) findViewById(R.id.lineTop);
		btnShare = (ImageView) findViewById(R.id.btnShare);
		btnLeftShare = (ImageView) findViewById(R.id.btnLeftShare);
		tvEnterCode = (TextView) findViewById(R.id.tvEnterCode);
		// textViewOldPrice = (TextView)
		// findViewById(R.id.tv_shop_detail_old_price);
		textViewNewPrice = (TextView) findViewById(R.id.tv_shop_detail_new_price);
		relProductDet = (RelativeLayout) findViewById(R.id.relProductDet);
		relProductHowItWorks = (RelativeLayout) findViewById(R.id.relProductHowItWorks);
		relTestimonial = (RelativeLayout) findViewById(R.id.relTestimonial);
		buttonBuy = (Button) findViewById(R.id.btn_buy);
		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		btnEnquiry = (Button) findViewById(R.id.btnEnquiry);
		ratingLL = (LinearLayout) findViewById(R.id.ratingLL);
		productRatingBar = (RatingBar) findViewById(R.id.productRatingBar);
		productRatingBar.setIsIndicator(false);
		productRatingBar.setStepSize(1.0f);
		buyLL = (LinearLayout) findViewById(R.id.buyLL);
		enquiryLL = (LinearLayout) findViewById(R.id.enquiryLL);
		// if (Helper.getSharedHelper().disablePayment.equals("1")) {
		// // buyLL.setVisibility(View.GONE);
		// // enquiryLL.setVisibility(View.VISIBLE);
		// buyLL.setVisibility(View.VISIBLE);
		// enquiryLL.setVisibility(View.GONE);
		// buttonBuy
		// .setText(getResources().getString(R.string.single_enquiry));
		// } else {
		// if (product.availQty != null) {
		// if (Integer.parseInt(product.availQty) <= 0) {
		// buttonBuy.setText(getResources().getString(
		// R.string.sold_out));
		// buttonBuy.setEnabled(false);
		// }
		// }
		// buyLL.setVisibility(View.VISIBLE);
		// enquiryLL.setVisibility(View.GONE);
		// }

		buttonBuy.setText(getResources().getString(R.string.buy));
		if (Helper.getSharedHelper().enableRating.equals("1")) {

			ratingLL.setVisibility(View.GONE);
		} else {
			ratingLL.setVisibility(View.GONE);
		}

		cartView = (LinearLayout) findViewById(R.id.cartView);

		txtCartTotal = (TextView) findViewById(R.id.txtCartTotal);
		txtCartTotal.setTextColor(Color.parseColor("#"
				+ retailer.getRetailerTextColor()));

		if (Helper.getSharedHelper().enableShoppingCart.equals("1")) {
			cartView.setVisibility(View.GONE);
			buttonBuy.setText("Buy");
			// if (product.availQty != null) {
			// if (Integer.parseInt(product.availQty) <= 0) {
			// buttonBuy.setText(getResources().getString(
			// R.string.sold_out));
			// buttonBuy.setEnabled(false);
			// }
			// }
			btnEnquiry.setText(R.string.multiple_enquiry);
			buttonBuy.setTextSize(14);
			txtCartTotal.setText(Helper.getSharedHelper().getCartTotal());
			btnLeftShare.setVisibility(View.VISIBLE);
			btnShare.setVisibility(View.GONE);
		} else if (Helper.getSharedHelper().enableCreditCode.equals("1")) {

			cartView.setVisibility(View.GONE);
			buttonBuy.setText("Buy");
			btnLeftShare.setVisibility(View.VISIBLE);
			btnShare.setVisibility(View.GONE);
			btnEnquiry.setText(R.string.single_enquiry);
		} else {
			cartView.setVisibility(View.GONE);
			buttonBuy.setText("Buy");
			btnShare.setVisibility(View.VISIBLE);
			btnLeftShare.setVisibility(View.GONE);
			btnEnquiry.setText(R.string.single_enquiry);
		}
		tvEnterCode.setVisibility(View.VISIBLE);
		btnSearch.setVisibility(View.GONE);

		tvQty = (TextView) findViewById(R.id.tvQty);
		tvQty.setTextColor(Color.parseColor("#" + retailer.getHeaderColor()));
		editTextQty.setBackgroundDrawable(getGradientDrawableEditText(retailer
				.getHeaderColor()));

		textViewHeader.setText("E-Shop");

		textViewName.setText(product.getName());
		textViewShortDesc.setText(product.getShortDescription());
		String rewardPoints = product.reward_points;
		if (rewardPoints == null) {
			rewardPoints = "0";
		}
		if (Helper.getSharedHelper().reatiler.enableRewards
				.equalsIgnoreCase("1")) {
			tvRewardsPoints.setVisibility(View.GONE);
			SpannableString spanableText = new SpannableString(rewardPoints
					+ " Credit Points");
			spanableText.setSpan(new StyleSpan(Typeface.BOLD), 0,
					rewardPoints.length(), 0);
			tvRewardsPoints.setText(spanableText);
		} else {
			tvRewardsPoints.setVisibility(View.GONE);
		}

		textViewDetail.setText(product.getDescription());
		textViewHowItWorks.setText(product.getHowItWorks());
		// textViewOldPrice.setText("$" + product.getOldPrice());
		// textViewNewPrice.setText(Helper.getSharedHelper().currency_symbol
		// + product.getNewPrice());
		editTextQty.setText("1");
		updatePrice();
		// textViewNewPrice.setTextColor(Color.parseColor("#"
		// + retailer.getRetailerTextColor()));
		// textViewOldPrice.setPaintFlags(textViewOldPrice.getPaintFlags()
		// | Paint.STRIKE_THRU_TEXT_FLAG);

		lineTop.setBackgroundColor(Color.parseColor("#"
				+ retailer.getHeaderColor()));
		lineBot.setBackgroundColor(Color.parseColor("#"
				+ retailer.getHeaderColor()));
		textViewNewPrice.setTextColor(Color.parseColor("#"
				+ retailer.getHeaderColor()));

		// editTextQty.setTextColor(Color.parseColor("#"
		// + retailer.getHeaderColor()));

		buttonBuy.setTextColor(Color.parseColor("#"
				+ retailer.getRetailerTextColor()));
		btnEnquiry.setTextColor(Color.parseColor("#"
				+ retailer.getRetailerTextColor()));
		buttonBuy.setBackgroundDrawable(getGradientDrawable(retailer
				.getHeaderColor()));
		btnEnquiry.setBackgroundDrawable(getGradientDrawable(retailer
				.getHeaderColor()));
		// tvEnterCode.setTextColor(Color.parseColor("#"
		// + retailer.getRetailerTextColor()));

		edt_comments.setBackgroundDrawable(Helper.getSharedHelper()
				.getGradientDrawableEditText(retailer.getHeaderColor()));
		edt_Name.setBackgroundDrawable(Helper.getSharedHelper()
				.getGradientDrawableEditText(retailer.getHeaderColor()));
		edt_email.setBackgroundDrawable(Helper.getSharedHelper()
				.getGradientDrawableEditText(retailer.getHeaderColor()));

		btn_submitReview.setBackgroundDrawable(getGradientDrawable(retailer
				.getHeaderColor()));

		btn_submitReview.setTextColor(Color.parseColor("#"
				+ retailer.getRetailerTextColor()));
		btnSubmit
				.setTextColor(Color.parseColor("#" + retailer.getHeaderColor()));
		btnSubmit.setBackgroundDrawable(getGradientDrawableEditText(retailer
				.getHeaderColor()));

		if (product.getDescription().length() == 0) {
			relProductDet.setVisibility(View.GONE);
			textViewDetail.setVisibility(View.GONE);
		}

		if (product.getHowItWorks().length() == 0) {
			relProductHowItWorks.setVisibility(View.GONE);
			textViewHowItWorks.setVisibility(View.GONE);
		}
		String testimonials = product.getTestimonials();
		if (testimonials == null || testimonials.length() == 0) {
			// relTestimonial.setVisibility(View.GONE);
			// tv_testimonial_txt.setVisibility(View.GONE);
			tv_testimonial_txt.setText("No reviews for this product.");
			tv_testimonial_txt.setVisibility(View.VISIBLE);
		} else {
			tv_testimonial_txt
					.setText(Html.fromHtml(product.getTestimonials()));
			tv_testimonial_txt.setVisibility(View.VISIBLE);
		}

		setHeaderTheme(activity, retailer.getRetailerTextColor(),
				retailer.getHeaderColor());
		list_bot1.setBackgroundColor(Color.parseColor("#"
				+ retailer.getHeaderColor()));
		list_bot2.setBackgroundColor(Color.parseColor("#"
				+ retailer.getHeaderColor()));
		list_bot3.setBackgroundColor(Color.parseColor("#"
				+ retailer.getHeaderColor()));

		list_left1.setBackgroundColor(Color.parseColor("#"
				+ retailer.getHeaderColor()));
		list_left2.setBackgroundColor(Color.parseColor("#"
				+ retailer.getHeaderColor()));
		list_left3.setBackgroundColor(Color.parseColor("#"
				+ retailer.getHeaderColor()));
		vwReadReaviewUnderline.setBackgroundColor(Color.parseColor("#"
				+ retailer.getHeaderColor()));

		// vwFirstOptionRight1.setBackgroundColor(Color.parseColor("#"
		// + retailer.getHeaderColor()));
		// vwFirstOptionUnderline.setBackgroundColor(Color.parseColor("#"
		// + retailer.getHeaderColor()));

		// vwSecondOptionRight1.setBackgroundColor(Color.parseColor("#"
		// + retailer.getHeaderColor()));
		// vwSecondOptionUnderline.setBackgroundColor(Color.parseColor("#"
		// + retailer.getHeaderColor()));

		try {
			if (intent.getStringExtra("FROM").equalsIgnoreCase("PAYPAL")) {
				Log.i(TAG, intent.getStringExtra("message"));

				if (intent.getStringExtra("status").equalsIgnoreCase("success")) {
					DisplayMetrics metrics = getResources().getDisplayMetrics();
					int width = metrics.widthPixels;

					final Dialog dialog = new Dialog(context);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.dialog_order_success);

					dialog.getWindow().setLayout((6 * width) / 7,
							LayoutParams.WRAP_CONTENT);

					TextView textViewProductName = (TextView) dialog
							.findViewById(R.id.textViewProductName);
					TextView textViewMessage = (TextView) dialog
							.findViewById(R.id.textViewMessage);
					TextView textViewTitle = (TextView) dialog
							.findViewById(R.id.textViewTitle);

					TextView textViewProductQuantity = (TextView) dialog
							.findViewById(R.id.textViewProductQuantity);
					ImageView imageView = (ImageView) dialog
							.findViewById(R.id.imageView);

					Button buttonClose = (Button) dialog
							.findViewById(R.id.buttonClose);

					textViewMessage.setText(intent.getStringExtra("message"));
					textViewProductName.setText(product.getShortDescription());

					textViewTitle.setTextColor(Color.parseColor("#"
							+ retailer.getRetailerTextColor()));
					textViewTitle
							.setBackgroundDrawable(getGradientDrawableNoRad(retailer
									.getHeaderColor()));

					textViewProductQuantity.setText(editTextQty.getText()
							.toString());

					buttonClose.setTextColor(Color.parseColor("#"
							+ retailer.getRetailerTextColor()));
					buttonClose
							.setBackgroundDrawable(getGradientDrawable(retailer
									.getHeaderColor()));

					buttonClose.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							dialog.dismiss();

						}
					});

					imageCacheloader.displayImage(product.getImage(),
							R.drawable.image_placeholder, imageView);

					dialog.show();
				}
			}
		} catch (Exception e) {

		}

		editTextQty.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				updatePrice();
				// try {
				//
				// double val = Double.parseDouble(product.getNewPrice())
				// * Double.parseDouble(editTextQty.getText()
				// .toString());
				//
				// if (Helper.getSharedHelper().isDecialFromat) {
				// textViewNewPrice.setText(Helper.getSharedHelper().currency_symbol
				// + String.format("%.2f", val));
				// } else {
				// textViewNewPrice.setText(Helper.getSharedHelper().currency_symbol
				// + String.format("%.0f", val));
				// }
				//
				// } catch (Exception e) {
				// if (Helper.getSharedHelper().isDecialFromat) {
				// textViewNewPrice.setText(Helper.getSharedHelper().currency_symbol
				// + String.format("%.2f", 0.00));
				// } else {
				// textViewNewPrice.setText(Helper.getSharedHelper().currency_symbol
				// + String.format("%.0f", 0.00));
				// }
				// e.printStackTrace();
				// }

			}
		});

		btnShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showShareIntent();

			}
		});
		btnLeftShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showShareIntent();

			}
		});

		setFont();
		tvEnterCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showCouponAlert();
			}
		});
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(EShopDetailActivity.this,
						ImageZoomActivity.class);
				intent.putExtra("image", product.getImage());
				startActivity(intent);
			}
		});
		if (pageCount > 1) {
			// loadSecondPage();
		} else {
			// mPager.removeView(secondPage);
		}

		// ProductOption options = product.getProduct_options();
		// if (options != null) {
		// if (options.) {
		//
		// }
		// }
		List<ProductOption> productOption = product.getProduct_options();
		if (productOption != null) {
			if (productOption.size() > 0) {
				ProductOption option = productOption.get(0);
				populateFirstList(option);
				llFirstOption.setVisibility(View.VISIBLE);
			} else {
				llFirstOption.setVisibility(View.GONE);
			}
			if (productOption.size() > 1) {
				ProductOption option = productOption.get(1);
				populateSecondList(option);
				llSecondOption.setVisibility(View.VISIBLE);
			} else {
				llSecondOption.setVisibility(View.GONE);
			}
		}
		horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable() {
					public void run() {
						initializeTab();

					}
				});

			}
		}, 500);

		viewFlipper.setDisplayedChild(1);

		// final Boolean bannerLoaded = false;
		// final ViewTreeObserver hvto = rlBannerlayout.getViewTreeObserver();
		//
		// hvto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
		//
		// @Override
		// public void onGlobalLayout() {
		//
		// if (!bannerLoaded) {
		// if (product.product_imgArray != null
		// && product.product_imgArray.size() > 0) {
		//
		// imageView.setVisibility(View.GONE);
		// imageBanner = new BannerLayout(
		// EShopDetailActivity.this,
		// product.product_imgArray);
		// rlBannerlayout.addView(imageBanner);
		// } else {
		// imageView.setVisibility(View.VISIBLE);
		// imageCacheloader.displayImage(product.getImage(),
		// R.drawable.image_placeholder, imageView);
		// }
		// }
		//
		// }
		// });
		if (product.product_imgArray != null
				&& product.product_imgArray.size() > 0) {

			float pixels = TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 215, getResources()
							.getDisplayMetrics());
			imageView.setVisibility(View.GONE);
			imageBanner = new BannerLayout(EShopDetailActivity.this,
					product.product_imgArray, (int) pixels);
			rlBannerlayout.addView(imageBanner);
		} else {
			imageView.setVisibility(View.VISIBLE);
			imageCacheloader.displayImage(product.getImage(),
					R.drawable.image_placeholder, imageView);
		}
		tvQtyIndicator.bringToFront();
		favToggle.bringToFront();
		llDiscountInfo.bringToFront();
		RelativeLayout rlCircularView = (RelativeLayout) findViewById(R.id.rlCircularView);
		GradientDrawable bgShape = (GradientDrawable) rlCircularView
				.getBackground();
		bgShape.setColor(Color.parseColor("#" + retailer.getHeaderColor()));

		if (product.availQty != null) {
			tvQtyIndicator.setText(product.availQty + " sold");
			tvQtyIndicator.setVisibility(View.VISIBLE);

		}
		ImageView imgCart = (ImageView) findViewById(R.id.imgCart);
		if (Helper.getSharedHelper().reatiler.appIconColor != null
				&& Helper.getSharedHelper().reatiler.appIconColor
						.equalsIgnoreCase("black")) {
			imageViewOverflow
					.setBackgroundResource(R.drawable.backbutton_black);
			imageScan.setBackgroundResource(R.drawable.ic_barcode);
			imgCart.setBackgroundResource(R.drawable.shop_cart_black);
			btnSearch.setBackgroundResource(R.drawable.search_black);
			btnLeftShare.setBackgroundResource(R.drawable.share_black);
		} else {
			imageViewOverflow.setBackgroundResource(R.drawable.backbutton);
			imageScan.setBackgroundResource(R.drawable.ic_barcode_white);
			imgCart.setBackgroundResource(R.drawable.shop_cart);
			btnSearch.setBackgroundResource(R.drawable.search);
			btnLeftShare.setBackgroundResource(R.drawable.share);
		}
		updateItemPrice();

	}

	void updateItemPrice() {
		float conversionValue = 0.0f;
		try {
			conversionValue = Float
					.parseFloat(Helper.getSharedHelper().currency_conversion_map
							.get(Helper.getSharedHelper().currency_code));
		} catch (Exception e) {
			// TODO: handle exception
		}

		float newPrice = Float.parseFloat(product.getNewPrice());
		String selectedCurrencyCode = Helper.getSharedHelper().currency_code;
		if (conversionValue == 0) {
			conversionValue = 1.0f;
			selectedCurrencyCode = Helper.getSharedHelper().reatiler.defaultCurrency;
		}
		if (product.getOldPrice() != null) {
			float oldProce = Float.parseFloat(product.getOldPrice());
			oldProce = oldProce * conversionValue;
			tvOldPrice.setText(Helper.getSharedHelper().getCurrencySymbol(
					selectedCurrencyCode)
					+ Helper.getSharedHelper().conertfloatToSTring(oldProce));
			tvOldPrice.setVisibility(View.VISIBLE);
		} else {
			tvOldPrice.setVisibility(View.GONE);
		}

		newPrice = newPrice * conversionValue;

		tvNewPrice.setText(Helper.getSharedHelper().getCurrencySymbol(
				selectedCurrencyCode)
				+ Helper.getSharedHelper().conertfloatToSTring(newPrice));

		tvOldPrice.setPaintFlags(tvOldPrice.getPaintFlags()
				| Paint.STRIKE_THRU_TEXT_FLAG);
		if (product.distance == null) {
			RetailerStores store = product.outlets.get(0);
			product.distance = Helper.getSharedHelper().getDistanceBetween(
					Constants.TARGET_LAT, Constants.TARGET_LNG,
					Double.parseDouble(store.getLatitude()),
					Double.parseDouble(store.getLongitude()));
		}
		tvDistance.setText(product.distance + "KM");
		tvAddress.setText(product.outletName);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// your code
			if (etSearch.getVisibility() == View.VISIBLE) {
				etSearch.setVisibility(View.GONE);
				imageScan.setVisibility(View.GONE);
				textViewHeader.setVisibility(View.VISIBLE);
				imageViewOverflow.setVisibility(View.VISIBLE);
				btnLeftShare.setVisibility(View.VISIBLE);
				return true;
			}

		}

		return super.onKeyDown(keyCode, event);
	}

	void searchText(String keyWord) {
		if (keyWord != null && keyWord.length() > 0) {
			Intent intent = new Intent(context, SlidingMenuActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra(Constants.KEY_FROM_SEARCH, true);
			intent.putExtra(Constants.KEY_KEYWORD, etSearch.getText()
					.toString().trim());
			startActivity(intent);
			Helper.getSharedHelper().discountPercent = "0";
			finish();
		}
	}

	void initializeTab() {

		final float normalTextSize = 16.0f;
		final float selectedTextSize = 18.0f;
		llTabContainer = (LinearLayout) findViewById(R.id.llTabContainer);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		width = displaymetrics.widthPixels;
		if (product.getHowItWorks().length() != 0) {
			width = (int) (width / 2);
			// width = (int) (width - width*.25);
		} else {
			width = (int) (width / 2);
		}

		RelativeLayout rlReview, rlProductDetail, rlHowItWorks;
		final TextView tvReview;
		final TextView tvProductDetail;
		final TextView tvHowItWorks;

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View vwReview = inflater
				.inflate(R.layout.product_detail_tab_item, null);
		rlReview = (RelativeLayout) vwReview.findViewById(R.id.item);

		tvReview = (TextView) rlReview.findViewById(R.id.tvItemName);
		underLineReview = (View) rlReview.findViewById(R.id.vwTabUnderline);
		tvReview.getLayoutParams().width = width;

		View vwProductDetail = inflater.inflate(
				R.layout.product_detail_tab_item, null);
		rlProductDetail = (RelativeLayout) vwProductDetail
				.findViewById(R.id.item);
		tvProductDetail = (TextView) rlProductDetail
				.findViewById(R.id.tvItemName);
		tvProductDetail.getLayoutParams().width = width;
		underlineProductDetail = (View) rlProductDetail
				.findViewById(R.id.vwTabUnderline);

		View vwHowItWorks = inflater.inflate(R.layout.product_detail_tab_item,
				null);
		rlHowItWorks = (RelativeLayout) vwHowItWorks.findViewById(R.id.item);
		tvHowItWorks = (TextView) rlHowItWorks.findViewById(R.id.tvItemName);
		tvHowItWorks.getLayoutParams().width = width;
		underlineHowItWorks = (View) rlHowItWorks
				.findViewById(R.id.vwTabUnderline);

		tvReview.setText("Reviews");
		tvProductDetail.setText("Resturant Details");
		tvHowItWorks.setText("How It Works");
		underLineReview.setBackgroundColor(Color.TRANSPARENT);

		underlineHowItWorks.setBackgroundColor(Color.TRANSPARENT);

		String testimonials = product.getTestimonials();
		if (testimonials == null || testimonials.length() == 0) {
			llTabContainer.addView(rlReview);
		} else {
			llTabContainer.addView(rlReview);
		}
		llTabContainer.addView(rlProductDetail);
		if (product.getHowItWorks().length() != 0) {
			llTabContainer.addView(rlHowItWorks);
		}

		tvReview.setTypeface(Helper.getSharedHelper().normalFont);

		tvHowItWorks.setTypeface(Helper.getSharedHelper().normalFont);
		if (enableUnderLine) {
			underlineProductDetail.setBackgroundColor(Color.parseColor("#F2"
					+ Helper.getSharedHelper().reatiler.getHeaderColor()));
			tvProductDetail.setTypeface(Helper.getSharedHelper().normalFont);

		} else {
			underlineProductDetail.setBackgroundColor(Color.TRANSPARENT);
			tvProductDetail.setTextSize(selectedTextSize);
			tvProductDetail.setTypeface(Helper.getSharedHelper().boldFont);
		}

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				horizontalScrollView.scrollTo((int) (width / 2), 0);
				// viewFlipper.setDisplayedChild(1);
			}
		}, 100);

		rlReview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (viewFlipper.getDisplayedChild() == 0) {
					return;
				}
				if (enableUnderLine) {
					underLineReview.setBackgroundColor(Color.parseColor("#F2"
							+ Helper.getSharedHelper().reatiler
									.getHeaderColor()));
					underlineProductDetail
							.setBackgroundColor(Color.TRANSPARENT);
					underlineHowItWorks.setBackgroundColor(Color.TRANSPARENT);

				} else {
					underLineReview.setBackgroundColor(Color.TRANSPARENT);
					underlineProductDetail
							.setBackgroundColor(Color.TRANSPARENT);
					underlineHowItWorks.setBackgroundColor(Color.TRANSPARENT);

					tvReview.setTextSize(selectedTextSize);
					tvProductDetail.setTextSize(normalTextSize);
					tvHowItWorks.setTextSize(normalTextSize);

					tvReview.setTypeface(Helper.getSharedHelper().boldFont);
					tvProductDetail.setTypeface(Helper.getSharedHelper().normalFont);
					tvHowItWorks.setTypeface(Helper.getSharedHelper().normalFont);
				}

				horizontalScrollView
						.fullScroll(HorizontalScrollView.FOCUS_LEFT);
				viewFlipper.setDisplayedChild(0);
				if (imageBanner != null) {
					imageBanner.stopTimer();
				}

				// viewFlipper.setInAnimation(context,
				// R.anim.slide_in_from_left);
				// viewFlipper.setOutAnimation(context,
				// R.anim.slide_out_to_right);
			}
		});
		rlProductDetail.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (viewFlipper.getDisplayedChild() == 1) {
					return;
				}

				horizontalScrollView.smoothScrollTo((int) (width / 2), 0);

				if (enableUnderLine) {
					underLineReview.setBackgroundColor(Color.TRANSPARENT);
					underlineProductDetail.setBackgroundColor(Color
							.parseColor("#F2"
									+ Helper.getSharedHelper().reatiler
											.getHeaderColor()));
					underlineHowItWorks.setBackgroundColor(Color.TRANSPARENT);

				} else {
					underLineReview.setBackgroundColor(Color.TRANSPARENT);
					underlineProductDetail
							.setBackgroundColor(Color.TRANSPARENT);
					underlineHowItWorks.setBackgroundColor(Color.TRANSPARENT);
					tvReview.setTextSize(normalTextSize);
					tvProductDetail.setTextSize(selectedTextSize);
					tvHowItWorks.setTextSize(normalTextSize);

					tvReview.setTypeface(Helper.getSharedHelper().normalFont);
					tvProductDetail.setTypeface(Helper.getSharedHelper().boldFont);
					tvHowItWorks.setTypeface(Helper.getSharedHelper().normalFont);
				}

				viewFlipper.setDisplayedChild(1);
				if (imageBanner != null) {
					imageBanner.startTimer();
				}
			}
		});
		rlHowItWorks.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (viewFlipper.getDisplayedChild() == 2) {
					return;
				}
				if (enableUnderLine) {
					underLineReview.setBackgroundColor(Color.TRANSPARENT);
					underlineProductDetail
							.setBackgroundColor(Color.TRANSPARENT);
					underlineHowItWorks.setBackgroundColor(Color
							.parseColor("#F2"
									+ Helper.getSharedHelper().reatiler
											.getHeaderColor()));

				} else {
					underLineReview.setBackgroundColor(Color.TRANSPARENT);
					underlineProductDetail
							.setBackgroundColor(Color.TRANSPARENT);
					underlineHowItWorks.setBackgroundColor(Color.TRANSPARENT);
					tvReview.setTextSize(normalTextSize);
					tvProductDetail.setTextSize(normalTextSize);
					tvHowItWorks.setTextSize(selectedTextSize);

					tvReview.setTypeface(Helper.getSharedHelper().normalFont);
					tvProductDetail.setTypeface(Helper.getSharedHelper().normalFont);
					tvHowItWorks.setTypeface(Helper.getSharedHelper().boldFont);
				}
				horizontalScrollView
						.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
				viewFlipper.setDisplayedChild(2);
				if (imageBanner != null) {
					imageBanner.stopTimer();
				}

				// viewFlipper.setInAnimation(context,
				// R.anim.slide_in_from_right);
				// viewFlipper.setOutAnimation(context,
				// R.anim.slide_out_to_left);
			}
		});

	}

	/*
	 * void loadSecondPage() { try { if
	 * (retailer.getRetailerFileType().equalsIgnoreCase("Image")) { String
	 * imageUrl = /*
	 * "http://appwizlive.com/uploads/retailer/1/bgupload/OTTO_SOLUTION1.jpg"
	 * ;//retailer.getRetailerFile(); String path =
	 * this.getFilesDir().toString(); String[] paths =
	 * retailer.getRetailerFile().split("/"); String fileName =
	 * paths[paths.length - 1]; localPath = path + "/" + fileName;
	 * 
	 * if (imageUrl.endsWith(".gif")) { loadGifPage2(); } else {
	 * imageCacheloader.displayImage(imageUrl, R.drawable.image_placeholder,
	 * imageView2); webview.setVisibility(View.GONE); }
	 * 
	 * imageView2.setVisibility(View.VISIBLE); ((View)
	 * secondPage.findViewById(R.id.frameImageView))
	 * .setVisibility(View.VISIBLE); videoView.setVisibility(View.GONE); ((View)
	 * secondPage.findViewById(R.id.frameVideoView)) .setVisibility(View.GONE);
	 * youtubeplayerview.setVisibility(View.GONE); ((View)
	 * secondPage.findViewById(R.id.frameYoutubeView))
	 * .setVisibility(View.GONE);
	 * 
	 * } else { Boolean isYoutube = false; if (isYoutube) {
	 * youtubeplayerview.setVisibility(View.VISIBLE);
	 * videoView.setVisibility(View.GONE); imageView2.setVisibility(View.GONE);
	 * youtubeplayerview.initialize( getResources().getString(R.string.map_key),
	 * this); ((View) secondPage.findViewById(R.id.frameVideoView))
	 * .setVisibility(View.GONE); ((View)
	 * secondPage.findViewById(R.id.frameImageView)) .setVisibility(View.GONE);
	 * ((View) secondPage.findViewById(R.id.frameYoutubeView))
	 * .setVisibility(View.VISIBLE); } else {
	 * 
	 * /* if (false && currentVideoURL.equalsIgnoreCase(retailer
	 * .getRetailerFile())) {
	 * 
	 * } else {
	 * 
	 * videoView.setVideoPath(retailer.getRetailerFile()); // currentVideoURL2 =
	 * retailer.getRetailerFile();
	 * 
	 * MediaController mediaController = new MediaController(this);
	 * mediaController.setAnchorView(videoView);
	 * videoView.setMediaController(mediaController);
	 * videoView.setVisibility(View.VISIBLE);
	 * imageView2.setVisibility(View.GONE); ((View)
	 * secondPage.findViewById(R.id.frameVideoView))
	 * .setVisibility(View.VISIBLE); ((View)
	 * secondPage.findViewById(R.id.frameImageView)) .setVisibility(View.GONE);
	 * videoView.requestFocus(); videoView.seekTo(1);
	 * youtubeplayerview.setVisibility(View.GONE); ((View)
	 * secondPage.findViewById(R.id.frameYoutubeView))
	 * .setVisibility(View.GONE); // videoView2.start(); // } }
	 * 
	 * } } catch (Exception e) { Log.i("error", e.toString()); } }
	 */
	void updatePrice() {
		String selectedCurrencyCode = Helper.getSharedHelper().currency_code;
		float conversionValue = 0.0f;
		// try {
		// conversionValue = Float
		// .parseFloat(Helper.getSharedHelper().currency_conversion_map
		// .get(Helper.getSharedHelper().currency_code));
		// } catch (Exception e) {
		// // TODO: handle exception
		// }

		if (conversionValue == 0.0f) {
			conversionValue = 1.0f;
			selectedCurrencyCode = Helper.getSharedHelper().reatiler.defaultCurrency;
		}
		try {

			double val = Double.parseDouble(product.getNewPrice())
					* Double.parseDouble(editTextQty.getText().toString());
			if (Helper.getSharedHelper().enableCreditCode.equals("1")
					&& !Helper.getSharedHelper().enableShoppingCart.equals("1")) {
				double discount;
				if (Helper.getSharedHelper().discountType
						.equalsIgnoreCase(Constants.KEY_DEFAULT_DISCOUNT_TYPE)) {

					discount = val
							* Double.parseDouble(Helper.getSharedHelper().discountPercent)
							/ 100;
				} else {

					discount = Double
							.parseDouble(Helper.getSharedHelper().discountPercent);
				}
				val = val - discount;
				if (val < 0) {
					val = 0;
				}
			}

			float newPrice = (float) val;
			newPrice = Helper.getSharedHelper().totalPriceAfterDiscount(
					newPrice);
			newPrice = newPrice * conversionValue;
			if (Helper.getSharedHelper().isDecialFromat) {
				textViewNewPrice.setText(Helper.getSharedHelper()
						.getCurrencySymbol(selectedCurrencyCode)
						+ Helper.getSharedHelper()
								.conertfloatToSTring(newPrice));
			} else {
				textViewNewPrice.setText(Helper.getSharedHelper()
						.getCurrencySymbol(selectedCurrencyCode)
						+ Helper.getSharedHelper()
								.conertfloatToSTring(newPrice));
			}
		} catch (Exception e) {
			if (Helper.getSharedHelper().isDecialFromat) {
				textViewNewPrice.setText(Helper.getSharedHelper()
						.getCurrencySymbol(selectedCurrencyCode)
						+ Helper.getSharedHelper().conertfloatToSTring(0.00f));
			} else {
				textViewNewPrice.setText(Helper.getSharedHelper()
						.getCurrencySymbol(selectedCurrencyCode)
						+ Helper.getSharedHelper().conertfloatToSTring(0.00f));
			}
			e.printStackTrace();
		}
	}

	void setFont() {
		try {
			tvDistance.setTextColor(Color.parseColor("#"
					+ retailer.getHeaderColor()));
			tvAddress.setTypeface(Helper.getSharedHelper().normalFont);
			tvDistance.setTypeface(Helper.getSharedHelper().boldFont);
			tvNewPrice.setTypeface(Helper.getSharedHelper().boldFont);
			tvOldPrice.setTypeface(Helper.getSharedHelper().normalFont);
			textViewHeader.setTypeface(Helper.getSharedHelper().boldFont);
			txtCartTotal.setTypeface(Helper.getSharedHelper().normalFont);
			btnSubmit.setTypeface(Helper.getSharedHelper().boldFont);
			buttonBuy.setTypeface(Helper.getSharedHelper().boldFont);
			btnEnquiry.setTypeface(Helper.getSharedHelper().boldFont);
			btn_submitReview.setTypeface(Helper.getSharedHelper().boldFont);

			tvEnterCode.setTypeface(Helper.getSharedHelper().normalFont);

			textViewName.setTypeface(Helper.getSharedHelper().normalFont);
			textViewShortDesc.setTypeface(Helper.getSharedHelper().normalFont);
			tvRewardsPoints.setTypeface(Helper.getSharedHelper().normalFont);
			textViewDetail.setTypeface(Helper.getSharedHelper().normalFont);
			textViewHowItWorks.setTypeface(Helper.getSharedHelper().normalFont);
			tv_testimonial_txt.setTypeface(Helper.getSharedHelper().normalFont);
			tvReadReviews.setTypeface(Helper.getSharedHelper().boldFont);
			textViewNewPrice.setTypeface(Helper.getSharedHelper().boldFont);
			tvQty.setTypeface(Helper.getSharedHelper().boldFont);
			editTextQty.setTypeface(Helper.getSharedHelper().boldFont);
			edt_comments.setTypeface(Helper.getSharedHelper().normalFont);
			edt_Name.setTypeface(Helper.getSharedHelper().normalFont);
			edt_email.setTypeface(Helper.getSharedHelper().normalFont);

			tvFirstOptinLbl.setTypeface(Helper.getSharedHelper().normalFont);
			tvSeconoptionLbl.setTypeface(Helper.getSharedHelper().normalFont);
			tvFirstOptionValue.setTypeface(Helper.getSharedHelper().normalFont);
			tvSecondOptionValue
					.setTypeface(Helper.getSharedHelper().normalFont);
		} catch (Exception e) {

		}
	}

	void showShareIntent() {
		// String PACKAGE_NAME = getApplicationContext().getPackageName();
		// String shareString = product.getShortDescription()
		// + "\nProduct Price :"
		// + Helper.getSharedHelper().currency_symbol
		// + " "
		// + product.getNewPrice()
		// +
		// "\nDownload app here : https://play.google.com/store/apps/details?id="
		// + PACKAGE_NAME;
		// Intent i = new Intent(Intent.ACTION_SEND);
		// i.setType("*/*");
		// String productName = product.getName();
		// String appname = getResources().getString(R.string.app_name);
		// i.putExtra(Intent.EXTRA_SUBJECT, "Super Deal on:" + appname);
		// i.putExtra(Intent.EXTRA_TITLE, productName);
		// i.putExtra(Intent.EXTRA_TEXT, shareString);
		// BitmapDrawable bitmapDrawable = ((BitmapDrawable) imageView
		// .getDrawable());
		// Bitmap bitmap = imageCacheloader.imageforUrl(product.getImage());//
		// bitmapDrawable
		// // .getBitmap();
		// // imageView.setImageBitmap(bitmap);
		// File media = new File(saveToInternalSorage(bitmap));
		// Uri uri = Uri.fromFile(media);
		//
		// // Add the URI and the caption to the Intent.
		// i.putExtra(Intent.EXTRA_STREAM, uri);
		//
		// i.putExtra(Intent.EXTRA_HTML_TEXT, shareString);
		// startActivity(Intent.createChooser(i, "Share"));

		List<Intent> targetedShareIntents = new ArrayList<Intent>();

		Intent wechat = getShareIntent(SHARINGTYPE.WECHAT);
		if (wechat != null)
			targetedShareIntents.add(wechat);

		Intent facebookIntent = getShareIntent(SHARINGTYPE.FACEBOOK);
		if (facebookIntent != null)
			targetedShareIntents.add(facebookIntent);

		Intent email = getShareIntent(SHARINGTYPE.EMAIL);
		if (email != null)
			targetedShareIntents.add(email);

		Intent whatsapp = getShareIntent(SHARINGTYPE.WHATSAPP);
		if (whatsapp != null)
			targetedShareIntents.add(whatsapp);

		Intent instagram = getShareIntent(SHARINGTYPE.INSTAGRAM);
		if (instagram != null)
			targetedShareIntents.add(instagram);

		Intent chooser = Intent.createChooser(targetedShareIntents.remove(0),
				"Share");

		chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,
				targetedShareIntents.toArray(new Parcelable[] {}));

		startActivity(chooser);
	}

	private Intent getShareIntent(SHARINGTYPE type) {
		boolean found = false;
		// Intent share = new Intent(android.content.Intent.ACTION_SEND);
		// share.setType("text/plain");
		//
		// // gets the list of intents that can be loaded.
		// List<ResolveInfo> resInfo = this.getPackageManager()
		// .queryIntentActivities(share, 0);
		// System.out.println("resinfo: " + resInfo);
		// if (!resInfo.isEmpty()) {
		// for (ResolveInfo info : resInfo) {
		// if (info.activityInfo.packageName.toLowerCase().contains(type)
		// || info.activityInfo.name.toLowerCase().contains(type)) {
		// share.putExtra(Intent.EXTRA_SUBJECT, subject);
		// share.putExtra(Intent.EXTRA_TEXT, text);
		// share.setPackage(info.activityInfo.packageName);
		// found = true;
		// break;
		// }
		// }
		// if (!found)
		// return null;
		//
		// return share;
		// }
		String appType = "";
		switch (type) {
		case EMAIL:
			appType = "gmail";
			break;
		case FACEBOOK:
			appType = "facebook";
			break;
		case WHATSAPP:
			appType = "whatsapp";
			break;
		case WECHAT:
			appType = "tencent";
			break;
		case INSTAGRAM:
			appType = "instagram";
			break;

		default:
			break;
		}

		String PACKAGE_NAME = getApplicationContext().getPackageName();
		String shareString = product.getShortDescription()
				+ "\nProduct Price :"
				+ Helper.getSharedHelper().currency_symbol
				+ " "
				+ product.getNewPrice()
				+ "\nDownload app here : https://play.google.com/store/apps/details?id="
				+ PACKAGE_NAME;
		Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType("*/*");
		String productName = product.getName();
		String appname = getResources().getString(R.string.app_name);
		intent.putExtra(Intent.EXTRA_SUBJECT, "Super Deal on:" + appname);
		intent.putExtra(Intent.EXTRA_TITLE, productName);
		intent.putExtra(Intent.EXTRA_TEXT, shareString);
		List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(
				intent, 0);
		System.out.println("resinfo: " + resInfo);
		if (!resInfo.isEmpty()) {
			for (ResolveInfo info : resInfo) {
				if (info.activityInfo.packageName.toLowerCase().contains(
						appType)
						|| info.activityInfo.name.toLowerCase().contains(
								appType)) {
					intent.setPackage(info.activityInfo.packageName);
				}
			}
		}

		if (type == SHARINGTYPE.WECHAT) {

		} else if (type == SHARINGTYPE.FACEBOOK) {
			intent.setType("text/plain");
		} else if (type == SHARINGTYPE.INSTAGRAM) {
			intent.setType("image/*");
			Bitmap bitmap = imageCacheloader.imageforUrl(product.getImage());// bitmapDrawable
			// .getBitmap();
			// imageView.setImageBitmap(bitmap);
			File media = new File(saveToInternalSorage(bitmap));
			Uri uri = Uri.fromFile(media);

			// Add the URI and the caption to the Intent.
			intent.putExtra(Intent.EXTRA_STREAM, uri);
		} else {
			// BitmapDrawable bitmapDrawable = ((BitmapDrawable) imageView
			// .getDrawable());
			Bitmap bitmap = imageCacheloader.imageforUrl(product.getImage());// bitmapDrawable
																				// .getBitmap();
			// imageView.setImageBitmap(bitmap);
			File media = new File(saveToInternalSorage(bitmap));
			Uri uri = Uri.fromFile(media);

			// Add the URI and the caption to the Intent.
			intent.putExtra(Intent.EXTRA_STREAM, uri);
		}
		return intent;
	}

	private String saveToInternalSorage(Bitmap bitmapImage) {
		ContextWrapper cw = new ContextWrapper(getApplicationContext());
		// path to /data/data/yourapp/app_data/imageDir
		// File directory = cw.getDir("imageDir", Context.MODE_WORLD_WRITEABLE);
		File file = new File(
				context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
				"profile.jpg");
		// Create imageDir
		// File mypath=new File(directory,"profile.jpg");

		FileOutputStream fos = null;
		try {

			fos = new FileOutputStream(file);

			// Use the compress method on the BitMap object to write image to
			// the OutputStream
			bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}

	public void cartPressed(View v) {
		if (Helper.getSharedHelper().shoppintCartList.size() > 0) {
			Intent intent = new Intent(this, ShoppingCartActivity.class);

			startActivity(intent);
		} else {
			Toast.makeText(context, "Your shopping cart is empty.",
					Toast.LENGTH_LONG).show();
		}

	}

	public void buyPressed(View v) {
		String errorMsg = null;
		List<ProductOption> productOption = product.getProduct_options();
		// if (productOption != null) {
		// if (productOption.size() > 0) {
		// ProductOption option = productOption.get(0);
		// if (product.getSelectedOption1() == null) {
		// errorMsg = "Please select " + option.optionLabel;
		// }
		//
		// }
		// if (productOption.size() > 1) {
		// ProductOption option = productOption.get(1);
		// if (product.getSelectedOption2() == null) {
		// errorMsg = "Please select " + option.optionLabel;
		// }
		// }
		// }
		//
		// if (errorMsg != null) {
		// Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
		// return;
		// }

		if (editTextQty.getText().toString().length() != 0
				&& !editTextQty.getText().toString().equalsIgnoreCase("0")) {
			Boolean isLoggedIn = spref.getBoolean(
					Constants.KEY_IS_USER_LOGGED_IN, false);
			if (!isLoggedIn) {
				Intent in = new Intent(EShopDetailActivity.this,
						ProfileActivity.class);
				in.putExtra("FROM", "CARTLOGIN");
				startActivityForResult(in, Constants.LOGIN_SUCCESS);
			} else {
				requestPayPalToken();
			}

		} else {
			Toast.makeText(context, "Please enter a valid quantity.",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void submitPressed(View v) {
		float rating = productRatingBar.getRating();
		if (rating > 0) {
			if (Utils.hasNetworkConnection(context)) {
				new AsyncRating().execute();
			}
		}

	}

	private final class AsyncRating extends AsyncTask<Void, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog();
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			// sqliteHelper.openDataBase();
			// profile = sqliteHelper.getProfile();
			// sqliteHelper.close();

			JSONObject param = new JSONObject();
			try {
				param.put(Constants.PARAM_RETAILER_ID, Constants.RETAILER_ID);
				param.put(Constants.PARAM_PRODUCTID_FOR_TOKEN, product.getId());
				param.put(Constants.PARAM_RATING,
						Float.toString(productRatingBar.getRating()));

				JSONObject jsonObject = HTTPHandler.defaultHandler().doPost(
						Constants.URL_SEND_RATING, param);

				return jsonObject;

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		}

		@Override
		protected void onPostExecute(JSONObject json) {
			dismissLoadingDialog();
			if (json != null && json.has("errorMessage")) {
				try {
					Toast.makeText(context, json.getString("errorMessage"),
							Toast.LENGTH_SHORT).show();

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		if (Helper.getSharedHelper().enableShoppingCart.equals("1")) {
			if (txtCartTotal != null) {
				txtCartTotal.setText(Helper.getSharedHelper().getCartTotal());
			}
		} else {
		}
		if (viewFlipper.getDisplayedChild() == 1) {
			if (imageBanner != null) {
				imageBanner.startTimer();
			}
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (imageBanner != null) {
			imageBanner.stopTimer();
		}
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (imageBanner != null) {
			imageBanner.stopTimer();
		}
	}

	private void showCouponAlert() {
		try {
			DisplayMetrics metrics = getResources().getDisplayMetrics();
			int width = metrics.widthPixels;

			// final AlertDialog dialog = new AlertDialog.Builder(
			// this).create();
			// dialog.setCancelable(false);
			// dialog.setContentView(R.layout.dialog_enter_code);

			final Dialog dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setCancelable(false);
			dialog.setContentView(R.layout.dialog_enter_code);

			dialog.getWindow().setLayout((6 * width) / 7,
					LayoutParams.WRAP_CONTENT);

			Button buttonApply = (Button) dialog.findViewById(R.id.buttonApply);
			Button buttonClose = (Button) dialog.findViewById(R.id.buttonClose);
			resultImage = (ImageView) dialog.findViewById(R.id.imageView);
			resultImage.setVisibility(View.GONE);
			tvDiscountInfo = (TextView) dialog
					.findViewById(R.id.tvDiscountInfo);
			tvDiscountInfo.setVisibility(View.GONE);

			edtCoupon = (EditText) dialog.findViewById(R.id.edtCoupon);
			lineTop = (View) dialog.findViewById(R.id.lineTop);

			edtCoupon
					.setBackgroundDrawable(getGradientDrawableEditText(retailer
							.getHeaderColor()));
			buttonApply.setBackgroundDrawable(getGradientDrawable(retailer
					.getHeaderColor()));
			buttonClose.setBackgroundDrawable(getGradientDrawable(retailer
					.getHeaderColor()));
			lineTop.setBackgroundDrawable(getGradientDrawable(retailer
					.getHeaderColor()));

			buttonApply.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					new AsyncApplyCoupon().execute();
				}
			});
			buttonClose.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					dialog.dismiss();
				}
			});

			buttonApply.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));
			buttonClose.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));
			buttonApply.setTypeface(Helper.getSharedHelper().boldFont);
			buttonClose.setTypeface(Helper.getSharedHelper().boldFont);
			tvDiscountInfo.setTypeface(Helper.getSharedHelper().boldFont);
			dialog.show();
		} catch (Exception e) {
		}

	}

	private final class AsyncApplyCoupon extends
			AsyncTask<Void, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog();
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			// sqliteHelper.openDataBase();
			// profile = sqliteHelper.getProfile();
			// sqliteHelper.close();
			JSONObject param = new JSONObject();
			try {
				param.put(Constants.PARAM_RETAILER_ID, Constants.RETAILER_ID);
				param.put("creditCode", edtCoupon.getText().toString());
				param.put(Constants.PARAM_PRODUCTID_FOR_TOKEN, product.getId());
				JSONObject jsonObject = HTTPHandler.defaultHandler().doPost(
						Constants.URL_APPLY_COUPON, param);

				return jsonObject;

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		}

		@Override
		protected void onPostExecute(JSONObject json) {
			dismissLoadingDialog();
			if (json != null) {
				if (json.has("errorCode")) {
					try {
						resultImage.setVisibility(View.VISIBLE);
						tvDiscountInfo.setVisibility(View.VISIBLE);
						String errorCode = json.getString("errorCode");
						if (errorCode != null && errorCode.equals("1")) {
							String discount = "0";
							if (json.has("discount")) {
								discount = json.getString("discount");
							}
							if (json.has("discountType")) {
								Helper.getSharedHelper().discountType = json
										.getString("discountType");
								;
							}
							if (Helper.getSharedHelper().discountType
									.equalsIgnoreCase(Constants.KEY_DEFAULT_DISCOUNT_TYPE)) {
								tvDiscountInfo.setText(discount
										+ "% discount awarded");
							} else {
								tvDiscountInfo
										.setText(Helper.getSharedHelper().currency_code
												+ " "
												+ discount
												+ " discount awarded");
							}
							resultImage.setImageResource(R.drawable.success);
							Helper.getSharedHelper().discountPercent = discount;

							updatePrice();
						} else {
							resultImage.setImageResource(R.drawable.fail);
							tvDiscountInfo.setText("Invalid Voucher");
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						resultImage.setImageResource(R.drawable.fail);
						tvDiscountInfo.setText("Invalid Voucher");
						e.printStackTrace();
					}
				}
			} else {
				Toast.makeText(context, "Internal Error occured",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private final HorizontalPager.OnScreenSwitchListener onScreenSwitchListener = new HorizontalPager.OnScreenSwitchListener() {
		@Override
		public void onScreenSwitched(final int screen) {
			// Check the appropriate button when the user swipes screens.
			switch (screen) {
			case 0:

				if (videoView.getVisibility() == View.VISIBLE) {
					videoView.pause();
					videoView.setMediaController(null);
				}
				visiblePage = 1;

				break;
			case 1:
				if (videoView.getVisibility() == View.VISIBLE) {
					videoView.start();
					MediaController ctrl = new MediaController(context);
					videoView.setMediaController(ctrl);
				}
				visiblePage = 2;

				break;
			}
			// updatePageIndictor();
			// handler.removeCallbacks(r);
			// startTimer();
		}
	};

	// void loadGifPage2() {
	//
	// if (Utils.hasNetworkConnection(getApplicationContext())) {
	// webview.setVisibility(View.VISIBLE);
	// imageView2.setVisibility(View.GONE);
	// String htnlString =
	// "<!DOCTYPE html><html><body style=\"background-color:transparent;margin: 0; padding: 0\"><img src=\""
	// + retailer.getRetailerFile()
	// + "\" alt=\"pageNo\" width=\"100%\" height=\"100%\"></body></html>";
	// webview.loadDataWithBaseURL(null, htnlString, "text/html", "UTF-8",
	// null);
	// webview.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
	// } else {
	// webview.setVisibility(View.GONE);
	// imageView2.setVisibility(View.VISIBLE);
	// }
	// }

	@Override
	public void onInitializationFailure(Provider arg0,
			YouTubeInitializationResult arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onInitializationSuccess(Provider arg0, YouTubePlayer player,
			boolean arg2) {
		// TODO Auto-generated method stub
		player.cueVideo("o7VVHhK9zf0");

	}

	void populateFirstList(ProductOption option) {

		tvFirstOptinLbl.setText(option.optionLabel);
		firstList = new ArrayList<String>();
		// firstList.add("Select " + option.optionLabel);

		String[] values = option.optionValue.split(",");
		for (String value : values) {
			firstList.add(value);
		}
		// firstList.add("S");
		// firstList.add("M");
		// firstList.add("L");
		// firstList.add("Xl");
		// firstList.add("XXl");
		tvFirstOptionValue.setText("Select " + option.optionLabel);

		rlFirstOption.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				selectedOptionIndex = 0;
				showMenuOption();

				// sp_option1.performClick();
				// showtemppopup();
			}
		});
		sp_option1.setAdapter(getAdapter(firstList));
		sp_option1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				tvFirstOptionValue.setText(firstList.get(arg2));

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});
	}

	void showtemppopup() {
		PopupWindow popupWindow = new PopupWindow(this);

		ListView listViewDogs = new ListView(this);

		// set our adapter and pass our pop up window contents
		listViewDogs.setAdapter(dogsAdapter(firstList));

		// set the item click listener
		listViewDogs.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub

			}
		});
		popupWindow.setFocusable(true);
		popupWindow.setWidth(250);
		popupWindow.setHeight(500);
		popupWindow.showAsDropDown(vwFirstOptionUnderline, -5, 0);
	}

	void populateSecondList(ProductOption option) {
		tvSeconoptionLbl.setText(option.optionLabel);
		secondList = new ArrayList<String>();
		// secondList.add("Red color product");
		// secondList.add("Green");
		// secondList.add("Blue");
		// secondList.add("Black");

		String[] values = option.optionValue.split(",");
		for (String value : values) {
			secondList.add(value);
		}
		tvSecondOptionValue.setText("Select " + option.optionLabel);
		rlSecondOption.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				selectedOptionIndex = 1;
				showMenuOption();
			}
		});
	}

	void showMenuOption() {
		listPopupWindow = new ListPopupWindow(this);
		if (selectedOptionIndex == 0) {
			listPopupWindow.setAdapter(new ArrayAdapter(this,
					R.layout.list_item, firstList));
			listPopupWindow.setAnchorView(vwFirstOptionUnderline);
		} else if (selectedOptionIndex == 1) {
			listPopupWindow.setAdapter(new ArrayAdapter(this,
					R.layout.list_item, secondList));
			listPopupWindow.setAnchorView(vwSecondOptionUnderline);
		}

		listPopupWindow.setWidth(vwFirstOptionUnderline.getWidth());
		// listPopupWindow
		// .setHeight(android.app.ActionBar.LayoutParams.WRAP_CONTENT);
		listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);

		// listPopupWindow.setBackgroundDrawable(getResources().getDrawable(
		// R.drawable.boarder_around));
		listPopupWindow.setModal(true);
		// listPopupWindow.getListView().setDividerHeight(2);
		// listPopupWindow.getListView().setDivider(new
		// ColorDrawable(Color.parseColor("#b6b6b6")));;
		listPopupWindow.setOnItemClickListener(this);
		listPopupWindow.show();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

		if (selectedOptionIndex == 0) {
			tvFirstOptionValue.setText(firstList.get(arg2));
			product.setSelectedOption1(firstList.get(arg2));
		} else if (selectedOptionIndex == 1) {
			tvSecondOptionValue.setText(secondList.get(arg2));
			product.setSelectedOption2(secondList.get(arg2));
		}
		listPopupWindow.dismiss();
	}

	ArrayAdapter<String> getAdapter(List<String> list) {
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				R.layout.list_item, list) {
			public View getView(int position, View convertView,
					android.view.ViewGroup parent) {

				TextView v = (TextView) super.getView(position, convertView,
						parent);
				v.setTypeface(Helper.getSharedHelper().normalFont);
				return v;
			}

			public View getDropDownView(int position, View convertView,
					android.view.ViewGroup parent) {
				TextView v = (TextView) super.getView(position, convertView,
						parent);
				v.setTypeface(Helper.getSharedHelper().normalFont);

				return v;
			}
		};
		return adapter1;
	}

	private ArrayAdapter<String> dogsAdapter(List<String> dogsArray) {

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, dogsArray) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				// setting the ID and text for every items in the list
				String item = getItem(position);
				String[] itemArr = item.split("::");
				String text = itemArr[0];
				String id = itemArr[1];

				// visual settings for the list item
				TextView listItem = new TextView(EShopDetailActivity.this);

				listItem.setText(text);
				listItem.setTag(id);
				listItem.setTextSize(22);
				listItem.setPadding(10, 10, 10, 10);
				listItem.setTextColor(Color.WHITE);

				return listItem;
			}
		};

		return adapter;
	}

	void animateItemToCart() {
		imgAnimationImage.setVisibility(View.VISIBLE);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		int height = displaymetrics.heightPixels;
		int width = displaymetrics.widthPixels;
		int[] strtCoordinates = { 0, 0 };
		int[] endCoordinates = { 0, 0 };
		buttonBuy.getLocationOnScreen(strtCoordinates);
		cartView.getLocationOnScreen(endCoordinates);
		int xStarst, yStart, xEnd, yEnd;
		xStarst = strtCoordinates[0] + imgAnimationImage.getWidth();
		yStart = strtCoordinates[1] - imgAnimationImage.getHeight();
		xEnd = endCoordinates[0];
		yEnd = -yStart;
		yStart = height;
		// TranslateAnimation tAnimation = new TranslateAnimation(xStarst, xEnd,
		// yStart, yEnd);
		BezierTranslationAnimation tAnimation = new BezierTranslationAnimation(
				width + 2 * imgAnimationImage.getWidth(), xEnd, yStart, yEnd,
				-250, -height / 2);
		tAnimation.setDuration(2500);
		tAnimation.setRepeatCount(0);
		tAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
		tAnimation.setFillAfter(true);
		tAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				// imgAnimationImage.setAlpha(1);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				Toast.makeText(context, "Product added to cart sucessfully.",
						Toast.LENGTH_LONG).show();

				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						imgAnimationImage.setVisibility(View.INVISIBLE);
						txtCartTotal.setText(Helper.getSharedHelper()
								.getCartTotal());
						if (Helper.getSharedHelper().reatiler.enableGiftWrap
								.equalsIgnoreCase("1")) {
							showGiftWarpAlert();
						}

					}
				});
				imgAnimationImage.setVisibility(View.INVISIBLE);
				// imgAnimationImage.setAlpha(0);
			}
		});

		Animation animationScaleDown = AnimationUtils.loadAnimation(this,
				R.anim.scale_down);
		AnimationSet growShrink = new AnimationSet(true);
		growShrink.addAnimation(animationScaleDown);

		AnimationSet animations = new AnimationSet(true);
		animations.addAnimation(growShrink);
		animations.addAnimation(tAnimation);
		imgAnimationImage.startAnimation(animations);
		// imgAnimationImage.startAnimation(growShrink);
		//
		// imgAnimationImage.startAnimation(tAnimation);
	}

	void showGiftWarpAlert() {

		final Product addedProduct = Helper.getSharedHelper().getProduct(
				product.getId());
		if (addedProduct != null && !addedProduct.getIsOptedGiftWrap()) {
			DisplayMetrics metrics = getResources().getDisplayMetrics();
			int width = metrics.widthPixels;

			// final AlertDialog dialog = new AlertDialog.Builder(
			// this).create();
			// dialog.setCancelable(false);
			// dialog.setContentView(R.layout.dialog_enter_code);

			final Dialog dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setCancelable(false);
			dialog.setContentView(R.layout.dialog_gift_wrap);

			dialog.getWindow().setLayout((6 * width) / 7,
					LayoutParams.WRAP_CONTENT);

			RelativeLayout btnClose = (RelativeLayout) dialog
					.findViewById(R.id.btnClose);
			TextView tvMessage;
			final EditText etMessage = (EditText) dialog
					.findViewById(R.id.etMessage);
			final RadioGroup radioGroup = (RadioGroup) dialog
					.findViewById(R.id.radioGroup);
			;
			RadioButton rdHim, rdHer;

			Button btnWrapIt = (Button) dialog.findViewById(R.id.btnWrapIt);
			rdHim = (RadioButton) dialog.findViewById(R.id.rdHim);
			rdHer = (RadioButton) dialog.findViewById(R.id.rdHer);

			try {
				tvMessage = (TextView) dialog.findViewById(R.id.tvMessage);

				tvMessage.setTypeface(Helper.getSharedHelper().normalFont);
				etMessage.setTypeface(Helper.getSharedHelper().normalFont);
				rdHim.setTypeface(Helper.getSharedHelper().normalFont);
				rdHer.setTypeface(Helper.getSharedHelper().normalFont);
				btnWrapIt.setTypeface(Helper.getSharedHelper().boldFont);
				etMessage
						.setBackgroundDrawable(Helper.getSharedHelper()
								.getGradientDrawableEditText(
										retailer.getHeaderColor()));
				btnWrapIt.setBackgroundDrawable(getGradientDrawable(retailer
						.getHeaderColor()));
				btnWrapIt.setTextColor(Color.parseColor("#"
						+ retailer.getRetailerTextColor()));
				btnWrapIt.setText("Gift Wrap "
						+ Helper.getSharedHelper().currency_code
						+ Helper.getSharedHelper().reatiler.gift_price);
			} catch (Exception e) {
				// TODO: handle exception
			}

			btnClose.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					dialog.dismiss();
				}
			});
			btnWrapIt.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					addedProduct.setIsOptedGiftWrap(true);
					addedProduct.setGiftMsg(etMessage.getText().toString());
					if (radioGroup.getCheckedRadioButtonId() == R.id.rdHim) {
						addedProduct.setGiftTo("Male");
					} else {
						addedProduct.setGiftTo("Female");
					}
					dialog.dismiss();

				}
			});

			dialog.show();
		}
	}

	@Override
	public void reviewSubmitted(Boolean isSucess, String msg) {
		// TODO Auto-generated method stub
		if (isSucess) {
			edt_comments.setText("");
			edt_Name.setText("");
			edt_email.setText("");
			Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
		} else {
			Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case IntentIntegrator.REQUEST_CODE:
			IntentResult scanResult = IntentIntegrator.parseActivityResult(
					requestCode, resultCode, data);
			if (scanResult == null) {
				return;
			}
			final String result = scanResult.getContents();
			if (result != null) {
				if (Helper.getSharedHelper().isScanAdminPWD) {

				} else {
					searchText(result.trim());
				}

			}
			break;
		case Constants.LOGIN_SUCCESS: {
			Boolean isLoggedIn = spref.getBoolean(
					Constants.KEY_IS_USER_LOGGED_IN, false);
			if (isLoggedIn) {
				requestPayPalToken();
			}
		}
			break;
		default:
		}
	}

	void requestPayPalToken() {

		String emailId = spref.getString(Constants.KEY_EMAIL, "");
		PayPalDataHandler payPalDataHandler = new PayPalDataHandler(this,
				product.getId(), emailId, editTextQty.getText().toString());
		payPalDataHandler.getpayPalData();
		showLoadingDialog();
	}

	@Override
	public void payPalDataDownloaded(PayPalModelObject payPalObj) {
		// TODO Auto-generated method stub
		dismissLoadingDialog();
		Bundle bundle = new Bundle();

		bundle.putSerializable("product", product);
		if (Helper.getSharedHelper().reatiler.enablePay.equalsIgnoreCase("1")) {
			bundle.putString("token", payPalObj.token);
		} else {
			bundle.putString("redirectUrl", payPalObj.redirectUrl);
		}

		bundle.putString("sucessUrl", payPalObj.sucessUrl);
		bundle.putString("cancelUrl", payPalObj.cancelUrl);
		if (payPalObj.paypalMode != null) {
			bundle.putString("paypalMode", payPalObj.paypalMode);
		}
		float unitPrice = Float.parseFloat(product.getNewPrice());
		float totalPrice = unitPrice
				* Integer.parseInt(editTextQty.getText().toString());
		bundle.putString("grandTotal", Float.toString(Helper.getSharedHelper()
				.totalPriceAfterDiscount(totalPrice)));

		Intent i = new Intent(context, PaypalActivity.class);
		i.putExtras(bundle);
		startActivity(i);
	}

	@Override
	public void payPalDataFailed(String errorMsg) {
		// TODO Auto-generated method stub
		dismissLoadingDialog();
		Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
	}
}
