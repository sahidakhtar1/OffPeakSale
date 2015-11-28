package com.appsauthority.appwiz.models;

import android.view.View;
import android.widget.TextView;

import com.offpeaksale.consumer.R;

public class ViewHolderOrderHistory {

	private View row;
	private TextView tvOrderId, tvOrderIdValue, tvOrderDate, tvOrderTotalValue,
			tvOrderStatus, tvOrderStatusValue, tvTotalCount,tvExpiryDate,tvProductPrice;

	public ViewHolderOrderHistory(View row) {
		this.row = row;
	}

	public TextView getTvOrderId() {
		if (null == tvOrderId) {
			tvOrderId = (TextView) row.findViewById(R.id.tvOrderId);
		}
		return tvOrderId;
	}

	public TextView getTvOrderIdValue() {
		if (null == tvOrderIdValue) {
			tvOrderIdValue = (TextView) row.findViewById(R.id.tvOrderIdValue);
		}
		return tvOrderIdValue;
	}

	public TextView getTvOrderDate() {
		if (null == tvOrderDate) {
			tvOrderDate = (TextView) row.findViewById(R.id.tvOrderDate);
		}
		return tvOrderDate;
	}

	

	public TextView getTvOrderTotalValue() {
		if (null == tvOrderTotalValue) {
			tvOrderTotalValue = (TextView) row
					.findViewById(R.id.tvOrderTotalValue);
		}
		return tvOrderTotalValue;
	}

	public TextView getTvOrderStatus() {
		if (null == tvOrderStatus) {
			tvOrderStatus = (TextView) row.findViewById(R.id.tvOrderStatus);
		}
		return tvOrderStatus;
	}

	public TextView getTvOrderStatusValue() {
		if (null == tvOrderStatusValue) {
			tvOrderStatusValue = (TextView) row
					.findViewById(R.id.tvOrderStatusValue);
		}
		return tvOrderStatusValue;
	}

	/*public TextView getTvTotalCount() {
		if (null == tvTotalCount) {
			tvTotalCount = (TextView) row
					.findViewById(R.id.tvTotalCount);
		}
		return tvTotalCount;
	}*/

	public void setTvTotalCount(TextView tvTotalCount) {
		this.tvTotalCount = tvTotalCount;
	}
	public TextView getTvExpiryDate()
	{
		if (null == tvExpiryDate) {
			tvExpiryDate = (TextView) row.findViewById(R.id.tvExpiryDate);
		}
		return tvExpiryDate;
	}
	public TextView getTvProductPrice()
	{
		if (null == tvProductPrice) {
			tvProductPrice = (TextView) row.findViewById(R.id.tvProductPrice);
		}
		return tvProductPrice;
	}

}
