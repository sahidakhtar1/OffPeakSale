package com.appsauthority.appwiz.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

public class Utils {
	public static boolean hasNetworkConnection(Context context) {

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;

	}

	public static int lightenColor(int color, int amt) {

		int r = (color >> 16) + amt;
		int b = ((color >> 8) & 0x00FF) + amt;
		int g = (color & 0x0000FF) + amt;
		int newColor = g | (b << 8) | (r << 16);
		return newColor;
	}

	public static boolean isProfileAvailable(Context context) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPreferences.getBoolean(Constants.PROFILE, false);
	}

	public static void setProfile(Context context, boolean status) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(Constants.PROFILE, status);
		editor.commit();
	}

	public static int getStatusLoyaltyStamp1(Context context) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPreferences.getInt("STAMP1", 0);
	}

	public static int getStatusLoyaltyStamp2(Context context) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPreferences.getInt("STAMP2", 0);
	}

	public static int getStatusLoyaltyStamp3(Context context) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPreferences.getInt("STAMP3", 0);
	}

	public static int getStatusLoyaltyStamp4(Context context) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPreferences.getInt("STAMP4", 0);
	}

	public static int getStatusLoyaltyStamp5(Context context) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPreferences.getInt("STAMP5", 0);
	}

	public static String getFeedbackImage(Context context) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPreferences.getString("FEEDBACK", "");
	}

	public static String getPasswordImage(Context context) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		return sharedPreferences.getString("PASSWORD", "");
	}

	public static void setStatusLoyaltyStamp1(Context context, int status) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt("STAMP1", status);
		editor.commit();
	}

	public static void setStatusLoyaltyStamp2(Context context, int status) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt("STAMP2", status);
		editor.commit();
	}

	public static void setStatusLoyaltyStamp3(Context context, int status) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt("STAMP3", status);
		editor.commit();
	}

	public static void setStatusLoyaltyStamp4(Context context, int status) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt("STAMP4", status);
		editor.commit();
	}

	public static void setStatusLoyaltyStamp5(Context context, int status) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt("STAMP5", status);
		editor.commit();
	}

	public static void setFeedbackImage(Context context, String image) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("FEEDBACK", image);
		editor.commit();
	}

	public static void setPassword(Context context, String image) {
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("PASSWORD", image);
		editor.commit();
	}

}
