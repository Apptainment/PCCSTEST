package com.sudhir.test.pccstest;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ArrayList<AppointmentList> appointments;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    AppointmentDetailMain appointmentDetailMain;
    ActionBar actionBar;
    static EditText edt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        // use this setting to
        // improve performance if you know that changes
        // in content do not change the layout size
        // of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        actionBar.setTitle("PCCS TEST");
        String url = "https://www.skylinecms.co.uk/alpha/RemoteEngineerAPI/GetAppointmentDetails";

        new SendPostRequest().execute();
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {
        String date;
        protected void onPreExecute(){
            if (edt != null){
                date = edt.getText().toString();
                if (date.isEmpty()){
                    final Calendar c = Calendar.getInstance();
                    int year = c.get(Calendar.YEAR);
                    int month = c.get(Calendar.MONTH);
                    int day = c.get(Calendar.DAY_OF_MONTH);

                    date =day + "/" + (month + 1) + "/" + year;
                }
            }else {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                date =day + "/" + (month + 1) + "/" + year;
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected String doInBackground(String... arg0) {

            try{
            String address = "https://www.skylinecms.co.uk/alpha/RemoteEngineerAPI/GetAppointmentDetails";
                String body =
                        "<GetAppointmentDetails>" +
                        "<CurrentDate>"+date+"</CurrentDate>" +
                        "<SLPassword>andriodtest</SLPassword>" +
                        "<SLUsername>ON</SLUsername>" +

                        "</GetAppointmentDetails>";
            URL url = new URL(address);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setRequestProperty("Content-Type", "application/xml");
            OutputStream outputStream = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
            writer.write(body);
            writer.flush();
            writer.close();
            outputStream.close();

            InputStream inputStream;
            // get stream
            if (urlConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                inputStream = urlConnection.getInputStream();
            } else {
                inputStream = urlConnection.getErrorStream();
            }
            // parse stream
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String temp, response = "";
            while ((temp = bufferedReader.readLine()) != null) {
                response += temp;
            }
                XmlPullParserFactory pullParserFactory;
                try {
                    pullParserFactory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = pullParserFactory.newPullParser();

                    //InputStream in_s = getApplicationContext().getAssets().open("sample.xml");
                    InputStream stream = new ByteArrayInputStream(response.toString().getBytes(StandardCharsets.UTF_8));
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(new StringReader(response.toString()));
                    parseXML(parser);
                    String text="";

                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            return response.toString();

        } catch (IOException e) {
            return e.toString();
        }
    }

        @Override
        protected void onPostExecute(String result) {
            if (appointmentDetailMain.responsecode.equalsIgnoreCase("SC0001")){
                Toast.makeText(getApplicationContext(),"Refersh completed", Toast.LENGTH_LONG).show();
                recyclerView.setAdapter(new MyRecyclerAdapter(appointments, getApplicationContext()));
            }else if (appointmentDetailMain.responsecode.equalsIgnoreCase("SC0002") && appointmentDetailMain.responsedesc.equalsIgnoreCase("No Appointments Found")){
                Toast.makeText(getApplicationContext(),"Select different date", Toast.LENGTH_LONG).show();
            }else if (appointmentDetailMain.responsecode.equalsIgnoreCase("SC0002")){
                Toast.makeText(getApplicationContext(),appointmentDetailMain.responsedesc, Toast.LENGTH_LONG).show();
            }
            actionBar.setTitle(appointmentDetailMain.fullname);
        }
    }

    void parseXML(XmlPullParser parser) throws XmlPullParserException,IOException
    {
        appointments = null;

        appointmentDetailMain = new AppointmentDetailMain();
        int eventType = parser.getEventType();
        String customerdetails = "", customeraddress ="", warrantydetail="";
        String customerPostcode="", customerMobile="";
        while (eventType != XmlPullParser.END_DOCUMENT){
            String name = null;

            switch (eventType){
                case XmlPullParser.START_DOCUMENT:
                    appointments = new ArrayList();

                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if (name.equals("ResponseCode")){
                        appointmentDetailMain.responsecode= parser.nextText();
                    }
                   else if (name.equals("APIName")){
                        appointmentDetailMain.apiname= parser.nextText();
                    }
                    else if (name.equals("ResponseDescription")){
                        appointmentDetailMain.responsedesc= parser.nextText();
                    }
                    else if (name.equals("FullName")){
                        appointmentDetailMain.fullname= parser.nextText();
                    }
                   else if (name.equals("CustomerTitle") || name.equals("CustomerForename") || name.equals("CustomerSurname")){
                        customerdetails = customerdetails+" " + parser.nextText();
                    }
                    else if (name.equals("CustomerCompanyName") || name.equals("CustomerBuildingName")
                            || name.equals("CustomerStreet") || name.equals("CustomerAddressArea") || name.equals("CustomerAddressTown")
                            || name.equals("CustomerCounty")){
                        customeraddress = customeraddress + parser.nextText();
                    } else if (name.equals("CustomerPostcode")){
                        customerPostcode= parser.nextText();
                    }else if (name.equals("CustomerMobileNo")){
                        customerMobile= parser.nextText();
                    }else if (name.equals("ChargeType")){
                        warrantydetail= parser.nextText();
                        AppointmentList appointmentList = new AppointmentList(customerdetails, customeraddress, warrantydetail, customerPostcode, customerMobile);
                        appointments.add(appointmentList);
                        customerdetails = ""; customeraddress =""; warrantydetail="";customerPostcode=""; customerMobile="";
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                   /* if (name.equalsIgnoreCase("country") && country != null){
                        countries.add(country);
                    }*/
                    break;
            }
            eventType = parser.next();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.search:
                //your code here
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Appoinment Settings");
                // this is set the view from XML inside AlertDialog
                //alert.setView(R.layout.alert_settings);
                LayoutInflater inflater = this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.alert_settings, null);
                alert.setView(dialogView);
                edt = (EditText) dialogView.findViewById(R.id.editText3);
                edt.setInputType(InputType.TYPE_NULL);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(false);
                Calendar dateSelected = Calendar.getInstance();
                final DatePickerDialog datePickerDialog;
                edt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogFragment newFragment = new DatePickerFragment();
                        newFragment.show(getSupportFragmentManager(), "datePicker");
                    }
                });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      //  Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                    }
                });

                alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new SendPostRequest().execute();
                                           }
                });
                AlertDialog dialog = alert.create();
                dialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            //dialog.getDatePicker().setMaxDate(c.getTimeInMillis());
            return  dialog;
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            //btnDate.setText(ConverterDate.ConvertDate(year, month + 1, day));

            edt.setText(day + "/" + (month + 1) + "/"
                    + year);
        }
    }}

/*
<CustomerTitle>Mr</CustomerTitle>
<CustomerForename>Bish</CustomerForename>
<CustomerSurname>Bash</CustomerSurname>
<CustomerCompanyName/>
<CustomerBuildingName/>
<CustomerStreet>27 Plop House</CustomerStreet>
<CustomerAddressArea/>
<CustomerAddressTown>Plop Town</CustomerAddressTown>
<CustomerCounty/>
<CustomerPostcode>NN7 4TH</CustomerPostcode>
<CustomerMobileNo>07725748484</CustomerMobileNo>
<ChargeType>Chargeable</ChargeType>*/
