package com.appsauthority.appwiz.models;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class OrderHistoryResponseObject implements Serializable{
	
	@SerializedName("data")
	public AllOrdersResponseObject data;
	

}
