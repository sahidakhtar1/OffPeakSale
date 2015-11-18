package com.appsauthority.appwiz.models;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class SucessResponseObject implements Serializable{
	
	@SerializedName("errorCode")
	public String errorCode;
	
	@SerializedName("errorMessage")
	public String errorMessage;
	
	@SerializedName("discount")
	public String discount;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
