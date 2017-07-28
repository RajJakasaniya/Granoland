package com.example.raj.granol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;

/**
 * Created by raj on 13/5/17.
 */

public class Laying extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.laying);

        Toolbar toolbarlay = (Toolbar) findViewById(R.id.toolbarlay);
        setSupportActionBar(toolbarlay);

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

            Intent i=new Intent(Laying.this,Guide.class);
            startActivity(i);
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.startActivity(new Intent(Laying.this,Guide.class));
    }
}
