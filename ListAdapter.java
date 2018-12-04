package com.codemaven.xadapp.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.codemaven.xadapp.Constant.Constant;
import com.codemaven.xadapp.Model.DonateModel;
import com.codemaven.xadapp.R;
import com.codemaven.xadapp.activity.DonatedList;
import com.codemaven.xadapp.api.VolleyJSONRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    Context context;
    String[] listDevises = {"Device1", "Devices2", "Devices3"};
    private ArrayList<DonateModel> donateList;
    String deviceId;
    int flag = 0;
    ProgressDialog progressDialog;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public ListAdapter(Context context, ArrayList<DonateModel> donateList) {
        this.context = context;
        this.donateList = donateList;
        sharedPreferences = context.getSharedPreferences("User_Info", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.list_card_view, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        DonateModel donateModel = donateList.get(position);
        //  holder.title_tv.setText(listDevises[position]);
        holder.title_tv.setText(donateModel.getCategory_name());
        holder.details_tv.setText(donateModel.getAdded_at());
        //  Log.e("imageInlist",""+donateModel.getImages());

        Glide.with(context)
                .load(donateModel.getImages()) // image url
                .error(R.drawable.noimage)  // any image in case of error
                .override(200, 200) // resizing
                .centerCrop()
                .into(holder.image);

        Long i = Long.valueOf(donateModel.getWorking_status());
        if (i == 0) {
            holder.status_tv.setText("Not Working");
            holder.status_tv.setTextColor(Color.parseColor("#FF0000"));
        } else {
            holder.status_tv.setText("Working");
            holder.status_tv.setTextColor(Color.parseColor("#228b22"));
        }
        int j = Integer.parseInt(donateModel.getMark_donate());
        if (j == 0) {
            flag = 1;
            deviceId = donateModel.getDeviceId();
            holder.statusChange_btn.setText("Not Donated");
            holder.statusChange_btn.setBackgroundColor(Color.parseColor("#FF0000"));

        } else {
            holder.statusChange_btn.setText("Donated");
            holder.statusChange_btn.setBackgroundColor(Color.parseColor("#228b22"));
            holder.statusChange_btn.setClickable(false);
        }
        if (flag == 1) {
            holder.statusChange_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Please Confirm ");
                    alertDialog.setMessage("Are you sure to change device status ");
                    alertDialog.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // DO SOMETHING HERE
                            //    Toast.makeText(context, "Thanks for confirming", Toast.LENGTH_SHORT).show();
                            deviceStatusChangeApi(deviceId);
                            notifyDataSetChanged();
                        }
                    });

                    AlertDialog dialog = alertDialog.create();
                    dialog.show();
                }


            });
        } else {
            holder.statusChange_btn.setClickable(false);
        }

    }

    //Api------
    private void deviceStatusChangeApi(String deviceId) {
        String url = Constant.BaseURL + "devices.php";
        HashMap<String, String> parms = new HashMap<>();
        parms.put("user_id", sharedPreferences.getString("user_id", ""));
        parms.put("change_donate_status", "1");
        parms.put("donate_status", "1");
        parms.put("device_id", deviceId);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Processing......");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        VolleyJSONRequest volleyJSONRequest = new VolleyJSONRequest(context, url, parms);
        volleyJSONRequest.executeStringRequest(new VolleyJSONRequest.VolleyJSONRequestInterface() {
            @Override
            public void onSuccess(JSONObject obj) {
                try {
                    String status = obj.getString("status");

                    if (status.equalsIgnoreCase("success")) {
                        progressDialog.hide();

                           Intent intent=new Intent(context, DonatedList.class);
                           intent.putExtra("fromList","1");
                           context.startActivity(intent);

                        //notifyDataSetChanged();
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

    @Override
    public int getItemCount() {
        if (donateList != null) {
            return donateList.size();
        } else {
            return 0;
        }
        // return listDevises.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title_tv, details_tv, status_tv;
        ImageView image, del_iv;
        Button statusChange_btn;


        public ViewHolder(View itemView) {
            super(itemView);
            title_tv = itemView.findViewById(R.id.title_tv);
            details_tv = itemView.findViewById(R.id.details_tv);
            status_tv = itemView.findViewById(R.id.status_tv);
            image = itemView.findViewById(R.id.image);
            statusChange_btn = itemView.findViewById(R.id.statusChange_btn);
            // del_iv=itemView.findViewById(R.id.del_iv);

        }


    }
}
