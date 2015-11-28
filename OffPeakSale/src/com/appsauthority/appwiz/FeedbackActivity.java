package com.appsauthority.appwiz;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.appsauthority.appwiz.custom.BaseActivity;
import com.appsauthority.appwiz.models.Profile;
import com.appsauthority.appwiz.models.Retailer;
import com.appsauthority.appwiz.models.SucessResponseObject;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.HTTPHandler;
import com.appsauthority.appwiz.utils.Helper;
import com.appsauthority.appwiz.utils.Utils;
import com.google.gson.Gson;
import com.offpeaksale.consumer.R;

public class FeedbackActivity extends BaseActivity {

	private Context context;
	private Activity activity;
	private TextView textViewHeader;
	private View viewLineHome, viewLineEShop, viewLineLoyalty,
			viewLineFeedback, viewLineVouchers;
	private EditText editTextFeedback, editTextFriendName, editTextFriendEmail,
			editTextFriendMobile, imagePicker;
	private Button buttonSubmit, buttonRefer;
	private ImageView imageViewLoyaltyLogo;
	private Profile profile;
	private RadioGroup radioGroupFeedback;
	private RadioButton radioButtonFeedback;
	private Retailer retailer;
	String feedbackOption1, feedbackOption2, feedbackOption3;
	RadioButton rd_like, rd_dislike, rd_reservation;
	private Uri fileUri;
	public static final int MEDIA_TYPE_IMAGE = 1;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	String filePath = "";
	Button btn_clearImage;

	private SharedPreferences spref;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActionBar().hide();
		setContentView(R.layout.activity_feedback);
		activity = this;
		context = this;
		spref = PreferenceManager.getDefaultSharedPreferences(this);
		retailer = Helper.getSharedHelper().reatiler;

		try {
			setHeaderTheme(activity, retailer.getRetailerTextColor(),
					retailer.getHeaderColor());
		} catch (Exception e) {
		}
		textViewHeader = (TextView) findViewById(R.id.textViewHeader);
		buttonRefer = (Button) findViewById(R.id.btn_refer);
		buttonSubmit = (Button) findViewById(R.id.btn_submit);
		editTextFeedback = (EditText) findViewById(R.id.edt_feedback);
		editTextFriendEmail = (EditText) findViewById(R.id.edt_friend_email);
		editTextFriendMobile = (EditText) findViewById(R.id.edt_friend_mobile);
		editTextFriendName = (EditText) findViewById(R.id.edt_friend_name);
		imageViewLoyaltyLogo = (ImageView) findViewById(R.id.iv_loyalty_logo);
		radioGroupFeedback = (RadioGroup) findViewById(R.id.rg_feedback);
		rd_like = (RadioButton) findViewById(R.id.rd_like);
		rd_dislike = (RadioButton) findViewById(R.id.rd_dislike);
		rd_reservation = (RadioButton) findViewById(R.id.rd_reservation);
		btn_clearImage = (Button) findViewById(R.id.btn_clearImage);
		imagePicker = (EditText) findViewById(R.id.imagePicker);

