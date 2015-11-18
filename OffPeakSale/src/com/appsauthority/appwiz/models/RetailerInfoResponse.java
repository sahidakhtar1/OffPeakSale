package com.appsauthority.appwiz.models;

/* Copyright (C)
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Kevin Irish Antonio <irish.antonio@yahoo.com>, February 2014
 */
import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class RetailerInfoResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	@SerializedName("errorCode")
	String errorCode;
	
	@SerializedName("isSSL")
	public String isSSL;

	@SerializedName("data")
	Retailer retailerData;
	

	private RetailerInfoResponse() {
		super();
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public Retailer getRetailerData() {
		return retailerData;
	}

	public void setRetailerData(Retailer retailerData) {
		this.retailerData = retailerData;
	}

}
