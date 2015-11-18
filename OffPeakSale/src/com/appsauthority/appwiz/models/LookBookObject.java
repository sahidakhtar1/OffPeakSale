package com.appsauthority.appwiz.models;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class LookBookObject implements Serializable{

	@SerializedName("id")
	public String id;
	
	@SerializedName("imgUrl")
	public String imgUrl;
	
	@SerializedName("title")
	public String title;
	
	@SerializedName("caption")
	public String caption;
	
	@SerializedName("description")
	public String description;
	
	@SerializedName("likesCnt")
	public String likesCnt;
}
