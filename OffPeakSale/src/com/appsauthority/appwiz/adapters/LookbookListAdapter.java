package com.appsauthority.appwiz.adapters;

/* Copyright (C)
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Kevin Irish Antonio <irish.antonio@yahoo.com>, February 2014
 */
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appauthority.appwiz.interfaces.LookBookAdapterCaller;
import com.appsauthority.appwiz.models.LookBookObject;
import com.appsauthority.appwiz.models.ViewHolderLookbook;
import com.appsauthority.appwiz.utils.Helper;
import com.appsauthority.appwiz.utils.ImageCacheLoader;
import com.offpeaksale.consumer.R;

public class LookbookListAdapter extends ArrayAdapter<LookBookObject> {

	private List<LookBookObject> objects;
	private Context context;
	private ImageCacheLoader imageCacheLoader;
	public int selectedIndex = -1;
	LookBookAdapterCaller caller;

	public LookbookListAdapter(Context context, int resource,
			List<LookBookObject> objects,LookBookAdapterCaller caller) {
		super(context, resource, objects);
		this.objects = objects;
		this.context = context;
		this.caller = caller;
		imageCacheLoader = new ImageCacheLoader(context);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolderLookbook holder = null;
		ImageView imageView = null;
		TextView title,caption,likeCount;
		LinearLayout llTitle,llLikeView;
		

		if (null == convertView) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// if (Helper.getSharedHelper().disablePayment.endsWith("1")) {
			// convertView = vi.inflate(R.layout.row_eshop_with_delete, null);
			// }else{
			//
			// }
			convertView = vi.inflate(R.layout.row_lookbook, null);

			holder = new ViewHolderLookbook(convertView);
			convertView.setTag(holder);
		}
		holder = (ViewHolderLookbook) convertView.getTag();
		LookBookObject object = getItem(position);

		imageView = holder.getImage();
		title = holder.getTitle();
		caption = holder.getCaption();
		likeCount = holder.getLikeCount();
		llTitle = holder.getLlTitle();
		llLikeView = holder.getLlLikeView();
		
		title.setTypeface(Helper.getSharedHelper().boldFont);
		caption.setTypeface(Helper.getSharedHelper().normalFont);
		likeCount.setTypeface(Helper.getSharedHelper().normalFont);
		title.setText(object.title);
		caption.setText(object.caption);
		likeCount.setText(object.likesCnt);
		
//		title.setText("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
		

		imageCacheLoader.displayImage(object.imgUrl,
				R.drawable.image_placeholder, imageView);
		if (selectedIndex == position) {
			title.setVisibility(View.VISIBLE);
			caption.setVisibility(View.VISIBLE);
			likeCount.setVisibility(View.VISIBLE);
			llTitle.setVisibility(View.VISIBLE);
			llLikeView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					caller.lookBookitemLikeTapped(position);
				}
			});
		}else{
			title.setVisibility(View.GONE);
			caption.setVisibility(View.GONE);
			likeCount.setVisibility(View.GONE);
			llTitle.setVisibility(View.GONE);
			llLikeView.setOnClickListener(null);
		}

		return convertView;
	}
}
