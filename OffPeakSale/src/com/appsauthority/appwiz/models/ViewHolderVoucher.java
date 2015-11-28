package com.appsauthority.appwiz.models;

import android.view.View;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.offpeaksale.consumer.R;

public class ViewHolderVoucher {
	private View row;
	public ImageButton imageViewClose = null;
	public ImageView imageViewVoucher = null;
	public VideoView videoView = null;
	public RelativeLayout rlVideoview,rlRow;
	public RelativeLayout getRlRow() {
		if (null == rlRow) {
			rlRow = (RelativeLayout) row.findViewById(R.id.rlRow);
		}
		return rlRow;
	}

	public void setRlRow(RelativeLayout rlRow) {
		
		this.rlRow = rlRow;
	}

	WebView webview = null;

	public ViewHolderVoucher(View row) {
		this.row = row;
	}

	public ImageButton getClose() {
		if (null == imageViewClose) {
			imageViewClose = (ImageButton) row.findViewById(R.id.iv_close);
		}
		return imageViewClose;
	}

	public ImageView getImage() {
		if (null == imageViewVoucher) {
			imageViewVoucher = (ImageView) row.findViewById(R.id.iv_voucher);
		}
		return imageViewVoucher;
	}

	public VideoView getVideo() {
		if (null == videoView) {
			videoView = (VideoView) row.findViewById(R.id.videoView);
		}
		return videoView;
	}

	public RelativeLayout getRlVideoview() {
		if (null == rlVideoview) {
			rlVideoview = (RelativeLayout) row.findViewById(R.id.rlVideoview);
		}
		return rlVideoview;
	}

	public WebView getWebview() {
		if (null == webview) {
			webview = (WebView) row.findViewById(R.id.gifWebview);
		}
		return webview;
	}

	

}
