package com.appsauthority.appwiz.models;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class AllOrdersResponseObject implements Serializable{

	@SerializedName("active")
	public List<OrderObject> active;
	
	@SerializedName("used")
	public List<OrderObject> used;
}
