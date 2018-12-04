package com.codemaven.xadapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.codemaven.xadapp.Model.RequestModel;
import com.codemaven.xadapp.R;
import com.codemaven.xadapp.activity.Prescription;

import java.util.ArrayList;

public class RequestListAdapter extends RecyclerView.Adapter<RequestListAdapter.ViewHolder> {
    Context context;
    //  String[] listDevises = {"Device1", "Devices2", "Devices3"};
    private ArrayList<RequestModel> requestList;


    public RequestListAdapter(Context context, ArrayList<RequestModel> requestList) {
        this.context = context;
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.response_list_card_view, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final RequestModel requestModel=requestList.get(position);
        holder.title_tv.setText(requestModel.getCategory_name());
        holder.title_tv1.setText(requestModel.getAdded_at());
        holder.title_tv2.setText(requestModel.getPatient_condition());
        holder.details_tv.setText(requestModel.getDescription());
        holder.status_tv.setText(requestModel.getStatus());
        holder.desp_tv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, Prescription.class);
                intent.putExtra("imvUrl",requestModel.getPhoto());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(requestList !=null){
            return requestList.size();
        }else
        {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView title_tv, details_tv, status_tv,title_tv1,title_tv2,desp_tv2;


        public ViewHolder(View itemView) {
            super(itemView);
            title_tv = itemView.findViewById(R.id.title_tv);
            title_tv1 = itemView.findViewById(R.id.title_tv1);
            title_tv2 = itemView.findViewById(R.id.title_tv2);
            details_tv = itemView.findViewById(R.id.details_tv);
            status_tv = itemView.findViewById(R.id.status_tv);
            desp_tv2=itemView.findViewById(R.id.desp_tv2);

        }
    }
}

