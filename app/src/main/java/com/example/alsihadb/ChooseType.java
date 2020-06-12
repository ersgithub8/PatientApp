package com.example.alsihadb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ChooseType extends AppCompatActivity {

    ImageView doctor,patient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_type);

        doctor=findViewById(R.id.imageView);
        patient=findViewById(R.id.imageView2);

        doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ChooseType.this,DocMain.class);
                i.putExtra("login","doc");
                startActivity(i);
            }
        });
        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(ChooseType.this,PateintMain.class);
                i.putExtra("login","pat");
                startActivity(i);
            }
        });
    }
}
