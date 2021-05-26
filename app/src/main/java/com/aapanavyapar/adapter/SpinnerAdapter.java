package com.aapanavyapar.adapter;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aapanavyapar.aapanavyapar.R;

public abstract class SpinnerAdapter extends AppCompatActivity implements
        OnItemSelectedListener {
    
    Spinner spin;   
    String[] choice = new String[]{"Product","Shop"};
  
    public SpinnerAdapter(){}
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.fragment_product_search);

        spin = findViewById(R.id.ps_spinner);
               
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,choice); 
                
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);          
        spin.setAdapter(adapter);  
                
        spin.setOnItemSelectedListener(new OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> parent,View view,int position,long id){

                switch(position)
                {
                    case 0: Toast.makeText(getApplicationContext(),"Product Search", Toast.LENGTH_LONG).show();   
                    case 1: Toast.makeText(getApplicationContext(),"Shop Search", Toast.LENGTH_LONG).show();                                  
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}