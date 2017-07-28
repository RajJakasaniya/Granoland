package com.example.raj.granol;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by raj on 3/4/17.
 */

public class calculator extends MainActivity {
    private static final String[]paths = {"600x600mm", "600x900mm","600x1200mm"};//in mm (twin category 4 tiles) (Outdoor category 2 tiles) (2nd category 3) (3rd category 2)
    EditText length,breadth;
    LinearLayout main;
    Spinner len,bread,spinner;
    private static final String[]units={ "inch" , "ft","cm","mm"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator);
        len=(Spinner)findViewById(R.id.len);
        bread=(Spinner)findViewById(R.id.bread);
        length=(EditText)findViewById(R.id.length);
        breadth=(EditText)findViewById(R.id.breadth);
        main=(LinearLayout)findViewById(R.id.LL);

        Toolbar toolbarc = (Toolbar) findViewById(R.id.toolbarcalc);
        setSupportActionBar(toolbarc);
        spinner=(Spinner)findViewById(R.id.spin2);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(calculator.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        ArrayAdapter<String>adapter2 = new ArrayAdapter<String>(calculator.this,
                android.R.layout.simple_spinner_item,units);

        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        len.setAdapter(adapter2);
        bread.setAdapter(adapter2);
        len.setSelection(1);
        bread.setSelection(1);

        Button bt=(Button)findViewById(R.id.calculate);
        final TextView tile=(TextView)findViewById(R.id.tiles);
        final TextView box=(TextView)findViewById(R.id.box);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String lt=length.getText().toString();
                String bd=breadth.getText().toString();
                if(lt!="" && bd!="") {
                    Log.e("length :-",lt);
                    float L=0;
                    try{
                        L=Float.valueOf(lt);
                    }catch(Exception e){
                        L=0;
                    }
                    float B=0;
                    try{
                        B=Float.valueOf(bd);
                    }catch(Exception e){
                        B=0;
                    }

                    if (L != 0 && B != 0) {
                        try {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(main.getWindowToken(), 0);
                        } catch (Exception e) {
                            // TODO: handle exception
                        }

                        double l = 0, b = 0, area = 0, t_area = 0, t_n, box_n, t_b = 0;

                        switch (len.getSelectedItem().toString()) {
                            case "inch":
                                l = Double.valueOf(length.getText().toString());
                                l = l * (25.4);
                                break;
                            case "ft":
                                Log.e("length :-", length.getText().toString());
                                l = Double.valueOf(length.getText().toString());
                                l = l * (304.8);
                                break;
                            case "cm":
                                l = Double.valueOf(length.getText().toString());
                                l = l * 10;
                                break;
                            case "mm":
                                l = Double.valueOf(length.getText().toString());
                                break;
                        }

                        switch (bread.getSelectedItem().toString()) {
                            case "inch":
                                b = Double.valueOf(breadth.getText().toString());
                                b = b * (25.4);
                                break;
                            case "ft":
                                b = Double.valueOf(breadth.getText().toString());
                                b = b * (304.8);
                                break;
                            case "cm":
                                b = Double.valueOf(breadth.getText().toString());
                                b = b * 10;
                                break;
                            case "mm":
                                b = Double.valueOf(breadth.getText().toString());
                                break;
                        }

                        switch (spinner.getSelectedItem().toString()) {
                            case "600x600mm":
                                t_area = (360000);
                                t_b = 4;
                                break;
                            case "600x900mm":
                                t_area = (540000);
                                t_b = 3;
                                break;
                            case "600x1200mm":
                                t_area = (720000);
                                t_b = 2;
                                break;
                        }

                        area = l * b;

                        t_n = (int) (area / t_area);
                        box_n = (int) (t_n / t_b);
                        if (box_n == 0) {
                            box_n = 1;
                        }
                        tile.setText("TILES NEEDED       " + t_n + "        Nos");
                        box.setText("BOX NEEDED       " + box_n + "        Nos");
                    }else {
                        Toast.makeText(calculator.this, "Please fill all the Details", Toast.LENGTH_LONG).show();
                    }
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

            Intent i=new Intent(calculator.this,Guide.class);
            startActivity(i);
            finish();
            return true;

    }

    @Override
    public void onBackPressed() {
        super.startActivity(new Intent(calculator.this,Guide.class));
        finish();
    }
}
