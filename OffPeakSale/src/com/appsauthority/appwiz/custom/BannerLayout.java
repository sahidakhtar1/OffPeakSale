package com.appsauthority.appwiz.custom;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.appsauthority.appwiz.models.MediaObject;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.HorizontalPager;
import com.offpeaksale.consumer.R;

public class BannerLayout extends LinearLayout {

	Context context;
	List<MediaObject> allMedias;
	HorizontalPager horizontal_pager;
	LinearLayout llPagerIndicator;
	PageIndicatorView pageIndicator;
	Handler handler = new Handler();
	Runnable r;

	List<MediaView> mediaViewList;
	int prevPage = 0;
	Boolean isFragment = false;
	int height = 0;

	public BannerLayout(Context context, List<MediaObject> medias, int height) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.allMedias = medias;
		this.height = height;
		init();
	}

	public BannerLayout(Context context, List<MediaObject> medias,
			Boolean isFragment, int height) {
		super(context);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.allMedias = medias;
		this.isFragment = isFragment;
		this.height = height;
		init();
	}

	void init() {
		mediaViewList = new ArrayList<MediaView>();
		LayoutInflater inflater = LayoutInflater.from(context);
		View view = inflater.inflate(R.layout.banner_layout, null, false);

		LinearLayout llPager = (LinearLayout) view.findViewById(R.id.llPager);

		if (height == 0) {
			height = 215;
		}
		horizontal_pager = new HorizontalPager(context);
		horizontal_pager.setLayoutParams(new LayoutParams(
				LayoutParams.MATCH_PARENT, height));
		llPager.addView(horizontal_pager);

		llPagerIndicator = (LinearLayout) view
				.findViewById(R.id.llPagerIndicator);
		if (allMedias.size() <= 1) {
			llPagerIndicator.setVisibility(View.GONE);
		} else {
			llPagerIndicator.setVisibility(View.VISIBLE);
			pageIndicator = new PageIndicatorView(context, allMedias.size());
			llPagerIndicator.addView(pageIndicator);
		}
		this.addView(view);
		for (int i = 0; i < allMedias.size(); i++) {
			MediaObject media = allMedias.get(i);
			MediaView mediaView = new MediaView(context, media, isFragment);
			horizontal_pager.addView(mediaView);
			mediaViewList.add(mediaView);

		}
		horizontal_pager.setOnScreenSwitchListener(onScreenSwitchListener);
	}

	private final HorizontalPager.OnScreenSwitchListener onScreenSwitchListener = new HorizontalPager.OnScreenSwitchListener() {
		@Override
		public void onScreenSwitched(final int screen) {
			// Check the appropriate button when the user swipes screens.
			switch (screen) {
			case 0:

				break;
			case 1:

				break;
			}

			if (allMedias.size() > 1) {
				handler.removeCallbacks(r);
				startTimer();
				pageIndicator.setSelectedPage(screen);
				MediaView prevMediaView = mediaViewList.get(prevPage);
				MediaView curMediaView = mediaViewList.get(screen);
				prevPage = screen;
				curMediaView.playVideo();
				prevMediaView.pauseVideo();
			}

		}
	};

	public void startTimer() {
		r = new Runnable() {
			@Override
			public void run() {
				int curPage = horizontal_pager.getCurrentScreen();
				MediaView curMediaView = mediaViewList.get(curPage);
				curMediaView.pauseVideo();
				MediaView nextMediaView;
				if (curPage >= allMedias.size() - 1) {
					horizontal_pager.setCurrentScreen(0, true);
					pageIndicator.setSelectedPage(0);
					nextMediaView = mediaViewList.get(0);
					prevPage = 0;
				} else {
					horizontal_pager.setCurrentScreen(curPage + 1, true);
					pageIndicator.setSelectedPage(curPage + 1);
					nextMediaView = mediaViewList.get(curPage + 1);
					prevPage = curPage + 1;

				}
				nextMediaView.playVideo();

				startTimer();
			}
		};
		if (allMedias.size() > 1) {
			handler.postDelayed(r, Constants.BANNER_CHANGE_INTERVAL);
		}

	}

	public void stopTimer() {
		handler.removeCallbacks(r);

	}

}
