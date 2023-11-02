package com.example.chatz.Adapters;

import android.content.Context;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatz.Models.messagemodel;
import com.example.chatz.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class chatadapter extends RecyclerView.Adapter{

    ArrayList<messagemodel> messagemodels;
    Context context;

    int  senderviewtype = 1;
    int reciverviewtype = 2;


    public chatadapter(ArrayList<messagemodel> messagemodels, Context context) {
        this.messagemodels = messagemodels;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType  == senderviewtype)
        {
            View view  = LayoutInflater.from(context).inflate(R.layout.samplesender,parent,false);
            return new SenderViewHolder(view);
        }
        else{
            View view  = LayoutInflater.from(context).inflate(R.layout.samplereciver,parent,false);
            return new ReciverViewHolder(view);
        }
    }
    @Override
    public int getItemViewType(int position) {
        if(messagemodels.get(position).getUid().equals(FirebaseAuth.getInstance().getUid()))
        {
            return senderviewtype;
        }
        else
        {
            return reciverviewtype;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        messagemodel messagemodel = messagemodels.get(position);

        long timestamp = messagemodel.getTimestamp();
        Instant instant = Instant.ofEpochMilli(timestamp);
        ZonedDateTime localDateTime = instant.atZone(ZoneId.systemDefault());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        String formattedTime = localDateTime.format(formatter);
        String s = String.valueOf(formattedTime);


        if(holder.getClass()==SenderViewHolder.class) {
            ((SenderViewHolder)holder).sendertext.setText(messagemodel.getMessage());
            ((SenderViewHolder)holder).sendertime.setText((s));
        }
        else{
            ((ReciverViewHolder)holder).recivertext.setText(messagemodel.getMessage());
            ((ReciverViewHolder)holder).recivertime.setText((s));
        }
    }

    @Override
    public int getItemCount() {
        return messagemodels.size();
    }

    public class ReciverViewHolder extends RecyclerView.ViewHolder {

        TextView recivertext , recivertime;

        public ReciverViewHolder(@NonNull View itemView) {
            super(itemView);
            recivertext = itemView.findViewById(R.id.recivertext);
            recivertime = itemView.findViewById(R.id.recivertime);
        }
    }
    public class SenderViewHolder extends RecyclerView.ViewHolder{
        TextView sendertext , sendertime;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            sendertext = itemView.findViewById(R.id.sendertext);
            sendertime = itemView.findViewById(R.id.sendertime);
        }
    }

}
