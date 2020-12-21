package com.login.logindesign;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Display extends AppCompatActivity {
    TextView name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_display );
        name=findViewById( R.id.name1 );

       // Intent iin= getIntent();
        Bundle extras = getIntent().getExtras();
        String arrayB = extras.getString("Data");
        name.setText( arrayB );


    }



}
