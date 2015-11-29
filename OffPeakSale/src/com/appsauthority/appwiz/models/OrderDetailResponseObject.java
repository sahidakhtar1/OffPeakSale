package com.appsauthority.appwiz.models;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class OrderDetailResponseObject implements Serializable {

	@SerializedName("data")
	public OrderObject data;
}
