package com.appsauthority.appwiz.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.appsauthority.appwiz.models.Country;
import com.appsauthority.appwiz.models.Loyalty;
import com.appsauthority.appwiz.models.Product;
import com.appsauthority.appwiz.models.ProductCategory;
import com.appsauthority.appwiz.models.Profile;
import com.appsauthority.appwiz.models.Retailer;
import com.appsauthority.appwiz.models.RetailerStores;
import com.appsauthority.appwiz.models.Voucher;

public class SQLiteHelper extends SQLiteOpenHelper {

	private String TAG = "SQLiteHelper";
	private Context context;
	private static final String DATABASE_NAME = "appwiz.sqlite";
	private static final int DATABASE_VERSION = 1;
	private String DATABASE_PATH;
	private SQLiteDatabase db = null;
	private String retailerTable = "RETAILER";
	private String storeTable = "STORE";
	private String countryTable = "COUNTRY";
	private String profileTable = "CONSUMER_PROFILE";
	private String voucherTable = "VOUCHER";
	private String categoryTable = "CATEGORY";
	private String productTable = "PRODUCT";
	private String loyaltyTable = "LOYALTY";

	public SQLiteHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
		DATABASE_PATH = context.getDatabasePath(DATABASE_NAME).getPath();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();

		if (dbExist) {
			Log.d(TAG, "There is an existing database");
		} else {
			this.getReadableDatabase();
			copyDataBase();
		}

	}

	private boolean checkDataBase() {

		File dbFile = new File(DATABASE_PATH);
		return dbFile.exists();

	}

	private void copyDataBase() throws IOException {

		InputStream myInput = context.getAssets().open(DATABASE_NAME);

		String outFileName = DATABASE_PATH;

		OutputStream myOutput = new FileOutputStream(outFileName);

		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void openDataBase() throws SQLException {

		String myPath = context.getDatabasePath(DATABASE_NAME).getPath();

		db = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);

	}

	@Override
	public synchronized void close() {

		if (db != null)
			db.close();

		super.close();

	}

	public Retailer getRetailer() {
		Helper sharedHelper = Helper.getSharedHelper();
		return sharedHelper.reatiler;
		// Retailer retailer = new Retailer();
		// String strQuery =
		// "SELECT splashImage, poweredBy, headerColor, retailerTextColor, retailerName, retailerFileType, retailerFile, companyLogo, backdropColor1, backdropColor2, backdropType, backdropFile FROM RETAILER";
		// Cursor cursor = db.rawQuery(strQuery, null);
		// if (cursor.getCount() > 0) {
		// cursor.moveToFirst();
		// retailer.setSplashImage(cursor.getString(0));
		// retailer.setPoweredBy(cursor.getString(1));
		// retailer.setHeaderColor(cursor.getString(2));
		// retailer.setRetailerTextColor(cursor.getString(3));
		// retailer.setRetailerName(cursor.getString(4));
		// retailer.setRetailerFileType(cursor.getString(5));
		// retailer.setRetailerFile(cursor.getString(6));
		// retailer.setCompanyLogo(cursor.getString(7));
		// retailer.setBackdropColor1(cursor.getString(8));
		// retailer.setBackdropColor2(cursor.getString(9));
		// retailer.setBackdropType(cursor.getString(10));
		// retailer.setBackdropFile(cursor.getString(11));
		// }
		// cursor.close();

	}

	public ArrayList<RetailerStores> getStores() {
		ArrayList<RetailerStores> stores = new ArrayList<RetailerStores>();
		String strQuery = "SELECT storeAddress, storeContact, latitude, longitude FROM STORE";
		Cursor cursor = db.rawQuery(strQuery, null);
		RetailerStores store;
		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
			store = new RetailerStores();
			store.setStoreAddress(cursor.getString(0));
			store.setStoreContact(cursor.getString(1));
			store.setLatitude(cursor.getString(2));
			store.setLongitude(cursor.getString(3));
			stores.add(store);
		}
		cursor.close();
		return stores;
	}

	public ArrayList<Country> getCountries() {
		ArrayList<Country> countryList = new ArrayList<Country>();
		Country country;
		String strQuery = "SELECT countryCode, countryName FROM COUNTRY";
		Cursor cursor = db.rawQuery(strQuery, null);
		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
			country = new Country();
			country.setCountryCode(cursor.getString(0));
			country.setCountryName(cursor.getString(1));
			countryList.add(country);
		}
		cursor.close();
		return countryList;
	}

	public Profile getProfile() {
		Profile profile = new Profile();
		String strQuery = "SELECT fname, lname, gender, age, dob, mobile_num, email, address, city, state, country, zip FROM CONSUMER_PROFILE";
		Cursor cursor = db.rawQuery(strQuery, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
//			profile.setFirstName(cursor.getString(0));
//			profile.setLastName(cursor.getString(1));
//			profile.setGender(cursor.getString(2));
//			profile.setAge(cursor.getInt(3));
//			profile.setDob(cursor.getString(4));
//			profile.setMobileNo(cursor.getLong(5));
//			profile.setEmail(cursor.getString(6));
//			profile.setAddress(cursor.getString(7));
//			profile.setCity(cursor.getString(8));
//			profile.setState(cursor.getString(9));
//			profile.setCountry(cursor.getString(10));
//			profile.setZip(cursor.getLong(11));
		}
		return profile;
	}

	public String getCountryCode(String country) {
		String countryCode = "";
		String strQuery = "SELECT countryCode FROM COUNTRY WHERE countryName = '"
				+ country + "'";
		Cursor cursor = db.rawQuery(strQuery, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			countryCode = cursor.getString(0);
		}
		cursor.close();

		return countryCode;
	}

	public ArrayList<Voucher> getVouchers() {
		ArrayList<Voucher> vouchers = new ArrayList<Voucher>();
		String strQuery = "SELECT voucherMsg, voucherType FROM VOUCHER";
		Cursor cursor = db.rawQuery(strQuery, null);
		Voucher voucher;
		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
			voucher = new Voucher();
			voucher.setMsg(cursor.getString(0));
			voucher.setType(cursor.getString(1));
			vouchers.add(voucher);
		}
		cursor.close();
		return vouchers;
	}

	public ArrayList<Product> getProducts(int categoryId) {
		ArrayList<Product> products = new ArrayList<Product>();
		String strQuery = "SELECT id, name, short_desc, desc, how_it_works, old_price, new_price, product_img FROM PRODUCT WHERE category_id = "
				+ categoryId;
		Cursor cursor = db.rawQuery(strQuery, null);
		Product product;
		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
			product = new Product();
			product.setId(cursor.getString(0));
			product.setName(cursor.getString(1));
			product.setShortDescription(cursor.getString(2));
			product.setDescription(cursor.getString(3));
			product.setHowItWorks(cursor.getString(4));
			product.setOldPrice(cursor.getString(5));
			product.setNewPrice(cursor.getString(6));
			product.setImage(cursor.getString(7));
			products.add(product);
		}

		cursor.close();
		return products;
	}

	public ArrayList<ProductCategory> getCategories() {
		ArrayList<ProductCategory> categories = new ArrayList<ProductCategory>();
		String strQuery = "SELECT id, name FROM CATEGORY";
		Cursor cursor = db.rawQuery(strQuery, null);
		ProductCategory category;
		for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor.moveToNext()) {
			category = new ProductCategory();
			category.setId(cursor.getInt(0));
			category.setCategory(cursor.getString(1));
			categories.add(category);
		}
		cursor.close();
		return categories;
	}

	public Loyalty getLoyalty() {
		Loyalty loyalty = new Loyalty();
		String strQuery = "SELECT loyaltyImage, termsCond, fbUrl FROM LOYALTY";
		Cursor cursor = db.rawQuery(strQuery, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			loyalty.setLoyaltyImage(cursor.getString(0));
			loyalty.setTermsCond(cursor.getString(1));
			loyalty.setFbUrl(cursor.getString(2));
		}
		cursor.close();
		return loyalty;
	}

	public void insertOrReplaceRetailer(Retailer retailer) {

		Helper sharedHelper = Helper.getSharedHelper();
		sharedHelper.reatiler = retailer;
		// ContentValues contentValues = new ContentValues();
		// contentValues.put("splashImage", retailer.getSplashImage());
		// contentValues.put("poweredBy", retailer.getPoweredBy());
		// contentValues.put("headerColor", retailer.getHeaderColor());
		// contentValues.put("retailerTextColor",
		// retailer.getRetailerTextColor());
		// contentValues.put("retailerName", retailer.getRetailerName());
		// contentValues.put("retailerFileType",
		// retailer.getRetailerFileType());
		// contentValues.put("retailerFile", retailer.getRetailerFile());
		// contentValues.put("companyLogo", retailer.getCompanyLogo());
		// contentValues.put("backdropColor1", retailer.getBackdropColor1());
		// contentValues.put("backdropColor2", retailer.getBackdropColor2());
		// contentValues.put("backdropType", retailer.getBackdropType());
		//
		// db.replace(retailerTable, null, contentValues);
	}

	public void insertOrReplaceStore(RetailerStores store) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("storeAddress", store.getStoreAddress());
		contentValues.put("storeContact", store.getStoreContact());
		contentValues.put("latitude", store.getLatitude());
		contentValues.put("longitude", store.getLongitude());

		db.replace(storeTable, null, contentValues);
	}

	public void insertOrReplaceCountry(Country country) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("countryCode", country.getCountryCode());
		contentValues.put("countryName", country.getCountryName());
		db.replace(countryTable, null, contentValues);
	}

	public void insertOrReplaceProfile(Profile profile) {
//		ContentValues contentValues = new ContentValues();
//		contentValues.put("retailerId", Constants.RETAILER_ID);
//		contentValues.put("fname", profile.getFirstName());
//		contentValues.put("lname", profile.getLastName());
//		contentValues.put("gender", profile.getGender() + "");
////		contentValues.put("age", profile.getAge());
//		contentValues.put("dob", profile.getDob());
//		contentValues.put("mobile_num", profile.getMobileNo());
//		contentValues.put("email", profile.getEmail());
//		contentValues.put("address", profile.getAddress());
//		contentValues.put("city", profile.getCity());
//		contentValues.put("state", profile.getState());
//		contentValues.put("country", profile.getCountry());
//		contentValues.put("zip", profile.getZip());
//		contentValues.put("lat", profile.getLat());
//		contentValues.put("long", profile.getLng());
//		contentValues.put("device_token", profile.getDeviceToken());
//		contentValues.put("device", 2);
//		contentValues.put("latestTime", profile.getTime());
//		db.replace(profileTable, null, contentValues);
	}

	public void insertOrReplaceVoucher(Voucher voucher) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("voucherMsg", voucher.getMsg());
		contentValues.put("voucherType", voucher.getType());
		db.replace(voucherTable, null, contentValues);
	}

	public long insertOrReplaceCategory(ProductCategory category) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("name", category.getCategory());
		return db.replace(categoryTable, null, contentValues);
	}

	public void insertOrReplaceProduct(Product product) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("id", product.getId());
		contentValues.put("name", product.getName());
		contentValues.put("short_desc", product.getShortDescription());
		contentValues.put("desc", product.getDescription());
		contentValues.put("how_it_works", product.getHowItWorks());
		contentValues.put("old_price", product.getOldPrice());
		contentValues.put("new_price", product.getNewPrice());
		contentValues.put("product_img", product.getImage());
		contentValues.put("category_id", product.getCategoryId());

		db.replace(productTable, null, contentValues);
	}

	public void insertOrReplaceLoyalty(Loyalty loyalty) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("loyaltyImage", loyalty.getLoyaltyImage());
		contentValues.put("termsCond", loyalty.getTermsCond());
		contentValues.put("fbUrl", loyalty.getFbUrl());
		contentValues.put("fbIconDisplay", loyalty.getFbIconDisplay());
		db.replace(loyaltyTable, null, contentValues);
	}

	public void deleteVoucher(Voucher voucher) {
		db.delete(voucherTable, "voucherMsg = ?",
				new String[] { voucher.getMsg() });
	}

	public void deleteCategories() {
		db.delete(categoryTable, null, null);
	}

	public void deleteStores() {
		db.delete(storeTable, null, null);
	}

	public void deleteLoyalty() {
		db.delete(loyaltyTable, null, null);
	}

	public void deleteRetailer() {
		db.delete(retailerTable, null, null);
	}

}
