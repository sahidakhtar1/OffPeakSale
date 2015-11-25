package com.appsauthority.appwiz.utils;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.location.Location;

import com.appsauthority.appwiz.models.CategoryObject;
import com.appsauthority.appwiz.models.EarliestSchedule;
import com.appsauthority.appwiz.models.Product;
import com.appsauthority.appwiz.models.Retailer;
import com.appsauthority.appwiz.models.RetailerStores;
import com.appsauthority.appwiz.models.Voucher;

public class Helper {

	private static Helper sharedHelper = null;
	public Retailer reatiler;
	public List<Product> shoppintCartList;
	public String currency_code, currency_symbol, enableRating,
			enableShoppingCart, enableCreditCode, creditCode, deliveryDays,
			enableDelivery, rewardPoints, enable_shipping, redeemPoints;
	public String siteFont;
	public Typeface normalFont, boldFont;
	public Boolean isDecialFromat = false;
	public String disablePayment;
	public List<Voucher> vouchers;
	public ArrayList<RetailerStores> stores;
	public String isSSL;
	public String discountPercent;
	public String termsConditions;
	public String discountType;
	public Float shippingCharge;
	public Float freeAmount;
	public Float discountAmount;
	public List<CategoryObject> categoryList;
	public int filterIndex;
	public Context context;
	public String deleveryScheduleSelected, selectedCollectionAddress;
	public String rewardPountsEarned;
	public HashMap<String, String> currency_conversion_map;
	public Boolean isShowingVoucher;
	public int voucherIndex;
	public Boolean isAppUpdateAlertShown = false;
	public Boolean isScanAdminPWD = false;
	public int deliveryOptionSelectedIndex = 0;
	public Date earliestDate = null;

	public static Helper getSharedHelper() {
		if (sharedHelper == null) {
			sharedHelper = new Helper();
			sharedHelper.reatiler = new Retailer();
			sharedHelper.currency_code = "SGD";
			sharedHelper.currency_symbol = "$";
			sharedHelper.enableRating = "0";
			sharedHelper.enableShoppingCart = "1";
			sharedHelper.disablePayment = "0";
			sharedHelper.enableCreditCode = "0";
			sharedHelper.shoppintCartList = new ArrayList<Product>();
			sharedHelper.vouchers = new ArrayList<Voucher>();
			sharedHelper.stores = new ArrayList<RetailerStores>();
			sharedHelper.categoryList = new ArrayList<CategoryObject>();
			sharedHelper.isSSL = "0";
			sharedHelper.discountPercent = "0";
			sharedHelper.termsConditions = "";
			sharedHelper.shippingCharge = 0f;
			sharedHelper.freeAmount = 0f;
			sharedHelper.discountAmount = 0f;
			sharedHelper.deliveryDays = "0";
			sharedHelper.enableDelivery = "0";
			sharedHelper.rewardPoints = "0";
			sharedHelper.filterIndex = 0;
			sharedHelper.enable_shipping = "0";
			sharedHelper.redeemPoints = "0";
			sharedHelper.deleveryScheduleSelected = "";
			sharedHelper.selectedCollectionAddress = "";
			sharedHelper.rewardPountsEarned = "0";
			sharedHelper.isShowingVoucher = false;
			sharedHelper.voucherIndex = 0;
			sharedHelper.currency_conversion_map = new HashMap<String, String>();
			sharedHelper.discountType = Constants.KEY_DEFAULT_DISCOUNT_TYPE;
			sharedHelper.earliestDate = null;
		}
		return sharedHelper;
	}

	public void setCurrencySymbol() {
		HashMap<String, String> meMap = new HashMap<String, String>();
		meMap.put("AUD", "$");
		meMap.put("BRL", "R$");
		meMap.put("CAD", "$");
		meMap.put("CZK", "Kc");
		meMap.put("DKK", "kr");
		meMap.put("EUR", "€");
		meMap.put("HKD", "$");
		meMap.put("HUF", "Ft");
		meMap.put("ILS", "~");
		meMap.put("Y", "JPY");
		meMap.put("MYR", "RM");
		meMap.put("MXN", "$");
		meMap.put("NOK", "kr");
		meMap.put("NZD", "$");
		meMap.put("PHP", "P");
		meMap.put("PLN", "zl");
		meMap.put("GBP", "£");
		meMap.put("RUB", "ру6");
		meMap.put("SGD", "$");
		meMap.put("SEK", "kr");
		meMap.put("CHF", "CHF");
		meMap.put("TWD", "NT$");
		meMap.put("THB", "B");
		meMap.put("TRY", "Tr");
		meMap.put("USD", "$");
		meMap.put("INR", "₹");
		sharedHelper.currency_symbol = sharedHelper
				.getCurrencySymbol(sharedHelper.currency_code);
		// if (meMap.containsKey(sharedHelper.currency_code)) {
		// sharedHelper.currency_symbol = (String) meMap
		// .get(sharedHelper.currency_code);
		// }

	}

