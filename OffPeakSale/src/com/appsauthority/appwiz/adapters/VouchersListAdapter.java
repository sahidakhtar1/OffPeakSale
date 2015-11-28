package com.appsauthority.appwiz.adapters;

/* Copyright (C)
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Kevin Irish Antonio <irish.antonio@yahoo.com>, February 2014
 */
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.appauthority.appwiz.interfaces.VoucherDeleteCaller;
import com.appsauthority.appwiz.models.ViewHolderVoucher;
import com.appsauthority.appwiz.models.Voucher;
import com.appsauthority.appwiz.utils.ImageCacheLoader;
import com.offpeaksale.consumer.R;

public class VouchersListAdapter extends ArrayAdapter<Voucher> {

	private Context context;
	private List<Voucher> voucherList;
	private ImageCacheLoader imageLoader;
	private VoucherDeleteCaller caller;

	public VouchersListAdapter(Context context, int resource,
			List<Voucher> voucherList, VoucherDeleteCaller caller) {
		super(context, resource, voucherList);

		this.context = context;
		this.voucherList = voucherList;
		this.imageLoader = new ImageCacheLoader(context);
		this.caller = caller;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolderVoucher holder = null;
		ImageView imageViewVoucher = null;
		VideoView videoView = null;
		WebView webView = null;
		ImageButton close = null;
		RelativeLayout rlVideoview = null;
		RelativeLayout rlRow;

		if (null == convertView) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.row_voucher, null);
			holder = new ViewHolderVoucher(convertView);
			convertView.setTag(holder);
		}

		holder = (ViewHolderVoucher) convertView.getTag();

		Voucher voucher = voucherList.get(position);

		imageViewVoucher = holder.getImage();
		videoView = holder.getVideo();
		webView = holder.getWebview();
		rlVideoview = holder.getRlVideoview();
		rlRow = holder.getRlRow();
		close = holder.getClose();
		close.setTag(position);
		rlRow.setTag(position);
		webView.setTag(position);

		if (voucher.getType().equalsIgnoreCase("Image")) {
			imageViewVoucher.setVisibility(View.VISIBLE);
			videoView.setVisibility(View.GONE);
			rlVideoview.setVisibility(View.GONE);
			if (voucher.getMsg().endsWith(".gif")) {
				imageViewVoucher.setVisibility(View.INVISIBLE);
				webView = holder.getWebview();
				// loadGif();
				webView.setVisibility(View.VISIBLE);
				String htnlString = "<!DOCTYPE html><html><body style=\"background-color:transparent;margin: 0; padding: 0\"><img src=\""
						+ voucher.getMsg()
						+ "\" alt=\"pageNo\" width=\"100%\" height=\"100%\"></body></html>";
				webView.loadDataWithBaseURL(null, htnlString, "text/html",
						"UTF-8", null);
				webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
			} else {
				imageViewVoucher.setVisibility(View.VISIBLE);
				webView.setVisibility(View.GONE);
				imageLoader.displayImage(voucher.getMsg(),
						R.drawable.image_placeholder, imageViewVoucher);
			}
		} else {
			imageViewVoucher.setVisibility(View.GONE);
			videoView.setVisibility(View.VISIBLE);
			rlVideoview.setVisibility(View.VISIBLE);
			videoView.setVideoPath(voucher.getMsg());
			MediaController mediaController = new MediaController(context);
			mediaController.setAnchorView(videoView);
			videoView.setMediaController(mediaController);
			videoView.requestFocus();
			videoView.seekTo(1);
			// videoView.stopPlayback();
			webView.setVisibility(View.GONE);
		}
		close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int tag = (Integer) arg0.getTag();
				if (caller != null) {
					caller.deleteVoucher(tag);
				}
			}
		});
		rlRow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int tag = (Integer) arg0.getTag();
				if (caller != null) {
					caller.voucherClicked(tag);
				}
			}
		});
		webView.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				int tag = (Integer) arg0.getTag();
				if (caller != null) {
					caller.voucherClicked(tag);
				}
				return true;
			}
		});

		return convertView;
	}

}