		// editTextFeedback.setBackgroundColor(Constants.HEADER_COLOR
		// - Constants.END_COLOR_LIGHTER);
		try {
			editTextFeedback
					.setBackgroundDrawable(getBoarderForTextField(retailer
							.getHeaderColor()));

			editTextFriendEmail
					.setBackgroundDrawable(Helper.getSharedHelper().getGradientDrawableEditText(retailer
							.getHeaderColor()));

			editTextFriendMobile
					.setBackgroundDrawable(Helper.getSharedHelper().getGradientDrawableEditText(retailer
							.getHeaderColor()));

			editTextFriendName
					.setBackgroundDrawable(Helper.getSharedHelper().getGradientDrawableEditText(retailer
							.getHeaderColor()));
			imagePicker
					.setBackgroundDrawable(Helper.getSharedHelper().getGradientDrawableEditText(retailer
							.getHeaderColor()));

			textViewHeader.setText("FEEDBACK");
			buttonRefer.setBackgroundDrawable(getGradientDrawable(retailer
					.getHeaderColor()));
			buttonSubmit.setBackgroundDrawable(getGradientDrawable(retailer
					.getHeaderColor()));
			buttonRefer.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));
			buttonSubmit.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));

			btn_clearImage.setBackgroundDrawable(getGradientDrawable(retailer
					.getHeaderColor()));
			btn_clearImage.setBackgroundDrawable(getGradientDrawable(retailer
					.getHeaderColor()));
			btn_clearImage.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));
		} catch (Exception e) {
		}

		editTextFeedback.setScroller(new Scroller(context));

		editTextFeedback.setVerticalScrollBarEnabled(true);

		setViewLines();

		imageCacheloader.displayImage(Utils.getFeedbackImage(context),
				R.drawable.ic_launcher, imageViewLoyaltyLogo);

		if (Utils.hasNetworkConnection(context)) {
			new AsyncGetGiftImage().execute();
		}

		imagePicker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				launchPhotoPickerOption();
				// getWindow().setSoftInputMode(
				// WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				getWindow()
						.setSoftInputMode(
								WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			}
		});

		setFont();
	}
	public Drawable getBoarderForTextField(String headerColor) {
		GradientDrawable gdDefault = new GradientDrawable();
		// gdDefault.setColor(Color.parseColor("#" + headerColor));
		// gdDefault.setAlpha(40);
		gdDefault.setCornerRadius(7);
		
		gdDefault.setStroke(2, getResources().getColor(R.color.triangle_underline_color));
		// Drawable gdDefault = getResources().getDrawable(
		// R.drawable.apptheme_edit_text_holo_light);
		gdDefault.setColor(Color.WHITE);
		return gdDefault;
	}
	void setFont() {
		try {
			textViewHeader.setTypeface(Helper.getSharedHelper().boldFont);

			buttonSubmit.setTypeface(Helper.getSharedHelper().boldFont);
			buttonRefer.setTypeface(Helper.getSharedHelper().boldFont);

			imagePicker.setTypeface(Helper.getSharedHelper().normalFont);
			editTextFeedback.setTypeface(Helper.getSharedHelper().normalFont);

			rd_like.setTypeface(Helper.getSharedHelper().normalFont);
			rd_dislike.setTypeface(Helper.getSharedHelper().normalFont);
			rd_reservation.setTypeface(Helper.getSharedHelper().normalFont);

			TextView textViewReferAFriend = (TextView) findViewById(R.id.textViewReferAFriend);
			textViewReferAFriend.setTypeface(Helper.getSharedHelper().boldFont);

			editTextFriendName.setTypeface(Helper.getSharedHelper().normalFont);
			editTextFriendEmail
					.setTypeface(Helper.getSharedHelper().normalFont);
			editTextFriendMobile
					.setTypeface(Helper.getSharedHelper().normalFont);
		} catch (Exception e) {

		}

	}

	private void setViewLines() {
		viewLineHome = (View) findViewById(R.id.viewLineHome);
		viewLineEShop = (View) findViewById(R.id.viewLineEShop);
		viewLineLoyalty = (View) findViewById(R.id.viewLineLoyalty);
		viewLineFeedback = (View) findViewById(R.id.viewLineFeedback);
		viewLineVouchers = (View) findViewById(R.id.viewLineVouchers);
		viewLineHome.setVisibility(View.INVISIBLE);
		viewLineEShop.setVisibility(View.INVISIBLE);
		viewLineLoyalty.setVisibility(View.INVISIBLE);
		viewLineFeedback.setVisibility(View.VISIBLE);
		viewLineVouchers.setVisibility(View.INVISIBLE);
		try {
			viewLineFeedback.setBackgroundColor(Color.parseColor("#80"
					+ Helper.getSharedHelper().reatiler.getHeaderColor()));
		} catch (Exception e) {

		}
	}

	public void submitPressed(View v) {
		if (Utils.hasNetworkConnection(context)) {
			if (Utils.isProfileAvailable(context)) {
				if (editTextFeedback.getText().toString().length() > 0) {
//					sendFeedback();
					new AsyncFeedback().execute();
				} else {
					Toast.makeText(context, "Enter your feedback.",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				showProfileAlert();
			}

		} else {
			Toast.makeText(context,
					"You need internet connection to send a feedback.",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void clearImagePressed(View V) {

		btn_clearImage.setVisibility(View.GONE);
		imagePicker.setText("");
		imagePicker.setHint("Insert Image (Optional)");
		filePath = "";
	}

	public void referPressed(View v) {
		if (Utils.hasNetworkConnection(context)) {
			if (Utils.isProfileAvailable(context)) {
				if (checkFields()) {
					new AsyncRefer().execute();
				}
			} else {
				showProfileAlert();
			}
		} else {
			Toast.makeText(context,
					"You need internet connection to refer a friend.",
					Toast.LENGTH_SHORT).show();
		}

	}

	@SuppressWarnings("deprecation")
	private void showProfileAlert() {
		try {
			DisplayMetrics metrics = getResources().getDisplayMetrics();
			int width = metrics.widthPixels;

			final Dialog dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.dialog_profile);

			dialog.getWindow().setLayout((6 * width) / 7,
					LayoutParams.WRAP_CONTENT);

			TextView textViewTitle = (TextView) dialog
					.findViewById(R.id.textViewTitle);
			
			TextView tvInfo1 = (TextView) dialog
					.findViewById(R.id.tvInfo1);
			TextView tvThankYou = (TextView) dialog
					.findViewById(R.id.tvThankYou);
			TextView tvInfo2 = (TextView) dialog
					.findViewById(R.id.tvInfo2);
			
			Button buttonProfile = (Button) dialog
					.findViewById(R.id.btn_profile);

			textViewTitle.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));
			textViewTitle
					.setBackgroundDrawable(getGradientDrawableNoRad(retailer
							.getHeaderColor()));

			buttonProfile.setBackgroundDrawable(getGradientDrawable(retailer
					.getHeaderColor()));
			
			buttonProfile
			.setTypeface(Helper.getSharedHelper().boldFont);
			textViewTitle
			.setTypeface(Helper.getSharedHelper().boldFont);
			
			tvInfo1
			.setTypeface(Helper.getSharedHelper().normalFont);
			tvThankYou
			.setTypeface(Helper.getSharedHelper().boldFont);
			tvInfo2
			.setTypeface(Helper.getSharedHelper().normalFont);
			

			buttonProfile.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(context, ProfileActivity.class);
					intent.putExtra("FROM", "FEEDBACK");
					startActivity(intent);
					dialog.dismiss();

				}
			});

			buttonProfile.setTextColor(Color.parseColor("#"
					+ retailer.getRetailerTextColor()));

			dialog.show();
		} catch (Exception e) {
		}

	}

	private boolean checkFields() {
		boolean status = true;

		if (editTextFriendName.getText().toString().trim().length() == 0) {
			Toast.makeText(context, "Enter your friend's name.",
					Toast.LENGTH_SHORT).show();
			status = false;
		} else if (editTextFriendEmail.getText().toString().trim().length() == 0) {
			Toast.makeText(context, "Enter your friend's email.",
					Toast.LENGTH_SHORT).show();
			status = false;
		} else if (editTextFriendMobile.getText().toString().trim().length() == 0) {
			Toast.makeText(context, "Enter your friend's mobile.",
					Toast.LENGTH_SHORT).show();
			status = false;
		} else {
			status = true;
		}

		return status;
	}

	public static void hideSoftKeyboard(Activity activity) {

		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getWindowToken(), 0);
	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		if ((requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
				&& resultCode == RESULT_OK) {
			filePath = fileUri.getPath();

			btn_clearImage.setVisibility(View.VISIBLE);
			imagePicker.setText("Image Selected");
		} else if (requestCode == PIC_IMAGE_FROM_GALLERY) {
			if (intent != null && resultCode == RESULT_OK) {

				Uri selectedImage = intent.getData();

				String[] filePathColumn = { MediaStore.Images.Media.DATA };
				Cursor cursor = getContentResolver().query(selectedImage,
						filePathColumn, null, null, null);
				cursor.moveToFirst();
				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				filePath = cursor.getString(columnIndex);
				cursor.close();
				btn_clearImage.setVisibility(View.VISIBLE);
				imagePicker.setText("Image Selected");

			}
		}
	}

	public void launchPhotoPickerOption() {
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Choose source");
		// Set an EditText view to get user input

		alert.setPositiveButton("Camera",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						openCamera();
					}
				});

		alert.setNegativeButton("Photo Library",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						// Canceled.
						openGallery();
					}
				});

		alert.show();

	}

	public static int PIC_IMAGE_FROM_GALLERY = 1;

	private static Uri getOutputMediaFileUri(int type) {
		return Uri.fromFile(getOutputMediaFile(type));
	}

	public void openCamera() {
		// create Intent to take a picture and return control to the calling
		// application
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to
															// save the image
		intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
															// name
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}

	private void openGallery() {
		Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
		photoPickerIntent.setType("image/*");
		startActivityForResult(photoPickerIntent, PIC_IMAGE_FROM_GALLERY);
	}

	private static File getOutputMediaFile(int type) {
		// To be safe, you should check that the SDCard is mounted
		// using Environment.getExternalStorageState() before doing this.

		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"MyCameraApp");
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ "IMG_" + timeStamp + ".jpg");
		}
		// else if(type == MEDIA_TYPE_VIDEO) {
		// mediaFile = new File(mediaStorageDir.getPath() + File.separator +
		// "VID_"+ timeStamp + ".mp4");
		// }
		else {
			return null;
		}

		return mediaFile;
	}

