package com.appsauthority.appwiz.models;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.offpeaksale.consumer.R;

public class ViewHolderShoppingCart {

	private View row;
	private ImageView image = null;
	private TextView name = null;
	private TextView shortDesc = null;
	private TextView newPrice = null;
	private EditText edt_qty = null;
	private Button btn_update = null;
	private Button btn_delete =  null;

	
	public Button getBtn_delete() {
		if (null == edt_qty) {
			btn_delete = (Button) row.findViewById(R.id.btn_delete);
		}
		return btn_delete;
	}



	public EditText getEdt_qty() {
		if (null == edt_qty) {
			edt_qty = (EditText) row.findViewById(R.id.edt_qty);
		}
		return edt_qty;
	}



	public Button getBtn_update() {
		if (null == btn_update) {
			btn_update = (Button) row.findViewById(R.id.btn_update);
		}
		return btn_update;
	}



	public ViewHolderShoppingCart(View row) {
		this.row = row;
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


	public TextView getNewPrice() {
		if (null == newPrice) {
			newPrice = (TextView) row.findViewById(R.id.tv_price);
		}
		return newPrice;
	}

}
