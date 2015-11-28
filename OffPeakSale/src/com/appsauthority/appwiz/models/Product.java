package com.appsauthority.appwiz.models;

/* Copyright (C)
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Kevin Irish Antonio <irish.antonio@yahoo.com>, February 2014
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Product implements Serializable {

	private static final long serialVersionUID = 1L;

	int categoryId;

	@SerializedName("id")
	String id;

	@SerializedName("name")
	String name;

	@SerializedName("short_desc")
	String shortDescription;

	@SerializedName("testimonials")
	String testimonials;

	@SerializedName("desc")
	String description;

	@SerializedName("how_it_works")
	String howItWorks;

	@SerializedName("old_price")
	public
	String oldPrice;

	@SerializedName("new_price")
	public	String newPrice;

	@SerializedName("product_img")
	String image;

	@SerializedName("product_imgArray")
	public List<MediaObject> product_imgArray;

	@SerializedName("oldPriceArr")
	public HashMap<String, String> oldPriceArr;

	@SerializedName("newPriceArr")
	public HashMap<String, String> newPriceArr;

	@SerializedName("reward_points")
	public String reward_points;

	@SerializedName("availQty")
	public String availQty;

	@SerializedName("onSale")
	public String onSale;

	@SerializedName("quantity")
	public String quantity;

	@SerializedName("prodOptions")
	public String prodOptions;

	@SerializedName("outlets")
	public ArrayList<RetailerStores> outlets;

	@SerializedName("offpeak_discount")
	public String offpeakDiscount;

	@SerializedName("outletDistance")
	public String distance;

	@SerializedName("outletName")
	public String outletName;

	@SerializedName("outletAddr")
	public String storeAddress;

	@SerializedName("outletContact")
	public String storeContact;

	@SerializedName("outletLat")
	public String latitude;

	@SerializedName("outletLong")
	public String longitude;

	Boolean isOptedGiftWrap;
	String qty;
	String giftMsg;
	String giftTo;

	String selectedOption1;
	String selectedOption2;

	public String getSelectedOption1() {
		return selectedOption1;
	}

	public void setSelectedOption1(String selectedOption1) {
		this.selectedOption1 = selectedOption1;
	}

	public String getSelectedOption2() {
		return selectedOption2;
	}

	public void setSelectedOption2(String selectedOption2) {
		this.selectedOption2 = selectedOption2;
	}

	@SerializedName("productRating")
	String productRating;

	@SerializedName("product_options")
	public List<ProductOption> product_options;

	public List<ProductOption> getProduct_options() {
		return product_options;
	}

	public void setProduct_options(List<ProductOption> product_options) {
		this.product_options = product_options;
	}

	public Product() {
		super();
	}

	public String getProductRating() {
		return productRating;
	}

	public void setProductRating(String productRating) {
		this.productRating = productRating;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHowItWorks() {
		return howItWorks;
	}

	public void setHowItWorks(String howItWorks) {
		this.howItWorks = howItWorks;
	}

	public String getOldPrice() {
		if (oldPrice != null && Float.parseFloat(oldPrice) <= 0.0f) {
			return null;
		}
		return oldPrice;
	}

	public void setOldPrice(String oldPrice) {
		if (oldPrice != null && Float.parseFloat(oldPrice) <= 0.0f) {
			this.oldPrice = null;
		} else {
			this.oldPrice = oldPrice;
		}
	}

	public String getNewPrice() {
		return newPrice;
	}

	public void setNewPrice(String newPrice) {
		this.newPrice = newPrice;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getTestimonials() {
		return testimonials;
	}

	public void setTestimonials(String testimonials) {
		this.testimonials = testimonials;
	}

	public Boolean getIsOptedGiftWrap() {
		return isOptedGiftWrap;
	}

	public void setIsOptedGiftWrap(Boolean isOptedGiftWrap) {
		this.isOptedGiftWrap = isOptedGiftWrap;
	}

	public String getGiftMsg() {
		return giftMsg;
	}

	public void setGiftMsg(String giftMsg) {
		this.giftMsg = giftMsg;
	}

	public String getGiftTo() {
		return giftTo;
	}

	public void setGiftTo(String giftTo) {
		this.giftTo = giftTo;
	}

	public Product copyProduct() {
		Product prod = new Product();
		prod.setId(getId());
		prod.setName(getName());
		prod.setDescription(getDescription());
		prod.setTestimonials(getTestimonials());
		prod.setShortDescription(getShortDescription());
		prod.setHowItWorks(getHowItWorks());
		prod.setOldPrice(getOldPrice());
		prod.setNewPrice(getNewPrice());
		prod.setImage(getImage());
		prod.setSelectedOption1(getSelectedOption1());
		prod.setSelectedOption2(getSelectedOption2());
		prod.setProductRating(getProductRating());
		prod.setProduct_options(getProduct_options());
		prod.setQty(getQty());
		prod.oldPriceArr = oldPriceArr;
		prod.newPriceArr = newPriceArr;
		prod.reward_points = reward_points;
		prod.setIsOptedGiftWrap(false);
		return prod;
	}

}
