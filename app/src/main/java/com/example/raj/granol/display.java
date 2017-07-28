package com.example.raj.granol;

import android.*;
import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
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

public class display extends MainActivity {

    private RecyclerView recyclerView;

    //adapter object
    private RecyclerView.Adapter adapter;

    //database reference
    private DatabaseReference mDatabase;

    Button download;
    String url,name,N;
    Upload u;
    //progress dialog
    private ProgressDialog progressDialog;

    //list to hold all the uploaded images
    private List<Upload> uploads;
    int flag;

    String link ;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.display);
            Intent intent = getIntent();
            flag=0;
            String selection = intent.getExtras().getString("selected");
            if(selection.equals("Sparkle Series")){
                url="https://firebasestorage.googleapis.com/v0/b/granoland-ee8e9.appspot.com/o/GL_Double%20Charge.pdf?alt=media&token=2a2ddc01-15c8-4806-826f-4b5e5cc30175";
                name="GL_Double Charge.pdf";
                N="GranoLand";
            }else{
                url="https://firebasestorage.googleapis.com/v0/b/granoland-ee8e9.appspot.com/o/LG_Double%20Charge.pdf?alt=media&token=d681ccab-7add-4646-b0da-230995bb54c9";
                name="LG_Double Charge.pdf";
                N="LandGrace";
            }

            File pdfFile = new File(Environment.getExternalStorageDirectory() + "/" + N + "/" + name);  // -> filename = maven.pdf
            if(pdfFile.exists()) {
                Uri path = Uri.fromFile(pdfFile);
                Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
                pdfIntent.setDataAndType(path, "application/pdf");
                pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                flag=1;
                try {
                    Toast.makeText(display.this, "Opening PDF", Toast.LENGTH_SHORT).show();
                    startActivity(pdfIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(display.this, "No Application available to view PDF", Toast.LENGTH_SHORT).show();
                }
            }else {
                //Toast.makeText(display.this, "File Not Downloaded", Toast.LENGTH_SHORT).show();
            }
                download = (Button) findViewById(R.id.download);
                download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(display.this, "Downloading started in Foreground", Toast.LENGTH_SHORT).show();
                        haveSPermission();
                    /*DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.setTitle(N);
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name);
                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    manager.enqueue(request);*/
                    }
                });

                if (selection.equals("Sparkle Series") || selection.equals("Natural Series")) {
                    recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(this));




                    uploads = new ArrayList<>();

                    //displaying progress dialog while fetching images
                    if(flag==0) {
                        progressDialog = new ProgressDialog(this);
                        progressDialog.setMessage("Please wait...");
                        progressDialog.show();
                    }
                    mDatabase = FirebaseDatabase.getInstance().getReference().child(selection);


                    //adding an event listener to fetch values
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            //dismissing the progress dialog
                            if(flag==0){
                                progressDialog.dismiss();
                            }


                            //iterating through all the values in database
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                link = postSnapshot.getValue().toString();
                                Upload upload = new Upload();
                                upload.name = postSnapshot.getKey().toString();
                                upload.url = link;
                                uploads.add(upload);
                            }
                            //creating adapter
                            adapter = new myAdapter(getApplicationContext(), uploads);

                            //adding adapter to recyclerview
                            recyclerView.setAdapter(adapter);
                            //adapter.setClickListener(this); not working


                            recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                                    recyclerView, new ClickListener() {
                                @Override
                                public void onClick(View view, int position) {
                                    Log.e("Clicked & Position is ", String.valueOf(position));
                                    u = uploads.get(position);
                                    havePermission(u.getUrl());
                                }

                                @Override
                                public void onLongClick(View view, int position) {

                                }
                            }));
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            progressDialog.dismiss();
                        }
                    });
                } else {
                    //offline 10mm 14mm 16mm 20mm
                }
            Toolbar toolbaren = (Toolbar) findViewById(R.id.toolbardisp);
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

                Intent i=new Intent(display.this,Collection.class);
                startActivity(i);
                finish();
                return true;

        }

        @Override
        public void onBackPressed() {
            super.startActivity(new Intent(display.this,Collection.class));
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



    public  boolean havePermission(String url) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission error","You have permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return true;
            } else {

                Log.e("Permission error","You have asked for permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
                return false;
            }
        }
        else { //you dont need to worry about these stuff below api level 23
            Log.e("Permission error","You already have the permission");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            return true;
        }
    }
    public  boolean haveSPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission error","You have permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
                return true;
            } else {

                Log.e("Permission error","You have asked for permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
                return false;
            }
        }
        else { //you dont need to worry about these stuff below api level 23
            Log.e("Permission error","You already have the permission");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 3);
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            switch (requestCode) {
                case 2:
                    Log.e("Downloading from URL:", u.getUrl());
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(u.getUrl()));
                    request.setTitle(N);
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "Design.jpg");
                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    manager.enqueue(request);
                    break;
                case 3:
                    String fileUrl = url;  // -> http://maven.apache.org/maven-1.x/maven.pdf
                    String fileName = name;  // -> maven.pdf
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
