package com.appsauthority.appwiz.models;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class CategoryObject implements Serializable {

	@SerializedName("id")
	public String id;

	@SerializedName("category_name")
	public String category_name;
}
