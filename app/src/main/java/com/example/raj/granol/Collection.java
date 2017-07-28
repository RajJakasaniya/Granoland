package com.example.raj.granol;

import android.Manifest;
import android.app.DownloadManager;
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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by raj on 30/3/17.
 */

public class Collection extends MainActivity  implements NavigationView.OnNavigationItemSelectedListener{
    ExpandableListView expandableListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect);

        expandableListView=(ExpandableListView)findViewById(R.id.exp_listview);
        List<String> headings=new ArrayList<String>();
        List<String> L0=new ArrayList<String>();
        List<String> L1=new ArrayList<String>();
        List<String> L2=new ArrayList<String>();
        List<String> L3=new ArrayList<String>();

        HashMap<String,List<String>> childList = new HashMap<String, List<String>>();
        String heading_items[] = getResources().getStringArray(R.array.header_titles);
        String l0[] = getResources().getStringArray(R.array.h0_items);
        String l1[] = getResources().getStringArray(R.array.h1_items);
        String l2[] = getResources().getStringArray(R.array.h2_items);
        String l3[] = getResources().getStringArray(R.array.h3_items);

        for(String title :heading_items){
            headings.add(title);
        }
        for(String title :l0){
            L0.add(title);
        }
        for(String title :l1){
            L1.add(title);
        }
        for(String title :l2){
            L2.add(title);
        }
        for(String title :l3){
            L3.add(title);
        }
        childList.put(headings.get(0),L0);
        childList.put(headings.get(1),L1);
        childList.put(headings.get(2),L2);
        childList.put(headings.get(3),L3);

        final MyAdapter myadapter=new MyAdapter(this,headings,childList);
        expandableListView.setAdapter(myadapter);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub

                final String selected = (String) myadapter.getChild(
                        groupPosition, childPosition);

                // Switch case to open selected child element activity on child element selection.
                if(selected.equals("12mm")){
                    Intent i = new Intent(Collection.this,digital.class);
                    i.putExtra("selected", selected);
                    i.putExtra("parent",(String) myadapter.getGroup(groupPosition));
                    startActivity(i);

                }else if(selected.equals("Sparkle Series") || selected.equals("Natural Series")){
                    Intent i = new Intent(Collection.this,display.class);
                    i.putExtra("selected", selected);
                    i.putExtra("parent",(String) myadapter.getGroup(groupPosition));
                    startActivity(i);

                }else if(selected.equals("14mm")){
                    Intent i = new Intent(Collection.this,Duty.class);
                    i.putExtra("selected", selected);
                    i.putExtra("parent",(String) myadapter.getGroup(groupPosition));
                    startActivity(i);

                }else if(selected.equals("20mm")) {
                    Intent i = new Intent(Collection.this, heavyduty.class);
                    i.putExtra("selected", selected);
                    i.putExtra("parent", (String) myadapter.getGroup(groupPosition));
                    startActivity(i);
                }
                return true;
            }
        });







        Toolbar mtoolbar = (Toolbar)findViewById(R.id.toolbarc);
        setSupportActionBar(mtoolbar);

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
                        //finish();
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(Collection.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Collection.this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mtoolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.getMenu().findItem(R.id.home).setChecked(false);
        navigationView.getMenu().findItem(R.id.collection).setChecked(true);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        super.startActivity(new Intent(Collection.this,MainActivity.class));
        finish();
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            Intent in=new Intent(Collection.this,MainActivity.class);
            startActivity(in);
            finish();
        } else if (id == R.id.collection) {

        } else if (id == R.id.location) {
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?daddr=22.736256,70.978265"));
            intent.setPackage("com.google.android.apps.maps");
            startActivity(intent);
            finish();
        } else if (id == R.id.about) {

            Intent in=new Intent(Collection.this,About_us.class);
            startActivity(in);
            finish();
        } else if (id == R.id.nav_share) {
            //Uri pictureUri = Uri.parse("/storage/emulated/0/saved_images/image.jpg");
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Granoland Tiles LLP");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Download Granoland Tiles L.L.P. official android application :- https://play.google.com/store/apps/details?id=com.raj.granol");
            //sharingIntent.putExtra(Intent.EXTRA_STREAM, pictureUri);
            sharingIntent.setType("text/plain");
            //sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
            finish();
        } else if(id==R.id.contact) {
            Intent in=new Intent(Collection.this,Contact_us.class);
            startActivity(in);
            finish();
        }else if(id==R.id.guide) {
            Intent in=new Intent(Collection.this,Guide.class);
            startActivity(in);
            finish();
        }else if (id == R.id.rating) {
            Intent intent=new Intent(Collection.this,Enquiry.class);
            startActivity(intent);
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public  boolean haveStoragePermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.e("Permission error","You have permission");
                return true;
            } else {

                Log.e("Permission error","You have asked for permission");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //you dont need to worry about these stuff below api level 23
            Log.e("Permission error","You already have the permission");
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            //you have the permission now.
            String url = "https://firebasestorage.googleapis.com/v0/b/granoland-ee8e9.appspot.com/o/Brochure.pdf?alt=media&token=10b27102-b8be-4fe3-98ae-50eb2ba63530";
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
            request.setDescription("Brochures");
            request.setTitle("GranoLand");
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "GL_Brochures.pdf");
            DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            manager.enqueue(request);
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
