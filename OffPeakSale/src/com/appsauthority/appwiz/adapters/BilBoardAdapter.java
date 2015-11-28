package com.appsauthority.appwiz.adapters;

import java.util.List;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.appsauthority.appwiz.models.Product;
import com.appsauthority.appwiz.models.Retailer;
import com.appsauthority.appwiz.utils.Helper;
import com.appsauthority.appwiz.utils.ImageCacheLoader;
import com.offpeaksale.consumer.R;

public class BilBoardAdapter extends BaseAdapter {

	private Context mContext;
	private List<Product> categoryList;
	LayoutInflater mInflater;
	ImageCacheLoader imageCacheloader;
	public int maxHeight = -1;
	String themeColor;
	String textColor;

	public BilBoardAdapter(Context mContext, List<Product> categoryList) {
		this.mContext = mContext;
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.categoryList = categoryList;
		imageCacheloader = new ImageCacheLoader(mContext);
		Retailer retailer = Helper.getSharedHelper().reatiler;
		themeColor = retailer.getHeaderColor();
		textColor = retailer.getRetailerTextColor();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return categoryList.size();
	}

	@Override
	public Product getItem(int position) {
		// TODO Auto-generated method stub
		return categoryList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = mInflater.inflate(R.layout.bill_board_item, null);

		TextView name = (TextView) convertView.findViewById(R.id.tvProductName);
		ImageView imgProd = (ImageView) convertView.findViewById(R.id.imgProd);
		float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				30, mContext.getResources().getDisplayMetrics());

		if (maxHeight > 0) {
			int availableHeight = (int) (maxHeight - pixels);
			int imageWidth = Helper.getSharedHelper().getWidthforHeight(
					availableHeight);
			imgProd.getLayoutParams().width = (int) imageWidth;
			name.getLayoutParams().width = (int) imageWidth;
			// int width = imgProd.getWidth();
			// System.out.println(width);
			//
			// RelativeLayout.LayoutParams imgvwDimens =
			// new RelativeLayout.LayoutParams(imageWidth, availableHeight);
			// imgProd.setLayoutParams(imgvwDimens);
			// new LayoutParams(imageWidth, availableHeight)
		}

		// name.setBackgroundColor(Color.parseColor("#" + themeColor));
		// name.setTextColor(Color.parseColor("#" + textColor));
		Product product = getItem(position);
		name.setText(product.getShortDescription());
		name.setTypeface(Helper.getSharedHelper().boldFont);
		imageCacheloader.displayImage(product.getImage(),
				R.drawable.image_placeholder, imgProd);
		return convertView;
	}

	// int getWidthforHeight(int imgHeight) {
	//
	// int width = (int) (1.67f * imgHeight);
	//
	// return width;
	// }
}