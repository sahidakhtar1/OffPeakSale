package com.appsauthority.appwiz.models;

/* Copyright (C)
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Kevin Irish Antonio <irish.antonio@yahoo.com>, February 2014
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Retailer implements Serializable {

	private static final long serialVersionUID = 1L;

	@SerializedName("splashImage")
	String splashImage;

	@SerializedName("poweredBy")
	String poweredBy;

	@SerializedName("headerColor")
	String headerColor;

	@SerializedName("retailerTextColor")
	String retailerTextColor;

	@SerializedName("retailerName")
	String retailerName;

	@SerializedName("retailerFileType")
	String retailerFileType;

	@SerializedName("retailerFile")
	String retailerFile;

	@SerializedName("companyLogo")
	String companyLogo;

	@SerializedName("backdropColor1")
	String backdropColor1;

	@SerializedName("backdropColor2")
	String backdropColor2;

	@SerializedName("backdropType")
	String backdropType;

	@SerializedName("backdropFile")
	String backdropFile;

	@SerializedName("siteFont")
	String siteFont;

	@SerializedName("termsUrl")
	public String termsConditions;

	@SerializedName("defaultCurrency")
	public String defaultCurrency;

	@SerializedName("deliveryDays")
	public String deliveryDays;

	@SerializedName("allowedCurrencies")
	public String allowedCurrencies;

	@SerializedName("enableShoppingCart")
	public String enableShoppingCart;


	@SerializedName("enableDelivery")
	public String enableDelivery;

	@SerializedName("enable_shipping")
	public String enable_shipping;

	@SerializedName("enableRating")
	public String enableRating;

	@SerializedName("enableCreditCode")
	public String enableCreditCode;

	@SerializedName("products")
	public List<Product> products;

	@SerializedName("retailerStores")
	List<RetailerStores> retailerStores;

	@SerializedName("deliveryTimeSlots")
	public String deliveryTimeSlots;

	@SerializedName("appIconColor")
	public String appIconColor ;

	@SerializedName("enablePay")
	public String enablePay;
	
	@SerializedName("enableVerit")
	public String enableVerit;

	@SerializedName("enableCOD")
	public String enableCOD;

	@SerializedName("featuredStores")
	public List<FeaturedStore> featuredStores;

	@SerializedName("reward_points")
	public String reward_points = "0";

	@SerializedName("aboutUs")
	public String aboutUs;

	@SerializedName("home_imgArray")
	public List<MediaObject> home_imgArray = new ArrayList<MediaObject>();
	
	@SerializedName("aboutUrl")
	public String aboutUrl;
	
	@SerializedName("enableRewards")
	public String enableRewards;
	
	@SerializedName("androidVersion")
	public String androidVersion;
	
	@SerializedName("googlePlayUrl")
	public String googlePlayUrl;
	
	@SerializedName("instagramUrl")
	public String instagramUrl;
	
	@SerializedName("instagramDisplay")
	public String instagramDisplay;
	
	@SerializedName("fbUrl")
	public String fbUrl;
	
	@SerializedName("fbIconDisplay")
	public String fbIconDisplay;
	
	@SerializedName("enableCollection")
	public String enableCollection;
	
	@SerializedName("collectionDays")
	public String collectionDays;
	
	@SerializedName("collectionTimeSlots")
	public String collectionTimeSlots;
	
	@SerializedName("collectionAddress")
	public String collectionAddress;
	
	@SerializedName("collectionHours")
	public String collectionHours;
	
	@SerializedName("deliveryHours")
	public String deliveryHours;
	
	@SerializedName("deliveryType")
	public String deliveryType;
	
	@SerializedName("deliveryStandard")
	public String deliveryStandard;
	
	@SerializedName("collectionType")
	public String collectionType;
	
	@SerializedName("collectionStandard")
	public String collectionStandard;
	
	
	@SerializedName("enableFeatured")
	public String enableFeatured;
	
	@SerializedName("calendarUrl")
	public String calendarUrl;
	
	@SerializedName("enableGiftWrap")
	public String enableGiftWrap;
	
	@SerializedName("gift_price")
	public String gift_price;
	
	@SerializedName("tutorialSlides")
	public ArrayList<TourObject> tutorialSlides;
	
	
	@SerializedName("menuList")
	public List<MenuItem> menuList;

	public Retailer() {
		super();
	}

	public String getBackdropFile() {
		return backdropFile;
	}

	public void setBackdropFile(String backdropFile) {
		this.backdropFile = backdropFile;
	}

	public String getBackdropType() {
		return backdropType;
	}

	public void setBackdropType(String backdropType) {
		this.backdropType = backdropType;
	}

	public String getBackdropColor1() {
		return backdropColor1;
	}

	public void setBackdropColor1(String backdropColor1) {
		this.backdropColor1 = backdropColor1;
	}

	public String getBackdropColor2() {
		return backdropColor2;
	}

	public void setBackdropColor2(String backdropColor2) {
		this.backdropColor2 = backdropColor2;
	}

	public String getCompanyLogo() {
		return companyLogo;
	}

	public void setCompanyLogo(String companyLogo) {
		this.companyLogo = companyLogo;
	}

	public String getSplashImage() {
		return splashImage;
	}

	public void setSplashImage(String splashImage) {
		this.splashImage = splashImage;
	}

	public String getPoweredBy() {
		return poweredBy;
	}

	public void setPoweredBy(String poweredBy) {
		this.poweredBy = poweredBy;
	}

	public String getHeaderColor() {
		return headerColor;
	}

	public void setHeaderColor(String headerColor) {
		this.headerColor = headerColor;
	}

	public String getRetailerTextColor() {
		return retailerTextColor;
	}

	public void setRetailerTextColor(String retailerTextColor) {
		this.retailerTextColor = retailerTextColor;
	}

	public String getRetailerName() {
		return retailerName;
	}

	public void setRetailerName(String retailerName) {
		this.retailerName = new String(retailerName);
	}

	public String getRetailerFileType() {
		return retailerFileType;
	}

	public void setRetailerFileType(String retailerFileType) {
		this.retailerFileType = retailerFileType;
	}

	public String getRetailerFile() {
		return retailerFile;
	}

	public void setRetailerFile(String retailerFile) {
		this.retailerFile = retailerFile;
	}

	public List<RetailerStores> getRetailerStores() {
		return retailerStores;
	}

	public void setRetailerStores(List<RetailerStores> retailerStores) {
		this.retailerStores = retailerStores;
	}

	public String getSiteFont() {
		return siteFont;
	}

	public void setSiteFont(String siteFont) {
		this.siteFont = siteFont;
	}

}
