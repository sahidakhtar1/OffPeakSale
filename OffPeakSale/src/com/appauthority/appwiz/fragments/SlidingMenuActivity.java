package com.appauthority.appwiz.fragments;

import java.util.ArrayList;
import java.util.List;

import jim.h.common.android.zxinglib.integrator.IntentIntegrator;
import jim.h.common.android.zxinglib.integrator.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.appauthority.appwiz.interfaces.AppThemeUpdateListener;
import com.appauthority.appwiz.interfaces.ProductDetailCaller;
import com.appauthority.appwiz.interfaces.UserProfileCaller;
import com.appsauthority.appwiz.EShopDetailActivity;
import com.appsauthority.appwiz.ProductDetailHandler;
import com.appsauthority.appwiz.ProfileActivity;
import com.appsauthority.appwiz.ShoppingCartActivity;
import com.appsauthority.appwiz.UserProfileDataHandler;
import com.appsauthority.appwiz.VoucherDisplayActivity;
import com.appsauthority.appwiz.adapters.AboutUsListAdapter;
import com.appsauthority.appwiz.adapters.CurrencyListAdapter;
import com.appsauthority.appwiz.adapters.EShopCategoryListAdapter;
import com.appsauthority.appwiz.adapters.FeaturedStoreListAdapter;
import com.appsauthority.appwiz.adapters.NavDrawerListAdapter;
import com.appsauthority.appwiz.custom.BaseActivity;
import com.appsauthority.appwiz.fragments.EShopListFragment;
import com.appsauthority.appwiz.models.CategoryObject;
import com.appsauthority.appwiz.models.CategoryResponseObject;
import com.appsauthority.appwiz.models.FeaturedStore;
import com.appsauthority.appwiz.models.NavDrawerItem;
import com.appsauthority.appwiz.models.Product;
import com.appsauthority.appwiz.models.ProductResponse;
import com.appsauthority.appwiz.models.Retailer;
import com.appsauthority.appwiz.models.Voucher;
import com.appsauthority.appwiz.models.VoucherList;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.Constants.DrawerItemType;
import com.appsauthority.appwiz.utils.HTTPHandler;
import com.appsauthority.appwiz.utils.Helper;
import com.appsauthority.appwiz.utils.Utils;
import com.google.gson.Gson;
import com.offpeaksale.consumer.R;

