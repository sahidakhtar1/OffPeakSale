package com.appsauthority.appwiz.models;

/* Copyright (C)
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential
 * Written by Kevin Irish Antonio <irish.antonio@yahoo.com>, February 2014
 */
import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ProductCategory implements Serializable {

	private static final long serialVersionUID = 1L;

	@SerializedName("category")
	String category;

	@SerializedName("products")
	List<Product> products;

	private int id;

	public ProductCategory() {
		super();

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<Product> getProducts() {
		return products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

}
