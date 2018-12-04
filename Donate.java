package com.codemaven.xadapp.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.codemaven.xadapp.R;
import com.codemaven.xadapp.Constant.Constant;
import com.codemaven.xadapp.api.VolleyJSONRequest;
import com.codemaven.xadapp.fragment.DonateMoney;
import com.codemaven.xadapp.fragment.SpaceForStorage;
import com.codemaven.xadapp.fragment.WorkAsVolunter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Donate extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    ProgressDialog progressDialog;

    static ArrayList<String> categoryName = new ArrayList<>();
    static ArrayList<String> centreName = new ArrayList<>();
    static ArrayList<String> categoirId = new ArrayList<>();
    static ArrayList<String> centreId = new ArrayList<>();

    public static SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_drawer);
//        categoryName.clear();
//        centreName.clear();

//        loadSpinnerData();
//        loadSpinnerData1();

        //-----------------------------
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

        //----------------------------
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        //-----ProgressBar Initialization
        progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.setMessage("Loading .......");
        progressDialog.setCanceledOnTouchOutside(false);

        sharedPreferences = this.getSharedPreferences("User_Info", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        if (sharedPreferences.getBoolean("fromSpaceForStorage", false)) {

            mViewPager.setCurrentItem(2);
            editor.putBoolean("fromSpaceForStorage", false);
            editor.commit();
        }
        if (sharedPreferences.getBoolean("isDelete", false)) {

            mViewPager.setCurrentItem(1);
            editor.putBoolean("isDelete", false);
            editor.commit();
        }
        if (sharedPreferences.getBoolean("isSubmit", false)) {

            mViewPager.setCurrentItem(1);
            editor.putBoolean("isSubmit", false);
            editor.commit();
        }


    }


    //----------ApiCalling and ProgressBAr----------------

  /*  private void loadSpinnerData1() {
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

                        }

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


            }
        });


    }*/

//    private void setUserName() {
//
//        //  user_name_appmenu.setText("");
//        textView.setText(sharedPreferences.getString("FIRST_NAME",""));
//    }

    /*private void loadSpinnerData() {
        String URL = Constant.BaseURL + "category.php";
        HashMap<String, String> params = new HashMap<>();
        params.put("category", "1");
        VolleyJSONRequest volleyJSONRequest = new VolleyJSONRequest(getApplicationContext(), URL, params);
        //  progressDialog.show();
        volleyJSONRequest.executeStringRequest(new VolleyJSONRequest.VolleyJSONRequestInterface() {
            @Override
            public void onSuccess(JSONObject obj) {

                try {
                    String status = obj.getString("status");
                    if (status.equalsIgnoreCase("success")) {
                        progressDialog.hide();
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


    }*/

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            Intent a = new Intent(this, Home.class);
            a.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(a);
            finish();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

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


    // --------------------------------------

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        Spinner category_spinner, nearest_center;
        ImageView deviceImagView;
        EditText deviceNameEditText, DeviceDescripEditText, remarkEditText;
        RadioGroup working_statusRG, radio_markRG;
        Button submit_button;
        private static final String ARG_SECTION_NUMBER = "section_number";
        String selection_item, facilation_item;
        Long id_device, id_facilation;
        int device_condition, mark_donate;
        private static int PICK_IMAGE_REQUEST = 1;
        String imageToupload;


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
        public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_assistive_device, container, false);
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            initWidgets(rootView);
            categoirId = Splash.categoirId;
            categoryName = Splash.categoryName;
            centreId = Splash.centreId;
            centreName = Splash.centreName;
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, categoryName);
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            category_spinner.setAdapter(arrayAdapter);
//            category_spinner.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    category_spinner.setAdapter(arrayAdapter);
//                }
//            }, 1500);
//
//            arrayAdapter.notifyDataSetChanged();

            category_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(
                        AdapterView<?> arg0, View view, int position, long id) {
                    id = Long.parseLong(categoirId.get(position));
                    selection_item = category_spinner.getItemAtPosition(position).toString();
                    id_device = id;
                    //         Toast.makeText(getContext(), "Spinner1: position=" + position + " id=" + id+"item :"+selection_item, Toast.LENGTH_SHORT).show();
                }

                public void onNothingSelected(AdapterView<?> parent) {
                    // showToast("Spinner1: unselected");
                }
            });

            final ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, centreName);
            arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            nearest_center.setAdapter(arrayAdapter1);