	public String getCurrencySymbol(String code) {
		HashMap<String, String> meMap = new HashMap<String, String>();
		meMap.put("AUD", "$");
		meMap.put("BRL", "R$");
		meMap.put("CAD", "$");
		meMap.put("CZK", "Kc");
		meMap.put("DKK", "kr");
		meMap.put("EUR", "€");
		meMap.put("HKD", "$");
		meMap.put("HUF", "Ft");
		meMap.put("ILS", "~");
		meMap.put("Y", "JPY");
		meMap.put("MYR", "RM");
		meMap.put("MXN", "$");
		meMap.put("NOK", "kr");
		meMap.put("NZD", "$");
		meMap.put("PHP", "P");
		meMap.put("PLN", "zl");
		meMap.put("GBP", "£");
		meMap.put("RUB", "ру6");
		meMap.put("SGD", "$");
		meMap.put("SEK", "kr");
		meMap.put("CHF", "CHF");
		meMap.put("TWD", "NT$");
		meMap.put("THB", "B");
		meMap.put("TRY", "Tr");
		meMap.put("USD", "$");
		meMap.put("INR", "₹");
		if (meMap.containsKey(code)) {
			return (String) meMap.get(code);
		}
		return code;

	}

	public void getVouchers() {

	}

	public void addVouchers(Voucher v) {

	}

	public void deleteVoucher(Voucher v) {

	}

	public String getDistanceBetween(double startLat, double startLng,
			double endLat, double endLng) {
		String distaceStr = "-";
//		startLat = 1.3115829;
//		startLng = 103.8382705;
		Location locationA = new Location("point A");
		locationA.setLatitude(startLat);
		locationA.setLongitude(startLng);
		Location locationB = new Location("point B");
		locationB.setLatitude(endLat);
		locationB.setLongitude(endLng);
		float distance = locationA.distanceTo(locationB)/1000.0f;
		DecimalFormat df;
		df = new DecimalFormat("0");
		df.setMaximumFractionDigits(2);
		distaceStr = df.format(distance);
		return distaceStr;
	}

	public String getRegularFontPath(String fontname) {
		String fontPath = "fonts/OpenSans.ttf";
		HashMap<String, String> normalFontMap = new HashMap<String, String>();
		// normalFontMap.put("American", "american_typewriter_medium_bt.ttf");
		normalFontMap.put("Arial", "Arial.ttf");

		normalFontMap.put("Opensans", "OpenSans.ttf");
		// normalFontMap.put("Petrona", "Petrona.ttf");

		normalFontMap.put("Folks", "Folks-Normal.ttf");
		normalFontMap.put("Aleo", "Aleo-Regular.otf");

		normalFontMap.put("Georgia", "Georgia.ttf");

		normalFontMap.put("Times", "Times New Roman.ttf");
		normalFontMap.put("Trebuchet", "Trebuchet MS.ttf");
		if (normalFontMap.containsKey(fontname)) {
			String normaFontName = normalFontMap.get(fontname);

			fontPath = "fonts/" + normaFontName;
		}
		return fontPath;

	}

