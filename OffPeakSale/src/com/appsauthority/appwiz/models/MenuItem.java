package com.appsauthority.appwiz.models;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class MenuItem implements Serializable{
	
	@SerializedName("displayName")
	public String displayName;
	
	@SerializedName("origName")
	public String origName;

}
