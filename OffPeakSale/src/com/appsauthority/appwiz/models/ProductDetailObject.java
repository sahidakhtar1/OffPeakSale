package com.appsauthority.appwiz.models;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class ProductDetailObject implements Serializable{
	
	@SerializedName("data")
	public Product data;

}
