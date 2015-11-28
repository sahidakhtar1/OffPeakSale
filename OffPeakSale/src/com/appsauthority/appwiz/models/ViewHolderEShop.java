package com.appsauthority.appwiz.models;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.appsauthority.appwiz.utils.Helper;
import com.offpeaksale.consumer.R;

public class ViewHolderEShop {

	private View row;
	private ImageView image = null;
	private TextView name = null;
	private TextView shortDesc = null;
	private TextView oldPrice = null;
	private TextView newPrice = null;
	private ImageView ivStroke = null;
	private Button btn_delete = null;
	private TextView tvQtyIndicator = null;
	private TextView tvSaleIndicator = null;
	private TextView tvDistance = null;
	private TextView tvAddress = null;
	private TextView tvDiscountValue = null;
	private TextView tvDiscountlbl = null;
	private RelativeLayout rlCircularView;
	

	public Button getBtn_delete() {
		if (null == btn_delete) {
			btn_delete = (Button) row.findViewById(R.id.btn_delete);
		}
		return btn_delete;
	}

	private RatingBar productRatingBar = null;

	public ViewHolderEShop(View row) {
		this.row = row;
	}

	public RatingBar getProductRatingBar() {
		if (null == productRatingBar) {
			productRatingBar = (RatingBar) row
					.findViewById(R.id.productRatingBar);
		}
		return productRatingBar;
	}

	public ImageView getStroke() {
		if (null == ivStroke) {
			ivStroke = (ImageView) row.findViewById(R.id.ivStrike);
		}
		return ivStroke;
	}

	public ImageView getImage() {
		if (null == image) {
			image = (ImageView) row.findViewById(R.id.iv_eshop);
		}
		return image;
	}

	public TextView getName() {
		if (null == name) {
			name = (TextView) row.findViewById(R.id.tv_eshop_name);
		}
		return name;
	}

	public TextView getShortDesc() {
		if (null == shortDesc) {
			shortDesc = (TextView) row.findViewById(R.id.tv_eshop_short_desc);
		}
		return shortDesc;
	}

	public TextView getOldPrice() {
		if (null == oldPrice) {
			oldPrice = (TextView) row.findViewById(R.id.tv_eshop_old_price);
		}
		return oldPrice;
	}

	public TextView getNewPrice() {
		if (null == newPrice) {
			newPrice = (TextView) row.findViewById(R.id.tv_eshop_new_price);
		}
		return newPrice;
	}

	public TextView getTvQtyIndicator() {
		if (tvQtyIndicator == null) {
			tvQtyIndicator = (TextView) row.findViewById(R.id.tvQtyIndicator);
		}
		return tvQtyIndicator;
	}

	public void setTvQtyIndicator(TextView tvQtyIndicator) {
		this.tvQtyIndicator = tvQtyIndicator;
	}

	public TextView getTvSaleIndicator() {
		if (tvSaleIndicator == null) {
			tvSaleIndicator = (TextView) row.findViewById(R.id.tvSaleIndicator);
		}
		return tvSaleIndicator;
	}

	public void setTvSaleIndicator(TextView tvSaleIndicator) {
		this.tvSaleIndicator = tvSaleIndicator;
	}

	public TextView getTvDistance() {
		if (tvDistance == null) {
			tvDistance = (TextView) row.findViewById(R.id.tvDistance);
		}
		return tvDistance;
	}

	public TextView getTvAddress() {
		if (tvAddress == null) {
			tvAddress = (TextView) row.findViewById(R.id.tvAddress);
		}
		return tvAddress;
	}

	public TextView getTvDiscountValue() {
		
		if (tvDiscountValue == null) {
			tvDiscountValue = (TextView) row.findViewById(R.id.tvDiscountValue);
		}
		return tvDiscountValue;
	}

	public TextView getTvDiscountlbl() {
		if (tvDiscountlbl == null) {
			tvDiscountlbl = (TextView) row.findViewById(R.id.tvDiscountLbl);
		}
		return tvDiscountlbl;
	}

	public RelativeLayout getRlCircularView() {
		if (rlCircularView == null) {
			rlCircularView = (RelativeLayout) row.findViewById(R.id.rlCircularView);
		}
		return rlCircularView;
	}
	

}
