package com.appauthority.appwiz.interfaces;

import com.appsauthority.appwiz.models.Product;

public interface ProductDetailCaller {

	public void productDetailLoaded(Product p);
	public void errorOccured();
}
