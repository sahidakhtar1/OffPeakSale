package com.appsauthority.appwiz.models;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class CategoryResponseObject implements Serializable {
	@SerializedName("category")
	public String category;

	@SerializedName("products")
	public List<Product> products;
}
