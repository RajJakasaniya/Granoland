package com.example.raj.granol;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private ViewFlipper mViewFlipper;

    public NavigationView navigationView;
    private GestureDetector mGestureDetector;
    int[] resources = {
            R.drawable.slider,
           R.drawable.slider1,
            R.drawable.slider2,
            R.drawable.slider3
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstTime", false)) {
            // <---- run your one time code here
            Bitmap img = BitmapFactory.decodeResource(getResources(), R.drawable.hi);
            SaveImage(img);

            // mark first time has runned.
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }

        mViewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);

        // Add all the images to the ViewFlipper
        for (int i = 0; i < resources.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(resources[i]);
            //imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            mViewFlipper.addView(imageView);
        }
        CustomGestureDetector customGestureDetector = new CustomGestureDetector();
        mGestureDetector = new GestureDetector(this, customGestureDetector);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewFlipper.setAutoStart(true);
        mViewFlipper.setFlipInterval(5000);


        mViewFlipper.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeRight() {

                mViewFlipper.showNext();
            }
            public void onSwipeLeft() {
                mViewFlipper.showPrevious();
            }
        });


        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkInternet()){
                    String em = "sales@granoland.com";/* Your email address here */
                    //String subject = "Enquiry Form";/* Your subject here */
                    String[] CC = {"ashok@granoland.com","jaydeep@granoland.com"};
                    /*String body = "Name :- "+name.getText().toString() + "\n" + "Email id :- "+ email.getText().toString() + "\n" +
                            "Country :- "+country.getSelectedItem().toString() + "\n" +
                            "State :- "+state.getText().toString() + "\n" +
                            "City :- "+city.getText().toString() + "\n" +
                            "Order Details :- "+description.getText().toString() + "\n";*/
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + em));
                    //emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
                    emailIntent.putExtra(Intent.EXTRA_CC, CC);
                    //emailIntent.putExtra(Intent.EXTRA_TEXT, body);
                    try {
                        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                        finish();
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    class CustomGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

            // Swipe left (next)
            if (e1.getX() > e2.getX()) {
                mViewFlipper.showNext();
            }

            // Swipe right (previous)
            if (e1.getX() < e2.getX()) {
                mViewFlipper.showPrevious();
            }

            return super.onFling(e1, e2, velocityX, velocityY);
        }
    }
    public void exitAppMethod(){

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


    @Override
    public void onBackPressed() {
        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {*/
        exitAppMethod();

        //}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {

        } else if (id == R.id.collection) {

            Intent in=new Intent(MainActivity.this,Collection.class);
            startActivity(in);
        } else if (id == R.id.location) {

            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr=22.736256,70.978265"));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
        } else if (id == R.id.about) {

            Intent in=new Intent(MainActivity.this,About_us.class);
            startActivity(in);
        } else if (id == R.id.nav_share) {

            Uri pictureUri = Uri.parse("/storage/emulated/0/saved_images/image.jpg");
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Granoland Tiles LLP");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Tiling elegance redefined click here to visit https://xyz.com/ ");
            sharingIntent.putExtra(Intent.EXTRA_STREAM, pictureUri);
            sharingIntent.setType("image/jpg");
            sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

            /*try {
                Uri imageUri = null;
                try {
                    imageUri = Uri.parse(MediaStore.Images.Media.insertImage(MainActivity.this.getContentResolver(),
                            BitmapFactory.decodeResource(getResources(), R.drawable.hi), null, null));
                } catch (NullPointerException e) {
                }

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("**");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "your text");

                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);

                startActivity(shareIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(MainActivity.this, "no app fount", Toast.LENGTH_LONG).show();
            }*/

        //shareImageWhatsApp();

        } else if(id==R.id.contact) {

            Intent in=new Intent(MainActivity.this,Contact_us.class);
            startActivity(in);
        }else if(id==R.id.guide) {

            Intent in=new Intent(MainActivity.this,Guide.class);
            startActivity(in);
        }else if (id == R.id.rating) {
            Intent intent=new Intent(MainActivity.this,Enquiry.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        navigationView.getMenu().findItem(R.id.home).setChecked(true);
        return true;
    }


    public  boolean haveStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission error","You have permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return true;
            } else {

                Log.e("Permission error","You have asked for permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //you dont need to worry about these stuff below api level 23
            Log.e("Permission error","You already have the permission");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            //you have the permission now.
            switch (requestCode) {
                case 1:
                    String url = "https://firebasestorage.googleapis.com/v0/b/granoland-ee8e9.appspot.com/o/Brochure.pdf?alt=media&token=10b27102-b8be-4fe3-98ae-50eb2ba63530";
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.setDescription("Brochures");
                    request.setTitle("GranoLand");
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "GL_Brochures.pdf");
                    DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                    manager.enqueue(request);
                    break;
            }
        }
    }

    private void SaveImage(Bitmap finalBitmap) {

        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {

            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root + "/saved_images");
            myDir.mkdirs();
            String fname = "image.jpg";
            File file = new File(myDir, fname);
            if (file.exists()) file.delete();
            try {
                FileOutputStream out = new FileOutputStream(file);
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            MediaScannerConnection.scanFile(this, new String[]{file.toString()}, null,
                    new MediaScannerConnection.OnScanCompletedListener() {
                        public void onScanCompleted(String path, Uri uri) {
                            Log.e("ExternalStorage", "Scanned " + path + ":");
                            Log.e("ExternalStorage", "-> uri=" + uri);
                        }
                    });
        }
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
