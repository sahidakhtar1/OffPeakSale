package com.appauthority.appwiz.fragments;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
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
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.appauthority.appwiz.interfaces.ForgotPWDCaller;
import com.appauthority.appwiz.interfaces.UserLoginCaller;
import com.appauthority.appwiz.interfaces.UserProfileCaller;
import com.appsauthority.appwiz.ForgotPasswordHandler;
import com.appsauthority.appwiz.LoginHandler;
import com.appsauthority.appwiz.ProfileActivity;
import com.appsauthority.appwiz.ShippingChargeDataHandler;
import com.appsauthority.appwiz.UserProfileDataHandler;
import com.appsauthority.appwiz.WebActivity;
import com.appsauthority.appwiz.models.Country;
import com.appsauthority.appwiz.models.Product;
import com.appsauthority.appwiz.models.Profile;
import com.appsauthority.appwiz.models.Retailer;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.HTTPHandler;
import com.appsauthority.appwiz.utils.Helper;
import com.appsauthority.appwiz.utils.ServiceHandler;
import com.appsauthority.appwiz.utils.Utils;
import com.google.gson.Gson;
import com.offpeaksale.restaurants.R;

public class ProfileFragment extends Fragment implements UserProfileCaller,
		UserLoginCaller, ForgotPWDCaller {
	private Activity activity;
	private TextView textViewHeader;
	private ImageView imageViewOverflow;

	private ArrayList<Country> countryList = new ArrayList<Country>();
	private ArrayList<String> countryNameList = new ArrayList<String>();

	private Spinner spinnerGender;
	private EditText editTextFirstName, editTextLastName, editTextMobileNumber,
			editTextEmail, editTextAddress, editTextCity, editTextState,
			editTextZip;
	private Button buttonSave, btn_buy, buttonCountries;
	private SimpleDateFormat sdf;

	private Context context;
	// private Intent intent;
	private Retailer retailer;
	private ArrayAdapter<String> adapter;
	private ArrayList<String> countrySearchList = new ArrayList<String>();
	Boolean isProceedtoCheckout;
	private SharedPreferences spref;
	LinearLayout veTermsOfUse;
	Switch pnSwitch;

	private ProgressDialog progressDialog;

	TextView tvRedeemlbl, tvRedeem, tvPNlbl;
	LinearLayout llBottonView, llDevider;

	TextView tvDOB;
	LinearLayout llRedeem;

	// Login
	LinearLayout llLoginForm, llForm, llPassword;

	TextView tvCreatePwd;
	EditText edtPwd, edtCnfPwd;

	LinearLayout llLogin, llForgotPwd;
	EditText etLoginEmailId, etLoginPWD, etEmailForgotPwd;

	TextView tvLogin, tvForgotPwd, tvRegister, tvBackToLogin;

	Button btnForgotPwd, btnLogin;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.activity_profile, container,
				false);
		initilizeView(rootView);
		return rootView;
	}

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
				buttonSave.setText(getActivity().getResources().getString(
						R.string.register));
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
					Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG)
							.show();
				} else {
					//
					LoginHandler loginHandler = new LoginHandler(emailid,
							password, ProfileFragment.this);
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
					Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG)
							.show();
				} else {
					// showLoginForm();
					ForgotPasswordHandler forgotPWDHandler = new ForgotPasswordHandler(emailid, ProfileFragment.this);
					forgotPWDHandler.getPassword();
				}
			}
		});
		Boolean isLoggedIn = spref.getBoolean(
				Constants.KEY_IS_USER_LOGGED_IN, false);
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

	}

	void loggedin(Boolean status) {
		if (status) {
			llForm.setVisibility(View.VISIBLE);
			llLoginForm.setVisibility(View.GONE);
			llBottonView.setVisibility(View.VISIBLE);
			llPassword.setVisibility(View.GONE);
			tvBackToLogin.setVisibility(View.GONE);
			llRedeem.setVisibility(View.VISIBLE);
			editTextEmail.setEnabled(false);
			buttonSave.setText(getActivity().getResources().getString(
					R.string.save));
			String emailId = spref.getString(Constants.KEY_EMAIL, "");
			new UserProfileDataHandler(emailId, ProfileFragment.this);
			if (Utils.isProfileAvailable(context)) {
				setUpFields();
			}
		} else {
			showLoginForm();
			editTextEmail.setEnabled(true);
			llRedeem.setVisibility(View.GONE);

		}
	}

	void initilizeView(View view) {
		activity = this.getActivity();
		context = this.getActivity();

		// sqliteHelper.openDataBase();
		retailer = Helper.getSharedHelper().reatiler;
		// sqliteHelper.close();

		RelativeLayout rootHeader = (RelativeLayout) view
				.findViewById(R.id.header);
		rootHeader.setVisibility(View.GONE);
		spref = PreferenceManager.getDefaultSharedPreferences(activity);
		textViewHeader = (TextView) view.findViewById(R.id.textViewHeader);
		imageViewOverflow = (ImageView) view
				.findViewById(R.id.imageViewOverflow);
		tvBackToLogin = (TextView) view.findViewById(R.id.tvBackToLogin);
		llLoginForm = (LinearLayout) view.findViewById(R.id.llLoginForm);
		llForm = (LinearLayout) view.findViewById(R.id.llForm);
		llPassword = (LinearLayout) view.findViewById(R.id.llPassword);

		spinnerGender = (Spinner) view.findViewById(R.id.sp_gender);
		buttonSave = (Button) view.findViewById(R.id.btn_save);
		btn_buy = (Button) view.findViewById(R.id.btn_buy);
		btn_buy.setVisibility(View.GONE);
		editTextFirstName = (EditText) view.findViewById(R.id.edt_fname);
		editTextLastName = (EditText) view.findViewById(R.id.edt_lname);
		editTextMobileNumber = (EditText) view
				.findViewById(R.id.edt_mobile_number);
		editTextEmail = (EditText) view.findViewById(R.id.edt_email);
		editTextAddress = (EditText) view.findViewById(R.id.edt_address);
		editTextCity = (EditText) view.findViewById(R.id.edt_city);
		editTextState = (EditText) view.findViewById(R.id.edt_state);
		editTextZip = (EditText) view.findViewById(R.id.edt_zip);
		sdf = new SimpleDateFormat("dd MMM yyyy");
		TextView hyperlink = (TextView) view.findViewById(R.id.hyperlink);
		tvPNlbl = (TextView) view.findViewById(R.id.tvPNlbl);
		veTermsOfUse = (LinearLayout) view.findViewById(R.id.veTermsOfUse);
		tvRedeemlbl = (TextView) view.findViewById(R.id.tvRedeemlbl);
		llRedeem = (LinearLayout) view.findViewById(R.id.llRedeem);
		tvRedeem = (TextView) llRedeem.findViewById(R.id.tv_optonsValue);
		if (Helper.getSharedHelper().reatiler.enableRewards
				.equalsIgnoreCase("1")) {

		} else {
			llRedeem.setVisibility(View.GONE);
		}

		buttonCountries = (Button) view.findViewById(R.id.sp_country);
		buttonCountries.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				countryPressed(arg0);
			}
		});
		tvDOB = (TextView) view.findViewById(R.id.tv_date);
		tvDOB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Calendar c = Calendar.getInstance();
				if (tvDOB.getText().toString().length() > 0) {
					try {
						c.setTime(sdf.parse(tvDOB.getText().toString()));
					} catch (ParseException e) {

						e.printStackTrace();
					}
				}

				DatePickerDialog dialog = new DatePickerDialog(context,
						dateListener, c.get(Calendar.YEAR), c
								.get(Calendar.MONTH), c
								.get(Calendar.DAY_OF_MONTH));
				dialog.show();
			}
		});

		ImageView list_right1 = (ImageView) llRedeem
				.findViewById(R.id.list_right1);
		View list_underline = (View) llRedeem.findViewById(R.id.list_underline);
		list_right1.setVisibility(View.INVISIBLE);
		list_underline.setVisibility(View.GONE);

		llBottonView = (LinearLayout) view.findViewById(R.id.llBottonView);
		tvRedeem.setText(Helper.getSharedHelper().rewardPoints);
		// String redeemText =
		// getResources().getString(R.string.available_points);
		// String rewardPointTxt = redeemText + "   "
		// + Helper.getSharedHelper().rewardPoints;
		// final SpannableString spanableText = new
		// SpannableString(rewardPointTxt);
		// spanableText.setSpan(new StyleSpan(Typeface.BOLD),
		// redeemText.length(),
		// rewardPointTxt.length(), 0);
		// // spanableText.setSpan(new RelativeSizeSpan(1.2f),
		// redeemText.length(),
		// // rewardPointTxt.length(), 0);
		//
		// tvRedeemlbl.setText(spanableText);
		pnSwitch = (Switch) view.findViewById(R.id.pnSwitch);
		Boolean isOff = spref.getBoolean(Constants.KEY_PN, false);
		pnSwitch.setChecked(!isOff);
		String udata = hyperlink.getText().toString();
		SpannableString content = new SpannableString(udata);
		content.setSpan(new UnderlineSpan(), 0, udata.length(), 0);
		hyperlink.setText(content);

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
			editTextFirstName.setBackgroundDrawable(Helper.getSharedHelper()
					.getGradientDrawableEditText(retailer.getHeaderColor()));

			editTextLastName.setBackgroundDrawable(Helper.getSharedHelper()
					.getGradientDrawableEditText(retailer.getHeaderColor()));

			editTextMobileNumber.setBackgroundDrawable(Helper.getSharedHelper()
					.getGradientDrawableEditText(retailer.getHeaderColor()));

			editTextEmail.setBackgroundDrawable(Helper.getSharedHelper()
					.getGradientDrawableEditText(retailer.getHeaderColor()));

			editTextAddress.setBackgroundDrawable(Helper.getSharedHelper()
					.getGradientDrawableEditText(retailer.getHeaderColor()));

			editTextCity.setBackgroundDrawable(Helper.getSharedHelper()
					.getGradientDrawableEditText(retailer.getHeaderColor()));

			editTextState.setBackgroundDrawable(Helper.getSharedHelper()
					.getGradientDrawableEditText(retailer.getHeaderColor()));

			editTextZip.setBackgroundDrawable(Helper.getSharedHelper()
					.getGradientDrawableEditText(retailer.getHeaderColor()));

			tvDOB.setBackgroundDrawable(Helper.getSharedHelper()
					.getGradientDrawableEditText(retailer.getHeaderColor()));

			buttonCountries.setBackgroundDrawable(Helper.getSharedHelper()
					.getGradientDrawableEditText(retailer.getHeaderColor()));

			spinnerGender.setBackgroundDrawable(Helper.getSharedHelper()
					.getGradientDrawableEditText(retailer.getHeaderColor()));

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
			buttonSave.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));
			buttonSave.setBackgroundDrawable(Helper.getSharedHelper()
					.getGradientDrawable(retailer.getHeaderColor()));

			// btn_buy.setTextColor(Color.parseColor("#"
			// + retailer.getRetailerTextColor()));
			// btn_buy.setBackgroundDrawable(Helper.getSharedHelper()
			// .getGradientDrawable(retailer.getHeaderColor()));
		} catch (Exception e) {
		}

		btn_buy.setVisibility(View.GONE);
		veTermsOfUse.setVisibility(View.GONE);
		// intent = getIntent();

		setFont();
		// setSpinnerAdapter();

		initializeLoginRegisterView();
		new AsyncGetCountries().execute();
	}

	public void countryPressed(View v) {
		final Dialog dialog = new Dialog(context);
		dialog.setContentView(R.layout.dialog_countries);
		dialog.setTitle("Countries");

		final EditText editTextSearch = (EditText) dialog
				.findViewById(R.id.editTextSearch);

		final ListView listViewCountries = (ListView) dialog
				.findViewById(R.id.listViewCountries);

		adapter = new ArrayAdapter<String>(activity.getApplicationContext(),
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
				if (countryId.length() > 0) {
					ShippingChargeDataHandler scdh = new ShippingChargeDataHandler(
							countryId, null);
					scdh.getShippingCharge();
				}
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

				activity.runOnUiThread(new Runnable() {

					@Override
					public void run() {

						ArrayAdapter<String> adapterSearch = new ArrayAdapter<String>(
								activity.getApplicationContext(),
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

	DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {

		@Override
		public void onDateSet(DatePicker view, int yr, int monthOfYear,
				int dayOfMonth) {

			Calendar calendar = Calendar.getInstance();
			calendar.set(yr, monthOfYear, dayOfMonth);

			String dateString = sdf.format(calendar.getTime());

			tvDOB.setText(dateString);
		}

	};

	void setSpinnerAdapter() {
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(activity,
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
		spinnerGender.setAdapter(adapter1);
	}

	void setFont() {
		try {
			textViewHeader.setTypeface(Helper.getSharedHelper().boldFont);

			buttonSave.setTypeface(Helper.getSharedHelper().boldFont);
			// btn_buy.setTypeface(Helper.getSharedHelper().boldFont);

			editTextFirstName.setTypeface(Helper.getSharedHelper().normalFont);
			editTextLastName.setTypeface(Helper.getSharedHelper().normalFont);
			editTextAddress.setTypeface(Helper.getSharedHelper().normalFont);
			editTextCity.setTypeface(Helper.getSharedHelper().normalFont);
			editTextEmail.setTypeface(Helper.getSharedHelper().normalFont);
			editTextMobileNumber
					.setTypeface(Helper.getSharedHelper().normalFont);
			editTextState.setTypeface(Helper.getSharedHelper().normalFont);
			buttonCountries.setTypeface(Helper.getSharedHelper().normalFont);
			editTextZip.setTypeface(Helper.getSharedHelper().normalFont);

			tvDOB.setTypeface(Helper.getSharedHelper().normalFont);

			tvRedeemlbl.setTypeface(Helper.getSharedHelper().normalFont);
			tvRedeem.setTypeface(Helper.getSharedHelper().normalFont);
			tvPNlbl.setTypeface(Helper.getSharedHelper().normalFont);
		} catch (Exception e) {

		}

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
			editTextLastName.setText(profile.getLastName());
			editTextAddress.setText(profile.getAddress());
			editTextCity.setText(profile.getCity());
			editTextEmail.setText(profile.getEmail());
			editTextMobileNumber.setText(profile.getMobileNo() + "");
			editTextState.setText(profile.getState());
			buttonCountries.setText(profile.getCountry());
			editTextZip.setText(profile.getZip() + "");

			tvDOB.setText(profile.getDob());

			spinnerGender.setSelection(profile.getGender().startsWith("M") ? 0 : 1);
		} catch (Exception e) {

		}
	}

	public void savePressed(View v) {

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
				Toast.makeText(context,
						"You need internet connection to save your profile.",
						Toast.LENGTH_LONG).show();
				// if (intent.getStringExtra("FROM").equalsIgnoreCase("ESHOP"))
				// {
				// Toast.makeText(context,
				// "You need internet connection to buy a product.",
				// Toast.LENGTH_LONG).show();
				// }
			} catch (Exception e) {

			}

		}
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

	private boolean checkFields() {
		boolean status = true;

		TextView tv = null;
		if (editTextFirstName.getText().toString().trim().length() == 0) {
			Toast.makeText(context, "Enter your firstname", Toast.LENGTH_SHORT)
					.show();
			status = false;
			tv = editTextFirstName;
		} else if (editTextLastName.getText().toString().trim().length() == 0) {
			Toast.makeText(context, "Enter your last name", Toast.LENGTH_SHORT)
					.show();
			status = false;
			tv = editTextLastName;
		} else if (tvDOB.getText().toString().trim().length() == 0) {
			Toast.makeText(context, "Enter your date of birth",
					Toast.LENGTH_SHORT).show();
			status = false;
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
		} else if (editTextAddress.getText().toString().trim().length() == 0) {
			Toast.makeText(context, "Enter your address", Toast.LENGTH_SHORT)
					.show();
			status = false;
			tv = editTextAddress;
		} else if (editTextCity.getText().toString().trim().length() == 0) {
			Toast.makeText(context, "Enter your city", Toast.LENGTH_SHORT)
					.show();
			status = false;
			tv = editTextCity;
		} else if (buttonCountries.getText().length() == 0) {

			Toast.makeText(context, "Select your country", Toast.LENGTH_SHORT)
					.show();
			status = false;
		} else if (editTextZip.getText().toString().trim().length() == 0) {

			Toast.makeText(context, "Enter your zip", Toast.LENGTH_SHORT)
					.show();
			status = false;
			tv = editTextZip;
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
			InputMethodManager imm = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(tv, InputMethodManager.SHOW_IMPLICIT);
		}
		return status;
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

	private class AsyncWorker extends AsyncTask<Void, Void, Boolean> {

		@SuppressWarnings("deprecation")
		@Override
		protected Boolean doInBackground(Void... params) {

			if (Utils.hasNetworkConnection(activity.getApplicationContext())) {
				JSONObject obj = new JSONObject();
				Boolean status = false;
				try {

					Date dateOfBirth = new Date();
					if (tvDOB.getText().length() > 0) {
						dateOfBirth = sdf.parse(tvDOB.getText().toString());
					}

					Calendar cal = Calendar.getInstance();
					// sqliteHelper.openDataBase();

					String countryCode = getCountryCode(buttonCountries
							.getText().toString());
					// sqliteHelper
					// .getCountryCode(buttonCountries.getText()
					// .toString());
					// //sqliteHelper.close();

					obj.put("retailerId", Constants.RETAILER_ID);
					obj.put("fname", editTextFirstName.getText().toString());
					obj.put("lname", editTextLastName.getText().toString());
					obj.put("gender",
							(spinnerGender.getSelectedItemPosition() == 0) ? "M"
									: "F");
					obj.put("age", new Date().getYear() - dateOfBirth.getYear());
					obj.put("dob", tvDOB.getText().toString());
					obj.put("mobile_num", editTextMobileNumber.getText()
							.toString());
					obj.put("email", editTextEmail.getText().toString());
					obj.put("address", editTextAddress.getText().toString());
					obj.put("city", editTextCity.getText().toString());
					obj.put("state", editTextState.getText().toString());

					obj.put("country", countryCode);
					obj.put("zip",
							Integer.parseInt(editTextZip.getText().toString()));
					obj.put("lat", Constants.LAT);
					obj.put("long", Constants.LNG);
					obj.put("device_token", Constants.REG_ID);
					obj.put("device", 2);
					obj.put("latestTime", cal.getTimeInMillis());
					Boolean isLoggedIn = spref.getBoolean(
							Constants.KEY_IS_USER_LOGGED_IN, false);
					if (!isLoggedIn) {
						obj.put("password", edtPwd.getText().toString());
					}

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
				if (buttonSave
						.getText()
						.toString()
						.equalsIgnoreCase(
								getResources().getString(R.string.register))) {
					Toast.makeText(context, "Registered successfully",
							Toast.LENGTH_LONG).show();
					loggedin(true);
				} else {
					Toast.makeText(context, "Profile successfully saved",
							Toast.LENGTH_LONG).show();
				}
				Date dateOfBirth = new Date();
				if (tvDOB.getText().length() > 0) {

					try {
						dateOfBirth = sdf.parse(tvDOB.getText().toString());
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}

				Profile profile = new Profile();
				profile.setAddress(editTextAddress.getText().toString());
				// profile.setAge(new Date().getYear() - dateOfBirth.getYear());
				profile.setCity(editTextCity.getText().toString());
				profile.setCountry(buttonCountries.getText().toString());
				profile.setDeviceToken(Constants.REG_ID);
				profile.setDob(tvDOB.getText().toString().trim());
				profile.setEmail(editTextEmail.getText().toString());
				profile.setFirstName(editTextFirstName.getText().toString());
				profile.setLastName(editTextLastName.getText().toString());
				profile.setGender(spinnerGender.getSelectedItem().toString());
				profile.setLat(Constants.LAT);
				profile.setLng(Constants.LNG);
				profile.setMobileNo(Long.parseLong(editTextMobileNumber
						.getText().toString()));
				profile.setState(editTextState.getText().toString());
				profile.setTime(new Date().getTime());
				profile.setZip(Long.parseLong(editTextZip.getText().toString()));

				Gson gson = new Gson();
				String json = gson.toJson(profile);
				spref.edit().putString(Constants.KEY_PROFILE_INFO, json)
						.commit();

				// sqliteHelper.openDataBase();
				// sqliteHelper.insertOrReplaceProfile(profile);
				// sqliteHelper.close();

				spref.edit().putString(Constants.KEY_EMAIL, profile.getEmail())
						.commit();
				spref.edit()
						.putBoolean(Constants.KEY_PN, !pnSwitch.isChecked())
						.commit();
				String countryCode = getCountryCode(buttonCountries.getText()
						.toString());
				spref.edit().putString(Constants.KEY_COUNTRY_ID, countryCode)
						.commit();
				Utils.setProfile(context, true);

				dismissLoadingDialog();
				// Toast.makeText(context, "Profile successfully saved",
				// Toast.LENGTH_LONG).show();

			} else {
				dismissLoadingDialog();
			}

		}

		@Override
		protected void onPreExecute() {
			showLoadingDialog();
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
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		String emailId = spref.getString(Constants.KEY_EMAIL, "");
		if (!emailId.equalsIgnoreCase("")) {
			new UserProfileDataHandler(emailId, this);
		}
	}

	public void showLoadingDialog() {
		try {
			if (progressDialog == null || !progressDialog.isShowing()) {
				progressDialog = new ProgressDialog(context);
				progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				progressDialog.setMessage("Loading...");
				// progressDialog.setCancelable(false);
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.show();
			}
		} catch (Exception ex) {
			Log.e("",
					"Could not display progress dialog because the activity that called it is no longer active");
		}
	}

	public void dismissLoadingDialog() {
		try {
			progressDialog.dismiss();
		} catch (Exception ex) {
			Log.e("",
					"Could not dismiss progress dialog because the activity that called it is no longer active");
		}
	}

	@Override
	public void userProfileFetched(String rewardsPoints) {
		// TODO Auto-generated method stub
		tvRedeem.setText(Helper.getSharedHelper().rewardPoints);
	}

	@Override
	public void loggedIn(Boolean isSucess, String msg, Profile userProfile) {
		// TODO Auto-generated method stub
		if (isSucess) {
			String countryName = getCountryName(userProfile.getCountry());
			spref.edit()
					.putString(Constants.KEY_COUNTRY_ID,
							userProfile.getCountry()).commit();
			userProfile.setCountry(countryName);
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
		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();

	}

	@Override
	public void passwordSent(Boolean isSucess, String msg) {
		// TODO Auto-generated method stub
		if (isSucess) {
			showLoginForm();
		}
		Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
	}

}
