package com.appsauthority.appwiz.models;

/* Copyright (C)
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Kevin Irish Antonio <irish.antonio@yahoo.com>, February 2014
 */
import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class RetailerStores implements Serializable {

	private static final long serialVersionUID = 1L;

	@SerializedName("outletAddr")
	String storeAddress;

	@SerializedName("outletContact")
	String storeContact;

	@SerializedName("outletLat")
	String latitude;

	@SerializedName("outletLong")
	String longitude;

	@SerializedName("outletName")
	public String outletName;

	public RetailerStores() {
		super();
	}

	public String getStoreAddress() {
		return storeAddress;
	}

	public void setStoreAddress(String storeAddress) {
		this.storeAddress = storeAddress;
	}

	public String getStoreContact() {
		return storeContact;
	}

	public void setStoreContact(String storeContact) {
		this.storeContact = storeContact;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

}
