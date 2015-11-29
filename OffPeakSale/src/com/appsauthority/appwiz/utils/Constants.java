package com.appsauthority.appwiz.utils;

import android.graphics.Color;

public class Constants {

	public static String RETAILER_ID = "MerchantA1";
	public static String SENDER_ID = "610499968214";
	
	final public static int LOGIN_SUCCESS = 1001;

	// --- Global Var --- //

	public static String REG_ID = "";
	public static double LAT = 0;
	public static double LNG = 0;
	public static double TARGET_LAT = 0;
	public static double TARGET_LNG = 0;
	public static int BANNER_CHANGE_INTERVAL = 30000;

	public static enum DrawerItemType {
		NONE, HOME, CONTACTUS, ESHOP, FEATUREDSTORE, LAYALITY, FEEDBACK, VOUCHER, LOOKBOOK, CALENDER, PROFILE, TERMSNCONDITION, ABOUTUS, CURRENCY,ORDER_HISTORY,
	}

	// --- Theme --- //
	public static int BACKDROP1 = Color.parseColor("#FFFFFF");
	public static int BACKDROP2 = Color.parseColor("#FFFFFF");
	// public static int HEADER_COLOR = Color.parseColor("#951015");
	// public static int END_COLOR = Color.parseColor("#B8B8B8");
	// public static int END_COLOR = Color.parseColor("#AAA41212");
	// public static int END_COLOR_LIGHTER = Color.parseColor("#7E7E7E");
	// public static int TEXT_COLOR = Color.parseColor("#000000");

	// --- Shared Preferences --- //
	public static String SPREF = "APPWIZ_SHARED_PREF";
	public static String SPLASH_IMG = "SPLASH_IMAGE";
	public static String POWERED_BY = "POWERED_BY";
	public static String PROFILE = "PROFILE";

	public static String KEY_PUSH_TYPE = "type";
	public static String KEY_PUSH_MSG = "msg";
	public static String KEY_IS_APP_RUNNING = "app_state";
	public static String KEY_IS_USER_LOGGED_IN = "isUserLoggedIn";

	// --- Parameters --- //

	public static String PARAM_ID = "id";
	public static String PARAM_RETAILER_ID = "retailerId";
	public static String PARAM_CATEGORY_ID = "cid";
	public static String PARAM_FILTER_ID = "sortBy";
	public static String PARAM_KEYWORD_ID = "keyword";
	public static String PARAM_EMAIL = "email";
	public static String PARAM_CONSUMER_LAT = "consumer_lat";
	public static String PARAM_CONSUMER_LONG = "consumer_long";
	public static String PARAM_LAT = "lat";
	public static String PARAM_LONG = "long";
	public static String PARAM_DEVICE_TOKEN = "device_token";
	public static String PARAM_DEVICE = "device";
	public static String PARAM_PRODUCTID_FOR_TOKEN = "productId";
	public static String PARAM_QUANTITY = "quantity";
	public static String PARAM_FRIEND_EMAIL = "frndEmail";
	public static String PARAM_FRIEND_NAME = "frndName";
	public static String PARAM_FRIEND_MOBILE = "frndMobile";
	public static String PARAM_FEEDBACK_SUB = "feedbackSub";
	public static String PARAM_FEEDBACK_MSG = "feedbackMsg";
	public static String PARAM_DOWNLOAD_URL = "downloadUrl";
	public static String PARAM_PASSWORD = "merchantPwd";
	public static String PARAM_USER_PASSWORD = "password";
	public static String PARAM_RATING = "rating";

	public static String KEY_REATILER_THEME_COLOR = "themeColor";
	public static String KEY_RETAILER_TEXT_COLOR = "textColor";
	public static String KEY_RETAILER_FONT = "fontType";
	// ------KEY-----//
	public static String KEY_GET_RETAILER_INFO = "getRetailerInfo";
	public static String KEY_GET_PRODUCTS_INFO = "getProducts";
	public static String KEY_GET_CTEGORY_INFO = "getCategories";
	public static String KEY_GET_LOYALITY_INFO = "getLoyaltyInfo";
	public static String KEY_GET_COUNTRY_INFO = "getCountries";
	public static String KEY_PROFILE_INFO = "profile";
	public static String KEY_EMAIL = "emailid";
	public static String KEY_PN = "kPN";
	public static String KEY_COUNTRY_ID = "kCountryId";
	public static String KEY_VOUCHERS = "vouchers";
	public static String KEY_DEFAULT_DISCOUNT_TYPE = "Percentage";
	public static String KEY_FROM_PAYPAL = "KIsFromPayPal";
	public static String KEY_FROM_SEARCH = "KIsFromSearch";
	public static String KEY_KEYWORD = "kKeyword";
	public static String KEY_USER_CURRECY = "kUserCurrency";
	public static String KEY_GET_LOOK_BOOK_INFO = "getLookBookInfo";
	public static String KEY_GET_ALL_ORDERS = "getAllOrders";

