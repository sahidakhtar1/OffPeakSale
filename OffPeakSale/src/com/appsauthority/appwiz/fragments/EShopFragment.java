package com.appsauthority.appwiz.fragments;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

import com.appsauthority.appwiz.models.ProductCategory;
import com.appsauthority.appwiz.utils.Helper;
import com.appsauthority.appwiz.utils.SQLiteHelper;
import com.offpeaksale.consumer.R;

public class EShopFragment extends Fragment implements OnTabChangeListener {

	public String TAG = getClass().getSimpleName();
	private View root;
	private FragmentTabHost tabHost;
	public ArrayList<ProductCategory> categories = new ArrayList<ProductCategory>();
	private SQLiteHelper sqliteHelper;
	private Context context;
	public List<ProductCategory> categoryList;

	public EShopFragment() {
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.fragment_eshop, null);
		context = getActivity();

		tabHost = (FragmentTabHost) root.findViewById(android.R.id.tabhost);
		tabHost.setOnTabChangedListener(this);

		tabHost.setBackgroundColor(R.color.footer_bg);

		// sqliteHelper = new SQLiteHelper(context);
		// sqliteHelper.openDataBase();
		// categories = sqliteHelper.getCategories();
		// sqliteHelper.close();
		if (categories != null) {
			setupTabHost();
		}else{
			//setupTabHost();
		}
		

		return root;
	}

	private void setupTabHost() {
		try {
			tabHost.setup(getActivity(), getChildFragmentManager(),
					R.id.tabContent);
			tabHost.computeScroll();

			populateTabs();
		} catch (Exception e) {
		}
	}

	private void populateTabs() {
		try {
			tabHost.getTabWidget().setDividerDrawable(
					R.drawable.devider_with_shadow);

			for (int i = 0; i < categories.size(); i++) {
				Bundle bundle = new Bundle();
				bundle.putInt("ID", categories.get(i).getId());
				bundle.putSerializable("ProductList", (Serializable) categories
						.get(i).getProducts());

				tabHost.addTab(
						tabHost.newTabSpec(categories.get(i).getCategory())
								.setIndicator(
										createTabView(categories.get(i)
												.getCategory())),
						EShopListFragment.class, bundle);

				// TabWidget tw = (TabWidget) tabHost
				// .findViewById(android.R.id.tabs);
				//
				// View tabView = tw.getChildTabViewAt(i);
				//
				// TextView tv = (TextView) tabView
				// .findViewById(android.R.id.title);
				//
				// int dp = (int) (getResources().getDimension(R.dimen.textsize)
				// / getResources()
				// .getDisplayMetrics().density);
				//
				// tv.setTextSize(dp - 6);
				// tv.setTypeface(Helper.getSharedHelper().normalFont);
				// tv.setShadowLayer(1.5f, -1, 1, Color.GRAY);
			}
			// tabHost.getTabWidget().getChildAt(0).getLayoutParams().height =
			// 44;
		} catch (Exception e) {
			Log.e("Ecxeption i tabhost", e.getMessage());
		}

	}

	public void onTabChanged(String tabId) {

		/************ Called when tab changed *************/

		// ********* Check current selected tab and change according images
		// *******/
		try {
			int currentTab = tabHost.getCurrentTab();
			for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
				TabWidget tw = (TabWidget) tabHost
						.findViewById(android.R.id.tabs);
				View tabView = tw.getChildTabViewAt(i);
				View tab_icon = (View) tabView.findViewById(R.id.tabsLayout);
				View selectionIndicator = (View) tab_icon
						.findViewById(R.id.selectionIndicator);
				if (i == currentTab) {
					selectionIndicator.setBackgroundColor(Color
							.parseColor("#80"
									+ Helper.getSharedHelper().reatiler
											.getHeaderColor()));
				} else {
					selectionIndicator.setBackgroundColor(Color
							.parseColor("#00000000"));
				}

			}
		} catch (Exception e) {

		}
		// Log.i("tabs", "CurrentTab: " + tabHost.getCurrentTab());

	}

	private View createTabView(String category) {
		View view = LayoutInflater.from(context).inflate(R.layout.tabs_icon,
				null);
		TextView tvCategory = (TextView) view.findViewById(R.id.tvCategory);
		tvCategory.setText("  " + category.toUpperCase() + "  ");
		try {
			tvCategory.setTypeface(Helper.getSharedHelper().normalFont);
		} catch (Exception e) {

		}

		View selectionIndicator = (View) view
				.findViewById(R.id.selectionIndicator);
		selectionIndicator.setBackgroundColor(Color.parseColor("#00000000"));

		return view;
	}
}
