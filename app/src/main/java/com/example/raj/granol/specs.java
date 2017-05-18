package com.example.raj.granol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



/**
 * Created by raj on 11/5/17.
 */

public class specs extends MainActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.specs);

        Toolbar toolbarspecs = (Toolbar) findViewById(R.id.toolbarspecs);
        setSupportActionBar(toolbarspecs);

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
        if (id != R.id.action_settings) {
            Intent i=new Intent(specs.this,Guide.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.startActivity(new Intent(specs.this,Guide.class));
        finish();
    }
}
