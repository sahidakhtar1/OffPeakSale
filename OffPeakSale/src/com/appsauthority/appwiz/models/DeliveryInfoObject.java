package com.appsauthority.appwiz.models;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class DeliveryInfoObject implements Serializable{
	
	@SerializedName("name")
	public String name;
	
	@SerializedName("email")
	public String email;
	
	@SerializedName("mobile_num")
	public String mobile_num;
	
	@SerializedName("address")
	public String address;
	
	@SerializedName("city")
	public String city;
	
	@SerializedName("zip")
	public String zip;
	
	@SerializedName("country")
	public String country;

}
