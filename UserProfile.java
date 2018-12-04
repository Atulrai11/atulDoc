package com.codemaven.xadapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.codemaven.xadapp.Constant.Constant;
import com.codemaven.xadapp.R;
import com.codemaven.xadapp.api.VolleyJSONRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserProfile extends AppCompatActivity {
    EditText fname, lname, emailId, contactNo, adharNo, address, panNo;
    RadioGroup gender;
    Button update;
    LinearLayout profile_View, profile_Edit;
    //  String flag;
    String  gender_conf;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String fnam, lnam, mob, unm,email,gend,adh,pan,add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        // setSupportActionBar(toolbar);
        toolbar.setTitle("Edit Profile");
        toolbar.setNavigationIcon(R.drawable.arrow);
        initWidgets();
        sharedPreferences = getApplicationContext().getSharedPreferences("User_Info", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        unm = sharedPreferences.getString("user_name", "");
        fnam = sharedPreferences.getString("FIRST_NAME", "");
        lnam = sharedPreferences.getString("LAST_NAME", "");
        mob = sharedPreferences.getString("PHONE", "");
        email=sharedPreferences.getString("Email", "");
        gend=sharedPreferences.getString("Gender", "");
        adh=sharedPreferences.getString("Adhar_No", "");
        pan=sharedPreferences.getString("Pan_NO", "");
        add=sharedPreferences.getString("Address", "");

        fname.setText(fnam);
        lname.setText(lnam);
        contactNo.setText(mob);
        emailId.setText(email);
        if(gend.equals("0")){
            gender.check(R.id.male);
        }else if(gend.equals("1")){
           gender.check(R.id.female);
        }else{
            gender.check(R.id.transgender);
        }
        adharNo.setText(adh);
        panNo.setText(pan);
        address.setText(add);
           gender_conf=gend;
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.male:
                        gender_conf ="0";
                        break;
                    case R.id.female:
                        gender_conf ="1";
                        break;
                    case R.id.transgender:
                        gender_conf ="2";
                    //    Toast.makeText(getApplicationContext(), "Others", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inputValidation()) {
                    try {
                        callRegistrationAPI();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // UserProfile.super.onBackPressed();
                startActivity(new Intent(UserProfile.this,Home.class));
                finish();
            }
        });
    }

    private void callRegistrationAPI() throws JSONException {
     //   String url = "http://xadnew.quickbooksupport365.com/service/profile.php";
       // String url = Constant.OldURL+"profile.php";
        String url = Constant.BaseURL+"profile.php";
        HashMap<String, String> params = new HashMap<>();
        params.put("update", "1");
       params.put("user_id", sharedPreferences.getString("user_id", ""));
      //  Log.d("cgjgvh",sharedPreferences.getString("user_id",""));
      //  params.put("user_id", "94");

        JSONObject obj_value = new JSONObject();

        obj_value.put("first_name", fname.getText().toString());
        obj_value.put("last_name", lname.getText().toString());
        obj_value.put("email", emailId.getText().toString());
        obj_value.put("gender", gender_conf);
        obj_value.put("adhar_no", adharNo.getText().toString());
        obj_value.put("address", address.getText().toString());
        obj_value.put("pan_no", panNo.getText().toString());

      //  String mystring= mystring.replace("\"", "\\\"");
        params.put("responsedata", obj_value.toString().replace("\\", ""));

       // Log.d("ProfileData",""+obj_value.toString().replace("\\", ""));

        VolleyJSONRequest volleyJSONRequest = new VolleyJSONRequest(getApplicationContext(), url, params);
        volleyJSONRequest.executeStringRequest(new VolleyJSONRequest.VolleyJSONRequestInterface() {
            @Override
            public void onSuccess(JSONObject obj) {
                try {
                    String status = obj.getString("status");
                    if (status.equalsIgnoreCase("success")) {
                        // mViewPager.setCurrentItem(1);

                     //   Log.d("ProfileData",""+obj.toString());
                        saveDataInPref();
                        Intent intent = new Intent(getApplicationContext(), Home.class);
                        startActivity(intent);

                    } else {
                        String msg = obj.getString("message");
                        Toast.makeText(getApplicationContext(), msg,
                                Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage().toString(),
                        Toast.LENGTH_LONG).show();

            }
        });
        //mViewPager.setCurrentItem(1);


    }

    private void saveDataInPref() {
       // Toast.makeText(getApplicationContext(),"In this saveDataInPref",Toast.LENGTH_SHORT).show();
        editor.putString("FIRST_NAME",fname.getText().toString());
        editor.putString("LAST_NAME",lname.getText().toString());
        editor.putString("Email_ID",emailId.getText().toString());
        editor.putString("Gender",gender_conf);
        editor.putString("Adhar_No",adharNo.getText().toString());
        editor.putString("Pan_NO",panNo.getText().toString());
        editor.putString("Address",address.getText().toString());
        editor.apply();
        editor.commit();
}

    private boolean inputValidation() {
        if (fname.getText().toString().isEmpty()) {
           fname.setError("First Name Can not be Empty");
            return false;
        } else if (lname.getText().toString().isEmpty()) {
            lname.setError("Last Name Can not be Empty");
            return false;
        }else if(!emailId.getText().toString().isEmpty()){
            if(emailValidator(emailId.getText().toString())==false) {
                emailId.setError("Enter correct emailId");
                return false;
            }
        }else if(!adharNo.getText().toString().isEmpty()){
               if (adharNo.length()<12) {
                   adharNo.setError("Enter Correct Adhar No");
                   return false;
               }
        }

        return true;
    }

    private boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();

    }

    private void initWidgets() {

        profile_Edit = findViewById(R.id.profile_Edit);
        update = findViewById(R.id.update);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        emailId = findViewById(R.id.emailId);
        contactNo = findViewById(R.id.contactNo);
        gender = findViewById(R.id.gender);
        adharNo = findViewById(R.id.adharNo);
        address = findViewById(R.id.address);
        panNo = findViewById(R.id.panNo);


        //     flag = getIntent().getStringExtra("flag");
        //    profile_View = findViewById(R.id.profile_View);

//        if (flag != null && flag.equalsIgnoreCase("profile_view")) {
//            profile_Edit.setVisibility(View.GONE);
//            profile_View.setVisibility(View.VISIBLE);
//        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,Home.class));
        finish();
    }
}
