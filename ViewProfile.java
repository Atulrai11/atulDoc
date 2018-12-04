package com.codemaven.xadapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.codemaven.xadapp.Constant.Constant;
import com.codemaven.xadapp.R;
import com.codemaven.xadapp.api.VolleyJSONRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ViewProfile extends AppCompatActivity {
    TextView fname, lname, emailId, contactNo, adharNo, address, panNo, gender;
    Button update;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    static ProgressDialog progressDialog;
    String first_name,last_name,email,mobile,genders,adhar_no,pan_no,addres;
    //String first_name,last_name,email,mobile,gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
      //  Log.d("InViewProdile","hiiiiiiiiii");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        toolbar.setTitle("View Profile");
        toolbar.setNavigationIcon(R.drawable.arrow);




        sharedPreferences = this.getSharedPreferences("User_Info", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        initWidgets();
//Log.d("InViewProdile","hiiiiiiiiii11111111");
        profileViewApi();

   //     setDataInTV();



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(),UserProfile.class);
                startActivity(intent);
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // ViewProfile.super.onBackPressed();
                startActivity(new Intent(ViewProfile.this,Home.class));
                finish();
            }
        });
    }

    private void profileViewApi() {
        String url= Constant.profileURL;
        HashMap<String, String> params = new HashMap<>();

        params.put("profile","1");
        params.put("user_id",sharedPreferences.getString("user_id",""));
        String s=sharedPreferences.getString("user_id","");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Geting User Profile....");
        progressDialog.setCanceledOnTouchOutside(false);

        VolleyJSONRequest volleyJSONRequest = new VolleyJSONRequest(ViewProfile.this,url,params);
        progressDialog.show();
        volleyJSONRequest.executeStringRequest(new VolleyJSONRequest.VolleyJSONRequestInterface() {
            @Override
            public void onSuccess(JSONObject obj) {
               progressDialog.hide();
                try {
                    String status=obj.getString("status");
                    if(status.equalsIgnoreCase("Success")){
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject profileObj = jsonArray.getJSONObject(i);
                             first_name=profileObj.getString("first_name");
                             last_name=profileObj.getString("last_name");
                             email=profileObj.getString("email");
                             mobile=profileObj.getString("mobile");
                             genders=profileObj.getString("gender");
                             adhar_no=profileObj.getString("adhar_no");
                             pan_no=profileObj.getString("pan_no");
                             addres=profileObj.getString("address");

                            editor.putString("FIRST_NAME",first_name);
                            editor.putString("Email",email);
                            editor.putString("LAST_NAME",last_name);
                            editor.putString("Gender",genders);
                            editor.putString("Adhar_No",adhar_no);
                            editor.putString("Pan_NO",pan_no);
                            editor.putString("Address",addres);
                            editor.commit();

                            setDataInTV();
                        }
                    }else {
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
                progressDialog.hide();


            }
        });

    }

    private void setDataInTV() {

          fname.setText(first_name);
        lname.setText(last_name);
        emailId.setText(email);
        contactNo.setText(mobile);
        String s=genders;
        if(genders.equals("0")) {
             gender.setText("Male");
         }else if(genders.equals("1")) {
             gender.setText("Female");
         }else{
             gender.setText("Others");
        }
        adharNo.setText(adhar_no);
        panNo.setText(pan_no);
        address.setText(addres);




    }

    private void initWidgets() {
      //  Log.d("InViewProdile","hi2IntWidgets");
        update = findViewById(R.id.update);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        emailId = findViewById(R.id.email);
        contactNo = findViewById(R.id.phoneNo);
        gender = findViewById(R.id.gender);
        adharNo = findViewById(R.id.adharNo);
        address = findViewById(R.id.address);
        panNo = findViewById(R.id.panNo);
      //  setDataInTV();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,Home.class));
        finish();
    }
}
