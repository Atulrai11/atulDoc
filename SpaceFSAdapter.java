package com.codemaven.xadapp.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.codemaven.xadapp.Constant.Constant;
import com.codemaven.xadapp.Model.SForSModel;
import com.codemaven.xadapp.R;
import com.codemaven.xadapp.activity.Donate;
import com.codemaven.xadapp.activity.SpaceForStorageList;
import com.codemaven.xadapp.api.VolleyJSONRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SpaceFSAdapter extends RecyclerView.Adapter<SpaceFSAdapter.ViewHolder> {

    Context context;
    private ArrayList<SForSModel> sForSModels;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String listId;
    Boolean isFromAdapter;
    ProgressDialog progressDialog;
    Boolean fromSpaceForStorage;

    public SpaceFSAdapter(Context context, ArrayList<SForSModel> sForSModels) {
        this.context = context;
        this.sForSModels = sForSModels;
        sharedPreferences = context.getSharedPreferences("User_Info", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        isFromAdapter=false;
        fromSpaceForStorage=false;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.space_for_storage_cardview, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         final SForSModel sForSModel= sForSModels.get(position);
        holder.address_tv.setText(sForSModel.getAddress());
        holder.location_tv.setText(sForSModel.getLocation());
        holder.comments_tv.setText(sForSModel.getComments());
        holder.mobileNo_tv.setText(sForSModel.getMobile());
        listId=sForSModel.getId();
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setTitle("Please Confirm ");
                alertDialog.setMessage("Are you sure to delete ???? ");
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

                        locationChangeApi(listId);
                        notifyDataSetChanged();
                    }
                });

                AlertDialog dialog = alertDialog.create();
                dialog.show();
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFromAdapter=true;
                Intent intent=new Intent(context, SpaceForStorageList.class);
                intent.putExtra("address",sForSModel.getAddress());
                intent.putExtra("location",sForSModel.getLocation());
                intent.putExtra("comments",sForSModel.getComments());
                intent.putExtra("mobile",sForSModel.getMobile());
                intent.putExtra("flag","ok");
                context.startActivity(intent);
                 editor.putBoolean("Button",isFromAdapter);
                 editor.commit();
            }
        });

    }

    private void locationChangeApi(String listId) {

        String url = Constant.BaseURL + "space.php";
        HashMap<String, String> parms = new HashMap<>();
        parms.put("user_id", sharedPreferences.getString("user_id", ""));
        parms.put("delete", "1");
       // parms.put("donate_status", "1");
        parms.put("id", listId);
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
                        fromSpaceForStorage=true;
                        // Prescription.super.onBackPressed();
                        Intent intent=new Intent(context, Donate
                                .class);
                        editor.putBoolean("fromSpaceForStorage",fromSpaceForStorage);
                        editor.commit();
                        intent.putExtra("viewpager_position", 2);
                        context.startActivity(intent);
                        notifyDataSetChanged();
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
        if(sForSModels !=null){
            return sForSModels.size();
        }else
        {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView location_tv, address_tv, comments_tv, mobileNo_tv;
        CardView cardView;
        ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);
            location_tv = itemView.findViewById(R.id.location_tv);
            address_tv = itemView.findViewById(R.id.address_tv);
            comments_tv = itemView.findViewById(R.id.comments_tv);
            mobileNo_tv = itemView.findViewById(R.id.mobileNo_tv);
            cardView = itemView.findViewById(R.id.sfs_cardView);
            delete = itemView.findViewById(R.id.delete_imv);
        }
    }
}
