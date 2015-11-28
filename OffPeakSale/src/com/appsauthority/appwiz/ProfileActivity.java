package com.appsauthority.appwiz;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.appauthority.appwiz.fragments.SlidingMenuActivity;
import com.appauthority.appwiz.interfaces.ForgotPWDCaller;
import com.appauthority.appwiz.interfaces.RedeemRewadsCaller;
import com.appauthority.appwiz.interfaces.ShippingChargeCaller;
import com.appauthority.appwiz.interfaces.UserLoginCaller;
import com.appauthority.appwiz.interfaces.UserProfileCaller;
import com.appsauthority.appwiz.custom.BaseActivity;
import com.appsauthority.appwiz.models.Country;
import com.appsauthority.appwiz.models.PaypalTokenRequest;
import com.appsauthority.appwiz.models.Product;
import com.appsauthority.appwiz.models.Profile;
import com.appsauthority.appwiz.models.Retailer;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.HTTPHandler;
import com.appsauthority.appwiz.utils.Helper;
import com.appsauthority.appwiz.utils.ServiceHandler;
import com.appsauthority.appwiz.utils.Utils;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.gson.Gson;
import com.offpeaksale.consumer.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends BaseActivity implements
		ShippingChargeCaller, UserProfileCaller, RedeemRewadsCaller,
		UserLoginCaller, ForgotPWDCaller {
	private Activity activity;
	private TextView textViewHeader;
	private ImageView imageViewOverflow;

	private ArrayList<Country> countryList = new ArrayList<Country>();
	private ArrayList<String> countryNameList = new ArrayList<String>();
	private ArrayAdapter<String> adapter;
	private ArrayList<String> countrySearchList = new ArrayList<String>();

	private EditText editTextFirstName, editTextMobileNumber, editTextEmail;
	private Button buttonSave, buttonCountries;
	private SimpleDateFormat sdf;

	private Context context;
	private Product product;
	private PaypalTokenRequest payPalTokenRequest;
	private Intent intent;
	private Retailer retailer;
	Boolean isProceedtoCheckout;
	private SharedPreferences spref;
	LinearLayout veTermsOfUse;
	Switch pnSwitch;

	// TextView tvRedeemlbl, tvRedeem, tvNoRewardsForCOD;

	TextView tvCashCreditValue, tvTotalRewardsValue, tvResultMsg;
	EditText etRewards;

	LinearLayout llBottonView;

	// LinearLayout llRedeem;

	Boolean isCOD;

	// Login
	LinearLayout llLoginForm, llForm, llPassword;

	TextView tvCreatePwd;
	EditText edtPwd, edtCnfPwd;

	LinearLayout llLogin, llForgotPwd;
	EditText etLoginEmailId, etLoginPWD, etEmailForgotPwd;

	TextView tvLogin, tvForgotPwd, tvRegister, tvBackToLogin, tvShowLogin;

	Button btnForgotPwd, btnLogin;
	private UiLifecycleHelper uiHelper;
	private static final String TAG = "ProfileActivity";

	private boolean isFacebookLogin = false;
	private String facebookUsername = "", facebookUserEmail = "";

	void initializeLoginRegisterView() {
		llLogin = (LinearLayout) llLoginForm.findViewById(R.id.llLogin);
		llForgotPwd = (LinearLayout) llLoginForm.findViewById(R.id.llForgotPwd);

		tvCreatePwd = (TextView) llPassword.findViewById(R.id.tvCreatePwd);
		edtPwd = (EditText) llPassword.findViewById(R.id.edtPwd);
		edtCnfPwd = (EditText) llPassword.findViewById(R.id.edtCnfPwd);

		tvLogin = (TextView) llLoginForm.findViewById(R.id.tvLogin);
		etLoginEmailId = (EditText) llLoginForm
				.findViewById(R.id.etLoginEmailId);
		etLoginPWD = (EditText) llLoginForm.findViewById(R.id.etLoginPWD);

		tvForgotPwd = (TextView) llLoginForm.findViewById(R.id.tvForgotPwd);
		tvShowLogin = (TextView) llLoginForm.findViewById(R.id.tvShowLogin);
		etEmailForgotPwd = (EditText) llLoginForm
				.findViewById(R.id.etEmailForgotPwd);

		tvRegister = (TextView) llLoginForm.findViewById(R.id.tvRegister);

		btnForgotPwd = (Button) llLoginForm.findViewById(R.id.btnForgotPwd);
		btnLogin = (Button) llLoginForm.findViewById(R.id.btnLogin);

		tvForgotPwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				llLogin.setVisibility(View.GONE);
				llForgotPwd.setVisibility(View.VISIBLE);
				etEmailForgotPwd.setVisibility(View.VISIBLE);
				btnForgotPwd.setVisibility(View.VISIBLE);
				tvBackToLogin.setVisibility(View.VISIBLE);
				tvForgotPwd.setVisibility(View.GONE);
				tvShowLogin.setVisibility(View.VISIBLE);
			}
		});
		tvShowLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showLoginForm();
				// llLogin.setVisibility(View.GONE);
				// llForgotPwd.setVisibility(View.VISIBLE);
				// etEmailForgotPwd.setVisibility(View.VISIBLE);
				// btnForgotPwd.setVisibility(View.VISIBLE);
				// tvBackToLogin.setVisibility(View.VISIBLE);
				// tvForgotPwd.setVisibility(View.VISIBLE);
				// tvShowLogin.setVisibility(View.GONE);
			}
		});

		tvRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				llLoginForm.setVisibility(View.GONE);
				llForm.setVisibility(View.VISIBLE);
				llPassword.setVisibility(View.VISIBLE);
				llBottonView.setVisibility(View.VISIBLE);
				tvBackToLogin.setVisibility(View.VISIBLE);
				buttonSave.setText(getResources().getString(R.string.register));
			}
		});

		tvBackToLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showLoginForm();
			}
		});

		edtPwd.setBackgroundDrawable(Helper.getSharedHelper()
				.getGradientDrawableEditText(retailer.getHeaderColor()));
		edtCnfPwd.setBackgroundDrawable(Helper.getSharedHelper()
				.getGradientDrawableEditText(retailer.getHeaderColor()));

		etLoginEmailId.setBackgroundDrawable(Helper.getSharedHelper()
				.getGradientDrawableEditText(retailer.getHeaderColor()));
		etLoginPWD.setBackgroundDrawable(Helper.getSharedHelper()
				.getGradientDrawableEditText(retailer.getHeaderColor()));
		etEmailForgotPwd.setBackgroundDrawable(Helper.getSharedHelper()
				.getGradientDrawableEditText(retailer.getHeaderColor()));
		buttonCountries
				.setBackgroundDrawable(getGradientDrawableEditText(retailer
						.getHeaderColor()));

		btnLogin.setTextColor(Color.parseColor("#"
				+ retailer.getRetailerTextColor()));
		btnLogin.setBackgroundDrawable(Helper.getSharedHelper()
				.getGradientDrawable(retailer.getHeaderColor()));

		btnForgotPwd.setTextColor(Color.parseColor("#"
				+ retailer.getRetailerTextColor()));
		btnForgotPwd.setBackgroundDrawable(Helper.getSharedHelper()
				.getGradientDrawable(retailer.getHeaderColor()));

		tvCreatePwd.setTypeface(Helper.getSharedHelper().normalFont);
		tvLogin.setTypeface(Helper.getSharedHelper().normalFont);
		tvRegister.setTypeface(Helper.getSharedHelper().normalFont);
		tvForgotPwd.setTypeface(Helper.getSharedHelper().normalFont);
		tvShowLogin.setTypeface(Helper.getSharedHelper().normalFont);

		edtPwd.setTypeface(Helper.getSharedHelper().normalFont);
		edtCnfPwd.setTypeface(Helper.getSharedHelper().normalFont);
		etLoginEmailId.setTypeface(Helper.getSharedHelper().normalFont);
		etLoginPWD.setTypeface(Helper.getSharedHelper().normalFont);
		etEmailForgotPwd.setTypeface(Helper.getSharedHelper().normalFont);

		btnLogin.setTypeface(Helper.getSharedHelper().boldFont);
		btnForgotPwd.setTypeface(Helper.getSharedHelper().boldFont);
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String emailid = etLoginEmailId.getText().toString();
				String password = etLoginPWD.getText().toString();
				String errorMsg = null;
				if (!Helper.getSharedHelper().isEmailValid(emailid)) {
					errorMsg = "Invalid email id";
				} else if (password == null || password.length() == 0) {
					errorMsg = "Enter password";
				}
				if (errorMsg != null) {
					Toast.makeText(ProfileActivity.this, errorMsg,
							Toast.LENGTH_LONG).show();
				} else {

					// loggedin(true);
					LoginHandler loginHandler = new LoginHandler(emailid,
							password, ProfileActivity.this);
					loginHandler.login();
				}
			}
		});
		btnForgotPwd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String emailid = etEmailForgotPwd.getText().toString();
				String errorMsg = null;
				if (!Helper.getSharedHelper().isEmailValid(emailid)) {
					errorMsg = "Invalid email id";
				}
				if (errorMsg != null) {
					Toast.makeText(ProfileActivity.this, errorMsg,
							Toast.LENGTH_LONG).show();
				} else {
					// showLoginForm();
					ForgotPasswordHandler forgotPWDHandler = new ForgotPasswordHandler(
							emailid, ProfileActivity.this);
					forgotPWDHandler.getPassword();
				}
			}
		});
		Boolean isLoggedIn = spref.getBoolean(Constants.KEY_IS_USER_LOGGED_IN,
				false);
		loggedin(isLoggedIn);

	}

	void showLoginForm() {
		llForm.setVisibility(View.GONE);
		llLoginForm.setVisibility(View.VISIBLE);
		llLogin.setVisibility(View.VISIBLE);
		llBottonView.setVisibility(View.GONE);
		llPassword.setVisibility(View.GONE);
		etEmailForgotPwd.setVisibility(View.GONE);
		btnForgotPwd.setVisibility(View.GONE);
		tvBackToLogin.setVisibility(View.GONE);
		veTermsOfUse.setVisibility(View.GONE);
		tvForgotPwd.setVisibility(View.VISIBLE);
		tvShowLogin.setVisibility(View.GONE);

	}

	void loggedin(Boolean status) {
		if (status) {
			try {
				if (!intent.getStringExtra("FROM").equalsIgnoreCase("ESHOP")) {
					finish();
					return;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			// if (!intent.getStringExtra("FROM").equalsIgnoreCase("ESHOP")) {
			// finish();
			// } else {
			llForm.setVisibility(View.VISIBLE);
			llLoginForm.setVisibility(View.GONE);
			llBottonView.setVisibility(View.VISIBLE);
			llPassword.setVisibility(View.GONE);
			tvBackToLogin.setVisibility(View.GONE);
			editTextEmail.setEnabled(false);
			buttonSave.setText(getResources().getString(R.string.save));
			veTermsOfUse.setVisibility(View.GONE);

			if (Utils.isProfileAvailable(context)) {
				setUpFields();
				// }
			}
		} else {
			// llForm.setVisibility(View.GONE);
			// llLoginForm.setVisibility(View.VISIBLE);
			// llBottonView.setVisibility(View.GONE);
			// llPassword.setVisibility(View.VISIBLE);
			// etEmailForgotPwd.setVisibility(View.GONE);
			// btnForgotPwd.setVisibility(View.GONE);
			// tvBackToLogin.setVisibility(View.GONE);
			//
			showLoginForm();
			editTextEmail.setEnabled(true);

		}
	}

	// Called when session changes
	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state,
				Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	// When session is changed, this method is called from callback method
	private void onSessionStateChange(Session session, SessionState state,
			Exception exception) {
		// When Session is successfully opened (User logged-in)
		Log.e("Session: ", "" + state.isOpened());

		if (state.isOpened()) {
			Log.i(TAG, "Logged in...");
			// make request to the /me API to get Graph user
			Request request = Request.newMeRequest(session,
					new Request.GraphUserCallback() {
						// callback after Graph API response with user
						// object
						@Override
						public void onCompleted(GraphUser user,
								Response response) {
							if (user != null) {
								isFacebookLogin = true;
								facebookUsername = user.getName();
								facebookUserEmail = user.asMap().get("email")
										.toString();
								editTextFirstName.setText(facebookUsername);
								editTextEmail.setText(facebookUserEmail);
								editTextMobileNumber.setText("");
								buttonCountries.setText("");
								
								Log.e("User Name: ", user.getName());
								Log.e("User Gender: ",
										user.getProperty("gender").toString());
								Log.e("User Email: ", user.asMap().get("email")
										.toString());
								new AsyncWorker().execute();
							}
						}
					});
			Bundle parameters = new Bundle();
			parameters.putString("fields", "id,name,email,gender, birthday");
			request.setParameters(parameters);
			request.executeAsync();
		} else if (state.isClosed()) {
			Log.i(TAG, "Logged out...");
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data);
		Log.i(TAG, "OnActivityResult...");
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("SimpleDateFormat")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.activity_profile);
		activity = this;
		context = this;

		// To maintain FB Login session
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		LoginButton facebookLoginButton = (LoginButton) findViewById(R.id.facebookauthButton);
		facebookLoginButton.setReadPermissions(Arrays
				.asList("public_profile, email, user_birthday, user_friends"));

		retailer = Helper.getSharedHelper().reatiler;

		tvBackToLogin = (TextView) findViewById(R.id.tvBackToLogin);
		llLoginForm = (LinearLayout) findViewById(R.id.llLoginForm);
		llForm = (LinearLayout) findViewById(R.id.llForm);
		llPassword = (LinearLayout) findViewById(R.id.llPassword);

		spref = PreferenceManager.getDefaultSharedPreferences(activity);
		// spref.edit().putBoolean(Constants.KEY_IS_USER_LOGGED_IN, false)
		// .commit();
		textViewHeader = (TextView) findViewById(R.id.textViewHeader);
		imageViewOverflow = (ImageView) findViewById(R.id.imageViewOverflow);
		buttonSave = (Button) findViewById(R.id.btn_save);
		editTextFirstName = (EditText) findViewById(R.id.edt_fname);
		editTextMobileNumber = (EditText) findViewById(R.id.edt_mobile_number);
		editTextEmail = (EditText) findViewById(R.id.edt_email);
		sdf = new SimpleDateFormat("dd MMM yyyy");
		TextView hyperlink = (TextView) findViewById(R.id.hyperlink);
		veTermsOfUse = (LinearLayout) findViewById(R.id.veTermsOfUse);
		buttonCountries = (Button) findViewById(R.id.sp_country);
		buttonCountries.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				countryPressed(arg0);
			}
		});

		// RelativeLayout rlPn = (RelativeLayout) findViewById(R.id.rlPn);
		// rlPn.setVisibility(View.GONE);
		pnSwitch = (Switch) findViewById(R.id.pnSwitch);

		// tvRedeemlbl = (TextView) findViewById(R.id.tvRedeemlbl);
		// tvRedeem = (TextView) llRedeem.findViewById(R.id.tv_optonsValue);
		// if (Helper.getSharedHelper().reatiler.enableRewards
		// .equalsIgnoreCase("1")) {
		//
		// } else {
		// llRedeem.setVisibility(View.GONE);
		// }
		// RelativeLayout relValue = (RelativeLayout) llRedeem
		// .findViewById(R.id.relValue);
		llBottonView = (LinearLayout) findViewById(R.id.llBottonView);

		// tvRedeem.setText(Helper.getSharedHelper().rewardPoints);

		Boolean isOff = spref.getBoolean(Constants.KEY_PN, false);
		pnSwitch.setChecked(!isOff);
		String udata = hyperlink.getText().toString();
		String str = getResources().getString(R.string.terms_of_use);
		SpannableString content = new SpannableString(str + " " + udata);
		// content.setSpan(new UnderlineSpan(), str.length() + 1,
		// content.length(), 0);
		hyperlink.setText(str + " " + udata);

		buttonSave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				savePressed(arg0);
			}
		});

		hyperlink.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Bundle bundle = new Bundle();

				bundle.putString("terms_of_use_link",
						Helper.getSharedHelper().termsConditions);

				Intent i = new Intent(context, WebActivity.class);
				i.putExtras(bundle);
				startActivity(i);
				// AlertDialog.Builder alert = new
				// AlertDialog.Builder(ProfileActivity.this);
				//
				// alert.setTitle("Terms of Use");
				// // Set an EditText view to get user input
				// alert.setMessage(Helper.getSharedHelper().termsConditions);
				// alert.setPositiveButton("Okay",
				// new DialogInterface.OnClickListener() {
				// public void onClick(DialogInterface dialog, int whichButton)
				// {
				// }
				// });
				//
				// alert.show();

			}
		});

		try {
			llBottonView.setBackgroundDrawable(Helper.getSharedHelper()
					.getGradientDrawableNoRad(retailer.getHeaderColor()));
			// llDevider.setBackgroundDrawable(Helper.getSharedHelper()
			// .getGradientDrawable(retailer.getRetailerTextColor()))

			editTextFirstName
					.setBackgroundDrawable(getGradientDrawableEditText(retailer
							.getHeaderColor()));

			editTextMobileNumber
					.setBackgroundDrawable(getGradientDrawableEditText(retailer
							.getHeaderColor()));

			editTextEmail
					.setBackgroundDrawable(getGradientDrawableEditText(retailer
							.getHeaderColor()));

			int colorOn = Color.parseColor("#" + retailer.getHeaderColor());
			int colorOff = 0xFF666666;
			int colorDisabled = 0xFF333333;
			StateListDrawable thumbStates = new StateListDrawable();
			thumbStates.addState(new int[] { android.R.attr.state_checked },
					new ColorDrawable(colorOn));
			thumbStates.addState(new int[] { -android.R.attr.state_enabled },
					new ColorDrawable(colorDisabled));
			thumbStates.addState(new int[] {}, new ColorDrawable(colorOff)); // this
																				// one
																				// has
																				// to
																				// come
																				// last
			try {
				pnSwitch.setThumbDrawable(thumbStates);
			} catch (NoSuchMethodError e) {

			}

		} catch (Exception e) {
		}

		textViewHeader.setText("Profile");
		imageViewOverflow.setVisibility(View.INVISIBLE);
		try {
			setHeaderTheme(activity, retailer.getRetailerTextColor(),
					retailer.getHeaderColor());
			buttonSave.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));
			buttonSave.setBackgroundDrawable(getGradientDrawable(retailer
					.getHeaderColor()));

		} catch (Exception e) {
		}

		veTermsOfUse.setVisibility(View.GONE);
		intent = getIntent();
		try {
			isCOD = intent.getBooleanExtra("isCod", false);
			if (intent.getStringExtra("FROM").equalsIgnoreCase("ESHOP")) {
				product = (Product) intent.getSerializableExtra("product");
				veTermsOfUse.setVisibility(View.GONE);

			} else {
				veTermsOfUse.setVisibility(View.GONE);
			}
		} catch (Exception e) {
		}

		if (Utils.isProfileAvailable(context)) {
			setUpFields();
		}
		setFont();
		// setSpinnerAdapter();

		initializeLoginRegisterView();
		new AsyncGetCountries().execute();

	}

	DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int yr, int monthOfYear,
				int dayOfMonth) {

			Calendar calendar = Calendar.getInstance();
			calendar.set(yr, monthOfYear, dayOfMonth);

			String dateString = sdf.format(calendar.getTime());

		}
	};

	void setSpinnerAdapter() {
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, R.array.gender_values) {
			public View getView(int position, View convertView,
					android.view.ViewGroup parent) {

				TextView v = (TextView) super.getView(position, convertView,
						parent);
				v.setTypeface(Helper.getSharedHelper().normalFont);
				return v;
			}

			public View getDropDownView(int position, View convertView,
					android.view.ViewGroup parent) {
				TextView v = (TextView) super.getView(position, convertView,
						parent);
				v.setTypeface(Helper.getSharedHelper().normalFont);

				return v;
			}
		};
		// adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	}

	void setFont() {
		try {
			textViewHeader.setTypeface(Helper.getSharedHelper().boldFont);

			buttonSave.setTypeface(Helper.getSharedHelper().boldFont);

			editTextFirstName.setTypeface(Helper.getSharedHelper().normalFont);
			editTextEmail.setTypeface(Helper.getSharedHelper().normalFont);
			editTextMobileNumber
					.setTypeface(Helper.getSharedHelper().normalFont);

			buttonCountries.setTypeface(Helper.getSharedHelper().normalFont);

			// tvRedeemlbl.setTypeface(Helper.getSharedHelper().normalFont);
			// tvRedeem.setTypeface(Helper.getSharedHelper().normalFont);
			// tvNoRewardsForCOD.setTypeface(Helper.getSharedHelper().normalFont);

		} catch (Exception e) {

		}

		// spinnerGender
		// .setTypeface(Helper.getSharedHelper().normalFont);

	}

	private void setUpFields() {
		// sqliteHelper.openDataBase();
		// Profile profile = sqliteHelper.getProfile();
		// sqliteHelper.close();

		Profile profile;
		String json = spref.getString(Constants.KEY_PROFILE_INFO, "");
		if (json.equalsIgnoreCase("")) {
			return;
		} else {
			Gson gson = new Gson();
			profile = gson.fromJson(json, Profile.class);
		}

		if (profile == null) {
			return;
		}
		try {
			editTextFirstName.setText(profile.getFirstName());
			editTextEmail.setText(profile.getEmail());
			editTextMobileNumber.setText(profile.getMobileNo() + "");
			buttonCountries.setText(profile.getCountry());

		} catch (Exception e) {

		}
	}

	public void savePressed(View v) {
		isFacebookLogin = false;
		isProceedtoCheckout = false;
		saveProfile();
	}

	public void buyPressed(View v) {

		isProceedtoCheckout = true;
		saveProfile();
	}

	void saveProfile() {
		if (Utils.hasNetworkConnection(context)) {
			if (checkFields()) {
				new AsyncWorker().execute();
			}
		} else {

			try {
				if (intent.getStringExtra("FROM").equalsIgnoreCase("ESHOP")) {
					Toast.makeText(context,
							"You need internet connection to buy a product.",
							Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				Toast.makeText(context,
						"You need internet connection to save your profile.",
						Toast.LENGTH_LONG).show();
			}

		}
	}

	private boolean checkFields() {
		boolean status = true;

		TextView tv = null;
		if (editTextFirstName.getText().toString().trim().length() == 0) {
			Toast.makeText(context, "Enter your firstname", Toast.LENGTH_SHORT)
					.show();
			status = false;
			tv = editTextFirstName;
		} else if (editTextMobileNumber.getText().toString().trim().length() == 0) {
			Toast.makeText(context, "Enter your mobile number",
					Toast.LENGTH_SHORT).show();
			status = false;
			tv = editTextMobileNumber;
		} else if (editTextEmail.getText().toString().trim().length() == 0
				|| !Helper.getSharedHelper().isEmailValid(
						editTextEmail.getText().toString().trim())) {
			Toast.makeText(context, "Enter your email", Toast.LENGTH_SHORT)
					.show();
			status = false;
			tv = editTextEmail;
		} else {
			Boolean isLoggedIn = spref.getBoolean(
					Constants.KEY_IS_USER_LOGGED_IN, false);
			if (!isLoggedIn) {
				if (edtPwd.getText().toString().length() == 0) {
					Toast.makeText(context, "Enter password",
							Toast.LENGTH_SHORT).show();
					status = false;
					tv = edtPwd;
				} else if (edtPwd.getText().toString()
						.compareTo(edtCnfPwd.getText().toString()) != 0) {
					Toast.makeText(context,
							"Password and confirm password doesn't match",
							Toast.LENGTH_SHORT).show();
					status = false;
					tv = edtPwd;
				}
			} else {
				status = true;
			}
		}
		if (!status && tv != null) {
			tv.requestFocus();
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(tv, InputMethodManager.SHOW_IMPLICIT);
		}

		return status;
	}

	private class AsyncWorker extends AsyncTask<Void, Void, Boolean> {

		@SuppressWarnings("deprecation")
		@Override
		protected Boolean doInBackground(Void... params) {

			if (Utils.hasNetworkConnection(getApplicationContext())) {
				JSONObject obj = new JSONObject();
				Boolean status = false;
				try {

					Calendar cal = Calendar.getInstance();
					if (isFacebookLogin == false) {
						// sqliteHelper.openDataBase();
						

						Boolean isLoggedIn = spref.getBoolean(
								Constants.KEY_IS_USER_LOGGED_IN, false);
						if (!isLoggedIn) {
							obj.put("password", edtPwd.getText().toString());
						}
					} else {
						obj.put("fname", facebookUsername);
						obj.put("email", facebookUserEmail);
					}
					String countryCode = getCountryCode(buttonCountries
							.getText().toString());

					obj.put("fname", editTextFirstName.getText().toString());

					obj.put("mobile_num", editTextMobileNumber.getText()
							.toString());
					obj.put("email", editTextEmail.getText().toString());
					obj.put("country", countryCode);
					obj.put("retailerId", Constants.RETAILER_ID);
					obj.put("lat", Constants.LAT);
					obj.put("long", Constants.LNG);
					obj.put("device_token", Constants.REG_ID);
					obj.put("device", 2);
					obj.put("latestTime", cal.getTimeInMillis());

					JSONObject jsonObject = HTTPHandler.defaultHandler()
							.doPost(Constants.URL_SAVE_CONSUMER_PROFILE, obj);

					if (jsonObject.getString("errorCode").equals("1")) {
						status = true;

					} else {
						status = false;
					}

					return status;
				} catch (Exception e) {
					e.printStackTrace();
					return false;
				}

			} else {

				return false;
			}

		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(Boolean result) {

			if (result) {

				Profile profile = new Profile();
				// profile.setAge(new Date().getYear() - dateOfBirth.getYear());
//				profile.setDeviceToken(Constants.REG_ID);
				profile.setEmail(editTextEmail.getText().toString());
				profile.setFirstName(editTextFirstName.getText().toString());

//				profile.setLat(Constants.LAT);
//				profile.setLng(Constants.LNG);
				try {
					profile.setCountry(buttonCountries.getText().toString());
					profile.setMobileNo(editTextMobileNumber
							.getText().toString());

				} catch (Exception e) {
					// TODO: handle exception
				}

//				profile.setTime(new Date().getTime());

				Gson gson = new Gson();
				String json = gson.toJson(profile);
				spref.edit().putString(Constants.KEY_PROFILE_INFO, json)
						.commit();

				spref.edit().putString(Constants.KEY_EMAIL, profile.getEmail())
						.commit();
				spref.edit()
						.putBoolean(Constants.KEY_PN, !pnSwitch.isChecked())
						.commit();
				String countryCode = getCountryCode(buttonCountries.getText()
						.toString());
				spref.edit().putString(Constants.KEY_COUNTRY_ID, countryCode)
						.commit();

				spref.edit().putBoolean(Constants.KEY_IS_USER_LOGGED_IN, true)
						.commit();
				Utils.setProfile(context, true);

				if (buttonSave
						.getText()
						.toString()
						.equalsIgnoreCase(
								getResources().getString(R.string.register))) {
					Toast.makeText(context, "Registered successfully",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(context, "Profile successfully saved",
							Toast.LENGTH_LONG).show();
				}

				loggedin(true);
				setUpFields();
				dismissLoadingDialog();

				try {
					if (!intent.getStringExtra("FROM")
							.equalsIgnoreCase("ESHOP")) {

						finish();
					} else {
						String emailId = spref.getString(Constants.KEY_EMAIL,
								"");
						if (!emailId.equalsIgnoreCase("")) {
							new UserProfileDataHandler(emailId,
									ProfileActivity.this);
						}

					}
				} catch (Exception e) {

				}
			} else {
				dismissLoadingDialog();
			}

		}

		@Override
		protected void onPreExecute() {
			showLoadingDialog();
		}

	}

	private class AsyncGetPayPalToken extends AsyncTask<Void, Void, JSONObject> {

		@Override
		protected void onPreExecute() {

			// showLoadingDialog();
			payPalTokenRequest = new PaypalTokenRequest();
			payPalTokenRequest.setRetailerId(Constants.RETAILER_ID);
			if (Helper.getSharedHelper().enableShoppingCart.equals("1")) {

			} else {
				payPalTokenRequest.setProductId(Integer.parseInt(product
						.getId()));
				payPalTokenRequest.setQuantity(Integer.parseInt(product
						.getQty()));
			}

			payPalTokenRequest.seteMail(editTextEmail.getText().toString());

		}

		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
		protected JSONObject doInBackground(Void... params) {

			JSONObject result = null;
			try {

				if (Helper.getSharedHelper().enableShoppingCart.equals("1")) {

					// String jsonStr = createJsonString();

					try {

						List<Product> products = Helper.getSharedHelper().shoppintCartList;

						JSONArray jObj = new JSONArray();
						for (int i = 0; i < products.size(); i++) {
							Product p = products.get(i);
							JSONObject json = new JSONObject();
							json.put(Constants.PARAM_PRODUCTID_FOR_TOKEN,
									p.getId());
							if (Helper.getSharedHelper().disablePayment
									.equals("0")) {
								json.put(Constants.PARAM_QUANTITY, p.getQty());
							}
							String prodOptions = null;
							if (p.getSelectedOption1() != null) {
								prodOptions = p.getSelectedOption1();
								if (p.getSelectedOption2() != null) {
									prodOptions = prodOptions + ","
											+ p.getSelectedOption2();
								}
							}
							if (prodOptions != null) {
								json.put("prodOptions", prodOptions);
							}
							if (p.getIsOptedGiftWrap()) {
								JSONObject giftWrapJson = new JSONObject();
								giftWrapJson.put("gift_to", p.getGiftTo());
								giftWrapJson.put("msg", p.getGiftMsg());
								giftWrapJson
										.put("price",
												Helper.getSharedHelper().reatiler.gift_price);
								json.put("giftwrap", giftWrapJson);
							}

							jObj.put(json);
						}

						JSONObject jsonObj = new JSONObject();
						jsonObj.put(Constants.PARAM_RETAILER_ID,
								payPalTokenRequest.getRetailerId());
						jsonObj.put(Constants.PARAM_EMAIL,
								payPalTokenRequest.geteMail());
						jsonObj.put("products", jObj);

						if (Helper.getSharedHelper().enableCreditCode
								.equals("1")
								&& !Helper.getSharedHelper().discountPercent
										.equalsIgnoreCase("0")) {
							jsonObj.put("discount",
									Helper.getSharedHelper().discountPercent);
							jsonObj.put("discountType",
									Helper.getSharedHelper().discountType);
							jsonObj.put("discountCode",
									Helper.getSharedHelper().creditCode);
						}
						if (Helper.getSharedHelper().shippingCharge > 0
								&& Helper.getSharedHelper().deliveryOptionSelectedIndex == 0) {
							float total = Float.parseFloat(Helper
									.getSharedHelper().getCartTotalAmount());
							if (Helper.getSharedHelper().freeAmount == 0
									|| total < Helper.getSharedHelper().freeAmount) {
								jsonObj.put(
										"shippingAmt",
										Float.toString(Helper.getSharedHelper().shippingCharge));
							} else {

							}
						}
						if (Helper.getSharedHelper().enableDelivery
								.equalsIgnoreCase("1")
								&& Helper.getSharedHelper().deliveryOptionSelectedIndex == 0) {
							jsonObj.put(
									"deliveryDate",
									Helper.getSharedHelper().deleveryScheduleSelected);
						} else if (Helper.getSharedHelper().reatiler.enableCollection
								.equalsIgnoreCase("1")
								&& Helper.getSharedHelper().deliveryOptionSelectedIndex == 0) {
							jsonObj.put(
									"collectDate",
									Helper.getSharedHelper().deleveryScheduleSelected
											+ " at "
											+ Helper.getSharedHelper().selectedCollectionAddress);
						}
						System.out.println("input = " + jsonObj.toString());
						jsonObj.put("redeemPoints",
								Helper.getSharedHelper().redeemPoints);
						if (!isCOD) {// Helper.getSharedHelper().disablePayment.equals("0")
							// jsonObj.put("redeemPoints",
							// Helper.getSharedHelper().redeemPoints);
							String checkOut_url;
							if (Helper.getSharedHelper().reatiler.enablePay
									.equalsIgnoreCase("1")) {
								checkOut_url = Constants.URL_GET_PAYPAL_TOKEN;
							} else {
								checkOut_url = Constants.URL_GET_VERITRANS_LINK;
							}
							result = HTTPHandler.defaultHandler().doPost(
									checkOut_url, jsonObj);
						} else {
							result = HTTPHandler.defaultHandler().doPost(
									Constants.URL_SEND_EMAIL_ENQUIRY, jsonObj);
						}

					} catch (Exception e) {
						// TODO: handle exception
					}

				} else {
					JSONObject json = new JSONObject();
					json.put(Constants.PARAM_RETAILER_ID,
							payPalTokenRequest.getRetailerId());

					json.put(Constants.PARAM_EMAIL,
							payPalTokenRequest.geteMail());
					json.put(Constants.PARAM_PRODUCTID_FOR_TOKEN,
							payPalTokenRequest.getProductId());
					json.put("redeemPoints",
							Helper.getSharedHelper().redeemPoints);
					if (Helper.getSharedHelper().enableCreditCode.equals("1")
							&& !Helper.getSharedHelper().discountPercent
									.equalsIgnoreCase("0")) {
						json.put("discount",
								Helper.getSharedHelper().discountPercent);
						json.put("discountType",
								Helper.getSharedHelper().discountType);
						json.put("discountCode",
								Helper.getSharedHelper().creditCode);
					}
					if (Helper.getSharedHelper().shippingCharge > 0) {
						float total = Float.parseFloat(Helper.getSharedHelper()
								.getCartTotalAmount());
						if (Helper.getSharedHelper().freeAmount == 0
								|| total < Helper.getSharedHelper().freeAmount) {
							json.put("shippingAmt", Float.toString(Helper
									.getSharedHelper().shippingCharge));
						} else {

						}
					}
					// String prodOptions = null;
					// if (p.getSelectedOption1() != null) {
					// prodOptions = p.getSelectedOption1();
					// if (p.getSelectedOption2() != null) {
					// prodOptions = prodOptions+","+p.getSelectedOption2();
					// }
					// }
					// if (prodOptions != null) {
					// json.put("prodOptions", prodOptions);
					// }
					if (Helper.getSharedHelper().enableDelivery
							.equalsIgnoreCase("1")) {
						json.put(
								"deliveryDate",
								Helper.getSharedHelper().deleveryScheduleSelected);
					}
					System.out.println("input = " + json.toString());
					if (Helper.getSharedHelper().disablePayment.equals("0")) {
						json.put(Constants.PARAM_QUANTITY,
								payPalTokenRequest.getQuantity());
					}
					if (!isCOD) {// Helper.getSharedHelper().disablePayment.equals("0")
						String checkOut_url;
						if (Helper.getSharedHelper().reatiler.enablePay
								.equalsIgnoreCase("1")) {
							checkOut_url = Constants.URL_GET_PAYPAL_TOKEN;
						} else {
							checkOut_url = Constants.URL_GET_VERITRANS_LINK;
						}
						result = HTTPHandler.defaultHandler().doPost(
								checkOut_url, json);
					} else {
						result = HTTPHandler.defaultHandler().doPost(
								Constants.URL_SEND_EMAIL_ENQUIRY, json);
					}
				}

				return result;

			} catch (JSONException e) {
				e.printStackTrace();
				return null;

			}

		}

		@Override
		protected void onPostExecute(JSONObject result) {

			if (result != null && result.has("errorCode")) {

				try {
					if (result.getString("errorCode").equals("1")) {

						if (isCOD) {
							String amount = "";
							if (Helper.getSharedHelper().enableShoppingCart
									.equals("1")) {
								Float shipping = Helper.getSharedHelper().shippingCharge;
								amount = Helper.getSharedHelper()
										.getCartTotalAmount();
								float total = Float.parseFloat(amount);
								if (total >= Helper.getSharedHelper().freeAmount
										|| Helper.getSharedHelper().deliveryOptionSelectedIndex == 1) {

								} else {
									total = total + shipping;
								}
								total = total
										- Float.parseFloat(Helper
												.getSharedHelper().redeemPoints);
								amount = Float.toString(total);
								Helper.getSharedHelper().shoppintCartList
										.clear();
							} else {
								int qty = Integer.parseInt(product.getQty());
								float unitPrice = Float.parseFloat(product
										.getNewPrice());
								float total = qty * unitPrice;
								float discount = total
										* Float.parseFloat(Helper
												.getSharedHelper().discountPercent)
										/ 100;
								total = total - discount;
								Float shipping = Helper.getSharedHelper().shippingCharge;
								if (total >= Helper.getSharedHelper().freeAmount) {

								} else {
									total = total + shipping;
								}
								total = total
										- Float.parseFloat(Helper
												.getSharedHelper().redeemPoints);
								amount = Float.toString(total);
							}

							String paymetTitle = "Order success\nEmail confirmation sent\n\n";
							String grandTotal = "Grand Total: "
									+ Helper.getSharedHelper().currency_symbol
									+ " " + amount + "";
							// String orderNo = "Order No: " +
							// result.getString("token");
							String msg = "Dear Customer,\nYour purchase is sucessful.\nYou will receive E-Mail confirmation shortly.";

							String toasttext = paymetTitle + grandTotal; /*
																		 * +
																		 * orderNo
																		 */
							// + msg;
							Toast.makeText(context, toasttext,
									Toast.LENGTH_LONG).show();

							Helper.getSharedHelper().shoppintCartList.clear();
							// Intent intent = new Intent(context,
							// EShopFragmentActivity.class);
							// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							// startActivity(intent);
							// finish();
							Intent intent = new Intent(context,
									SlidingMenuActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							intent.putExtra(Constants.KEY_FROM_PAYPAL, true);
							startActivity(intent);
							Helper.getSharedHelper().discountPercent = "0";
							Helper.getSharedHelper().redeemPoints = "0";
							finish();
						} else {
							Bundle bundle = new Bundle();

							bundle.putSerializable("product", product);
							if (Helper.getSharedHelper().reatiler.enablePay
									.equalsIgnoreCase("1")) {
								bundle.putString("token",
										result.getString("token"));
							} else {
								bundle.putString("redirectUrl",
										result.getString("redirectUrl"));
							}

							bundle.putString("sucessUrl",
									result.getString("successUrl"));
							bundle.putString("cancelUrl",
									result.getString("cancelUrl"));
							if (result.has("paypalMode")) {
								bundle.putString("paypalMode",
										result.getString("paypalMode"));
							}

							Intent i = new Intent(context, PaypalActivity.class);
							i.putExtras(bundle);
							startActivity(i);
						}

					}
					// try {
					// Toast.makeText(context,
					// result.getString("errorMessage"),
					// Toast.LENGTH_SHORT).show();
					// } catch (JSONException e) {
					// // TODO Auto-generated catch block
					// e.printStackTrace();
					// }
				} catch (JSONException e) {

					e.printStackTrace();

					try {
						Toast.makeText(context,
								result.getString("errorMessage"),
								Toast.LENGTH_SHORT).show();

					} catch (JSONException e1) {

						e1.printStackTrace();
					}
				}
			} else if (result != null) {
				try {
					Toast.makeText(context, result.getString("errorMessage"),
							Toast.LENGTH_SHORT).show();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Toast.makeText(context, "Some error occured.",
						Toast.LENGTH_SHORT).show();
			}

			dismissLoadingDialog();

		}

	}

	// @SuppressWarnings("unchecked")
	// public org.json.simple.JSONArray createProductJSon() {
	// List<JSONObject> array = new ArrayList<JSONObject>();
	// // jsonArray.put("njn");
	// org.json.simple.JSONArray jsonArray = new org.json.simple.JSONArray();
	//
	// try {
	// List<Product> products = Helper.getSharedHelper().shoppintCartList;
	//
	// LinkedList l1 = new LinkedList();
	// for (int i = 0; i < products.size(); i++) {
	// Product p = products.get(i);
	// org.json.simple.JSONObject json = new org.json.simple.JSONObject();
	// json.put(Constants.PARAM_PRODUCTID_FOR_TOKEN, p.getId());
	// json.put(Constants.PARAM_QUANTITY, p.getQty());
	// LinkedHashMap m1 = new LinkedHashMap();
	// m1.put(Constants.PARAM_PRODUCTID_FOR_TOKEN, p.getId());
	// m1.put(Constants.PARAM_QUANTITY, p.getQty());
	// l1.add(m1);
	// }
	//
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	//
	// return jsonArray;
	// }

	public String createJsonString() {
		String jsonstr = "[";
		try {
			List<Product> products = Helper.getSharedHelper().shoppintCartList;

			for (int i = 0; i < products.size(); i++) {
				Product p = products.get(i);
				JSONObject json = new JSONObject();
				json.put(Constants.PARAM_PRODUCTID_FOR_TOKEN, p.getId());
				json.put(Constants.PARAM_QUANTITY, p.getQty());
				jsonstr = jsonstr + json.toString();
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		jsonstr = jsonstr + "]";
		return jsonstr;
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		uiHelper.onResume();
		String emailId = spref.getString(Constants.KEY_EMAIL, "");
		if (!emailId.equalsIgnoreCase("")) {
			new UserProfileDataHandler(emailId, this);
		}
	}

	@Override
	public void shippingChargeUpdated() {
		// TODO Auto-generated method stub

	}

	void showRewardPopUp() {
		try {
			DisplayMetrics metrics = getResources().getDisplayMetrics();
			int width = metrics.widthPixels;

			// final AlertDialog dialog = new AlertDialog.Builder(
			// this).create();
			// dialog.setCancelable(false);
			// dialog.setContentView(R.layout.dialog_enter_code);

			final Dialog dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setCancelable(false);
			dialog.setContentView(R.layout.dialog_rewards);

			dialog.getWindow().setLayout((6 * width) / 7,
					LayoutParams.WRAP_CONTENT);

			RelativeLayout btnClose = (RelativeLayout) dialog
					.findViewById(R.id.btnClose);
			TextView tvCashCreditLbl = (TextView) dialog
					.findViewById(R.id.tvCashCreditLbl);
			tvCashCreditValue = (TextView) dialog
					.findViewById(R.id.tvCashCreditValue);
			TextView tvTotalRewardsLbl = (TextView) dialog
					.findViewById(R.id.tvTotalRewardsLbl);
			tvTotalRewardsValue = (TextView) dialog
					.findViewById(R.id.tvTotalRewardsValue);

			tvCashCreditValue.setText(Helper.getSharedHelper().rewardPoints
					+ " points");
			tvTotalRewardsValue.setText(Helper.getSharedHelper().currency_code
					+ " " + Helper.getSharedHelper().rewardPoints);
			etRewards = (EditText) dialog.findViewById(R.id.etRewards);
			Button btnRedeemRewards = (Button) dialog
					.findViewById(R.id.btnRedeemRewards);
			tvResultMsg = (TextView) dialog.findViewById(R.id.tvResultMsg);

			etRewards
					.setBackgroundDrawable(getGradientDrawableEditText(retailer
							.getHeaderColor()));
			btnRedeemRewards.setBackgroundDrawable(getGradientDrawable(retailer
					.getHeaderColor()));

			etRewards.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					// new AsyncApplyCoupon().execute();
				}
			});
			btnClose.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					dialog.dismiss();
				}
			});

			btnRedeemRewards.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					String pointsToRedeem = etRewards.getText().toString();
					float cartTotal = Float.parseFloat(Helper.getSharedHelper()
							.getCartTotalAmount());
					if (Float.parseFloat(pointsToRedeem) > Float
							.parseFloat(Helper.getSharedHelper().rewardPoints)) {
						Toast.makeText(ProfileActivity.this,
								"Insufficient Credit Points", Toast.LENGTH_LONG)
								.show();
						return;
					} else if (Integer.parseInt(pointsToRedeem) > cartTotal) {
						Toast.makeText(
								ProfileActivity.this,
								"Enter credit point is hogher than cart total amount",
								Toast.LENGTH_LONG).show();
						return;
					}
					RedeemRewardsHandler rrh = new RedeemRewardsHandler(
							pointsToRedeem, spref.getString(
									Constants.KEY_EMAIL, ""),
							ProfileActivity.this);
					rrh.redeemRewards();
				}
			});

			btnRedeemRewards.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));

			btnRedeemRewards.setTypeface(Helper.getSharedHelper().boldFont);
			tvCashCreditLbl.setTypeface(Helper.getSharedHelper().boldFont);
			tvTotalRewardsLbl.setTypeface(Helper.getSharedHelper().boldFont);

			tvCashCreditValue.setTypeface(Helper.getSharedHelper().normalFont);
			tvTotalRewardsValue
					.setTypeface(Helper.getSharedHelper().normalFont);
			etRewards.setTypeface(Helper.getSharedHelper().normalFont);
			tvResultMsg.setTypeface(Helper.getSharedHelper().normalFont);
			dialog.show();
		} catch (Exception e) {
		}
	}

	@Override
	public void userProfileFetched(String rewardsPoints) {
		// TODO Auto-generated method stub
		// tvRedeem.setText(Helper.getSharedHelper().rewardPoints);
	}

	@Override
	public void rewadsPointsRedeemed(Boolean isSucess, String msg) {
		// TODO Auto-generated method stub
		if (isSucess) {
			tvResultMsg.setTextColor(Color.BLACK);
		} else {
			tvResultMsg.setTextColor(Color.RED);
		}
		tvResultMsg.setText(msg);
		tvResultMsg.setVisibility(View.VISIBLE);
		// tvRewardsValue.setText(Helper.getSharedHelper().redeemPoints);
	}

	@Override
	public void loggedIn(Boolean isSucess, String msg, Profile userProfile) {
		// TODO Auto-generated method stub
		if (isSucess) {

			Gson gson = new Gson();
			String json = gson.toJson(userProfile);
			spref.edit().putString(Constants.KEY_PROFILE_INFO, json).commit();
			spref.edit().putBoolean(Constants.KEY_IS_USER_LOGGED_IN, true)
					.commit();
			spref.edit().putString(Constants.KEY_EMAIL, userProfile.getEmail())
					.commit();

			Utils.setProfile(context, true);
			loggedin(true);
		}
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	@Override
	public void passwordSent(Boolean isSucess, String msg) {
		// TODO Auto-generated method stub
		if (isSucess) {
			showLoginForm();
		}
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	private void populateCountries() {

		for (int i = 0; i < countryList.size(); i++) {
			countryNameList.add(countryList.get(i).getCountryName());
		}

	}

	private String getCountryCode(String name) {
		String countryCode = "";
		for (Country country : countryList) {
			if (country.getCountryName().equalsIgnoreCase(name)) {
				countryCode = country.getCountryCode();
				break;
			}
		}
		return countryCode;
	}

	private String getCountryName(String code) {
		String countryName = "";
		for (Country country : countryList) {
			if (country.getCountryCode().equalsIgnoreCase(code)) {
				countryName = country.getCountryName();
				break;
			}
		}
		return countryName;
	}

	Boolean parseConries(String json) {
		Boolean status = false;
		JSONArray jsonArray;
		try {
			jsonArray = new JSONArray(json);
			countryList.clear();
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject country = (JSONObject) jsonArray.get(i);
				Country cat = new Country();
				cat.setCountryCode(country.getString("countryCode"));
				cat.setCountryName(country.getString("countryName"));
				countryList.add(cat);
			}
			status = true;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			status = false;
		}

		// sqliteHelper.openDataBase();
		// for (int i = 0; i < countryList.size(); i++) {
		// sqliteHelper.insertOrReplaceCountry(countryList
		// .get(i));
		// }
		// sqliteHelper.close();

		return status;
	}

	private class AsyncGetCountries extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected void onPreExecute() {
			String json = spref.getString(Constants.KEY_GET_COUNTRY_INFO, "");
			if (!json.equalsIgnoreCase("")) {
				Boolean stats = parseConries(json);
				if (stats) {
					populateCountries();
				}
			} else {
				showLoadingDialog();
			}

		}

		@Override
		protected Boolean doInBackground(Void... arg0) {

			if (Utils.hasNetworkConnection(context)) {

				Boolean status = false;
				try {
					ServiceHandler jsonParser = new ServiceHandler();
					String json = jsonParser.makeServiceCall(
							Constants.URL_GET_COUNTRIES, ServiceHandler.GET);
					if (json != null) {

						spref.edit()
								.putString(Constants.KEY_GET_COUNTRY_INFO, json)
								.commit();
						status = parseConries(json);
					} else {
						status = false;
					}
				} catch (Exception e) {
					return false;
				}
				return status;
			} else {

				// try {
				// sqliteHelper.openDataBase();
				// countryList = sqliteHelper.getCountries();
				// sqliteHelper.close();
				// } catch (Exception e) {
				// }

				return false;
			}

		}

		@Override
		protected void onPostExecute(Boolean result) {

			populateCountries();

			dismissLoadingDialog();

		}
	}

	public void countryPressed(View v) {
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.dialog_countries);
		dialog.setTitle("Countries");

		final EditText editTextSearch = (EditText) dialog
				.findViewById(R.id.editTextSearch);

		final ListView listViewCountries = (ListView) dialog
				.findViewById(R.id.listViewCountries);

		adapter = new ArrayAdapter<String>(getApplicationContext(),
				R.layout.list_row_text, android.R.id.text1, countryNameList);

		listViewCountries.setAdapter(adapter);

		listViewCountries.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View view,
					int position, long arg) {

				if (countrySearchList.size() > 0) {
					buttonCountries.setText(countrySearchList.get(position));
				} else {
					buttonCountries.setText(countryNameList.get(position));
				}
				String countryId = getCountryCode(buttonCountries.getText()
						.toString());
				// if (countryId.length() > 0) {
				// ShippingChargeDataHandler scdh = new
				// ShippingChargeDataHandler(
				// countryId, null);
				// scdh.getShippingCharge();
				// }
				dialog.dismiss();
			}

		});

		editTextSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				countrySearchList.clear();
				for (int i = 0; i < countryNameList.size(); i++) {
					if (countryNameList.get(i).toLowerCase(Locale.ENGLISH)
							.contains(s)) {
						countrySearchList.add(countryNameList.get(i));

					}
				}

				runOnUiThread(new Runnable() {

					@Override
					public void run() {

						ArrayAdapter<String> adapterSearch = new ArrayAdapter<String>(
								getApplicationContext(),
								R.layout.list_row_text, android.R.id.text1,
								countrySearchList);
						adapter.notifyDataSetChanged();
						listViewCountries.setAdapter(adapterSearch);

					}
				});

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		dialog.show();
	}
}
