package com.example.raj.granol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by raj on 3/4/17.
 */

public class calculator extends MainActivity {
    private static final String[]paths = {"Twin charge(600x600)","Outdoor(600x600)", "600x900","600x1200"};//in mm (twin category 4 tiles) (Outdoor category 2 tiles) (2nd category 3) (3rd category 2)
    EditText length,breadth;
    Spinner len,bread;
    private static final String[]units={ "inches" , "feet","cm","mm"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator);
        len=(Spinner)findViewById(R.id.len);
        bread=(Spinner)findViewById(R.id.bread);
        length=(EditText)findViewById(R.id.length);
        breadth=(EditText)findViewById(R.id.breadth);

        Toolbar toolbarc = (Toolbar) findViewById(R.id.toolbarcalc);
        setSupportActionBar(toolbarc);
        Spinner spinner=(Spinner)findViewById(R.id.spin2);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(calculator.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ArrayAdapter<String>adapter2 = new ArrayAdapter<String>(calculator.this,
                android.R.layout.simple_spinner_item,units);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        len.setAdapter(adapter2);
        bread.setAdapter(adapter2);

        Button bt=(Button)findViewById(R.id.calculate);
        final TextView tile=(TextView)findViewById(R.id.tiles);
        final TextView box=(TextView)findViewById(R.id.box);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(length.getText().toString()!="" && breadth.getText().toString()!="") {

                    tile.setText("TILES NEEDED       yoo        Nos");
                    box.setText("BOX NEEDED       hi        Nos");
                }else {
                    Toast.makeText(calculator.this, "Please fill all the Details", Toast.LENGTH_LONG).show();
                }
            }
        });

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
            Intent i=new Intent(calculator.this,MainActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
