package com.codemaven.xadapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.codemaven.xadapp.Constant.Constant;
import com.codemaven.xadapp.Model.DonateModel;
import com.codemaven.xadapp.R;
import com.codemaven.xadapp.adapter.ListAdapter;
import com.codemaven.xadapp.api.VolleyJSONRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DonatedList extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    //    public class DonatedList extends AppCompatActivity
//            implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    DonateModel donateModel;
    RecyclerView list_recycler_view;
    ListAdapter adapter;
    ArrayList<DonateModel> donateModelsList;
    TextView textView;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donated_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
//        textView=navigationView.getHeaderView(0).findViewById(R.id.textView);
//        setUserName();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading List");
        sharedPreferences = this.getSharedPreferences("User_Info", MODE_PRIVATE);
        editor = sharedPreferences.edit();


        //--------------------------
        String i= getIntent().getStringExtra("fromList");
        if("1".equals(i)){
            Intent it=new Intent(DonatedList.this,DonatedList.class);
            startActivity(it);
        }
        //-----------------------------
        Handler handler = new Handler();
        handler.postDelayed(
                new Runnable() {
                    public void run() {
                        AddedDeviceApi();
                    }
                }, 1000);

        setListAdapter();

    }
//    private void setUserName() {
//
//        //  user_name_appmenu.setText("");
//        textView.setText(sharedPreferences.getString("FIRST_NAME",""));
//    }

    private void AddedDeviceApi() {
        progressDialog.show();
        //  Toast.makeText(getApplicationContext(),"This in donate list AddedDeviceApi",Toast.LENGTH_SHORT).show();
        //   String url = "http://xadnew.quickbooksupport365.com/service/devices.php";
        String url = Constant.BaseURL + "devices.php";
        HashMap<String, String> parms = new HashMap<>();
        parms.put("user_id", sharedPreferences.getString("user_id", ""));  //user id
        String s = sharedPreferences.getString("user_id", "");
        parms.put("get_devices", "1");   //flag

        VolleyJSONRequest volleyJSONRequest = new VolleyJSONRequest(getApplicationContext(), url, parms);
        volleyJSONRequest.executeStringRequest(new VolleyJSONRequest.VolleyJSONRequestInterface() {
            @Override
            public void onSuccess(JSONObject obj) {
                try {
                    String status = obj.getString("status");
                    if (status.equalsIgnoreCase("success")) {
                        progressDialog.hide();
                        //         Toast.makeText(getApplicationContext(),"This in donate list AddedApiSuccess",Toast.LENGTH_LONG).show();
                        parseJson1(obj);
                    }else{
                        String msg=obj.getString("message");
                        progressDialog.hide();
                        //  Toast.makeText(getContext(),msg, Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(VolleyError error) {
                progressDialog.hide();
                //   Toast.makeText(getApplicationContext(),"This in donate list AddedApifail",Toast.LENGTH_LONG).show();

            }
        });
    }

    private void parseJson1(JSONObject obj) {
        //     donateModel = new DonateModel();
        try {
            JSONObject object = obj.getJSONObject("data");
         //   Log.e("ListValue", "" + object);

            int count = object.length();
            for (int i = 0; i < count; i++) {
                donateModel = new DonateModel();
                JSONObject listData = object.getJSONObject(String.valueOf(i));
                int cont = listData.length();
                //for (int k = 0; k < cont; k++) {
                //   donateModel.setId(listData.getString("id"));
                donateModel.setDeviceId(listData.getString("id"));
                donateModel.setId(listData.getString("user_id"));
                donateModel.setCategory_id(listData.getString("category_id"));
                donateModel.setDonation_center_id(listData.getString("donation_center_id"));
                donateModel.setDevice_name(listData.getString("device_name"));
                donateModel.setRemarks(listData.getString("remarks"));
                donateModel.setWorking_status(listData.getString("working_status"));
                donateModel.setContact_to(listData.getString("contact_to"));
                donateModel.setMark_donate(listData.getString("mark_donate"));
                donateModel.setPickup_request(listData.getString("pickup_request"));
                donateModel.setDescription(listData.getString("description"));
                donateModel.setIs_assigned(listData.getString("is_assigned"));
                donateModel.setAdded_at(listData.getString("added_at"));
                donateModel.setCategory_name(listData.getString("category_name"));
                donateModel.setDonation_center_name(listData.getString("donation_center_name"));
                donateModel.setImages(listData.getString("images"));

                donateModelsList.add(donateModel);
                // adapter.notifyDataSetChanged();
               // Log.d("Rai", "data : " + donateModelsList.toString());
            }
            //         donateModelsList.add(donateModel);
            adapter.notifyDataSetChanged();
           // Log.d("Rai1", "data Count : " + donateModelsList.size());
            //}

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void setListAdapter() {
        donateModelsList = new ArrayList<>();
        //  ArrayList<DonateModel> do=initddm();
        list_recycler_view = findViewById(R.id.list_recycler_view);
        //-------------------
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        list_recycler_view.setLayoutManager(mLayoutManager);
        //--------------------------------
        adapter = new ListAdapter(this, donateModelsList);
        // list_recycler_view.addItemDecoration(new SpacesItemDecoration(15));
      //  Log.e("ValueOfModleList", "" + donateModelsList);
        list_recycler_view.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //==============================================================OldCode=============================


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Intent a = new Intent(this, Donate.class);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
            finish();
            // super.onBackPressed();
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
