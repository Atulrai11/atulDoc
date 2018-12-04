package com.codemaven.xadapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.codemaven.xadapp.Constant.Constant;
import com.codemaven.xadapp.R;
import com.codemaven.xadapp.api.VolleyJSONRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


public class Splash extends AppCompatActivity {

    private static final int SPLASH_TIMEOUT = 3000;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public static ArrayList<String> categoryName = new ArrayList<>();
    public static ArrayList<String> centreName = new ArrayList<>();
    public static ArrayList<String> categoirId = new ArrayList<>();
    public static ArrayList<String> centreId = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        sharedPreferences=this.getSharedPreferences("User_Info",MODE_PRIVATE);
        editor=sharedPreferences.edit();

        categoirId.clear();
        categoryName.clear();
        centreId.clear();
        centreName.clear();



        loadSpinnerData();
        loadSpinnerData1();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(sharedPreferences.getBoolean("FIRST_LOGIN",false)){
                    Intent intent = new Intent(Splash.this, Home.class);
                    startActivity(intent);
                    finish();

                }else {
                    Intent intent = new Intent(Splash.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_TIMEOUT);
    }


    private void loadSpinnerData() {
        String URL = Constant.BaseURL + "category.php";
        HashMap<String, String> params = new HashMap<>();
        params.put("category", "1");
        VolleyJSONRequest volleyJSONRequest = new VolleyJSONRequest(getApplicationContext(), URL, params);
        volleyJSONRequest.executeStringRequest(new VolleyJSONRequest.VolleyJSONRequestInterface() {
            @Override
            public void onSuccess(JSONObject obj) {

                try {
                    String status = obj.getString("status");
                    if (status.equalsIgnoreCase("success")) {

                        JSONObject values = obj.getJSONObject("data");
                        int count = values.length();
                        for (int i = 0; i < count - 1; i++) {
                            JSONObject dxdd = values.getJSONObject(String.valueOf(i));
                            String cat_id = dxdd.getString("id");
                            String cat_name = dxdd.getString("cat_name");
                            categoryName.add(cat_name);
                            categoirId.add(cat_id);
                            int dgcc = categoryName.size();
                            //  Log.d("ghsdj", String.valueOf(dgcc));
                          //  Log.d("categoryValue","category id---"+categoryName+"category name"+cat_id);
                        }


                    } else {
                        String msg = obj.getString("message");
                        // progressDialog.hide();
                        //  Toast.makeText(getApplicationContext(), msg,
                        //    Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(VolleyError error) {
                // progressDialog.hide();
            }
        });


    }
    //------------------------------------------------------------------
    private void loadSpinnerData1() {
        String URL = Constant.BaseURL + "donationCenter.php";
        HashMap<String, String> params1 = new HashMap<>();
        params1.put("center", "1");
        VolleyJSONRequest volleyJSONRequest = new VolleyJSONRequest(getApplicationContext(), URL, params1);
        volleyJSONRequest.executeStringRequest(new VolleyJSONRequest.VolleyJSONRequestInterface() {
            @Override
            public void onSuccess(JSONObject obj) {

                try {
                    String status = obj.getString("status");
                    if (status.equalsIgnoreCase("success")) {
                        JSONArray jsonArray = obj.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                            String center_id = jsonObject1.getString("id");
                            String country = jsonObject1.getString("center_name");

                            centreId.add(center_id);
                            centreName.add(country);
                           // Log.d("centerValue","center id---"+center_id+"center name"+country);

                        }

                    } else {
                        String msg = obj.getString("message");
//                        Toast.makeText(getApplicationContext(), msg,
//                                Toast.LENGTH_LONG).show();

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


}
