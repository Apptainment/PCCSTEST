package com.sudhir.test.pccstest;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by pc on 3/29/2018.
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {


    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtCustomername;
        public TextView txtCustomeraddress;
        public TextView txtAppoinmentdetails;

        public ViewHolder(View v) {
            super(v);

            txtCustomername = (TextView) v.findViewById(R.id.textView_Customername);
            txtCustomeraddress = (TextView) v.findViewById(R.id.textView_Customeraddress);
            txtAppoinmentdetails = (TextView) v.findViewById(R.id.textView_Appoinmentdetails);
        }
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