	// --- URL --- //

	// public static String HOST = "http://appwizlive.com";
	// public static String HOST = "http://inceptionlive.com";
//	public static String HOST = "http://smartcommerce.asia";
	// public static String HOST = "http://www.digimall.asia";
	// public static String HOST = "http://119.81.207.36";

	// public static String HOST = "http://www.diyecommerce.net";
	// public static String HOST = "http://appwiz.cloudapp.net";
	
	public static String HOST = "http://119.81.207.44";

	public static String URL_GET_PRODUCTS = HOST + "/getProducts.php";
	public static String URL_GET_CATEGORIES = HOST + "/getCategories.php";
	public static String URL_GET_ALL_PRODUCTS = HOST + "/getAllProducts.php";
	public static String URL_MAKE_PAYMENT = HOST + "/place_order.php";
	public static String URL_GET_COUNTRIES = HOST + "/getCountries.php";
	public static String URL_GET_SPLASH = HOST + "/getSplash.php";
	public static String URL_GET_RETAILER_INFO = HOST + "/getRetailerInfo.php";
	public static String URL_SAVE_CONSUMER_PROFILE = HOST
			+ "/consumer_profile.php";
	public static String URL_SEND_DEVICE_TOKEN = HOST
			+ "/send_device_token.php";
	public static String URL_GET_LOYALTY = HOST + "/getLoyaltyInfo.php";
	public static String URL_GET_FEEDBACK_GIFT = HOST + "/getFeedbackGift.php";
	public static String URL_SEND_FEEDBACK = HOST + "/feedback_mail.php";
	public static String URL_REFER_FRIEND = HOST + "/refer_mail.php";
	public static String URL_APPLY_COUPON = HOST + "/validateToken.php";
	public static String URL_SHIPPING_CHARGES = HOST
			+ "/getShippingCharges.php";
	public static String URL_REDEEM_REWARDS = HOST + "/redeemRewards.php";
	public static String URL_VERIFY_LOYALTY_PASS = HOST + "/loyalty_pwd.php";
	public static String URL_GET_PAYPAL_TOKEN = HOST + "/paypal_token.php";
	public static String URL_GET_VERITRANS_LINK = HOST + "/veritrans_token.php";
	public static String URL_PAYPAL_SANDBOX = "https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_express-checkout&useraction=commit&token=";
	public static String URL_PAYPAL_LIVE = "https://www.paypal.com/cgi-bin/webscr?cmd=_express-checkout&useraction=commit&token=";
	public static String URL_PAY_SUCCESS = HOST + "/pay_success.php";
	public static String URL_GET_PUBLISHER_ID = HOST + "/getPublisherId.php";
	public static String URL_SEND_RATING = HOST + "/send_product_rating.php";
	public static String URL_SEND_EMAIL_ENQUIRY = HOST + "/enquiryMail.php";
	public static String URL_SEND_PRODUCT_REVIEW = HOST + "/submit_review.php";
	public static String URL_GET_PRODUCT_DETAIL = HOST + "/getProductInfo.php";
	public static String URL_GET_USER_PROFILE = HOST
			+ "/getConsumerProfile.php";
	public static String URL_LOOK_BOOK = HOST + "/getLookBook.php";
	public static String URL_LOOK_BOOK_LIKE = HOST + "/lookbook_like.php";
	public static String URL_USER_LOGIN = HOST + "/consumer_login.php";
	public static String URL_USER_FORGOT_PWD = HOST + "/api_forgot_password.php";

	public static String URL_ORDER_HISTORY = HOST + "/getAllOrders.php";
	public static String URL_ORDER_DETAIL = HOST + "/getOrderDetails.php";

	// public static String getHost(){
	// Retailer retailer = Helper.getSharedHelper().reatiler;
	// if (retailer != null &&retailer.isSSL.equals("1")) {
	// HOST = "https://appwizlive.com/";
	// }else{
	// HOST = "http://appwizlive.com/";
	// }
	// return HOST;
	// }

}
