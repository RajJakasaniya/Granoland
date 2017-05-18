package com.example.raj.granol;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by raj on 3/4/17.
 */

public class Enquiry extends MainActivity {
    EditText name,email,city,state,contact,description;
    Spinner country;
    Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.enquiry);
        name=(EditText)findViewById(R.id.name);
        email=(EditText)findViewById(R.id.email);
        city=(EditText)findViewById(R.id.city);
        state=(EditText)findViewById(R.id.state);
        contact=(EditText)findViewById(R.id.contact);
        description=(EditText)findViewById(R.id.description);
        country=(Spinner)findViewById(R.id.country);
        submit=(Button)findViewById(R.id.sub);
        String[] some_array = getResources().getStringArray(R.array.countries_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Enquiry.this,
                android.R.layout.simple_spinner_item,some_array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        country.setAdapter(adapter);
        country.setSelection(98);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( checkValidation () ) {
                    if(checkInternet()){
                        String em = "sales@granoland.com";/* Your email address here */
                        String subject = "Enquiry Form";/* Your subject here */
                        String[] CC = {"ashok@granoland.com","jaydeep@granoland.com"};
                        String body = "Name :- "+name.getText().toString() + "\n" + "Email id :- "+ email.getText().toString() + "\n" +
                                "Country :- "+country.getSelectedItem().toString() + "\n" +
                                "State :- "+state.getText().toString() + "\n" +
                                "City :- "+city.getText().toString() + "\n" +
                                "Order Details :- "+description.getText().toString() + "\n";/* Your body here */
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + em));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                        emailIntent.putExtra(Intent.EXTRA_CC, CC);
                        emailIntent.putExtra(Intent.EXTRA_TEXT, body);
                        try {
                            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                            finish();
                        } catch (android.content.ActivityNotFoundException ex) {
                            Toast.makeText(Enquiry.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(Enquiry.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(Enquiry.this, "Form contains error", Toast.LENGTH_LONG).show();
                }
            }
        });

        Toolbar toolbaren = (Toolbar) findViewById(R.id.toolbarcalenq);
        setSupportActionBar(toolbaren);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }
    private boolean isValidMail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private boolean isValidMobile(String number) {
        String regexStr = "^[+]?[0-9]{10,13}$";

        if(number.length()<10 || number.length()>13 || number.matches(regexStr)==false  ) {
            return false;
        }
        return true;
    }
    private boolean checkValidation() {
        boolean ret = true;

        if (!isValidMail(String.valueOf(email.getText().toString()))){
            Toast.makeText(Enquiry.this, "Please Enter a Valid Email Address", Toast.LENGTH_LONG).show();
            ret = false;
        }
        if (!isValidMobile(String.valueOf(contact.getText().toString()))){
            Toast.makeText(Enquiry.this, "Please Enter a Valid Contact Number", Toast.LENGTH_LONG).show();
            ret = false;
        }
        if(city.getText().toString()=="" || state.getText().toString()=="" || description.getText().toString()=="" ){
            Toast.makeText(Enquiry.this, "Please Fill up all the Details", Toast.LENGTH_LONG).show();
            return false;
        }
        return ret;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id != R.id.action_settings) {
            Intent i=new Intent(Enquiry.this,MainActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.startActivity(new Intent(Enquiry.this,MainActivity.class));
        finish();
    }

    public boolean checkInternet(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else {
            connected = false;
        }
        return connected;
    }
}