	public String getBoldFontPath(String fontname) {
		String fontPath = "fonts/OpenSans-Bold.ttf";
		HashMap<String, String> normalFontMap = new HashMap<String, String>();
		// normalFontMap.put("American", "american_typewriter_bold.ttf");
		normalFontMap.put("Arial", "Arial Bold.ttf");

		normalFontMap.put("Opensans", "OpenSans-Bold.ttf");
		// normalFontMap.put("Petrona", "Petrona.ttf");

		// normalFontMap.put("Bellota", "Bellota-Bold.otf");
		normalFontMap.put("Folks", "Folks-Bold.ttf");
		normalFontMap.put("Aleo", "Aleo-Bold.otf");

		// normalFontMap.put("Bellota", "elsie.black.ttf");
		normalFontMap.put("Georgia", "Georgia-Bold.ttf");

		normalFontMap.put("Times", "Times New Roman-Bold.ttf");
		normalFontMap.put("Trebuchet", "Trebuchet MS-Bold.ttf");
		if (normalFontMap.containsKey(fontname)) {
			String normaFontName = normalFontMap.get(fontname);

			fontPath = "fonts/" + normaFontName;
		}
		return fontPath;

	}

	public Boolean addPoductToCart(Product product) {
		for (Product p : sharedHelper.shoppintCartList) {
			if (p.getId().equalsIgnoreCase(product.getId())) {
				Boolean isFirstOptionSame = false;
				if (p.getSelectedOption1() != null) {
					if (p.getSelectedOption1().equalsIgnoreCase(
							product.getSelectedOption1())) {
						isFirstOptionSame = true;
					} else {
						isFirstOptionSame = false;
					}

				} else {
					isFirstOptionSame = true;
				}
				Boolean isSameProduct = false;
				if (isFirstOptionSame) {
					if (p.getSelectedOption2() != null) {
						if (p.getSelectedOption2().equalsIgnoreCase(
								product.getSelectedOption2())) {
							isSameProduct = true;
						} else {
							isSameProduct = false;
						}

					} else {
						isSameProduct = true;
					}
				} else {
					isSameProduct = false;
				}

				if (isSameProduct) {
					int qty = Integer.parseInt(p.getQty())
							+ Integer.parseInt(product.getQty());
					if (qty > 9) {
						return false;
					} else {
						p.setQty(Integer.toString(qty));
						return true;
					}
				}

			}
		}
		sharedHelper.shoppintCartList.add(product.copyProduct());
		sharedHelper.clearCreditCode();
		return true;
	}

	public Product getProduct(String productId) {
		for (Product p : sharedHelper.shoppintCartList) {
			if (p.getId().equalsIgnoreCase(productId)) {
				return p;
			}
		}

		return null;
	}

	public void clearCreditCode() {
		sharedHelper.discountAmount = 0f;
		sharedHelper.discountPercent = "0";
		sharedHelper.rewardPountsEarned = "0";
	}

	public String productIdsInCart() {
		String productIds = "";
		for (Product p : sharedHelper.shoppintCartList) {
			if (productIds.equalsIgnoreCase("")) {
				productIds = p.getId();
			} else {
				productIds = productIds + "," + p.getId();
			}
		}
		return productIds;
	}

	public String getCartTotal() {
		String total = "0";
		int totalCount = 0;
		for (Product product : sharedHelper.shoppintCartList) {
			totalCount += Integer.parseInt(product.getQty());
		}
		total = Integer.toString(totalCount);
		return total;

	}

	public String getCartTotalAmount() {
		String total = "0";
		float totalCount = 0;
		int totalRewads = 0;
		for (Product product : sharedHelper.shoppintCartList) {
			int qty = Integer.parseInt(product.getQty());
			float unitprice = Float.parseFloat(product.getNewPrice());

			if (product.getIsOptedGiftWrap()) {
				unitprice += Float.parseFloat(sharedHelper.reatiler.gift_price);
			}
			totalCount += qty * unitprice;
			totalRewads += qty * Integer.parseInt(product.reward_points);
			if (product.getIsOptedGiftWrap()) {

			}
		}

		float discount = 0;
		if (Helper.getSharedHelper().discountType
				.equalsIgnoreCase(Constants.KEY_DEFAULT_DISCOUNT_TYPE)) {

			discount = totalCount
					* Float.parseFloat(sharedHelper.discountPercent) / 100;
			;
		} else {

			discount = Float
					.parseFloat(Helper.getSharedHelper().discountPercent);
		}
		sharedHelper.discountAmount = discount;
		sharedHelper.rewardPountsEarned = Integer.toString(totalRewads);
		float netAmount = totalCount - discount;
		if (netAmount < 0) {
			netAmount = 0;
		}
		total = Float.toString(netAmount);

		return total;

	}

