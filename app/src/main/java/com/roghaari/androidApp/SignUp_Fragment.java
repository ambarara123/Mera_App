package com.roghaari.androidApp;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.roghaari.R;

import org.json.JSONException;
import org.json.JSONObject;



public class SignUp_Fragment extends Fragment implements OnClickListener {
	private static View view;
	private static EditText fullName, emailId, mobileNumber, location,
			dateView,password,cnfPassword;
	private static TextView login,datePicker;
	private static Button signUpButton;
	private static CheckBox terms_conditions;
	private static RadioButton maleButton,femaleButton;
	private static FragmentManager fragmentManager;
	private static ProgressDialog signupProgressDialog;

    Calendar myCalendar = Calendar.getInstance();


	public SignUp_Fragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.signup_layout, container, false);
		initViews();
		setListeners();
		signupProgressDialog = new ProgressDialog(getContext());
		signupProgressDialog.setCancelable(false);


		return view;
	}

	// Initialize all views
	private void initViews() {
		fullName = (EditText) view.findViewById(R.id.fullName);
		emailId = (EditText) view.findViewById(R.id.userEmailId);
		mobileNumber = (EditText) view.findViewById(R.id.mobileNumber);
		location = (EditText) view.findViewById(R.id.location);
		dateView = (EditText) view.findViewById(R.id.dob);
		signUpButton = (Button) view.findViewById(R.id.signUpBtn);
		login = (TextView) view.findViewById(R.id.already_user);
		terms_conditions = (CheckBox) view.findViewById(R.id.terms_conditions);
        datePicker = (TextView) view.findViewById(R.id.dob_picker);
        maleButton = (RadioButton) view.findViewById(R.id.radioButton_male);
        femaleButton = (RadioButton) view.findViewById(R.id.radioButton_female);
        password = (EditText) view.findViewById(R.id.password);
        cnfPassword = (EditText) view.findViewById(R.id.confirm_password);

		// Setting text selector over textviews
		XmlResourceParser xrp = getResources().getXml(R.drawable.text_selector);
		try {
			ColorStateList csl = ColorStateList.createFromXml(getResources(),
					xrp);

			login.setTextColor(csl);
			terms_conditions.setTextColor(csl);
		} catch (Exception e) {
		}
	}


	// Set Listeners
	private void setListeners() {
		signUpButton.setOnClickListener(this);
		login.setOnClickListener(this);
		datePicker.setOnClickListener(this);
		maleButton.setOnClickListener(this);
		femaleButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.signUpBtn:

			// Call checkValidation method
			checkValidation();

			break;

		case R.id.already_user:

			// Replace login fragment
			new MainActivity().replaceLoginFragment();
			break;

            case R.id.dob_picker:

                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;


            case R.id.radioButton_male:
                    maleButton.setChecked(true);
                    femaleButton.setChecked(false);
                    // Pirates are the best
                   // Toast.makeText(getContext(), "check", Toast.LENGTH_SHORT).show();


                    break;
            case R.id.radioButton_female:
                    maleButton.setChecked(false);
                    femaleButton.setChecked(true);


                    break;

		}

	}

	// Check Validation Method
	private void checkValidation() {



		// Get all edittext texts
		String getFullName = fullName.getText().toString().trim();
		String getEmailId = emailId.getText().toString().trim();
		String getMobileNumber = mobileNumber.getText().toString();
		String getLocation = location.getText().toString();
		String getDOB = dateView.getText().toString();
		String getPassword = password.getText().toString();
		String getCnfPassword = cnfPassword.getText().toString();


		// Pattern match for email id
		Pattern p = Pattern.compile(Utils.regEx);
		Matcher m = p.matcher(getEmailId);

		// Check if all strings are null or not
		if (getFullName.equals("") || getFullName.length() == 0
				|| getEmailId.equals("") || getEmailId.length() == 0
				|| getMobileNumber.equals("") || getMobileNumber.length() == 0
				|| getLocation.equals("") || getLocation.length() == 0
                || getDOB.equals("") || getDOB.length() == 0
				) {

			new CustomToast().Show_Toast(getActivity(), view,
					"All fields are required.");


		}

		// Check if email id valid or not
		else if (!m.find()) {
			new CustomToast().Show_Toast(getActivity(), view,
					"Your Email Id is Invalid.");


		}

		// Check if both password should be equal
		else if (!getCnfPassword.equals(getPassword)) {
			new CustomToast().Show_Toast(getActivity(), view,
					"Both password doesn't match.");

		}

		// Make sure user should check Terms and Conditions checkbox
		else if (!terms_conditions.isChecked()) {
			new CustomToast().Show_Toast(getActivity(), view,
					"Please select Terms and Conditions.");


		}

		// Else do signup or do your stuff
		else {
			serverRequest();
		}

	}





	//calender integration
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };


    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        dateView.setText(sdf.format(myCalendar.getTime()));
    }


    private void serverRequest(){

    	//progressbar title and other setting
		signupProgressDialog.setTitle("please wait....");
		showDialog();

		// Get all edittext texts
		final String getFullName = fullName.getText().toString().trim();
		final String getEmailId = emailId.getText().toString().trim();
		String getMobileNumber = mobileNumber.getText().toString();
		String getLocation = location.getText().toString();
		String getDOB = dateView.getText().toString();
		final String getPassword = password.getText().toString();
		String getCnfPassword = cnfPassword.getText().toString();




		StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.URL_REGISTER,
				new Response.Listener<String>() {
					@Override
					public void onResponse(String response) {
						hideDialog();


						try {
							//converting response to json object
							JSONObject obj = new JSONObject(response);

							//if no error in response
							if (!obj.getBoolean("error")) {
								Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();


								//getting the user from the response
								JSONObject userJson = obj.getJSONObject("user");

								//creating a new user object
								User user = new User(
										userJson.getInt("id"),
										userJson.getString("username"),
										userJson.getString("email"),
										userJson.getString("gender")
								);

								//storing the user in shared preferences
								//SharedPrefManager.getInstance(getContext()).userLogin(user);

								//starting the profile activity
								//finish();
								new MainActivity().replaceLoginFragment();
								//startActivity(new Intent(getContext(), TabBarActivity.class));
							} else {
								Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						hideDialog();
						Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
					}
				}) {
			@Override
			protected Map<String, String> getParams() throws AuthFailureError {
				Map<String, String> params = new HashMap<>();
				params.put("username", getFullName);
				params.put("email", getEmailId);
				params.put("password", getPassword);

				//need to be changed
				params.put("gender","male" );
				return params;
			}
		};

		VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);

	}

	//showing and hiding methods of progress dialog

	private void showDialog() {
		if (!signupProgressDialog.isShowing())
			signupProgressDialog.show();
	}

	private void hideDialog() {
		if (signupProgressDialog.isShowing())
			signupProgressDialog.dismiss();
	}


}



