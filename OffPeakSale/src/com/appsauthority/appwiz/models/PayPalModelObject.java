package com.appsauthority.appwiz.models;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class PayPalModelObject implements Serializable {

	@SerializedName("token")
	public String token;
	@SerializedName("redirectUrl")
	public String redirectUrl;
	@SerializedName("sucessUrl")
	public String sucessUrl;
	@SerializedName("cancelUrl")
	public String cancelUrl;
	@SerializedName("paypalMode")
	public String paypalMode;

}
