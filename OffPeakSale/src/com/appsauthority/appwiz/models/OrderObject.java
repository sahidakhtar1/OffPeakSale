package com.appsauthority.appwiz.models;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class OrderObject implements Serializable {

	@SerializedName("orderId")
	public String orderId;

	@SerializedName("qrCode")
	public String qrCode;

	@SerializedName("paymentTransactionId")
	public String paymentTransactionId;

	@SerializedName("orderDate")
	public String orderDate;

	@SerializedName("discount")
	public String discount;

	@SerializedName("discountAmt")
	public String discountAmt;

	@SerializedName("reward_redeemed")
	public String reward_redeemed;

	@SerializedName("shippingAmt")
	public String shippingAmt;

	@SerializedName("orderTotal")
	public String orderTotal;

	@SerializedName("orderStatus")
	public String shippingStatus;

	@SerializedName("deliverydate")
	public String deliverydate;

	@SerializedName("deliveryaddress")
	public String deliveryaddress;

	@SerializedName("collectiondate")
	public String collectiondate;

	@SerializedName("collectionaddress")
	public String collectionaddress;

	@SerializedName("consumerInfo")
	public DeliveryInfoObject consumerInfo;
	
	@SerializedName("orderExpiry")
	public String orderExpiryDate;
	
	@SerializedName("orderInstr")
	public String orderInstr;
	
	@SerializedName("merchantEmail")
	public String merchantEmail;

	@SerializedName("products")
	public List<Product> products;

}
