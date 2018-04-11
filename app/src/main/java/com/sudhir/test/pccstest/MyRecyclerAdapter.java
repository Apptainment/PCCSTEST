package com.sudhir.test.pccstest;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by pc on 4/5/2018.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> implements View.OnClickListener {

    private ArrayList<AppointmentList> items;
    private int itemLayout;
    Context context;

    public MyRecyclerAdapter(ArrayList<AppointmentList> items, Context context) {
        this.items = items;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        AppointmentList item = items.get(position);
        holder.text_cutmoerdaetail.setText(item.getCustomerdetails());
        holder.text_customeraddress.setText(item.getCustomeraddress());
        holder.text_appointment.setText(item.getWarrantydetail());
        holder.img_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // items.get(position).
                String map = "http://maps.google.co.in/maps?q=" + items.get(position).customerPostcode;
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });
        holder.img_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + items.get(position).customerMobile));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        holder.itemView.setTag(item);
    }

    @Override public int getItemCount() {
        return items.size();
    }

    @Override
    public void onClick(View v) {

        if (R.id.imageButton_Map == v.getId()){

        }
        if (R.id.imageButton_Call == v.getId()){

        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text_cutmoerdaetail, text_customeraddress, text_appointment;
        public ImageButton img_map, img_call;

        public ViewHolder(View itemView) {
            super(itemView);
            text_cutmoerdaetail = (TextView) itemView.findViewById(R.id.textView_Customername);
            text_customeraddress = (TextView) itemView.findViewById(R.id.textView_Customeraddress);
            text_appointment = (TextView) itemView.findViewById(R.id.textView_Appoinmentdetails);

            img_map= (ImageButton)itemView.findViewById(R.id.imageButton_Map);
            img_call= (ImageButton)itemView.findViewById(R.id.imageButton_Call);
        }
    }
}
