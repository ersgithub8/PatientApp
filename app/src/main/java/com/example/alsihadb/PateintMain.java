package com.example.alsihadb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class PateintMain extends AppCompatActivity {

    LinearLayout measure,PI;
    Button logout;
    SessionManagment managment;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pateint_main);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Alsiha");


        measure=findViewById(R.id.measure);
        PI=findViewById(R.id.pi);
        managment=new SessionManagment();

        logout=findViewById(R.id.logout);
        logout.setVisibility(View.GONE);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                managment.setPreferences(PateintMain.this, "status", "0");
                Intent i1 = new Intent(PateintMain.this, ChooseType.class);
                i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i1);
            }
        });
        measure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(PateintMain.this,MainActivity.class);
                startActivity(i);

            }
        });

        PI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(PateintMain.this,AdditionInfo.class);
                i.putExtra("from","main");
                startActivity(i);
            }
        });


    }
}