//
//            nearest_center.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    nearest_center.setAdapter(arrayAdapter1);
//                }
//            }, 1500);

            nearest_center.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> arg1, View view, int position, long id) {
                    id = Long.parseLong(centreId.get(position));
                    facilation_item = arg1.getItemAtPosition(position).toString();
                    id_facilation = id;

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            working_statusRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.working:
                            device_condition = 1;
                            break;
                        case R.id.not_working:
                            device_condition = 0;
                            break;
                    }
                }
            });

            radio_markRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    switch (checkedId) {
                        case R.id.yes:
                            mark_donate = 1;

                            break;
                        case R.id.no:
                            mark_donate = 0;

                            break;
                    }
                }
            });

            deviceImagView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setImageOfDevice();
                }
            });


            submit_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (inputValidate()) {

                        try {
                            saveDevice();
                            Intent intent = new Intent(getContext(), DonatedList.class);
                            startActivity(intent);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }
            });

            return rootView;
        }

        private void setImageOfDevice() {

            Intent pickImageIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageIntent.setType("image/*");
            pickImageIntent.putExtra("aspectX", 1);
            pickImageIntent.putExtra("aspectY", 1);
            pickImageIntent.putExtra("scale", true);
            pickImageIntent.putExtra("outputFormat",
                    Bitmap.CompressFormat.JPEG.toString());
            startActivityForResult(pickImageIntent, PICK_IMAGE_REQUEST);
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
                Uri filePath = data.getData();

            //    Log.e("Imagepath","image--"+filePath+"for check---------"+filePath.toString());
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                    Bitmap lastBitmap = null;
                    lastBitmap = bitmap;
                    deviceImagView.setImageBitmap(lastBitmap);
                    imageToupload = getStringImage(lastBitmap);
                    // Log.e("image_decode", imageToupload);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private String getStringImage(Bitmap lastBitmap) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            lastBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
            return encodedImage;
        }

        private void saveDevice() throws JSONException {
            String url = Constant.BaseURL + "devices.php";
            HashMap<String, String> parms = new HashMap<>();
            parms.put("user_id", sharedPreferences.getString("user_id", ""));
            parms.put("add", "1");
            JSONObject obj_data = new JSONObject();
            obj_data.put("category_id", id_device);
            // Long i=id_device;
            obj_data.put("donation_center_id", id_facilation);
            // Long j=id_device;
            obj_data.put("device_name", deviceNameEditText.getText().toString());
            obj_data.put("remarks", remarkEditText.getText().toString());
            obj_data.put("working_status", device_condition);
            obj_data.put("mark_donate", mark_donate);
            obj_data.put("description", DeviceDescripEditText.getText().toString());
            JSONArray jsonArray = new JSONArray();
            jsonArray.put("data:image/png;base64," + imageToupload);
            obj_data.put("images", jsonArray);
            //   Log.e("ImgJsonArray",""+jsonArray);
            parms.put("responsedata", obj_data.toString().replace("\\", ""));

            VolleyJSONRequest volleyJSONRequest = new VolleyJSONRequest(getContext(), url, parms);
            volleyJSONRequest.executeStringRequest(new VolleyJSONRequest.VolleyJSONRequestInterface() {

                @Override
                public void onSuccess(JSONObject obj) {

                    //----------------------------------------------------------------------------------
                    try {
                        String status = obj.getString("status");
                        if (status.equalsIgnoreCase("success")) {

//                            Intent intent = new Intent(getContext(), DonatedList.class);
//                            startActivity(intent);

                        } else {
                            String msg = obj.getString("message");
                            Toast.makeText(getContext(), msg,
                                    Toast.LENGTH_LONG).show();
                            // progressDialog.hide();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //--------------------------------------------------------------------------------------------------
                }

                @Override
                public void onFailure(VolleyError error) {
//                    Toast.makeText(getContext(), error.getMessage().toString(),
//                            Toast.LENGTH_LONG).show();
                }
            });

//            volleyJSONRequest.executeStringRequest(new VolleyJSONRequest.VolleyJSONRequestInterface() {
//
//                @Override
//                public void onSuccess(JSONObject obj) {
//                }
//
//                @Override
//                public void onFailure(VolleyError error) {
//                    Toast.makeText(getContext(), error.getMessage().toString(),
//                            Toast.LENGTH_LONG).show();
//                }
//            });
        }

        private void initWidgets(View rootView) {
            device_condition = 1;
            mark_donate = 1;
            category_spinner = rootView.findViewById(R.id.category_spinner);

            nearest_center = rootView.findViewById(R.id.nearest_center);

            deviceImagView = rootView.findViewById(R.id.imv);

            deviceNameEditText = rootView.findViewById(R.id.device_name);
            DeviceDescripEditText = rootView.findViewById(R.id.message);
            remarkEditText = rootView.findViewById(R.id.remark);

            working_statusRG = rootView.findViewById(R.id.working_status);
            radio_markRG = rootView.findViewById(R.id.radio_mark);

            submit_button = rootView.findViewById(R.id.button);
        }

        private boolean inputValidate() {
            if (deviceNameEditText.getText().toString().isEmpty()) {
                deviceNameEditText.setError("Enter device name ");
                return false;
            } else if (DeviceDescripEditText.getText().toString().isEmpty()) {
                DeviceDescripEditText.setError("Enter device description ");
                return false;
            } else if (working_statusRG.getCheckedRadioButtonId() == -1) {
//                Toast.makeText(getContext(), "Please Select one working status of device ",
//                        Toast.LENGTH_SHORT).show();
                return false;
            } else if (radio_markRG.getCheckedRadioButtonId() == -1) {
//                Toast.makeText(getContext(), "Please Select one mark ",
//                        Toast.LENGTH_SHORT).show();
                return false;
            } else if (selection_item == null) {
//                Toast.makeText(getContext(), "Please Select device category : ",
//                        Toast.LENGTH_SHORT).show();
                return false;
            } else if (facilation_item == null) {
//                Toast.makeText(getContext(), "Please Select facilation center : ",
//                        Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
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
            switch ((position)) {
                case 0:
                    return PlaceholderFragment.newInstance(position + 1);
                case 1:
                    WorkAsVolunter wrkasv = new WorkAsVolunter();
                    return wrkasv;

                case 2:
                    SpaceForStorage spaceForStorage = new SpaceForStorage();
                    return spaceForStorage;
                case 3:
                    DonateMoney donateMoney = new DonateMoney();
                    return donateMoney;

                default:
                    return PlaceholderFragment.newInstance(position + 1);
            }

        }

        @Override
        public int getCount() {
            return 4;
        }
    }
}
