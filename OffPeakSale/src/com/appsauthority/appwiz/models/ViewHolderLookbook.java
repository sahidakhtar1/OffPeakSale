package com.appsauthority.appwiz.models;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.offpeaksale.consumer.R;

public class ViewHolderLookbook {

	private View row;
	private ImageView image = null;
	private TextView title;
	private TextView caption;
	private TextView likeCount;
	private LinearLayout llTitle,llLikeView;

	public ViewHolderLookbook(View row) {
		this.row = row;
	}

	public ImageView getImage() {
		if (null == image) {
			image = (ImageView) row.findViewById(R.id.iv_lookbook);
		}
		return image;
	}

	public TextView getTitle() {
		if (null == title) {
			title = (TextView) row.findViewById(R.id.tv_title);
		}
		return title;
	}

	

	public TextView getCaption() {
		if (null == caption) {
			caption = (TextView) row.findViewById(R.id.tv_caption);
		}
		return caption;
	}

	public TextView getLikeCount() {
		if (null == likeCount) {
			likeCount = (TextView) row.findViewById(R.id.tv_likeCount);
		}
		return likeCount;
	}

	public LinearLayout getLlTitle() {
		if (null == llTitle) {
			llTitle = (LinearLayout) row.findViewById(R.id.llTitle);
		}
		return llTitle;
	}

	public LinearLayout getLlLikeView() {
		if (null == llLikeView) {
			llLikeView = (LinearLayout) row.findViewById(R.id.llLikeView);
		}
		return llLikeView;
	}

	
}
