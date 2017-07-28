package com.example.raj.granol;

/**
 * Created by raj on 13/5/17.
 */

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import android.*;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.FirebaseApp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by raj on 8/5/17.
 */

public class digital extends MainActivity {

    private RecyclerView recyclerView;

    //adapter object
    private RecyclerView.Adapter adapter;

    //database reference
    private DatabaseReference mDatabase;

    //progress dialog
    private ProgressDialog progressDialog;

    //list to hold all the uploaded images

    Button digital_D;


    String link,url ;
    String N="LandGrace",name="LG_Digital Parking.pdf";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.digital);

        Intent intent = getIntent();
        String selection = intent.getExtras().getString("selected");

        digital_D = (Button) findViewById(R.id.digdownload);
        digital_D.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "https://firebasestorage.googleapis.com/v0/b/granoland-ee8e9.appspot.com/o/LG_Digital%20Parking.pdf?alt=media&token=51b87f30-05f7-4303-be89-1eeb0b4d8fcb";
                haveStPermission();
                /*DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                request.setTitle("LandGrace");
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "LG_Digital Parking.pdf");
                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);*/
            }
        });

        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/LandGrace/" + "LG_Digital Parking.pdf");  // -> filename = maven.pdf
        if(pdfFile.exists()) {
            Uri path = Uri.fromFile(pdfFile);
            Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
            pdfIntent.setDataAndType(path, "application/pdf");
            pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            try {
                Toast.makeText(digital.this, "Opening PDF", Toast.LENGTH_SHORT).show();
                startActivity(pdfIntent);
                finish();
            } catch (ActivityNotFoundException e) {
                Toast.makeText(digital.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
            }
        }else {
            //Toast.makeText(digital.this, "File Not Downloaded", Toast.LENGTH_SHORT).show();

            url = "https://firebasestorage.googleapis.com/v0/b/granoland-ee8e9.appspot.com/o/LG_Digital%20Parking.pdf?alt=media&token=51b87f30-05f7-4303-be89-1eeb0b4d8fcb";
            haveStPermission();



            //displaying progress dialog while fetching images

            WebView mWebView = (WebView) findViewById(R.id.digital);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setLoadWithOverviewMode(true);
            mWebView.getSettings().setUseWideViewPort(true);
            mWebView.loadUrl("https://docs.google.com/gview?embedded=true&url=https://firebasestorage.googleapis.com/v0/b/granoland-ee8e9.appspot.com/o/LG_Digital%20Parking.pdf?alt=media&token=51b87f30-05f7-4303-be89-1eeb0b4d8fcb");
        }
            //adding an event listener to fetch values

        Toolbar toolbaren = (Toolbar) findViewById(R.id.toolbardigital);
        setSupportActionBar(toolbaren);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle arrow click here
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

            Intent i=new Intent(digital.this,Collection.class);
            startActivity(i);
            finish();
            return true;

    }

    @Override
    public void onBackPressed() {
        super.startActivity(new Intent(digital.this,Collection.class));
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

    public  boolean haveStPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission error","You have permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 4);
                return true;
            } else {

                Log.e("Permission error","You have asked for permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 4);
                return false;
            }
        }
        else { //you dont need to worry about these stuff below api level 23
            Log.e("Permission error","You already have the permission");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 4);
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {
                case 4:
                    String extStorageDirectory = Environment.getExternalStorageDirectory().toString();

                    File folder = new File(extStorageDirectory, N);
                    if(!folder.exists()) {
                        folder.mkdir();
                    }
                    Log.e("Downloading to:", folder.getAbsolutePath());
                    Log.e("Previous Download to:", folder.getPath());
                    Log.e("Down:",Environment.getExternalStorageDirectory().toString()+"/"+N );
                    DownloadManager.Request request1 = new DownloadManager.Request(Uri.parse(url));
                    request1.setTitle(N);
                    request1.allowScanningByMediaScanner();
                    request1.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                    request1.setDestinationInExternalPublicDir("/"+N, name);
                    DownloadManager manager1 = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    manager1.enqueue(request1);
                    break;
            }
            //you have the permission now.

        }
    }


}

