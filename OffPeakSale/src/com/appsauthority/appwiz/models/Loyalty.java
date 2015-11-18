package com.appsauthority.appwiz.models;

/* Copyright (C)
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Kevin Irish Antonio <irish.antonio@yahoo.com>, February 2014
 */
import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Loyalty implements Serializable {

	private static final long serialVersionUID = 1L;

	@SerializedName("errorCode")
	String errorCode;

	@SerializedName("loyaltyImage")
	String loyaltyImage;

	@SerializedName("termsCond")
	String termsCond;

	@SerializedName("fbUrl")
	String fbUrl;

	@SerializedName("fbIconDisplay")
	String fbIconDisplay;

	@SerializedName("instagramUrl")
	String instagramUrl;

	@SerializedName("instagramDisplay")
	String instagramDisplay;
	
	public String getInstagramUrl() {
		return instagramUrl;
	}

	public void setInstagramUrl(String instagramUrl) {
		this.instagramUrl = instagramUrl;
	}

	public String getInstagramDisplay() {
		return instagramDisplay;
	}

	public void setInstagramDisplay(String instagramDisplay) {
		this.instagramDisplay = instagramDisplay;
	}

	public Loyalty() {
		super();
	}

	public String getFbIconDisplay() {
		return fbIconDisplay;
	}

	public void setFbIconDisplay(String fbIconDisplay) {
		this.fbIconDisplay = fbIconDisplay;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getLoyaltyImage() {
		return loyaltyImage;
	}

	public void setLoyaltyImage(String loyaltyImage) {
		this.loyaltyImage = loyaltyImage;
	}

	public String getTermsCond() {
		return termsCond;
	}

	public void setTermsCond(String termsCond) {
		this.termsCond = termsCond;
	}

	public String getFbUrl() {
		return fbUrl;
	}

	public void setFbUrl(String fbUrl) {
		this.fbUrl = fbUrl;
	}

}
