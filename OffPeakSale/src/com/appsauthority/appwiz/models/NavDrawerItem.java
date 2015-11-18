package com.appsauthority.appwiz.models;

import com.appsauthority.appwiz.utils.Constants;


public class NavDrawerItem {
	
	private String title;
	private int icon;
	private int index;
	private String count = "0";
	// boolean to set visiblity of the counter
	private boolean isCounterVisible = false;
	public Boolean showRightArraow = false;
	public Constants.DrawerItemType itemType;
	
	public NavDrawerItem(){}

	public NavDrawerItem(String title, int icon, int index, Boolean showRightArraow){
		this.title = title;
		this.icon = icon;
		this.index = index;
		this.showRightArraow = showRightArraow;
	}
	
	public NavDrawerItem(String title, int icon, boolean isCounterVisible, String count){
		this.title = title;
		this.icon = icon;
		this.isCounterVisible = isCounterVisible;
		this.count = count;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public int getIcon(){
		return this.icon;
	}
	
	public String getCount(){
		return this.count;
	}
	
	public boolean getCounterVisibility(){
		return this.isCounterVisible;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public void setIcon(int icon){
		this.icon = icon;
	}
	
	public void setCount(String count){
		this.count = count;
	}
	
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setCounterVisibility(boolean isCounterVisible){
		this.isCounterVisible = isCounterVisible;
	}
}