public class SlidingMenuActivity extends BaseActivity implements
		SensorListener, AppThemeUpdateListener, UserProfileCaller,
		ProductDetailCaller {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList, eshop_category_list;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	Boolean isFeaturedStore = false;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	private EShopCategoryListAdapter eShopCategoryAdapter;
	FeaturedStoreListAdapter featuredStoreAdapter;
	CurrencyListAdapter curencyListAdapter;
	AboutUsListAdapter aboutUsAdapter;

	private ViewFlipper viewFlipper;
	private Retailer retailer;
	private ArrayList<CategoryObject> eshop_Category;
	private ArrayList<FeaturedStore> featuredStores;
	private ArrayList<String> currencies;
	private SharedPreferences spref;
	List<CategoryObject> categoryList;
	ArrayList<String> eShopCategories;
	ArrayList<String> aboutUsList;
	TextView textViewHeader;

	LinearLayout cartView;
	TextView txtCartTotal;

	ImageView btnSearch;
	AutoCompleteTextView etSearch;
	SensorManager sensorMgr;
	long lastUpdate;
	float x, y, z, last_x = -1.0f, last_y = -1.0f, last_z = -1.0f;
	private static final int SHAKE_THRESHOLD = 5000;
	ImageView imageViewOverflow, imageScan;

	Boolean isFeaturedStoresListed = false;
	private List<Voucher> voucherList = new ArrayList<Voucher>();

	void intializeShakeDetector() {
		sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
		sensorMgr.registerListener(this, SensorManager.SENSOR_ACCELEROMETER,
				SensorManager.SENSOR_DELAY_GAME);
	}

	Constants.DrawerItemType selectedMenuIndex = DrawerItemType.NONE;
	Boolean isCurrencyExpanded = false;
	WebFragment webFragment;

	enum SelectedList {
		ESHOP, FEATUREDSTORE, CURRENCY, PROFILE, ABOUTUS,
	}

	SelectedList selectedList = SelectedList.ESHOP;

	EShopListFragment eshopFragment;
	LoyalityFragment loyaltyFragment;
	OrderHistoryFragment orderHistoryFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sliding_menu_activity_main);
		spref = PreferenceManager.getDefaultSharedPreferences(this);
		intializeShakeDetector();
		retailer = Helper.getSharedHelper().reatiler;
		if (Helper.getSharedHelper().currency_code.equalsIgnoreCase("")) {
			Helper.getSharedHelper().currency_code = retailer.defaultCurrency;
		}

		mTitle = mDrawerTitle = getTitle();
		eshop_Category = new ArrayList<CategoryObject>();
		eShopCategories = new ArrayList<String>();
		featuredStores = new ArrayList<FeaturedStore>();
		currencies = new ArrayList<String>();
		aboutUsList = new ArrayList<String>();
		// populateAboutUs();
		aboutUsAdapter = new AboutUsListAdapter(getApplicationContext(),
				R.layout.about_us_list_item, aboutUsList);

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		eshop_category_list = (ListView) findViewById(R.id.eshop_category_list);
		mDrawerList.setTag(0);
		eshop_category_list.setTag(1);

		navDrawerItems = new ArrayList<NavDrawerItem>();
		populateDrawerItem();

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		eShopCategoryAdapter = new EShopCategoryListAdapter(
				getApplicationContext(), eshop_Category);
		featuredStoreAdapter = new FeaturedStoreListAdapter(
				getApplicationContext(), featuredStores);
		curencyListAdapter = new CurrencyListAdapter(getApplicationContext(),
				R.layout.filter_list_item, currencies);

		eshop_category_list.setAdapter(eShopCategoryAdapter);
		eshop_category_list
				.setOnItemClickListener(new EShopCategoryClickListener());

		setCustomHeader();

		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.profile, // nav menu toggle icon
				R.string.app_name, // nav drawer open - description for
									// accessibility
				R.string.app_name // nav drawer close - description for
									// accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
				if (eshopFragment != null) {
					eshopFragment.refreshList();
				}
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
				populateDrawerItem();
				adapter.notifyDataSetChanged();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			Intent intent = getIntent();
			Boolean isFromPayPal = intent.getBooleanExtra(
					Constants.KEY_FROM_PAYPAL, false);
			Boolean isFromSearch = intent.getBooleanExtra(
					Constants.KEY_FROM_SEARCH, false);
			if (isFromSearch) {
				String keyword = intent.getStringExtra(Constants.KEY_KEYWORD);
				searchText(keyword);
			}
			// else if (isFromPayPal) {
			// showEShop(0);
			// }
			else {
				displayView(DrawerItemType.ESHOP, null);
				textViewHeader.setText(retailer.getRetailerName());
			}
		}

		String emailId = spref.getString(Constants.KEY_EMAIL, "");
		if (!emailId.equalsIgnoreCase("")) {
			new UserProfileDataHandler(emailId, this);
		}

		// showDummyVoucher();
		Intent intent = getIntent();
		String pid = intent.getStringExtra("pid");
		if (pid != null) {
			loadProductDetail(pid);
		}

	}

	void populateDrawerItem() {
		navDrawerItems.clear();
		if (Helper.getSharedHelper().reatiler.featuredStores != null
				&& Helper.getSharedHelper().reatiler.featuredStores.size() > 0) {
			isFeaturedStore = true;
		} else {
			isFeaturedStore = false;
		}

		/*
		 * if (retailer.menuList != null) { for (MenuItem menuItem :
		 * retailer.menuList) { if (menuItem.origName.equalsIgnoreCase("index"))
		 * { NavDrawerItem home = new NavDrawerItem( menuItem.displayName,
		 * R.drawable.home, 0, false); home.itemType = DrawerItemType.HOME;
		 * navDrawerItems.add(home); } else if
		 * (menuItem.origName.equalsIgnoreCase("eshop")) { NavDrawerItem eshop =
		 * new NavDrawerItem( menuItem.displayName, R.drawable.eshop, 1, true);
		 * eshop.itemType = DrawerItemType.ESHOP; navDrawerItems.add(eshop); if
		 * (isFeaturedStore) { NavDrawerItem featuredStore = new NavDrawerItem(
		 * "Featured Store", R.drawable.featured_store, 2, true);
		 * featuredStore.itemType = DrawerItemType.FEATUREDSTORE;
		 * navDrawerItems.add(featuredStore); } } else if
		 * (menuItem.origName.equalsIgnoreCase("loyalty")) { NavDrawerItem
		 * loyalty = new NavDrawerItem( menuItem.displayName,
		 * R.drawable.loyalty, 3, false); loyalty.itemType =
		 * DrawerItemType.LAYALITY; navDrawerItems.add(loyalty);
		 * 
		 * } else if (menuItem.origName.equalsIgnoreCase("feedback")) {
		 * NavDrawerItem feedback = new NavDrawerItem( menuItem.displayName,
		 * R.drawable.feedback, 4, false); feedback.itemType =
		 * DrawerItemType.FEEDBACK; navDrawerItems.add(feedback); } else if
		 * (menuItem.origName.equalsIgnoreCase("lookbook")) { NavDrawerItem
		 * voucher = new NavDrawerItem( menuItem.displayName,
		 * R.drawable.ic_lookbook, 5, false); voucher.itemType =
		 * DrawerItemType.LOOKBOOK; navDrawerItems.add(voucher); } else if
		 * (menuItem.origName.equalsIgnoreCase("contact")) { NavDrawerItem
		 * locate_us = new NavDrawerItem( menuItem.displayName,
		 * R.drawable.locate_us, 6, false); locate_us.itemType =
		 * DrawerItemType.CONTACTUS; navDrawerItems.add(locate_us);
		 * 
		 * } else if (menuItem.origName.equalsIgnoreCase("calendar")) {
		 * NavDrawerItem voucher = new NavDrawerItem( menuItem.displayName,
		 * R.drawable.ic_calendar, 5, false); voucher.itemType =
		 * DrawerItemType.CALENDER; navDrawerItems.add(voucher); } } }
		 */

		// NavDrawerItem home = new NavDrawerItem("Resturants Nearby",
		// R.drawable.home, 0, false);
		// home.itemType = DrawerItemType.ESHOP;
		// navDrawerItems.add(home);

		NavDrawerItem eshop = new NavDrawerItem("Resturants Nearby",
				R.drawable.locate_us, 1, false);
		eshop.itemType = DrawerItemType.ESHOP;
		navDrawerItems.add(eshop);

		NavDrawerItem voucher = new NavDrawerItem("Voucher",
				R.drawable.voucher, 5, false);
		voucher.itemType = DrawerItemType.VOUCHER;
		navDrawerItems.add(voucher);

		NavDrawerItem history = new NavDrawerItem("My Order",
				R.drawable.shop_cart_black, 5, false);
		history.itemType = DrawerItemType.ORDER_HISTORY;
		navDrawerItems.add(history);

		NavDrawerItem my_profile = new NavDrawerItem("My Profile",
				R.drawable.my_profile, 7, true);
		my_profile.itemType = DrawerItemType.PROFILE;
		navDrawerItems.add(my_profile);

		NavDrawerItem tnc = new NavDrawerItem("Terms & Conditions"
		/* + retailer.getRetailerName() */, R.drawable.termsofuse, 8, false);
		tnc.itemType = DrawerItemType.TERMSNCONDITION;
		navDrawerItems.add(tnc);

		NavDrawerItem about_us = new NavDrawerItem("Contact"
		/* + retailer.getRetailerName() */, R.drawable.ic_online_help, 8, false);
		about_us.itemType = DrawerItemType.ABOUTUS;
		navDrawerItems.add(about_us);

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (Helper.getSharedHelper().enableShoppingCart.equals("1")) {
			txtCartTotal.setText(Helper.getSharedHelper().getCartTotal());
		}

	}

	void setNavItemsImage() {
		ImageView imgCart = (ImageView) findViewById(R.id.imgCart);
		retailer = Helper.getSharedHelper().reatiler;
		if (retailer.appIconColor != null
				&& retailer.appIconColor.equalsIgnoreCase("black")) {
			imageViewOverflow.setBackgroundResource(R.drawable.profile_black);
			imageScan.setBackgroundResource(R.drawable.ic_barcode);
			imgCart.setBackgroundResource(R.drawable.shop_cart_black);
			btnSearch.setBackgroundResource(R.drawable.search_black);
		} else {
			imageViewOverflow.setBackgroundResource(R.drawable.profile);
			imageScan.setBackgroundResource(R.drawable.ic_barcode_white);
			imgCart.setBackgroundResource(R.drawable.shop_cart);
			btnSearch.setBackgroundResource(R.drawable.search);
		}

		try {
			textViewHeader.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));
			textViewHeader.setTypeface(Helper.getSharedHelper().boldFont);
			txtCartTotal.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));
			txtCartTotal.setTypeface(Helper.getSharedHelper().normalFont);
			etSearch.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));
			etSearch.setTypeface(Helper.getSharedHelper().normalFont);
			NavDrawerItem item = navDrawerItems.get(0);
			// item.setTitle(Helper.getSharedHelper().reatiler.getRetailerName());
			adapter.notifyDataSetChanged();
			if (selectedMenuIndex == DrawerItemType.HOME) {
				textViewHeader.setText(retailer.getRetailerName());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	void setCustomHeader() {
		// LayoutInflater inflator = (LayoutInflater) this

		// getActionBar().setDisplayShowCustomEnabled(true);
		// getActionBar().setCustomView(v);
		//
		LayoutInflater inflator = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.header, null);

		textViewHeader = (TextView) v.findViewById(R.id.textViewHeader);

		cartView = (LinearLayout) v.findViewById(R.id.cartView);

		txtCartTotal = (TextView) v.findViewById(R.id.txtCartTotal);

		etSearch = (AutoCompleteTextView) v.findViewById(R.id.etSearch);

		etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					// Toast.makeText(
					// SlidingMenuActivity.this,
					// "Searched text is " + etSearch.getText().toString(),
					// Toast.LENGTH_LONG).show();
					etSearch.setVisibility(View.GONE);
					textViewHeader.setVisibility(View.VISIBLE);
					searchText(etSearch.getText().toString().trim());
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(etSearch.getWindowToken(),
							InputMethodManager.RESULT_UNCHANGED_SHOWN);
					return true;
				}
				return false;
			}
		});

		btnSearch = (ImageView) v.findViewById(R.id.btnSearch);
		btnSearch.setVisibility(View.VISIBLE);
		btnSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mDrawerLayout.isDrawerOpen(viewFlipper)) {
					return;
				}
				if (etSearch.getVisibility() == View.VISIBLE) {
					etSearch.setVisibility(View.GONE);
					imageScan.setVisibility(View.GONE);
					textViewHeader.setVisibility(View.VISIBLE);
					imageViewOverflow.setVisibility(View.VISIBLE);
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(etSearch.getWindowToken(),
							InputMethodManager.RESULT_UNCHANGED_SHOWN);
				} else {
					etSearch.setVisibility(View.VISIBLE);
					imageScan.setVisibility(View.VISIBLE);
					textViewHeader.setVisibility(View.GONE);
					imageViewOverflow.setVisibility(View.INVISIBLE);
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.showSoftInput(btnSearch,
							InputMethodManager.SHOW_IMPLICIT);
				}
			}
		});

		imageViewOverflow = (ImageView) v.findViewById(R.id.imageViewOverflow);
		imageScan = (ImageView) v.findViewById(R.id.imageScan);
		imageViewOverflow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (mDrawerLayout.isDrawerOpen(viewFlipper)) {
					mDrawerLayout.closeDrawer(viewFlipper);
				} else {
					adapter.notifyDataSetChanged();
					mDrawerLayout.openDrawer(viewFlipper);
					etSearch.setVisibility(View.GONE);
					textViewHeader.setVisibility(View.VISIBLE);
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(etSearch.getWindowToken(),
							InputMethodManager.RESULT_UNCHANGED_SHOWN);
				}
			}
		});
		imageScan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				imageScan.setVisibility(View.GONE);
				etSearch.setVisibility(View.GONE);
				textViewHeader.setVisibility(View.VISIBLE);
				imageViewOverflow.setVisibility(View.VISIBLE);
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(etSearch.getWindowToken(),
						InputMethodManager.RESULT_UNCHANGED_SHOWN);

				Helper.getSharedHelper().isScanAdminPWD = false;
				IntentIntegrator.initiateScan(SlidingMenuActivity.this,
						R.layout.capture, R.id.viewfinder_view,
						R.id.preview_view, true);
			}
		});
		// v.setBackgroundDrawable(Helper.getSharedHelper().getGradientDrawable(
		// retailer.getHeaderColor()));
		v.setBackgroundColor(android.R.color.transparent);

		getActionBar().setHomeButtonEnabled(true);

		// getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		getActionBar().setDisplayShowTitleEnabled(false);

		getActionBar().setDisplayUseLogoEnabled(false);

		getActionBar().setDisplayShowCustomEnabled(false);

		try {
			getActionBar().setBackgroundDrawable(
					new ColorDrawable(Color.parseColor("#"
							+ retailer.getHeaderColor())));
		} catch (Exception e) {
			// TODO: handle exception
		}

		// getActionBar().setIcon(
		// new ColorDrawable(getResources().getColor(
		// android.R.color.transparent)));
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		getActionBar().setCustomView(v);
		// LayoutParams lp = v.getLayoutParams();
		// lp.width = LayoutParams.MATCH_PARENT;
		// v.setLayoutParams(lp);
		setNavItemsImage();
	}

	/**
	 * Slide menu item click listener
	 * */
	void populateFeaturedStores() {
		featuredStores.clear();
		FeaturedStore store = new FeaturedStore();
		store.storeName = "Back";
		store.storeUrl = "";
		featuredStores.add(store);
		featuredStores.addAll(Helper.getSharedHelper().reatiler.featuredStores);
	}

	void populateCurrencies() {
		currencies.clear();
		currencies.add("Back");
		currencies.add("Profile");
		currencies.add("Currency");
		if (isCurrencyExpanded) {
			String[] allowedCurrency = Helper.getSharedHelper().reatiler.allowedCurrencies
					.split(",");
			for (int i = 0; i < allowedCurrency.length; i++) {
				currencies.add(allowedCurrency[i]);
			}

		}

	}

	void populateAboutUs() {
		aboutUsList.clear();
		aboutUsList.add("Back");
		aboutUsList.add("About Us"/* + retailer.getRetailerName() */);
		aboutUsList.add("Terms of Use");

		// String[] allowedCurrency =
		// Helper.getSharedHelper().reatiler.allowedCurrencies
		// .split(",");
		// for (int i = 0; i < allowedCurrency.length; i++) {
		// currencies.add(allowedCurrency[i]);
		// }

	}

	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			NavDrawerItem navItem = navDrawerItems.get(position);
			// if (navItem.itemType == DrawerItemType.ESHOP) {
			// viewFlipper.showNext();
			// eshop_Category.clear();
			// CategoryObject back = new CategoryObject();
			// back.category_name = "Back";
			// back.id = "";
			// eshop_Category.add(back);
			// eshop_category_list.setAdapter(eShopCategoryAdapter);
			// eShopCategoryAdapter.notifyDataSetChanged();
			// selectedList = SelectedList.ESHOP;
			// new AsyncGetCategories().execute();
			// } else
			if (navItem.itemType == DrawerItemType.FEATUREDSTORE) {
				viewFlipper.showNext();
				populateFeaturedStores();
				eshop_category_list.setAdapter(featuredStoreAdapter);
				featuredStoreAdapter.notifyDataSetChanged();
				selectedList = SelectedList.FEATUREDSTORE;
			}
			// else if (navItem.itemType == DrawerItemType.PROFILE) {
			// viewFlipper.showNext();
			// populateCurrencies();
			// eshop_category_list.setAdapter(curencyListAdapter);
			// curencyListAdapter.notifyDataSetChanged();
			// selectedList = SelectedList.CURRENCY;
			// }
			// else if (navItem.itemType == DrawerItemType.ABOUTUS) {
			// viewFlipper.showNext();
			// // populateCurrencies();
			// eshop_category_list.setAdapter(aboutUsAdapter);
			// aboutUsAdapter.notifyDataSetChanged();
			// selectedList = SelectedList.ABOUTUS;
			// }
			else {

				NavDrawerItem item = navDrawerItems.get(position);
				displayView(item.itemType, null);
				if (position == 0) {
					textViewHeader.setText(retailer.getRetailerName());
				} else if (item.itemType != DrawerItemType.PROFILE) {
					textViewHeader.setText(item.getTitle());
				}
			}

		}
	}

	private class EShopCategoryClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			if (position == 0) {
				viewFlipper.showNext();
				if (selectedList == SelectedList.CURRENCY
						&& eshopFragment != null) {
					eshopFragment.refreshList();
				}
			} else if (selectedList == SelectedList.FEATUREDSTORE) {

				FeaturedStore store = featuredStores.get(position);
				textViewHeader.setText(Helper.getSharedHelper().toTitleCase(
						store.storeName));
				displayView(DrawerItemType.FEATUREDSTORE, store.storeUrl);
			} else if (selectedList == SelectedList.CURRENCY) {
				if (position == 1) {
					displayView(DrawerItemType.PROFILE, "Profile");
					// textViewHeader.setText("Profile");
				} else if (position == 2) {

					isCurrencyExpanded = !isCurrencyExpanded;
					populateCurrencies();
					curencyListAdapter.notifyDataSetChanged();
				} else {
					Helper.getSharedHelper().currency_code = currencies
							.get(position);
					Helper.getSharedHelper().setCurrencySymbol();
					curencyListAdapter.notifyDataSetChanged();
					spref.edit()
							.putString(Constants.KEY_USER_CURRECY,
									Helper.getSharedHelper().currency_code)
							.commit();
				}
			} else if (selectedList == SelectedList.ABOUTUS) {
				if (position == 1) {
					displayView(DrawerItemType.ABOUTUS, "About Us" /*
																	 * +
																	 * retailer.
																	 * getRetailerName
																	 * ()
																	 */);
					textViewHeader.setText("About Us"
					/* + retailer.getRetailerName() */);
				} else if (position == 2) {
					displayView(DrawerItemType.TERMSNCONDITION, "Terms of Use");
					textViewHeader.setText("Terms of Use");
				}
			} else {
				// displayView(position);
				// textViewHeader.setText(navMenuTitles[2]);
				showEShop(position - 1);
			}
		}
	}

	void searchText(String keyword) {
		if (keyword == null || keyword.length() == 0) {
			return;
		}
		EShopListFragment fragment = null;
		fragment = new EShopListFragment();
		eshopFragment = fragment;

		if (fragment != null) {
			fragment.searchedKeyWord = keyword;
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			// mDrawerList.setItemChecked(2, true);
			// mDrawerList.setSelection(2);
			textViewHeader.setText(Helper.getSharedHelper()
					.toTitleCase(keyword));
			imageViewOverflow.setVisibility(View.VISIBLE);
			etSearch.setVisibility(View.GONE);
			imageScan.setVisibility(View.GONE);
			textViewHeader.setVisibility(View.VISIBLE);
			selectedMenuIndex = DrawerItemType.ESHOP;
		}
	}

	void showEShop(int categoryIndex) {
		if (Helper.getSharedHelper().enableShoppingCart.equals("1")) {
			cartView.setVisibility(View.GONE);
			txtCartTotal.setText(Helper.getSharedHelper().getCartTotal());
		} else {
			cartView.setVisibility(View.GONE);
		}

		if (categoryList == null || categoryList.size() == 0) {
			categoryList = Helper.getSharedHelper().categoryList;
		}
		if (categoryList == null || categoryList.size() == 0) {
			new AsyncGetCategories().execute();
			displayView(DrawerItemType.HOME, null);
			return;
		}
		CategoryObject category = categoryList.get(categoryIndex);
		textViewHeader.setText(category.category_name);

		EShopListFragment fragment = null;
		fragment = new EShopListFragment();
		eshopFragment = fragment;

		if (fragment != null) {
			fragment.category = category;
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(2, true);
			mDrawerList.setSelection(2);
			setTitle(navMenuTitles[2]);
			mDrawerLayout.closeDrawer(viewFlipper);
			selectedMenuIndex = DrawerItemType.ESHOP;
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(Constants.DrawerItemType itemType,
			String featuredStoreUrl) {
		// update the main content by replacing fragments
		if (itemType != DrawerItemType.FEATUREDSTORE
				&& itemType == selectedMenuIndex) {
			mDrawerLayout.closeDrawer(viewFlipper);
			return;
		}

		Fragment fragment = null;
		switch (itemType) {
		case HOME:
			fragment = new HomeFragment(this);
			break;
		case CONTACTUS:
			fragment = new BranchLocationFragment();

			break;
		case ESHOP:
			eshopFragment = new EShopListFragment();
			fragment = eshopFragment;
			break;
		case FEATUREDSTORE:
			webFragment = new WebFragment();
			webFragment.url = featuredStoreUrl;
			fragment = webFragment;
			break;
		case LAYALITY:
			loyaltyFragment = new LoyalityFragment();
			fragment = loyaltyFragment;
			break;
		case FEEDBACK:
			fragment = new FeedBackFragment();
			break;
		case VOUCHER:
			fragment = new VouchersFragment();
			break;
		case PROFILE:
			// fragment = new ProfileFragment();
			Intent in = new Intent(SlidingMenuActivity.this,
					ProfileActivity.class);
			startActivity(in);
			mDrawerLayout.closeDrawer(viewFlipper);
			return;
		case ABOUTUS:
			webFragment = new WebFragment();
			webFragment.url = retailer.aboutUrl;
			fragment = webFragment;
			break;
		case TERMSNCONDITION:
			webFragment = new WebFragment();
			webFragment.url = Helper.getSharedHelper().termsConditions;
			fragment = webFragment;
			break;
		case LOOKBOOK:
			fragment = new LookBookFragment();
			break;
		case CALENDER:
			webFragment = new WebFragment();
			webFragment.url = retailer.calendarUrl;
			fragment = webFragment;
			break;
		case ORDER_HISTORY:
			orderHistoryFragment = new OrderHistoryFragment();
			fragment = orderHistoryFragment;
			break;

		default:
			break;
		}
		selectedMenuIndex = itemType;
		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			// mDrawerList.setItemChecked(itemType, true);
			// mDrawerList.setSelection(position);
			// setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(viewFlipper);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
		cartView.setVisibility(View.GONE);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	private final class AsyncGetEShopDetails extends
			AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {

			String eshopConatent = spref.getString(
					Constants.KEY_GET_PRODUCTS_INFO, "");
			if (!eshopConatent.equalsIgnoreCase("")) {
				try {
					JSONObject jsonObject = new JSONObject(eshopConatent);
					Boolean status = parseJSON(jsonObject);
					if (status) {
						// loadEShop();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {

			}
			showLoadingDialog();
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			JSONObject param = new JSONObject();
			if (Utils.hasNetworkConnection(getApplicationContext())) {
				try {
					param.put(Constants.PARAM_RETAILER_ID,
							Constants.RETAILER_ID);
					JSONObject jsonObject = HTTPHandler.defaultHandler()
							.doPost(Constants.URL_GET_PRODUCTS, param);

					if (jsonObject != null) {
						spref.edit()
								.putString(Constants.KEY_GET_PRODUCTS_INFO,
										jsonObject.toString()).commit();
					}
					return parseJSON(jsonObject);
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			} else {
				return false;
			}

		}

		@Override
		protected void onPostExecute(Boolean status) {
			dismissLoadingDialog();
			eShopCategoryAdapter.notifyDataSetChanged();
		}
	}

	private final class AsyncGetCategories extends
			AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {

			String eshopConatent = spref.getString(
					Constants.KEY_GET_CTEGORY_INFO, "");
			if (!eshopConatent.equalsIgnoreCase("")) {
				try {
					JSONObject jsonObject = new JSONObject(eshopConatent);
					Boolean status = parseCategoryJson(jsonObject);
					if (status) {
						// loadEShop();
						eShopCategoryAdapter.notifyDataSetChanged();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {

			}
			showLoadingDialog();
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			JSONObject param = new JSONObject();
			if (Utils.hasNetworkConnection(getApplicationContext())) {
				try {
					param.put(Constants.PARAM_RETAILER_ID,
							Constants.RETAILER_ID);
					JSONObject jsonObject = HTTPHandler.defaultHandler()
							.doPost(Constants.URL_GET_CATEGORIES, param);

					if (jsonObject != null) {
						spref.edit()
								.putString(Constants.KEY_GET_CTEGORY_INFO,
										jsonObject.toString()).commit();
						return parseCategoryJson(jsonObject);
					}

				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}
			} else {
				return false;
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean status) {
			dismissLoadingDialog();
			if (status) {
				eShopCategoryAdapter.notifyDataSetChanged();
			}

		}
	}

	Boolean parseCategoryJson(JSONObject jsonObject) {
		Boolean status = false;
		Gson gson = new Gson();
		CategoryResponseObject data = gson.fromJson(jsonObject.toString(),
				CategoryResponseObject.class);
		// if (data.errorCode.equals("1")) {
		// categoryList = data.data;
		// Helper.getSharedHelper().categoryList = categoryList;
		// eshop_Category.clear();
		// CategoryObject back = new CategoryObject();
		// back.category_name = "Back";
		// back.id = "";
		// eshop_Category.add(back);
		// eshop_Category.addAll(data.data);
		// // eShopCategoryAdapter.notifyDataSetChanged();
		// status = true;
		// } else {
		// status = false;
		// }
		return status;
	}

	Boolean parseJSON(JSONObject jsonObject) {
		eShopCategories.clear();
		eshop_Category.clear();
		CategoryObject back = new CategoryObject();
		back.category_name = "Back";
		back.id = "";
		eshop_Category.add(back);
		Gson gson = new Gson();
		if (jsonObject.has("currency_code")) {
			try {
				String currencyCode = jsonObject.getString("currency_code")
						.toString();
				Helper.getSharedHelper().reatiler.defaultCurrency = currencyCode;
				// Helper.getSharedHelper().currency_code = currencyCode;
				// Helper.getSharedHelper().getCurrencySymbol();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (jsonObject.has("enableCreditCode")) {
			try {
				String enableCreditCode = jsonObject.getString(
						"enableCreditCode").toString();
				Helper.getSharedHelper().enableCreditCode = enableCreditCode;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (jsonObject.has("enableRating")) {
			try {
				String enableRating = jsonObject.getString("enableRating")
						.toString();
				Helper.getSharedHelper().enableRating = enableRating;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// if (jsonObject.has("enableShoppingCart")) {
		// try {
		// String enableShoppingCart = jsonObject.getString(
		// "enableShoppingCart").toString();
		// Helper.getSharedHelper().enableShoppingCart = enableShoppingCart;
		// } catch (JSONException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// }
		if (jsonObject.has("disablePayment")) {
			try {
				String disablePayment = jsonObject.getString("disablePayment")
						.toString();
				Helper.getSharedHelper().disablePayment = disablePayment;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (jsonObject.has("deliveryDays")) {
			try {
				String disablePayment = jsonObject.getString("deliveryDays")
						.toString();
				Helper.getSharedHelper().deliveryDays = disablePayment;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		if (jsonObject.has("enableDelivery")) {
			try {
				String disablePayment = jsonObject.getString("enableDelivery")
						.toString();
				Helper.getSharedHelper().enableDelivery = disablePayment;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		ProductResponse data = gson.fromJson(jsonObject.toString(),
				ProductResponse.class);

		if (data.getErrorCode().equals("1")) {
			// sqliteHelper.openDataBase();
			// sqliteHelper.deleteCategories();
			// sqliteHelper.close();
			// categoryList = new ArrayList<ProductCategory>();
			// categoryList = data.getData();
			Helper.getSharedHelper().categoryList = categoryList;

			for (int i = 0; i < categoryList.size(); i++) {
				// eshop_Category.add(Helper.getSharedHelper().toTitleCase(
				// categoryList.get(i).getCategory()));

				// List<Product> productList = new ArrayList<Product>();
				//
				// productList = categoryList.get(i).getProducts();
				//
				// if (productList.size() > 0) {
				// for (int index = 0; index < productList.size(); index++) {
				//
				// Product product = productList.get(index);
				// if (product.getNewPrice().contains(".")) {
				// Helper.getSharedHelper().isDecialFromat = true;
				// break;
				// } else {
				// // Helper.getSharedHelper().isDecialFromat = false;
				// }
				// }
				// }

			}

			return true;
		} else {
			return false;
		}
	}

	public void cartPressed(View v) {
		if (mDrawerLayout.isDrawerOpen(viewFlipper)) {
			return;
		}
		if (Helper.getSharedHelper().shoppintCartList.size() > 0) {
			Intent intent = new Intent(this, ShoppingCartActivity.class);

			startActivity(intent);
		} else {
			Toast.makeText(context, "Your shopping cart is empty.",
					Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onAccuracyChanged(int arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(int sensor, float[] values) {
		// TODO Auto-generated method stub
		if (sensor == SensorManager.SENSOR_ACCELEROMETER) {
			long curTime = System.currentTimeMillis();
			// only allow one update every 100ms.
			if ((curTime - lastUpdate) > 100) {
				long diffTime = (curTime - lastUpdate);
				lastUpdate = curTime;

				x = values[SensorManager.DATA_X];
				y = values[SensorManager.DATA_Y];
				z = values[SensorManager.DATA_Z];

				float speed = Math.abs(x + y + z - last_x - last_y - last_z)
						/ diffTime * 10000;

				if (speed > SHAKE_THRESHOLD) {
					Log.d("sensor", "shake detected w/ speed: " + speed);
					// Toast.makeText(this, "shake detected w/ speed: " + speed,
					// Toast.LENGTH_SHORT).show();
					getVouchers();
					if (voucherList != null && voucherList.size() > 0) {
						Voucher v;
						if (voucherList.size() > Helper.getSharedHelper().voucherIndex) {
							v = voucherList
									.get(Helper.getSharedHelper().voucherIndex);
							Helper.getSharedHelper().voucherIndex += 1;
						} else {
							Helper.getSharedHelper().voucherIndex = 0;
							v = voucherList
									.get(Helper.getSharedHelper().voucherIndex);
						}
						if (Helper.getSharedHelper().voucherIndex > 2) {
							Helper.getSharedHelper().voucherIndex = 0;
						}
						if (v != null) {
							Intent intent;
							intent = new Intent(this,
									VoucherDisplayActivity.class);
							intent.putExtra("type", v.getType());
							intent.putExtra("msg", v.getMsg());
							boolean isInForeGround = true;
							intent.putExtra(Constants.KEY_IS_APP_RUNNING,
									isInForeGround);
							if (v.getType().equalsIgnoreCase("text")) {

							} else {
								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								this.getApplicationContext().startActivity(
										intent);
							}
						}
					}

				}
				last_x = x;
				last_y = y;
				last_z = z;
			}
		}
	}

	@Override
	public void updateAppTheme() {
		// TODO Auto-generated method stub
		setNavItemsImage();

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

	@Override
	public void userProfileFetched(String rewardsPoints) {
		// TODO Auto-generated method stub

	}

	void showDummyVoucher() {
		Intent i = new Intent(this, VoucherDisplayActivity.class);
		i.putExtra(
				Constants.KEY_PUSH_MSG,
				"http://smartcommerce.asia/uploads/retailer/1/pnimages/deviceimg/1496_pn_14349960321_1434972810_8._Push_Notification_Voucher_xdhpi.jpg");
		i.putExtra(Constants.KEY_PUSH_TYPE, "Image");
		i.putExtra("pid", "745");
		i.putExtra(Constants.KEY_IS_APP_RUNNING, true);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		this.getApplicationContext().startActivity(i);
	}

	void loadProductDetail(String productid) {
		ProductDetailHandler productDetailHandler = new ProductDetailHandler(
				productid, this);
		productDetailHandler.fetchProductDetails();
	}

	@Override
	public void productDetailLoaded(Product p) {
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

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// your code
			if (mDrawerLayout.isDrawerOpen(viewFlipper)) {
				mDrawerLayout.closeDrawer(viewFlipper);
				return true;
			}
			if (etSearch.getVisibility() == View.VISIBLE) {
				etSearch.setVisibility(View.GONE);
				imageScan.setVisibility(View.GONE);
				textViewHeader.setVisibility(View.VISIBLE);
				imageViewOverflow.setVisibility(View.VISIBLE);
				return true;
			} else if (selectedMenuIndex == DrawerItemType.FEATUREDSTORE
					|| selectedMenuIndex == DrawerItemType.ABOUTUS
					|| selectedMenuIndex == DrawerItemType.TERMSNCONDITION) {
				if (webFragment != null) {
					if (webFragment.canGoback()) {
						return true;
					}
				}

			}
			if (selectedMenuIndex != DrawerItemType.ESHOP) {
				displayView(DrawerItemType.ESHOP, null);
				textViewHeader.setText(retailer.getRetailerName());
				return true;
			}

		}

		return super.onKeyDown(keyCode, event);
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
					if (loyaltyFragment != null) {
						loyaltyFragment.pwdScanned(result);
					}
				} else {
					searchText(result.trim());
				}

			}
			break;
		default:
		}
	}
}
