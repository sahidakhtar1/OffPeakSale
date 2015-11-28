package com.appsauthority.appwiz.models;

/* Copyright (C)
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Kevin Irish Antonio <irish.antonio@yahoo.com>, February 2014
 */
import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class Profile implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@SerializedName("fname")
	private String firstName;

//	@SerializedName("lname")
//	private String lastName;
//	@SerializedName("dob")
//	private String dob;
	// private int age;
//	@SerializedName("gender")
//	private String gender;
	@SerializedName("mobile_num")
	private String mobileNo;
	@SerializedName("email")
	private String email;
//	@SerializedName("address")
//	private String address;
//	@SerializedName("city")
//	private String city;
//	@SerializedName("state")
//	private String state;
	@SerializedName("country")
	private String country;
//	@SerializedName("zip")
//	private long zip;
//	@SerializedName("lat")
//	private double lat;
//	@SerializedName("long")
//	private double lng;
//	@SerializedName("device_token")
//	private String deviceToken;
//	private long time;
//	@SerializedName("rewardPoints")
//	private String rewardPoints;
//	@SerializedName("reward_points")
//	private String reward_points;

	public Profile() {

	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

//	public String getLastName() {
//		return lastName;
//	}
//
//	public void setLastName(String lastName) {
//		this.lastName = lastName;
//	}
//
//	public String getDob() {
//		return dob;
//	}
//
//	public void setDob(String dob) {
//		this.dob = dob;
//	}

	// public int getAge() {
	// return age;
	// }
	//
	// public void setAge(int age) {
	// this.age = age;
	// }

//	public String getGender() {
//		return gender;
//	}
//
//	public void setGender(String gender) {
//		this.gender = gender;
//	}
//
//	public long getZip() {
//		return zip;
//	}
//
//	public void setZip(long zip) {
//		this.zip = zip;
//	}
//
//	public double getLat() {
//		return lat;
//	}
//
//	public void setLat(double lat) {
//		this.lat = lat;
//	}
//
//	public double getLng() {
//		return lng;
//	}
//
//	public void setLng(double lng) {
//		this.lng = lng;
//	}
//
//	public String getDeviceToken() {
//		return deviceToken;
//	}
//
//	public void setDeviceToken(String deviceToken) {
//		this.deviceToken = deviceToken;
//	}
//
//	public long getTime() {
//		return time;
//	}
//
//	public void setTime(long time) {
//		this.time = time;
//	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

//	public String getAddress() {
//		return address;
//	}
//
//	public void setAddress(String address) {
//		this.address = address;
//	}
//
//	public String getCity() {
//		return city;
//	}
//
//	public void setCity(String city) {
//		this.city = city;
//	}
//
//	public String getState() {
//		return state;
//	}
//
//	public void setState(String state) {
//		this.state = state;
//	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

//	public String getRewardPoints() {
//		return rewardPoints;
//	}
//
//	public void setRewardPoints(String rewardPoints) {
//		this.rewardPoints = rewardPoints;
//	}
//
//	public String getReward_points() {
//		return reward_points;
//	}
//
//	public void setReward_points(String reward_points) {
//		this.reward_points = reward_points;
//	}

}
