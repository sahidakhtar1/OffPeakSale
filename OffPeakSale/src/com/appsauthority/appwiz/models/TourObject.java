package com.appsauthority.appwiz.models;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class TourObject implements Serializable{

	@SerializedName("imageUrl")
	public String imageUrl;
	
	@SerializedName("headerTitle")
	public String headerTitle;
	
	@SerializedName("description")
	public String description;
}
