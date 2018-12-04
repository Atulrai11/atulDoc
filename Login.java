package com.codemaven.xadapp.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.codemaven.xadapp.Constant.Constant;
import com.codemaven.xadapp.R;
import com.codemaven.xadapp.api.VolleyJSONRequest;
import com.codemaven.xadapp.fragment.FragmentOtpVarification;
import com.codemaven.xadapp.helper.GenericTextWatcher;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;

public class Login extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    public static ViewPager mViewPager;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    static ProgressDialog progressDialog;
    public static int otpVerifyFlag;
    public static  boolean resendOTPFlag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        sharedPreferences = this.getSharedPreferences("User_Info", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        otpVerifyFlag=0;
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        EditText et1, et2, et3, et4, phoneEditText,firstNameEditText,lastNameEditText;
        Button verfyButton,changePhoneButton,resendButton;
        LoginButton login_button;
        CallbackManager callbackManager;
        AccessTokenTracker accessTokenTracker;
        LoginManager mloginManager;
        public  Boolean isFirstLogging=false;
        String fname,lname,email,mobileNo,gender;
        private String facebook_id,f_name, m_name, l_name, genderfb, profile_image, full_name, email_id;


        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView;

            int i = getArguments().getInt(ARG_SECTION_NUMBER);
            if (i == 0) {
                rootView = inflater.inflate(R.layout.fragment_login, container, false);
                Button button = rootView.findViewById(R.id.phoneButton);
                firstNameEditText=rootView.findViewById(R.id.name_editText);
                lastNameEditText=rootView.findViewById(R.id.nameLast_editText);
                phoneEditText = rootView.findViewById(R.id.login_editText);
                login_button=rootView.findViewById(R.id.login_button);


                //------------
                FacebookSdk.sdkInitialize(getActivity());
                mloginManager=LoginManager.getInstance();
                //-----------------------
                //       callbackManager=CallbackManager.Factory.create();
                login_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        facebookLogin();
//                        Intent intent=new Intent(getActivity(),Home.class);
//                        startActivity(intent);
                    }
                });
             //   facebookLogin();
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(inputValidation()){
                            loginApiCall(false);
                        }
                    }
                });

            } else {
                rootView = inflater.inflate(R.layout.fragment_otp, container, false);
                setWidgets(rootView);
            }
            return rootView;
        }

        private void facebookLogin() {
            if (AccessToken.getCurrentAccessToken() != null) {
                mloginManager.logOut();
            } else {
                callbackManager = CallbackManager.Factory.create();
                mloginManager.logInWithReadPermissions(getActivity(),Arrays.asList("public_profile","email"));
                login_button.setReadPermissions("email");
                login_button.setFragment(this);
                LoginManager.getInstance().
                        registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {
                                //   Log.e("loginresult",""+loginResult);
                                facebook_id = f_name = m_name = l_name = genderfb = profile_image = full_name = email_id = "";
                                RequestData();
                                isFirstLogging = true;
                                editor.putBoolean("FIRST_LOGIN", isFirstLogging);
                                editor.commit();
                                Profile profile = Profile.getCurrentProfile();
                                if (profile != null) {
                                    facebook_id = profile.getId();
                                    //  Log.e("facebook_id", facebook_id);
                                    f_name = profile.getFirstName();
                                    //  Log.e("f_name", f_name);
                                    m_name = profile.getMiddleName();
                                    //  Log.e("m_name", m_name);
                                    l_name = profile.getLastName();
                                    //   Log.e("l_name", l_name);
                                    full_name = profile.getName();
                                    //   Log.e("full_name", full_name);
                                    profile_image = profile.getProfilePictureUri(400, 400).toString();
                                    //   Log.e("profile_image", profile_image);
                                }

                            }


                            @Override
                            public void onCancel() {
                                // App code
                            }

                            @Override
                            public void onError(FacebookException exception) {
                                // App code
                                //  Log.e("Facebook_Error", exception.getMessage());
                            }
                        });
            }
        }

        private void RequestData() {

            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object,GraphResponse response) {

                    JSONObject json = response.getJSONObject();
                    System.out.println("Json data :"+json);
                  //  Log.e("fbData",""+json);
                    try {
                        facebook_id=json.getString("id");
                        full_name=json.getString("name");
                        String[] parts = full_name.split(" ", 2);
                        String fname = parts[0];
                        String lname = parts[1];

                        email_id=json.getString("email");
                        editor.putString("FB_ID",facebook_id);
                        editor.putString("FIRST_NAME",fname);
                        editor.putString("LAST_NAME",lname);
                        editor.putString("Email",email_id);

                        editor.commit();
                        fbRegisterUser();

                       // Log.e("fbdata","--"+full_name+"----"+f_name);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        if(json != null){
                            String text = "<b>Name :</b> "+json.getString("name")+"<br><br><b>Email :</b> "+json.getString("email")+"<br><br><b>UserProfile link :</b> "+json.getString("link");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link,email,picture");
            request.setParameters(parameters);
            request.executeAsync();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                }
            },80000);
        }

        private void fbRegisterUser() {
            String url = Constant.BaseURL+"register.php";
            HashMap<String, String> params = new HashMap<>();

            params.put("facebook","1");  //flag
            params.put("fb_id",sharedPreferences.getString("FB_ID",""));
            String s=sharedPreferences.getString("FB_ID","");
            params.put("first_name",sharedPreferences.getString("FIRST_NAME",""));
            String s1=sharedPreferences.getString("FIRST_NAME","");

            VolleyJSONRequest volleyJSONRequest = new VolleyJSONRequest(getContext(),url,params);
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setCanceledOnTouchOutside(false);

            volleyJSONRequest.executeStringRequest(new VolleyJSONRequest.VolleyJSONRequestInterface() {
                @Override
                public void onSuccess(JSONObject obj) {


                    try {
                        String status = obj.getString("status");
                        if(status.equalsIgnoreCase("success")){
                            isFirstLogging=true;

                            JSONObject objData = new JSONObject();
                            objData = obj.getJSONObject("data");

                            editor.putString("user_id",objData.getString("id"));
                            editor.putBoolean("FIRST_LOGIN",isFirstLogging);
                            editor.commit();

                            Intent intent=new Intent(getActivity(),Home.class);
                            startActivity(intent);

                        }else{
                            String msg=obj.getString("message");
                            Toast.makeText(getContext(),msg,
                                    Toast.LENGTH_LONG).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(VolleyError error) {


                }
            });
        }

        protected void GraphLoginRequest(AccessToken accessToken){
          //  Log.e("InsideGraphLogin","2222222222222222222");
            GraphRequest graphRequest = GraphRequest.newMeRequest(accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        @Override
                        public void onCompleted(JSONObject jsonObject, GraphResponse graphResponse) {

                            try {

                                // Adding all user info one by one into TextView.
                              //  Log.e("InsideGraphLogin1","----222----------");
                                fname = jsonObject.getString("first_name");
                                lname = jsonObject.getString("last_name");
                                email = jsonObject.getString("email");
                                gender = jsonObject.getString("gender");


                                sharedPreferences.getString("FIRST_NAME",fname);
                                sharedPreferences.getString("LAST_NAME",lname);
                                sharedPreferences.getString("Gender",gender);
                                sharedPreferences.getString("Email_ID",email);
                                editor.commit();
                               // Log.e("fbValues",""+fname+""+lname+""+email+""+gender);
                                Intent intent=new Intent(getContext(),Home.class);
                                startActivity(intent);


                            }
                            catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

            Bundle bundle = new Bundle();
            bundle.putString(
                    "fields",
                    "email,gender,last_name,first_name"
            );
            graphRequest.setParameters(bundle);
            graphRequest.executeAsync();

        }
        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }


        public void onResume() {
            super.onResume();

            AppEventsLogger.activateApp(getActivity());
        }

        @Override
        public void onPause() {
            super.onPause();
            // Logs 'app deactivate' App Event.
            AppEventsLogger.deactivateApp(getActivity());
        }



        //--------Method for Validition for Login In App---------------------------
        private boolean inputValidationOfLogin(){
            if(phoneEditText.getText().length()<10){
                phoneEditText.setError("Invalid phone number");
                return false;
            }else if(phoneEditText.getText().toString().trim().isEmpty()){
                phoneEditText.setError("Enter phone number");
                return false;
            }
            return true;
        }
        //--------Method for Validition for Registration In App---------------------------

        private boolean inputValidation(){
            if(phoneEditText.getText().length()<10){
                phoneEditText.setError("Invalid phone number");
                return false;
            }else if(phoneEditText.getText().toString().trim().isEmpty()){
                phoneEditText.setError("Enter phone number");
                return false;
            }else if(firstNameEditText.getText().toString().isEmpty()){
                firstNameEditText.setError("Enter first Name");
                return false;

            }else if(lastNameEditText.getText().toString().isEmpty()){
                lastNameEditText.setError("Enter Last name");
                return false;
            }
            return true;
        }
        private void loginApiCall(final boolean resendOTP){
             resendOTPFlag = resendOTP;

            String url=Constant.BaseURL+"login.php";  //Url of Api
            HashMap<String,String>parms=new HashMap<>();
            parms.put("login","1");

            //flag
            if (resendOTPFlag)
            {
                parms.put("mobile_no",sharedPreferences.getString("PHONE",""));
            }
            else {

                parms.put("mobile_no", phoneEditText.getText().toString());  //Mobile No
            }
            VolleyJSONRequest volleyJSONRequest = new VolleyJSONRequest(getContext(),url,parms);
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setMessage("Sending OTP");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            volleyJSONRequest.executeStringRequest(new VolleyJSONRequest.VolleyJSONRequestInterface() {
                @Override
                public void onSuccess(JSONObject obj) {
                    progressDialog.hide();
                    String status = null;
                    try {
                        status = obj.getString("status");
                        if (status.equalsIgnoreCase("success")) {
                            JSONObject objData = new JSONObject();
                            objData = obj.getJSONObject("data");

                            if (!resendOTPFlag) {
                                editor.putString("PHONE", phoneEditText.getText().toString());
                                editor.putString("FIRST_NAME",firstNameEditText.getText().toString());
                                editor.putString("LAST_NAME",lastNameEditText.getText().toString());
                            }
                            editor.putString("LAST_ID",objData.getString("last_insert_id"));
                            editor.commit();
                            otpVerifyFlag=1;
                            mViewPager.setCurrentItem(1);

                        }else{
                            String msg=obj.getString("message");
                            if(resendOTPFlag)
                            {
                                callNext();
                            }
                            else {
                                if (inputValidation()) {
                                    callNext();   //here we calling the api of registration
                                }
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(VolleyError error) {

                }
            });
        }
        //-----------------------Api calling  for Registration-------------------
        private void callNext() {

            String url = Constant.registrationURL;
            HashMap<String, String> params = new HashMap<>();

            params.put("register","1");  //flag



            if(resendOTPFlag)
            {
                params.put("mobile_no", sharedPreferences.getString("PHONE",""));
                params.put("first_name", sharedPreferences.getString("FIRST_NAME",""));
                params.put("last_name", sharedPreferences.getString("LAST_NAME",""));
            }
            else {

                editor.putString("PHONE",phoneEditText.getText().toString());
                editor.putString("FIRST_NAME",firstNameEditText.getText().toString());
                editor.putString("LAST_NAME",lastNameEditText.getText().toString());
                editor.commit();

                params.put("mobile_no", phoneEditText.getText().toString());
                params.put("first_name", firstNameEditText.getText().toString());
                params.put("last_name", lastNameEditText.getText().toString());
            }
            VolleyJSONRequest volleyJSONRequest = new VolleyJSONRequest(getContext(),url,params);
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Sending OTP");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            volleyJSONRequest.executeStringRequest(new VolleyJSONRequest.VolleyJSONRequestInterface() {
                @Override
                public void onSuccess(JSONObject obj) {
                    progressDialog.hide();

                    try {
                        String status = obj.getString("status");
                        if(status.equalsIgnoreCase("success")){
                          //  isFirstLogging=true;
                            //editor.putString("PHONE",sharedPreferences.getString("PHONE",""));
                            JSONObject objData = new JSONObject();
                            objData = obj.getJSONObject("data");
                            editor.putString("LAST_ID",objData.getString("last_insert_id"));
//                            editor.putString("FIRST_NAME",firstNameEditText.getText().toString());
//                            editor.putString("LAST_NAME",lastNameEditText.getText().toString());
                            editor.putBoolean("FIRST_LOGIN",isFirstLogging);
                            editor.commit();
                            mViewPager.setCurrentItem(1);

                        }else{
                            String msg=obj.getString("message");
                            Toast.makeText(getContext(),msg,
                                    Toast.LENGTH_LONG).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(VolleyError error) {
                    progressDialog.hide();

                }
            });


        }

        private void setWidgets(View v) {
            verfyButton = v.findViewById(R.id.verfyButton);
            changePhoneButton = v.findViewById(R.id.changePhoneButton);
            resendButton = v.findViewById(R.id.resendButton);
            et1 = v.findViewById(R.id.editText1);
            et2 = v.findViewById(R.id.editText2);
            et3 = v.findViewById(R.id.editText3);
            et4 = v.findViewById(R.id.editText4);
            et1.addTextChangedListener(new GenericTextWatcher(et1,et2,et3,et4));
            et2.addTextChangedListener(new GenericTextWatcher(et2,et2,et3,et4));
            et3.addTextChangedListener(new GenericTextWatcher(et3,et2,et3,et4));
            et4.addTextChangedListener(new GenericTextWatcher(et4,et2,et3,et4));


            verfyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String digit1=et1.getText().toString();
                    String digit2=et2.getText().toString();
                    String digit3=et3.getText().toString();
                    String digit4=et4.getText().toString();

                    String inputValue=digit1+digit2+digit3+digit4;
                    if(otpVerifyFlag==1) {
                        verfyOTPLogin(inputValue);
                    }
                    else{
                        verfyOTP(inputValue);
                    }
                }
            });

            changePhoneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeNumber();
                }
            });

            resendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // resendButtonotp();
                    loginApiCall(true);
                }
            });
        }

        private void changeNumber() {
            mViewPager.setCurrentItem(0);
        }
        private void verfyOTPLogin(String inputValue) {
            String url = Constant.loginURL;
            HashMap<String, String> params = new HashMap<>();
            params.put("varify","1");
            params.put("mobile_no",sharedPreferences.getString("PHONE",""));
           // Log.d("hii1",""+sharedPreferences.getString("PHONE",""));
            params.put("otp",inputValue);
            String str=sharedPreferences.getString("LAST_ID","");
           // Log.d("Hiiii",""+str.toString());
            //  Toast.makeText(getActivity(), "Hiii"+str, Toast.LENGTH_SHORT).show();
            params.put("last_inser_id",str);

            VolleyJSONRequest volleyJSONRequest = new VolleyJSONRequest(getContext(),url,params);
            progressDialog.show();
            volleyJSONRequest.executeStringRequest(new VolleyJSONRequest.VolleyJSONRequestInterface() {
                @Override
                public void onSuccess(JSONObject obj) {
                  //  Log.e("Res",obj.toString());
                   // isFirstLogging=true;

                    try {
                        String status = obj.getString("status");
                        if(status.equalsIgnoreCase("success")){
                            progressDialog.hide();
                            isFirstLogging=true;
                            JSONObject obj1=obj.getJSONObject("data");
                            editor.putString("user_id",obj1.getString("id"));
                            String s=obj1.getString("id");
                            editor.putString("FIRST_NAME",obj1.getString("first_name"));
                            String s1=obj1.getString("first_name");
                            editor.putString("LAST_NAME",obj1.getString("last_name"));
                            String s2=obj1.getString("last_name");
                            editor.putBoolean("FIRST_LOGIN",isFirstLogging);
                            editor.commit();

                            Intent intent=new Intent(getActivity(),Home.class);
                            startActivity(intent);
                            getActivity().finish();
                        }else{
                            String msg=obj.getString("message");
                            Toast.makeText(getContext(),msg,
                                    Toast.LENGTH_LONG).show();
                            progressDialog.hide();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(VolleyError error) {
                    Toast.makeText(getContext(),error.getMessage().toString(),
                            Toast.LENGTH_LONG).show();
                    progressDialog.hide();
                }
            });


        }


        private void verfyOTP(String inputValue)     {


            String url = Constant.registrationURL;
            HashMap<String, String> params = new HashMap<>();
            params.put("varify","1");
            params.put("mobile_no",sharedPreferences.getString("PHONE",""));
            params.put("otp",inputValue);
            String str=sharedPreferences.getString("LAST_ID","");
          //  Log.d("Hiiii",""+str.toString());
            //       Toast.makeText(getActivity(), "Hiii"+str, Toast.LENGTH_SHORT).show();
            params.put("last_inser_id",str);

            VolleyJSONRequest volleyJSONRequest = new VolleyJSONRequest(getContext(),url,params);
            progressDialog.show();
            volleyJSONRequest.executeStringRequest(new VolleyJSONRequest.VolleyJSONRequestInterface() {
                @Override
                public void onSuccess(JSONObject obj) {
                    //Log.e("Res",obj.toString());

                    try {
                        editor.putString("user_id",obj.getString("data"));
                        editor.commit();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    try {
                        String status = obj.getString("status");
                        if(status.equalsIgnoreCase("success")){
                            isFirstLogging=true;
                            progressDialog.hide();
                            mViewPager.setCurrentItem(2);

                        }else{
                            String msg=obj.getString("message");
                            Toast.makeText(getContext(),msg,
                                    Toast.LENGTH_LONG).show();
                            progressDialog.hide();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(VolleyError error) {
                    Toast.makeText(getContext(),error.getMessage().toString(),
                            Toast.LENGTH_LONG).show();
                    progressDialog.hide();
                }
            });


        }
        private void resendButtonotp(){

        }


    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0:
                    return PlaceholderFragment.newInstance(0);

                case 1:
                    return PlaceholderFragment.newInstance(1);

                case 2:
                    FragmentOtpVarification fragmentOtpVarification = new FragmentOtpVarification();
                    return fragmentOtpVarification;

                default:
                    return PlaceholderFragment.newInstance(position + 1);
            }

        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }





}