//	void sendFeedback() {
//
//		// String string =
//		// "{ \"feedbackMsg\":\""+editTextFeedback.getText().toString()+"\",\"feedbackSub\":\""++"\",\"email\":\"%@\",\"retailerId\":\"%@\"}
//
//		try {
//			JSONObject param = new JSONObject();
//			String sub;
//			if (rd_like.isChecked()) {
//				sub = feedbackOption1;
//			} else if (rd_dislike.isChecked()) {
//				sub = feedbackOption2;
//			} else {
//				sub = feedbackOption3;
//			}
//
//			param.put(Constants.PARAM_RETAILER_ID, Constants.RETAILER_ID);
//			param.put(Constants.PARAM_EMAIL,
//					spref.getString(Constants.KEY_EMAIL, "test@gmail.com"));
//			param.put(Constants.PARAM_FEEDBACK_SUB, sub);
//			param.put(Constants.PARAM_FEEDBACK_MSG, editTextFeedback.getText()
//					.toString());
//			String jsonStr = param.toString();
//
//			new AsyncFeedback().execute();
//			// uploadFile(jsonStr);
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//		try {
//			// executeMultipartPost(jsonStr);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}// uploadFile(Constants.URL_SEND_FEEDBACK, jsonStr);
//	}

	String sendFeedBack( String input) {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		String link = Constants.URL_SEND_FEEDBACK;
		HttpPost httppost = new HttpPost(link);
		MultipartEntity entity = new MultipartEntity(
				HttpMultipartMode.BROWSER_COMPATIBLE);
		if (filePath != null) {
			FileBody fb = new FileBody(new File(filePath), "image/*");
			entity.addPart("upFile", fb);
		}

		// if (params != null && !params.isEmpty()) {
		// for (NameValuePair p : params) {
		// entity.addPart(p.getName(), new StringBody(p.getValue()
		// + ""));
		//
		// }
		// }
		String sub;
		if (rd_like.isChecked()) {
			sub = feedbackOption1;
		} else if (rd_dislike.isChecked()) {
			sub = feedbackOption2;
		} else {
			sub = feedbackOption3;
		}

		try {
			entity.addPart("data", new StringBody(
					input));
//			entity.addPart(
//					Constants.PARAM_EMAIL,
//					new StringBody(spref.getString(Constants.KEY_EMAIL,
//							"test@gmail.com")));
//			entity.addPart(Constants.PARAM_FEEDBACK_SUB, new StringBody(sub));
//			entity.addPart(Constants.PARAM_FEEDBACK_MSG, new StringBody(
//					editTextFeedback.getText().toString()));
			httppost.setEntity(entity);
			HttpResponse response = httpClient.execute(httppost);
			// Read the response
			String responseStr = EntityUtils.toString(response.getEntity());
			return responseStr;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";

	}

	public String uploadFile(String jsonStr) {

		int serverResponseCode = 0;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(filePath);

		try {

			// open a URL connection to the Servlet
			String link = Constants.URL_SEND_FEEDBACK;
			if (Helper.getSharedHelper().isSSL.equals("1")) {
				link = link.replace("http://", "https://");
			} else {

			}
			URL url = new URL(link);

			// Open a HTTP connection to the URL
			conn = (HttpURLConnection) url.openConnection();
			conn.setDoInput(true); // Allow Inputs
			conn.setDoOutput(true); // Allow Outputs
			conn.setUseCaches(false); // Don't use a Cached Copy
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Connection", "Keep-Alive");
			conn.setRequestProperty("ENCTYPE", "multipart/form-data");
			conn.setRequestProperty("Content-Type",
					"multipart/form-data;boundary=" + boundary);
			conn.setRequestProperty("uploaded_file", filePath);

			dos = new DataOutputStream(conn.getOutputStream());

			dos.writeBytes(twoHyphens + boundary + lineEnd);
			if (!filePath.equals("")) {
				FileInputStream fileInputStream = null;
				dos.writeBytes("Content-Disposition: form-data; name=\"upFile\";\"filename=\""
						+ filePath + "\"" + lineEnd);
				fileInputStream = new FileInputStream(sourceFile);

				// create a buffer of maximum size
				bytesAvailable = fileInputStream.available();

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {

					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				}

				// send multipart form data necesssary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				fileInputStream.close();
			}

			dos.writeBytes("Content-Disposition: form-data; name=\"data\""
					+ lineEnd);
			dos.writeBytes(lineEnd);
			dos.writeBytes("" + jsonStr);
			dos.writeBytes(lineEnd);
			dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

			InputStream is = conn.getInputStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is));
			String line;
			StringBuffer response = new StringBuffer();
			while ((line = rd.readLine()) != null) {
				response.append(line);
				response.append('\r');
			}
			// Responses from the server (code and message)
			serverResponseCode = conn.getResponseCode();
			String serverResponseMessage = conn.getResponseMessage();
			String responseStr = response.toString();
			Log.i("uploadFile", "HTTP Response is : " + serverResponseMessage
					+ ": " + serverResponseCode);
			dos.flush();
			dos.close();
			return responseStr;

			// close the streams //

		} catch (MalformedURLException ex) {

			ex.printStackTrace();

			Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
		} catch (Exception e) {

			e.printStackTrace();

			Log.e("Upload file to server Exception",
					"Exception : " + e.getMessage(), e);
		}
		return "";

	} // End else block

	private final class AsyncGetGiftImage extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {

		}

		@Override
		protected String doInBackground(Void... params) {

			if (Utils.hasNetworkConnection(getApplicationContext())) {
				JSONObject param = new JSONObject();
				try {
					param.put(Constants.PARAM_RETAILER_ID,
							Constants.RETAILER_ID);
					JSONObject jsonObject = HTTPHandler.defaultHandler()
							.doPost(Constants.URL_GET_FEEDBACK_GIFT, param);

					feedbackOption1 = jsonObject.getString("feedbackOption1");
					feedbackOption2 = jsonObject.getString("feedbackOption2");
					feedbackOption3 = jsonObject.getString("feedbackOption3");

					return jsonObject.getString("feedbackImage");

				} catch (Exception e) {
					e.printStackTrace();
					return "";
				}
			}
			return "";

		}

		@Override
		protected void onPostExecute(String image) {

			Utils.setFeedbackImage(context, image);
			imageCacheloader.displayImage(image, R.drawable.ic_launcher,
					imageViewLoyaltyLogo);

			if (feedbackOption1 != null) {
				rd_like.setText(feedbackOption1);
				rd_dislike.setText(feedbackOption2);
				rd_reservation.setText(feedbackOption3);
			}

		}
	}

	private final class AsyncRefer extends AsyncTask<Void, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog();
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			// sqliteHelper.openDataBase();
			// profile = sqliteHelper.getProfile();
			// sqliteHelper.close();
			JSONObject param = new JSONObject();
			try {
				param.put(Constants.PARAM_RETAILER_ID, Constants.RETAILER_ID);
				param.put(Constants.PARAM_EMAIL,
						spref.getString(Constants.KEY_EMAIL, "test@gmail.com"));
				param.put(Constants.PARAM_FRIEND_NAME, editTextFriendName
						.getText().toString());
				param.put(Constants.PARAM_FRIEND_MOBILE, editTextFriendMobile
						.getText().toString());
				param.put(Constants.PARAM_FRIEND_EMAIL, editTextFriendEmail
						.getText().toString());
				param.put(Constants.PARAM_DOWNLOAD_URL, "");

				JSONObject jsonObject = HTTPHandler.defaultHandler().doPost(
						Constants.URL_REFER_FRIEND, param);

				return jsonObject;

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		}

		@Override
		protected void onPostExecute(JSONObject json) {
			dismissLoadingDialog();
			if (json != null && json.has("errorMessage")) {
				try {
					Toast.makeText(context, json.getString("errorMessage"),
							Toast.LENGTH_SHORT).show();
					editTextFriendEmail.setText("");
					editTextFriendMobile.setText("");
					editTextFriendName.setText("");

					hideSoftKeyboard(activity);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private final class AsyncFeedback extends AsyncTask<Void, Void, String> {

		@Override
		protected void onPreExecute() {
			showLoadingDialog();
		}

		@Override
		protected String doInBackground(Void... params) {
			// sqliteHelper.openDataBase();
			// profile = sqliteHelper.getProfile();
			// sqliteHelper.close();
			int selectedId = radioGroupFeedback.getCheckedRadioButtonId();

			radioButtonFeedback = (RadioButton) findViewById(selectedId);
			// JSONObject param = new JSONObject();
			try {
				// param.put(Constants.PARAM_RETAILER_ID,
				// Constants.RETAILER_ID);
				// param.put(Constants.PARAM_EMAIL, "aa@gmail.com");
				// param.put(Constants.PARAM_FEEDBACK_SUB, radioButtonFeedback
				// .getText().toString());
				// param.put(Constants.PARAM_FEEDBACK_MSG, editTextFeedback
				// .getText().toString());
				//
				// JSONObject jsonObject = HTTPHandler.defaultHandler().doPost(
				// Constants.URL_SEND_FEEDBACK, param);

				JSONObject param = new JSONObject();
				String sub;
				if (rd_like.isChecked()) {
					sub = feedbackOption1;
				} else if (rd_dislike.isChecked()) {
					sub = feedbackOption2;
				} else {
					sub = feedbackOption3;
				}

				param.put(Constants.PARAM_RETAILER_ID, Constants.RETAILER_ID);
				param.put(Constants.PARAM_EMAIL,
						spref.getString(Constants.KEY_EMAIL, "test@gmail.com"));
				param.put(Constants.PARAM_FEEDBACK_SUB, sub);
				param.put(Constants.PARAM_FEEDBACK_MSG, editTextFeedback
						.getText().toString());
				String jsonStr = param.toString();

				String responseStr = sendFeedBack(jsonStr);

				return responseStr;

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		}

		@Override
		protected void onPostExecute(String responseStr) {
			dismissLoadingDialog();
			Gson gson = new Gson();
			SucessResponseObject result = gson.fromJson(responseStr,
					SucessResponseObject.class);
			if (result.getErrorCode().equals("1")) {
				clearImagePressed(btn_clearImage);
				editTextFeedback.setText("");

			}
			Toast.makeText(context, result.getErrorMessage(),
					Toast.LENGTH_SHORT).show();

		}
	}
}