	public float totalPriceAfterDiscount(float totalPrice) {
		float discount;
		if (Helper.getSharedHelper().discountType
				.equalsIgnoreCase(Constants.KEY_DEFAULT_DISCOUNT_TYPE)) {

			discount = totalPrice
					* Float.parseFloat(sharedHelper.discountPercent) / 100;
			;
		} else {

			discount = Float
					.parseFloat(Helper.getSharedHelper().discountPercent);
		}
		float netPrice = totalPrice - discount;
		if (netPrice < 0) {
			netPrice = 0;
		}
		return netPrice;
	}

	public String conertfloatToSTring(float value) {
		String convertedValue = "0";
		DecimalFormat df;
		df = new DecimalFormat("0");
		df.setMaximumFractionDigits(0);
		String stringValue = df.format(value);
		Boolean isDecimal = false;
		if (stringValue.length() < 7) {
			isDecimal = true;
		} else {
			isDecimal = false;
		}
		if (isDecimal) {
			df = new DecimalFormat("0.00");
			df.setMaximumFractionDigits(2);
		} else {
			df = new DecimalFormat("0");
			df.setMaximumFractionDigits(0);
		}
		// df.setMaximumFractionDigits(2);
		convertedValue = df.format(value);
		return convertedValue;
	}

	public String conertfloatToSTringBasedLength(float value) {
		String convertedValue = "0";
		DecimalFormat df;
		df = new DecimalFormat("0");
		df.setMaximumFractionDigits(0);
		String stringValue = df.format(value);
		Boolean isDecimal;
		if (stringValue.length() < 6) {
			isDecimal = true;
		} else {
			isDecimal = false;
		}
		if (isDecimal) {
			df = new DecimalFormat("0.00");
			df.setMaximumFractionDigits(2);
		} else {
			df = new DecimalFormat("0");
			df.setMaximumFractionDigits(0);
		}
		// df.setMaximumFractionDigits(2);
		convertedValue = df.format(value);
		return convertedValue;
	}

	public void getDeviceType() {

	}

	public GradientDrawable getGradientDrawableEditText(String headerColor) {
		GradientDrawable gdDefault = new GradientDrawable();
		// gdDefault.setColor(Color.parseColor("#" + headerColor));
		// gdDefault.setAlpha(40);
		gdDefault.setCornerRadius(7);
		gdDefault.setStroke(2, Color.parseColor("#" + headerColor));
		gdDefault.setColor(Color.WHITE);
		// Drawable gdDefault = context.getResources().getDrawable(
		// R.drawable.apptheme_edit_text_holo_light);
		return gdDefault;
	}

	public GradientDrawable getGradientDrawable(String headerColor) {
		// GradientDrawable gd;
		//
		// gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM,
		// new int[] { Color.parseColor("#80" + headerColor),
		// Color.parseColor("#" + headerColor),
		// Color.parseColor("#" + headerColor) });
		if (headerColor == null) {
			headerColor = sharedHelper.reatiler.getHeaderColor();
		}
		// ColorDrawable cd = new ColorDrawable(
		// Color.parseColor("#" + headerColor));
		// // gd.setCornerRadius(10);
		// return cd;
		GradientDrawable gdDefault = new GradientDrawable();
		// gdDefault.setColor(Color.parseColor("#" + headerColor));
		// gdDefault.setAlpha(40);
		gdDefault.setCornerRadius(7);
		gdDefault.setStroke(2, Color.parseColor("#" + headerColor));
		gdDefault.setColor(Color.parseColor("#" + headerColor));

		return gdDefault;
	}

	public ColorDrawable getGradientDrawableNoRad(String headerColor) {
		// GradientDrawable gd = new GradientDrawable(
		// GradientDrawable.Orientation.TOP_BOTTOM, new int[] {
		// Color.parseColor("#80" + headerColor),
		// Color.parseColor("#" + headerColor) });
		//
		// gd.setCornerRadius(0);
		ColorDrawable cd = new ColorDrawable(
				Color.parseColor("#" + headerColor));
		return cd;
	}

