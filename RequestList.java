package com.codemaven.xadapp.activity;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.codemaven.xadapp.Constant.Constant;
import com.codemaven.xadapp.Model.RequestModel;
import com.codemaven.xadapp.R;
import com.codemaven.xadapp.adapter.RequestListAdapter;
import com.codemaven.xadapp.api.VolleyJSONRequest;
import com.codemaven.xadapp.helper.SpacesItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RequestList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    RecyclerView requestlist_recycler_view;
    RequestListAdapter rladapter;
    RequestModel requestModel;
    ArrayList<RequestModel> requestModelsList;
    ProgressDialog progressDialog;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        sharedPreferences = this.getSharedPreferences("User_Info", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//        textView=navigationView.getHeaderView(0).findViewById(R.id.textView);
//        setUserName();
        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading List");
        sharedPreferences = this.getSharedPreferences("User_Info", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Handler handler = new Handler();
        handler.postDelayed(
                new Runnable() {
                    public void run() {
                        RequestDeviceApi();
                    }
                }, 1000);

        //  RequestDeviceApi();
        setListAdapter();
    }

//    private void setUserName() {
//
//        //  user_name_appmenu.setText("");
//        textView.setText(sharedPreferences.getString("FIRST_NAME",""));
//    }

    private void RequestDeviceApi() {
        progressDialog.show();
        //     String url = "http://xadnew.quickbooksupport365.com/service/request.php";
        String url = Constant.BaseURL + "request.php";
        HashMap<String, String> parms = new HashMap<>();
        parms.put("user_id", sharedPreferences.getString("user_id", ""));  //user id
        parms.put("request_list", "1");   //flag

        VolleyJSONRequest volleyJSONRequest = new VolleyJSONRequest(getApplicationContext(), url, parms);
        volleyJSONRequest.executeStringRequest(new VolleyJSONRequest.VolleyJSONRequestInterface() {
            @Override
            public void onSuccess(JSONObject obj) {
                try {
                    String status = obj.getString("status");
                    if (status.equalsIgnoreCase("success")) {
                        progressDialog.hide();
                        //         Toast.makeText(getApplicationContext(),"This in donate list AddedApiSuccess",Toast.LENGTH_LONG).show();
                        parseData(obj);
                    }else{
                        String msg=obj.getString("message");
                        progressDialog.hide();
                      //  Toast.makeText(getContext(),msg, Toast.LENGTH_LONG).show();

                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(VolleyError error) {
                progressDialog.hide();
                //     Toast.makeText(getApplicationContext(),"This in donate list AddedApifail",Toast.LENGTH_LONG).show();

            }
        });
    }

    private void parseData(JSONObject obj) {
        //     donateModel = new DonateModel();
        try {
            JSONObject object = obj.getJSONObject("data");
            int count = object.length();
            for (int i = 0; i < count; i++) {
                requestModel = new RequestModel();
                JSONObject listData = object.getJSONObject(String.valueOf(i));
                int cont = listData.length();
                //for (int k = 0; k < cont; k++) {
                //   donateModel.setId(listData.getString("id"));
                requestModel.setId(listData.getString("id"));
                requestModel.setCatagory(listData.getString("catagory"));
                requestModel.setCenter(listData.getString("center"));
                requestModel.setDevice_name(listData.getString("device_name"));
                requestModel.setPatient_condition(listData.getString("patient_condition"));
                requestModel.setDescription(listData.getString("description"));
                requestModel.setStatus(listData.getString("status"));
                requestModel.setAssigned_device_id(listData.getString("assigned_device_id"));
                requestModel.setAdded_at(listData.getString("added_at"));
                //   requestModel.setAdded_by(listData.getString("added_by"));
                requestModel.setCategory_name(listData.getString("category_name"));
                requestModel.setDonation_center_name(listData.getString("donation_center_name"));
                requestModel.setPhoto(listData.getString("photo"));

                requestModelsList.add(requestModel);
                // rladapter.notifyDataSetChanged();
               // Log.d("Rai", "data : " + requestModelsList.toString());
            }
            //         donateModelsList.add(donateModel);
            rladapter.notifyDataSetChanged();
           // Log.d("Rai1", "data Count : " + requestModelsList.size());
            //}

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setListAdapter() {
        requestModelsList = new ArrayList<>();
        //  ArrayList<DonateModel> do=initddm();
        requestlist_recycler_view = findViewById(R.id.request_list_recycler_view);
        //-------------------
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        requestlist_recycler_view.setLayoutManager(mLayoutManager);
        //--------------------------------
        rladapter = new RequestListAdapter(this, requestModelsList);
        requestlist_recycler_view.addItemDecoration(new SpacesItemDecoration(15));
        requestlist_recycler_view.setAdapter(rladapter);
        rladapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent a = new Intent(this, Request.class);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.donated_list, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.my_profile) {
            Intent intent = new Intent(getApplicationContext(), ViewProfile.class);
            startActivity(intent);
        } else if (id == R.id.doner) {
            Intent intent = new Intent(getApplicationContext(), Donate.class);
            startActivity(intent);
        } else if (id == R.id.doner_view) {
            Intent intent = new Intent(getApplicationContext(), DonatedList.class);
            startActivity(intent);
        } else if (id == R.id.recive) {
            Intent intent = new Intent(getApplicationContext(), Request.class);
            startActivity(intent);
        } else if (id == R.id.nav_send) {
            Intent intent = new Intent(getApplicationContext(), RequestList.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
