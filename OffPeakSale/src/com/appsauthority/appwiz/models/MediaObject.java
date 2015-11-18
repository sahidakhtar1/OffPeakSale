package com.appsauthority.appwiz.models;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class MediaObject implements Serializable{
	
	@SerializedName("fileType")
	public String fileType;
	
	
	@SerializedName("filePath")
	public String filePath;
	
	

}
