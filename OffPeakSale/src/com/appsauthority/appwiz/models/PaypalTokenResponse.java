package com.appsauthority.appwiz.models;
/* Copyright (C)
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Kevin Irish Antonio <irish.antonio@yahoo.com>, February 2014
 */
public class PaypalTokenResponse {
	
	String errorCode;
	String token;
	String sucessUrl;
	String cancelUrl;
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getSucessUrl() {
		return sucessUrl;
	}
	public void setSucessUrl(String sucessUrl) {
		this.sucessUrl = sucessUrl;
	}
	public String getCancelUrl() {
		return cancelUrl;
	}
	public void setCancelUrl(String cancelUrl) {
		this.cancelUrl = cancelUrl;
	}

}
