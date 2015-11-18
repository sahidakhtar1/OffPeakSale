package com.appsauthority.appwiz.models;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class FeaturedStore implements Serializable {

	@SerializedName("storeName")
	public String storeName;
	
	@SerializedName("storeUrl")
	public String storeUrl;
}