	public String toTitleCase(String string) {
		// String titelCaseString = "";
		//
		// String[] tokens = string.split(" ");
		//
		// for (int i = 0; i < tokens.length; i++) {
		// char capLetter = Character.toUpperCase(tokens[i].charAt(0));
		// titelCaseString += " " + capLetter
		// + tokens[i].substring(1, tokens[i].length());
		// }
		// return titelCaseString;
		string = string.toLowerCase();
		String[] arr = string.split(" ");
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < arr.length; i++) {
			sb.append(Character.toUpperCase(arr[i].charAt(0)))
					.append(arr[i].substring(1)).append(" ");
		}
		return sb.toString().trim();
	}

	public String dateByAddingDays(int days) {
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, days);
		Date dt = c.getTime();
		return stringFromDate(dt);
	}

	public String stringFromDate(Date date) {
		String DATE_FORMAT_NOW = "dd-MMM-yyyy";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		String stringDate = sdf.format(date);
		return stringDate;
	}

	public EarliestSchedule getDefalultEarliestDate(int hours, int mins,
			String timeSlots[], Boolean isTimeLapse) {
		int daysToAdd = hours / 24;
		hours = hours % 24;
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, daysToAdd);
		Date dt = c.getTime();
		int curentHour = dt.getHours();
		int curentMin = dt.getMinutes();
		int earliestMin = curentMin + mins;
		int extraHour = 0;
		if (earliestMin > 60) {
			extraHour = earliestMin / 60;
			earliestMin = earliestMin % 60;
		}
		int earliestHour = curentHour + hours + extraHour;
		if (earliestHour > 24) {
			daysToAdd++;
			c.setTime(new Date());
			c.add(Calendar.DATE, daysToAdd);
			dt = c.getTime();
			earliestHour = earliestHour - 24;
		}
		List<String> possibleTimeSlots = null;
		EarliestSchedule earliestSchedule = new EarliestSchedule();
		earliestSchedule.possibleTimeSlots = new ArrayList<String>();
		if (!isTimeLapse) {
			possibleTimeSlots = findTimePossibleTimeSlotes(timeSlots,
					earliestHour, dt.getMinutes());
			if (possibleTimeSlots.size() == 0) {
				possibleTimeSlots.clear();
				for (String slot : timeSlots) {
					possibleTimeSlots.add(slot);
				}
				daysToAdd++;
				c.setTime(new Date());
				c.add(Calendar.DATE, daysToAdd);
				dt = c.getTime();

			}
			earliestSchedule.possibleTimeSlots.addAll(possibleTimeSlots);
		}

		earliestSchedule.earliestDate = stringFromDate(dt);

		sharedHelper.earliestDate = dt;

		return earliestSchedule;

	}

	public List<String> findTimePossibleTimeSlotes(String timeSlots[],
			int hour, int min) {
		List<String> possibleSlots = new ArrayList<String>();
		for (String slot : timeSlots) {
			String slotComp[] = slot.split("-");
			if (slotComp.length > 1) {
				String endComp = slotComp[1];
				String hourNTimeComp[] = endComp.split(":");
				if (hourNTimeComp.length > 1) {
					int hourComp = Integer.parseInt(hourNTimeComp[0]);
					if (hourComp > hour) {// Add the slot
						possibleSlots.add(slot);
					} else if (hourComp == hour) {
						int minComp = Integer.parseInt(hourNTimeComp[1]);
						if (minComp >= min) {// Add the slot
							possibleSlots.add(slot);
						}
					}
				}
			}
		}
		return possibleSlots;
	}

	public String convert24hrSlotsTo12hr(String slot) {
		String slotComp[] = slot.split("-");
		String result = "";
		for (int i = 0; i < slotComp.length; i++) {
			String comp = slotComp[i];
			String hourNTimeComp[] = comp.split(":");

			int hourComp = Integer.parseInt(hourNTimeComp[0]);
			String meridian = "AM";
			if (hourComp > 12) {
				meridian = "PM";
				hourComp -= 12;
			}
			String hourPrefix = "";
			if (hourComp < 10) {
				hourPrefix = "0";
			}
			if (i == 0) {
				result += hourPrefix + hourComp + ":" + hourNTimeComp[1]
						+ meridian;
			} else {
				result += "-" + hourPrefix + hourComp + ":" + hourNTimeComp[1]
						+ meridian;
			}

		}
		return result;
	}

	public int getWidthforHeight(int imgHeight) {

		int width = (int) (1.67f * imgHeight);

		return width;
	}

	public boolean isEmailValid(String email) {
		boolean isValid = false;

		String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
		CharSequence inputStr = email;

		Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);
		if (matcher.matches()) {
			isValid = true;
		}
		return isValid;
	}
}
