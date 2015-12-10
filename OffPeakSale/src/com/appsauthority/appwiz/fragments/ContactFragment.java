package com.appsauthority.appwiz.fragments;

import org.json.JSONException;
import org.json.JSONObject;

import com.appsauthority.appwiz.LoginHandler;
import com.appsauthority.appwiz.ProfileActivity;
import com.appsauthority.appwiz.models.Profile;
import com.appsauthority.appwiz.models.Retailer;
import com.appsauthority.appwiz.models.UserDetailObject;
import com.appsauthority.appwiz.utils.Constants;
import com.appsauthority.appwiz.utils.HTTPHandler;
import com.appsauthority.appwiz.utils.Helper;
import com.google.gson.Gson;
import com.offpeaksale.consumer.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ContactFragment extends Fragment implements OnClickListener {

	public String TAG = getClass().getSimpleName();
	private View root;
	Retailer objRetailer;
	private TextView tvRetailerAddress, tvContact;
	private EditText edt_Name, edt_Email, edt_Subject, edt_Message;
	private Button btn_submit;

	public ContactFragment() {
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		root = inflater.inflate(R.layout.contact_fragment, null);
		objRetailer = Helper.getSharedHelper().reatiler;
		initializeAllView();
		return root;
	}

	private void initializeAllView() {
		tvContact = (TextView) root.findViewById(R.id.tvContact);
		tvRetailerAddress = (TextView) root.findViewById(R.id.tvRetailerAddress);
		edt_Name = (EditText) root.findViewById(R.id.edt_Name);
		edt_Email = (EditText) root.findViewById(R.id.edt_Email);
		edt_Subject = (EditText) root.findViewById(R.id.edt_Subject);
		edt_Message = (EditText) root.findViewById(R.id.edt_Message);
		btn_submit = (Button) root.findViewById(R.id.btn_submit);
		btn_submit.setOnClickListener(this);

		try {
			tvContact.setTypeface(Helper.getSharedHelper().boldFont);
			tvRetailerAddress.setTypeface(Helper.getSharedHelper().normalFont);
			edt_Name.setTypeface(Helper.getSharedHelper().normalFont);
			edt_Email.setTypeface(Helper.getSharedHelper().normalFont);
			edt_Subject.setTypeface(Helper.getSharedHelper().normalFont);
			edt_Message.setTypeface(Helper.getSharedHelper().normalFont);
		} catch (Exception e) {
			// TODO: handle exception
		}

		tvRetailerAddress.setText(objRetailer.contactAddr + "\n \n" + objRetailer.contactPhone);
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.btn_submit) {
			String emailid = edt_Email.getText().toString();
			String name = edt_Name.getText().toString();
			String subject = edt_Subject.getText().toString();
			String message = edt_Message.getText().toString();
			String errorMsg = null;
			if (name == null || name.length() == 0) {
				errorMsg = "Enter name";
			} else if (!Helper.getSharedHelper().isEmailValid(emailid)) {
				errorMsg = "Invalid email id";
			} else if (subject == null || subject.length() == 0) {
				errorMsg = "Enter subject";
			} else if (message == null || message.length() == 0) {
				errorMsg = "Enter message";
			}

			if (errorMsg != null) {
				Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
			} else {

				// loggedin(true);
				// LoginHandler loginHandler = new LoginHandler(emailid,
				// password, ProfileActivity.this);
				// loginHandler.login();
				new AsyncContact().execute();
			}
		}
	}
private void clearAllField()
{
	edt_Email.setText("");
	edt_Name.setText("");
	edt_Subject.setText("");
	edt_Message.setText("");
}
	private final class AsyncContact extends AsyncTask<Void, Void, JSONObject> {

		@Override
		protected void onPreExecute() {
			btn_submit.setEnabled(false);
		}

		@Override
		protected JSONObject doInBackground(Void... params) {
			
			JSONObject param = new JSONObject();

			try {
				param.put(Constants.PARAM_RETAILER_ID, Constants.RETAILER_ID);
				param.put(Constants.PARAM_NAME,  edt_Name.getText().toString());
				param.put(Constants.PARAM_EMAIL, edt_Email.getText().toString());
				param.put(Constants.PARAM_COMMENTS, edt_Message.getText().toString());
				param.put(Constants.PARAM_SUBJECTS, edt_Subject.getText().toString());
				JSONObject jsonObject = HTTPHandler.defaultHandler().doPost(Constants.URL_RETAILER_CONTACT, param);

				return jsonObject;

			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}

		}

		@Override
		protected void onPostExecute(JSONObject json) {
			btn_submit.setEnabled(true);
			if (json != null) {
				if (json.has("errorCode")) {
					try {
						String errorCode = json.getString("errorCode");
						if (errorCode != null && errorCode.equals("1")) {
							Toast.makeText(getActivity(), ""+json.getString("errorMessage"), Toast.LENGTH_SHORT).show();
							clearAllField();
							}
						 else {
							 Toast.makeText(getActivity(), "Contact Email Sent Failed", Toast.LENGTH_SHORT).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block

						e.printStackTrace();
						Toast.makeText(getActivity(), "Contact Email Sent Failed", Toast.LENGTH_SHORT).show();
					}
				}
			} else {
				Toast.makeText(getActivity(), "Contact Email Sent Failed", Toast.LENGTH_SHORT).show();
			}
		}
	}
}
