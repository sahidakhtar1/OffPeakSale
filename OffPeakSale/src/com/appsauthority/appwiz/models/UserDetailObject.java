package com.appsauthority.appwiz.models;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class UserDetailObject implements Serializable {

	@SerializedName("errorCode")
	public String errorCode;

	@SerializedName("data")
	public Profile userProfile;

}
